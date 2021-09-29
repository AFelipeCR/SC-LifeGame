package models.entities;

public class Point {
	public int x;
	public int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toText(){
		return "(%s, %s)".formatted(this.x, this.y);
	}
}
