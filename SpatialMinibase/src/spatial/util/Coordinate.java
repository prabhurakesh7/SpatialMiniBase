package spatial.util;

public class Coordinate{
	private double x;
	private double y;
	
	Coordinate(double _x, double _y){
		this.x = _x;
		this.y = _y;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
