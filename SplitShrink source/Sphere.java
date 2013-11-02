package edu.ncssm.splitshrink;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

abstract class Sphere implements Comparable<Sphere>, Cloneable{
	
	private double radius, mass;
	private double angleRad;
	private double[] anglePos = new double[2];
	private Point position;
	private Point velocity;
	private Point acceleration;
	private double dist;
	private Color color;
	
	//Constructors
	public Sphere(Color color,
				  double mass, double radius,
				  double xPos, double yPos, double zPos){
		this(color, mass, radius, xPos, yPos, zPos, 0, 0, 0, 0, 0, 0);
	}
	
	public Sphere(Color color,
			  	  double mass, double radius,
				  double xPos, double yPos, double zPos,
				  double xVel, double yVel, double zVel){
		this(color, mass, radius, xPos, yPos, zPos, xVel, yVel, zVel, 0, 0, 0);
	}
	
	public Sphere(Color color,
			      double mass, double radius,
				  double xPos, double yPos, double zPos,
				  double xVel, double yVel, double zVel,
				  double xAccel, double yAccel, double zAccel){
		this.color = color;
		this.radius = radius;
		this.mass = mass;
		position = new Point(xPos, yPos, zPos);
		velocity = new Point(xVel, yVel, zVel);
		acceleration = new Point(xAccel, yAccel, zAccel);
	}
	
	public double getAngleRad() {
		return angleRad;
	}
	
	public double[] getAnglePos() {
		return anglePos;
	}
	
	public double getAngleDist() {
		return Math.pow(anglePos[0]*anglePos[0] + anglePos[1]*anglePos[1], .5);
	}
	
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	
	public Point getVelocity() {
		return velocity;
	}
	public void setVelocity(Point velocity) {
		this.velocity = velocity;
	}
	
	public Point getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(Point acceleration) {
		this.acceleration = acceleration;
	}
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public double getDist() {
		return dist;
	}
	public void setDist(double dist) {
		this.dist = dist;
	}
	
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public double getMass() {
		return mass;
	}
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	public double getXPos() {
		return position.getX();
	}
	public void setXPos(double xPos) {
		position.setX(xPos);
	}
	
	public double getYPos() {
		return position.getY();
	}
	public void setYPos(double yPos) {
		position.setY(yPos);
	}
	
	public double getZPos() {
		return position.getZ();
	}
	public void setZPos(double zPos) {
		position.setZ(zPos);
	}
	
	public double getXVel() {
		return velocity.getX();
	}
	public void setXVel(double xVel) {
		velocity.setX(xVel);
	}
	
	public double getYVel() {
		return velocity.getY();
	}
	public void setYVel(double yVel) {
		velocity.setY(yVel);
	}
	
	public double getZVel() {
		return velocity.getZ();
	}
	public void setZVel(double zVel) {
		velocity.setZ(zVel);
	}
	
	public double getXAccel() {
		return acceleration.getX();
	}
	public void setXAccel(double xAccel) {
		acceleration.setX(xAccel);
	}
	
	public double getYAccel() {
		return acceleration.getY();
	}
	public void setYAccel(double yAccel) {
		acceleration.setY(yAccel);
	}
	
	public double getZAccel() {
		return acceleration.getZ();
	}
	public void setZAccel(double zAccel) {
		acceleration.setZ(zAccel);
	}

	@Override
	abstract public Sphere clone();
	
	public double collide(ArrayList<Sphere> spheres, int thisIndex, int index, double bbCOR, CollisionType collisionType){
		Sphere otherSphere = spheres.get(index);
		double velocityChange;
		if(collisionType == CollisionType.ELASTIC){
	        velocityChange = collideElast(otherSphere, bbCOR);
		}else if(collisionType == CollisionType.INELASTIC){
			velocityChange = collideInelast(spheres, otherSphere, thisIndex, index, bbCOR);
		}else{
			if(otherSphere.getColor() == getColor()){
				velocityChange = collideInelast(spheres, otherSphere, thisIndex, index, bbCOR);
			}else{
				velocityChange = collideElast(otherSphere, bbCOR);
			}
		}
		return velocityChange;
	}
	
