package edu.ncssm.splitshrink;

class Point implements Cloneable{
	private double x, y, z;
	
	public Point(double x, double y, double z){
		this.x = x; this.y = y; this.z = z;
	}
	
	public Point(double[] coords){
		x = coords[0]; y = coords[1]; z = coords[2];
	}
	
	public Point clone(){
		return new Point(x, y, z);
	}
	
	public double dist(Point otherPoint){
		double dx = x - otherPoint.getX();
		double dy = y - otherPoint.getY();
		double dz = z - otherPoint.getZ();
		return Math.pow(dx*dx + dy*dy + dz*dz, .5);
	}
	
	public double mag(){
		return Math.pow(x*x + y*y + z*z, .5);
	}
	
	public void normalize(){
		double mag = Math.pow(x*x + y*y + z*z, .5);
		x /= mag;
		y /= mag;
		z /= mag;
	}
	
	public Point add(double scalar){
		return new Point(
				x += scalar,
				y += scalar,
				z += scalar
				);
	}
	
	public Point add(Point point){
		return new Point(
				x + point.getX(),
				y + point.getY(),
				z + point.getZ()
				);
	}
	
	public Point subtract(Point point){
		return new Point(
				x - point.getX(),
				y - point.getY(),
				z - point.getZ()
				);
	}
	
	public Point mult(double scalar){
		return new Point(
				x*scalar,
				y*scalar,
				z*scalar
				);
	}
	
	public Point mult(double[][] matrix){
		return new Point(
				dot(new Point(matrix[0])),
				dot(new Point(matrix[1])),
				dot(new Point(matrix[2]))	
				);
	}
	
	public Point cross(Point otherPoint){
		return new Point(
				y*otherPoint.getZ() + z*otherPoint.getY(),
				z*otherPoint.getX() + x*otherPoint.getZ(),
				x*otherPoint.getY() + y*otherPoint.getX()
				);
	}
	
	public double dot(Point otherPoint){
		return x*otherPoint.getX()+
			   y*otherPoint.getY()+
			   z*otherPoint.getZ();
	}
	
	public String toString(){
		return (x + " " + y + " " + z);
	}

	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}

	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setZ(double z) {
		this.z = z;
	}
	
	
}