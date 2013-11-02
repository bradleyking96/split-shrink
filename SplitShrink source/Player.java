package edu.ncssm.splitshrink;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

class Player extends Sphere implements Cloneable{

	private Point forward, up, right;
	private double thrust, dragCoeff, maxVel;
	
	public Player(Color color, int alpha,
			double mass, double radius,
			double xPos, double yPos, double zPos,
			Point forward, Point up,
			double thrust, double dragCoeff) {
		super(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha), mass, radius, xPos, yPos, zPos);
		this.forward = forward;
		this.up = up;
		this.right = forward.cross(up);
		this.thrust = thrust; //acceleration, not force
		this.dragCoeff = dragCoeff;
		this.maxVel = thrust/dragCoeff;
	}
	
	@Override
	public Player clone(){
		return new Player(getColor(), getColor().getAlpha(),
				getMass(), getRadius(),
				getXPos(), getYPos(), getZPos(),
				forward.clone(), up.clone(),
				thrust, maxVel
				);
	}
	
	public double[][] getLOSMatrix(){
		double[][] LOS = new double[3][3];
		forward.normalize();
		up.normalize();
		right.normalize();
		double a = forward.getX();
		double b = forward.getY();
		double c = forward.getZ();
		double x = up.getX();
		double y = up.getY();
		double z = up.getZ();
		double root = Math.pow(a*a + b*b, .5);
		if(a == 0 && b == 0){
			LOS[0][0] = y;
			LOS[0][1] = -x;
			LOS[0][2] = 0;
			LOS[1][0] = x;
			LOS[1][1] = y;
			LOS[1][2] = 0;
			LOS[2][0] = 0;
			LOS[2][1] = 0;
			LOS[2][2] = 1;
		}else{
			double xn = (a*c/root)*x + (b*c/root)*y - root*z;
			double yn = (-b/root)*x + (a/root)*y;
			//double zn = a*x + b*y + c*z;
			LOS[0][0] = (yn*a*c + xn*b)/root;
			LOS[0][1] = (yn*b*c - xn*a)/root;
			LOS[0][2] = -yn*root;
			LOS[1][0] = (xn*a*c - yn*b)/root;
			LOS[1][1] = (xn*b*c + yn*a)/root;
			LOS[1][2] = -xn*root;
			LOS[2][0] = a;
			LOS[2][1] = b;
			LOS[2][2] = c;
		}
		return LOS;
	}
	
	public Point getPlayerPos(){
		double dist = Resource.getViewDist()*(1 + .18*forward.dot(getVelocity())/maxVel)*getRadius();
		Point point = new Point(
				this.getXPos() - dist*forward.getX(),
				this.getYPos() - dist*forward.getY(),
				this.getZPos() - dist*forward.getZ()
				);
		return point;
	}
	
	public void calcAccel(Level level) {
		double timeStep = level.getTimeStep();
		double ax = 0;
		double ay = 0;
		double az = 0;
		if(Resource.getKeyState(KeyEvent.VK_SPACE) != KeyState.UP){
			ax += forward.getX();
			ay += forward.getY();
			az += forward.getZ();
		}
		if(Resource.getKeyState(KeyEvent.VK_SHIFT) != KeyState.UP){
			ax -= forward.getX();
			ay -= forward.getY();
			az -= forward.getZ();
		}
		if(Resource.getKeyState(KeyEvent.VK_W) != KeyState.UP){
			ax += up.getX();
			ay += up.getY();
			az += up.getZ();
		}
		if(Resource.getKeyState(KeyEvent.VK_A) != KeyState.UP){
			ax -= right.getX();
			ay -= right.getY();
			az -= right.getZ();
		}
		if(Resource.getKeyState(KeyEvent.VK_S) != KeyState.UP){
			ax -= up.getX();
			ay -= up.getY();
			az -= up.getZ();	
		}
		if(Resource.getKeyState(KeyEvent.VK_D) != KeyState.UP){
			ax += right.getX();
			ay += right.getY();
			az += right.getZ();
		}
		if(Resource.getKeyState(KeyEvent.VK_Q) != KeyState.UP){
			up = Resource.rotate(forward, up, Resource.getRollSensitivity()*timeStep);
			right = Resource.rotate(forward, right, Resource.getRollSensitivity()*timeStep);
		}
		if(Resource.getKeyState(KeyEvent.VK_E) != KeyState.UP){
			up = Resource.rotate(forward, up, -Resource.getRollSensitivity()*timeStep);
			right = Resource.rotate(forward, right, -Resource.getRollSensitivity()*timeStep);
		}
		forward = Resource.rotate(up, forward, timeStep*Resource.getMousePos()[0]*Resource.getHorSensitivity()/300);
		right = Resource.rotate(up, right, timeStep*Resource.getMousePos()[0]*Resource.getHorSensitivity()/300);
		up = Resource.rotate(right, up, timeStep*Resource.getMousePos()[1]*Resource.getVertSensitivity()/300);
		forward = Resource.rotate(right, forward, timeStep*Resource.getMousePos()[1]*Resource.getVertSensitivity()/300);
		/*if(L Click){
		//split
		}
		if(R Click){
			//shrink
		}*/
		if(ax == 0 && ay == 0 && az == 0){
			setXAccel(-getXVel()*dragCoeff);
			setYAccel(-getYVel()*dragCoeff);
			setZAccel(-getZVel()*dragCoeff);
		}else{
			Point accelPoint = new Point(ax, ay, az);
			accelPoint.normalize();
			setXAccel(accelPoint.getX()*thrust - getXVel()*dragCoeff);
			setYAccel(accelPoint.getY()*thrust - getYVel()*dragCoeff);
			setZAccel(accelPoint.getZ()*thrust - getZVel()*dragCoeff);
		}
		
	}

	@Override
	Sphere createSphere(Color color,
			double mass, double radius, double xPos, double yPos, double zPos,
			double xVel, double yVel, double zVel, double xAccel, double yAccel,
			double zAccel) {
		setRadius(radius);
		setMass(mass);
		setXPos(xPos);
		setYPos(yPos);
		setZPos(zPos);
		setXVel(xVel);
		setYVel(yVel);
		setZVel(zVel);	
		return this;
	}

	@Override
	void cleanupSphere(ArrayList<Sphere> spheres, int thisIndex, int index, Sphere sphere) {
		spheres.remove(index);
	}
	
}
