package edu.ncssm.splitshrink;

import java.awt.Color;
import java.util.ArrayList;

public class ConstAccelSphere extends Sphere{
	
	public ConstAccelSphere(Color color, double radius, double mass, double xPos,
			double yPos, double zPos) {
		this(color, radius, mass, xPos, yPos, zPos, 0, 0, 0, 0, 0, 0);
	}
	
	public ConstAccelSphere(Color color, double radius, double mass, double xPos,
			double yPos, double zPos, double xVel, double yVel, double zVel) {
		this(color, radius, mass, xPos, yPos, zPos, xVel, yVel, zVel, 0, 0, 0);
	}
	
	public ConstAccelSphere(Color color, double radius, double mass, double xPos,
			double yPos, double zPos, double xVel, double yVel, double zVel,
			double xAccel, double yAccel, double zAccel) {
		super(color, radius, mass, xPos, yPos, zPos, xVel, yVel, zVel, xAccel, yAccel,
				zAccel);
	}

	@Override
	void calcAccel(Level level) {
		//do nothing
	}

	@Override
	Sphere createSphere(Color color, double mass, double radius, double xPos,
			double yPos, double zPos, double xVel, double yVel, double zVel,
			double xAccel, double yAccel, double zAccel) {
		return new ConstAccelSphere(
					color, mass, radius,
					xPos, yPos, zPos,
					xVel, yVel, zVel,
					xAccel, yAccel, zAccel);
	}

	@Override
	void cleanupSphere(ArrayList<Sphere> spheres, int thisIndex, int index, Sphere sphere) {
		spheres.set(index, sphere);
		spheres.set(thisIndex, spheres.get(spheres.size() - 1));
		spheres.remove(spheres.size() - 1);
	}

	@Override
	public ConstAccelSphere clone() {
		return new ConstAccelSphere(getColor(),
				getMass(), getRadius(),
				getXPos(), getYPos(), getZPos(),
				getXVel(), getYVel(), getZVel(),
				getXAccel(), getYAccel(), getZAccel()
				);
	}
}
