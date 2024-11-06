package projectoop;

import java.awt.*;
import javax.swing.JPanel;
import java.util.Random;
import javax.swing.Timer;

public class ItemHeal {
    private int x;
    private int y;
    private int diameter = 20;
    private boolean visible;
    private Random random;
    private Timer visibilityTimer;

    public ItemHeal() {
        this.random = new Random();
        this.visible = false; // start by not show
    }

    // setting itemHeal
    public void spawnItem() {
        this.x = random.nextInt(380); // random horizontal position
        this.y = random.nextInt(600 - 100); // random vertical position
        this.visible = true;

        // timer 10 time when time up hide itemHeal
        visibilityTimer = new Timer(10000, e -> {
            visible = false;
            visibilityTimer.stop();
        });
        visibilityTimer.setRepeats(false);
        visibilityTimer.start();
    }

    // draw itemHeal
    public void draw(Graphics g, JPanel panel) {
        if (visible) {
            g.setColor(Color.GREEN); // color of iteamHeal
            g.fillOval(x, y, diameter, diameter); // shape of iteamHeal
        }
    }

    // check hit ninja and itemHeal
    public boolean checkCollision(Rectangle characterHitbox) {
        if (visible && characterHitbox.intersects(new Rectangle(x, y, diameter, diameter))) {
            visible = false; // hide iteamHeal after hit
            return true;
        }
        return false;
    }

    public boolean isVisible() {
        return visible;
    }
    
}
