package testOpti;

public class City {

    private int x;
    private int y;

    public City(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters and toString()
    public int getX() {
    	return this.x;
    }

    public int getY() {
    	return this.y;
    }
    
    public String toString() {
    	return Integer.toString(this.x) + Integer.toString(this.y); 
    }
}