	public double collideInelast(ArrayList<Sphere> spheres, Sphere otherSphere, int thisIndex, int index, double bbCOR){
		double v1 = Math.pow(getRadius(), 3);
		double v2 = Math.pow(otherSphere.getRadius(), 3);
		double m1 = getMass();
		double m2 = otherSphere.getMass();
		double x1 = getXPos();
		double x2 = otherSphere.getXPos();
		double y1 = getYPos();
		double y2 = otherSphere.getYPos();
		double z1 = getZPos();
		double z2 = otherSphere.getZPos();
		double vx1 = getXVel();
		double vx2 = otherSphere.getXVel();
		double vy1 = getYVel();
		double vy2 = otherSphere.getYVel();
		double vz1 = getZVel();
		double vz2 = otherSphere.getZVel();
		double initVel = Math.abs((this.getVelocity()).mag());
		Sphere newSphere = 
				createSphere(getColor(),
						m1 + m2,
						Math.pow(v1 + v2, 1.0/3.0),
						(x1*v1 + x2*v2)/(v1 + v2),
						(y1*v1 + y2*v2)/(v1 + v2),
						(z1*v1 + z2*v2)/(v1 + v2),
						(vx1*m1 + vx2*m2)/(m1 + m2),
						(vy1*m1 + vy2*m2)/(m1 + m2),
						(vz1*m1 + vz2*m2)/(m1 + m2),
						0, 0, 0
						);
		cleanupSphere(
			spheres, thisIndex, index, newSphere
		);
		return Math.abs(initVel - newSphere.getVelocity().mag());
		
	}
	
	public double collideElast(Sphere otherSphere, double bbCOR){
		//for simplicity's sake
		double m1 = mass;
		double m2 = otherSphere.getMass();
		Point p1 = position;
		Point p2 = otherSphere.getPosition();
		Point v1 = velocity;
		Point v2 = otherSphere.getVelocity();
		//finding delta x, y, z (+ is toward otherSphere)
		Point dp = p2.subtract(p1);
        //ratios of sphere-intersector component velocities over distance between balls
        double j1 = dp.dot(v1)/dp.dot(dp);
        double j2 = dp.dot(v2)/dp.dot(dp);
        //find magnitudes of sphere-intersector component velocities
        double oldVel1 = j1*dp.mag();
        double oldVel2 = j2*dp.mag();
        //subtracting sphere-intersector component vectors' component vectors
        v1 = v1.subtract(dp.mult(j1));
        v2 = v2.subtract(dp.mult(j2));
        //find magnitudes of new sphere-intersector component vectors
        double newVel1 = (oldVel1*(m1 - bbCOR*m2) + oldVel2*(m2 + bbCOR*m2))/(m1 + m2);
        double newVel2 = (oldVel2*(m2 - bbCOR*m1) + oldVel1*(m1 + bbCOR*m1))/(m1 + m2);
        //new ratios of velocities over distance between balls
        double k1 = newVel1/dp.mag();
        double k2 = newVel2/dp.mag();
        //adding new sphere-intersector component vectors' component vectors
        v1 = v1.add(dp.mult(k1));
        v2 = v2.add(dp.mult(k2));
        velocity = v1;
        otherSphere.setVelocity(v2);
        //establish overlap and distance
        double dist = dp.mag();
        double overlap = radius + otherSphere.getRadius() - dist;
        //move 'em a tiny bit away (just outside of overlap) so as to not cause problems
        double dv1 = newVel1 - oldVel1;
        double dv2 = newVel2 - oldVel2;
        double change1, change2;
        if(dv1 - dv2 == 0){
    		change1 = -overlap*1.2*.5/dist;
    		change2 = overlap*1.2*.5/dist;
        }else{
    		change1 = overlap*1.2*(dv1/(dv2 - dv1))/dist;
    		change2 = overlap*1.2*(dv2/(dv2 - dv1))/dist;
        }
        position = p1.add(dp.mult(change1));
        otherSphere.setPosition(p2.add(dp.mult(change2)));
        return Math.abs(newVel1 - oldVel1);
	}
	
