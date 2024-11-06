package projectoop;

public class Distance {
    private int distance; // get distance in meter

    public Distance() {
        this.distance = 0; // start at 0 meter
    }

    public void increaseDistance(int increment) {
        distance += increment; // increase distance from set default
    }

    public int getDistance() {
        return distance; // restore now distance
    }

    public void resetDistance() {
        distance = 0; // reset distance to 0
    }
}
