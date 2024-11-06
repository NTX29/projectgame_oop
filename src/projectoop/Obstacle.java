package projectoop;

import java.awt.*;
import javax.swing.JPanel;
import java.util.Random;

public class Obstacle {

    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isOnLeft; // check obstacle at left?
    private boolean visible; //set visible
    private Random random;

    public Obstacle(int x, int width, int height, boolean isOnLeft) {
        this.x = x;
        this.width = width;
        this.height = height;
        this.isOnLeft = isOnLeft;
        this.random = new Random();
        this.visible = true; // start show visible
        resetPosition();
    }

    public void resetPosition() {
        // Set y position randomly within the frame
        this.y = random.nextInt(600 - 100); // Keep the obstacle within the frame
        this.visible = true; // reset obstacle comeback show
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void draw(Graphics g, JPanel panel) {
        // draw obstacle when visible == true
        if (visible) {
            g.setColor(isOnLeft ? Color.RED : Color.YELLOW); //Different color for each side
            g.fillRect(x, y, width, height); // Draw obstacle
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
