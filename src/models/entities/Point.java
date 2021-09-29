package models.entities;

public class Point {
	public float x;
	public float y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toText(){
		return "(%s, %s)".formatted((int) this.x, (int) this.y);
	}
	
	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}
}
