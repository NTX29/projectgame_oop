package projectoop;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.*;

/**
 *
 * @author studentcs
 */
public class Projectoop extends JFrame {

    URL imgBgURL = getClass().getResource("bg.png");
    Image imgBg = new ImageIcon(imgBgURL).getImage();

    Projectoop() {
        DrawArea p = new DrawArea(imgBg);
        p.setPreferredSize(new Dimension(400, 700));
        add(p);
        //JPnael focus when use keyboard
        p.setFocusable(true);
        p.requestFocusInWindow();
        pack();
    }

    public static void main(String[] args) {
        JFrame f = new Projectoop();
        f.setTitle("Ninja Adventure");
        f.setSize(400, 700);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    // inner class
    static class DrawArea extends JPanel {

        Image imgBg;
        Image imgBgNight;
        Image[] imgActorsLeft = new Image[5]; // array keep actor for left side
        Image[] imgActorsRight = new Image[5]; // array keep actor for right side
        int x = -58;
        int y = 570;
        int imgIndex = 0; // counter for choose img
        Timer movementTimer; // Timer move start
        Timer startTimer; // Timer delay start
        boolean jumping = false; //jump status
        boolean onLeftWall = true; // check side position of characters
        private int targetX; // position x for jump to target
        private static final int JUMP_SPEED = 50; //speed of horizontal

        private Obstacle obstacleLeft; // obstacle left side
        private Obstacle obstacleRight; // obstacle right side
        private static final int OBSTACLE_SPEED = 5; // move down

        Rectangle ninjaHitbox; //hitbox of ninja
        private int score = 0;

        private HP hp = new HP(); // obj hp

        // value keep blink
        private boolean blinking = false;
        private int blinkCounter = 0;
        private static final int BLINK_DURATION = 10; // count will blink

        private Distance distance = new Distance(); // obj distance

        private boolean nightMode = false;
        private Timer nightModeTimer;
        private ItemHeal itemHeal = new ItemHeal();

        public DrawArea(Image imgBg) {
            this.imgBg = imgBg;
            this.imgBgNight = new ImageIcon(getClass().getResource("bgnight.png")).getImage();
            ninjaHitbox = new Rectangle(x + 55, y + 50, 80, 20); // hitbox size of ninja

            // array keep bg and actorleft
            for (int i = 0; i < imgActorsLeft.length; i++) {
                URL imgURL = getClass().getResource("ninja00" + (i + 1) + ".png");
                imgActorsLeft[i] = new ImageIcon(imgURL).getImage();
            }

            // array keep bg and actorright
            for (int i = 0; i < imgActorsRight.length; i++) {
                URL imgURL = getClass().getResource("ninja11" + (i + 1) + ".png");
                imgActorsRight[i] = new ImageIcon(imgURL).getImage();
            }

            //creat obstacle at start
            obstacleLeft = new Obstacle(0, 80, 15, true); //left side obstacle setting
            obstacleRight = new Obstacle(320, 80, 15, false); //right side obstacle setting

            // Setting movement start
            movementTimer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    y -= 15; // move up
                    distance.increaseDistance(1);
                    if (distance.getDistance() % 500 == 0 && distance.getDistance() > 0) {
                        itemHeal.spawnItem(); // show iteamHeal every 500 m
                    }

                    // setting chang of scene
                    if (distance.getDistance() % 500 == 0 && distance.getDistance() > 0 && !nightMode) {
                        nightMode = true;
                        
                        repaint();

                        nightModeTimer = new Timer(10000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                nightMode = false;
                                repaint();
                                nightModeTimer.stop();
                            }
                        });
                        nightModeTimer.setRepeats(false);
                        nightModeTimer.start();
                    }

                    // update obstacle move down
                    obstacleLeft.setY(obstacleLeft.getY() + OBSTACLE_SPEED);
                    obstacleRight.setY(obstacleRight.getY() + OBSTACLE_SPEED);

                    // reset obstacle position when go out frame
                    if (obstacleLeft.getY() > 600) {  // 
                        obstacleLeft.resetPosition(); // reset over position
                    }
                    if (obstacleRight.getY() > 600) {
                        obstacleRight.resetPosition();
                    }

                    // move horizontal (jump smooth)
                    if (jumping) {
                        if (x < targetX) {
                            x = Math.min(x + JUMP_SPEED, targetX); //jump to right
                        } else if (x > targetX) {
                            x = Math.max(x - JUMP_SPEED, targetX); //jump to left
                        } else {
                            jumping = false; // stop jump when arrive target
                        }
                    }

                    //reset y position when go out screen
                    if (y < -140) {
                        y = 600;//set to start
                        //random obstacle start at y position
                        obstacleLeft.resetPosition();
                        obstacleRight.resetPosition();
                    }

                    //update imgIndex for change img
                    imgIndex = (imgIndex + 1) % imgActorsLeft.length;
                    ninjaHitbox.setLocation(x + 55, y + 50); //update hitbox position follow ninja

                    //check hit with obstacleLeft
                    if (obstacleLeft.isVisible() && ninjaHitbox.intersects(obstacleLeft.getBounds())) {
                        if (!blinking) {
                            hp.decreaseHealthL();
                            startBlinking(); // start blinking

                            // check hp die?
                            if (hp.getHealth() <= 0) {
                                gameOver();
                                movementTimer.stop(); // stop game
                            }
                        }
                    }

                    // check hit with obstacleRight
                    if (obstacleRight.isVisible() && ninjaHitbox.intersects(obstacleRight.getBounds())) {
                        if (!blinking) {
                            hp.decreaseHealthR();
                            startBlinking();

                            // check hp die?
                            if (hp.getHealth() <= 0) {
                                gameOver();
                                movementTimer.stop(); // stop game
                            }
                        }
                    }

                    if (itemHeal.checkCollision(ninjaHitbox)) {
                        hp.increaseHealth(1); // increase 1 hp
                    }

                    repaint();
                }
            });

            // Setting delay start
            startTimer = new Timer(3000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    movementTimer.start(); // start movement
                    startTimer.stop(); // stop this timer
                }
            });
            startTimer.setRepeats(false); // timer run one only
            startTimer.start(); // start timer delay

            //keylistener for spacebar
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        jumping = true; // set status jump when spacebar

                        //set x position from actor position
                        if (onLeftWall) {
                            targetX = getWidth() - 140; // target is right
                        } else {
                            targetX = -58;  // target is left
                        }

                        //swap wall position actor will jump
                        onLeftWall = !onLeftWall;
                    }
                }
            });
        }

        private void startBlinking() {
            blinking = true;
            blinkCounter = 0;
        }

        private void updateBlinking() {
            if (blinking) {
                blinkCounter++;
                if (blinkCounter > BLINK_DURATION) {
                    blinking = false;
                }
            }
        }

        // function GUI game over
        private void gameOver() {
            // show gui
            JFrame gameOverFrame = new JFrame("Game Over");
            gameOverFrame.setSize(300, 150);
            gameOverFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gameOverFrame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 1));

            JLabel messageLabel = new JLabel("Game Over", JLabel.CENTER);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
            panel.add(messageLabel);

            // show your distance
            JLabel scoreLabel = new JLabel("Your Distance: " + distance.getDistance() + " m", JLabel.CENTER);
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(scoreLabel);

            JPanel buttonPanel = new JPanel();
            JButton restartButton = new JButton("Restart");
            JButton exitButton = new JButton("Exit");

            restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // restart game
                    hp.resetHealth();
                    distance.resetDistance(); //reset distance
                    x = -58;
                    y = 570;
                    score = 0;
                    obstacleLeft.resetPosition();
                    obstacleRight.resetPosition();
                    onLeftWall = true; //start at left every time
                    gameOverFrame.dispose(); //close
                    movementTimer.start(); //restart
                }
            });

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); // close program
                }
            });

            buttonPanel.add(restartButton);
            buttonPanel.add(exitButton);
            panel.add(buttonPanel);

            gameOverFrame.add(panel);
            gameOverFrame.setVisible(true);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (nightMode) {
                g.drawImage(imgBgNight, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.drawImage(imgBg, 0, 0, getWidth(), getHeight(), this);
            }

            //draw obstacle on each side
            obstacleLeft.draw(g, this);
            obstacleRight.draw(g, this);

            hp.draw(g, 10, 50);

            //draw blinking ninja when hit obstacleLeft
            updateBlinking();
            if (!blinking || blinkCounter % 2 == 0) {
                //choose setimgleft or setimgright from character position
                Image[] currentImages = onLeftWall ? imgActorsLeft : imgActorsRight;
                g.drawImage(currentImages[imgIndex], x, y, 200, 140, this); // show img from your choose
            }

            // draw stroke of ninja hitbox
//            g.setColor(Color.BLUE);
//            g.drawRect(ninjaHitbox.x, ninjaHitbox.y, ninjaHitbox.width, ninjaHitbox.height);

            // show distance
            g.setColor(Color.WHITE);
            g.drawString("Distance: " + distance.getDistance() + " m", 10, 20);

            itemHeal.draw(g, this);

        }

    }
}
