package projectoop;

import java.awt.*;

public class HP {

    private int health;
    private static final int MAX_HEALTH = 3; // max hp

    public HP() {
        this.health = MAX_HEALTH; // start at max hp
    }
    
    public void increaseHealth(int amount) {
        health = Math.min(health + amount, MAX_HEALTH); // increase hp but not more than MAX_HEALTH
    }

    public void decreaseHealthL() {
        if (health > 0) {
            health -= 2; // decrease 2 hp
        }
    }

    public void decreaseHealthR() {
        if (health > 0) {
            health--; // decrease 1 hp
        }
    }

    public int getHealth() {
        return health; // restore hp now
    }

    public void resetHealth() {
        health = MAX_HEALTH; // reset to max hp
    }

    public void draw(Graphics g, int x, int y) {
        // draw hp
        g.setColor(Color.GREEN);
        for (int i = 0; i < health; i++) {
            g.fillRect(x + (i * 20), y, 15, 15); // draw each hp
        }
        // draw rectangle around hp
        g.setColor(Color.WHITE);
        g.drawRect(x, y, MAX_HEALTH * 20, 15);
    }


}