	abstract Sphere createSphere(
			Color color, double radius, double mass,
			double xPos, double yPos, double zPos,
			double xVel, double yVel, double zVel,
			double xAccel, double yAccel, double zAccel);
	
	abstract void cleanupSphere(
			ArrayList<Sphere> spheres, int thisIndex, int index,
			Sphere sphere);
	
	abstract void calcAccel(Level level);
	
	//Advance position of spheres
	public void update(double timeStep, Box box, double wbCOR, Point playerPos){
		//double result = 0;
		//move it forward a bit to see if it'll hit anything
		Point pos = position.add(velocity.mult(timeStep)).add(acceleration.mult(.5*timeStep*timeStep));
		Point vel = velocity.add(acceleration.mult(timeStep));
		//now check to see what it's hit
		Box.Plane[] faces = box.getFaces();
		double r = radius;
		for(int i = 0; i < faces.length; i++){	
			double a = faces[i].getA();
			double b = faces[i].getB();
			double c = faces[i].getC();
			Point normalVect = new Point(a, b, c);
			double k = faces[i].getK();
			double a2 = a*a;
			double b2 = b*b;
			double c2 = c*c;
			if(normalVect.dot(pos) + k < r){//find first one it hits
				if(normalVect.dot(vel) < 0){
					//result += Math.abs(vel.dot(normalVect));
					if(a2 == 0 && b2 == 0){//in this case, the plane is already perpendicular to the z-axis
						vel.setZ(-wbCOR*vel.getZ());
					}else{
						double [][] bounceMatrix = 
							{
								{
									(a2*(c2 - wbCOR*(a2 + b2))+ b2)/(a2 + b2),
									-(wbCOR + 1)*a*b,
									-(wbCOR + 1)*a*c
								}, {
									-(wbCOR + 1)*a*b,
									(b2*(c2 - wbCOR*(a2 + b2)) + a2)/(a2 + b2),
									-(wbCOR + 1)*b*c
								}, {
									-(wbCOR + 1)*a*c,
									-(wbCOR + 1)*b*c,
									a2 + b2 - wbCOR*c2
								}
							};
						vel = vel.mult(bounceMatrix);
					}
				}
				double overlap = -(normalVect.dot(pos) + k - r);
				pos = pos.add(normalVect.mult(overlap));
			}
			position = pos;
			velocity = vel;
			dist = position.dist(playerPos);
		}
		//return result;	
		//if you want to add damage for wall collisions
	}
	
	@Override
	public int compareTo(Sphere otherSphere) {
		if(dist - otherSphere.getDist() > 0){
			return -1;
		}else if (dist - otherSphere.getDist() < 0){
			return 1;
		}else if (dist - otherSphere.getDist() == 0){
			return 0;
		}else{
			throw new java.lang.IllegalArgumentException();
		}
	}

	public void draw(Graphics2D g, Point playerSpherePos, Point playerPos, double[][] playerLOS, Color bgColor, double fogDist) {
		anglePos = Resource.calcAnglePos(
				   Resource.adjustPos(position.getX() - playerPos.getX(),
							  position.getY() - playerPos.getY(),
							  position.getZ() - playerPos.getZ(), 
						      playerLOS));
		angleRad = Math.asin(radius/position.dist(playerPos));
		double[] pos = Resource.calcRealPos(anglePos);
		double radius = angleRad*Resource.getScreenWidth()/Resource.getHorizontalFOV();
		Ellipse2D.Double circle = new Ellipse2D.Double(pos[0] - radius, pos[1] - radius, 2*radius, 2*radius);
		double c1, c2;
		double playerDist = playerSpherePos.dist(position);
		if(playerDist > fogDist){
			c2 = 1;
			c1 = 0;
		}else{
			c2 = playerDist/fogDist;
			c1 = 1 - c2;
		}
		g.setColor(new Color((int) ((color.getRed()*c1 + bgColor.getRed()*c2)/(c1 + c2)),
							 (int) ((color.getGreen()*c1 + bgColor.getGreen()*c2)/(c1 + c2)),
							 (int) ((color.getBlue()*c1 + bgColor.getBlue()*c2)/(c1 + c2)),
							 color.getAlpha()
				));
		g.fill(circle);
		
	}
	
}
