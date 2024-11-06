package projectoop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGame extends JFrame {

    public StartGame() {
        setTitle("Start Game");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // creat start button and set center
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setFocusPainted(false);

        // set button at center frame
        setLayout(new GridBagLayout());
        add(startButton);

        // add function to start button open game
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // close this window
                new Projectoop().setVisible(true); // open game
            }
        });
    }

    public static void main(String[] args) {
        new StartGame().setVisible(true); // show game
    }
}
