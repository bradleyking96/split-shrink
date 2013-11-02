package edu.ncssm.splitshrink;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

class Display {
	private double cursorRad;
	private Color cursorColor;
	private Area sphereHealthBar;
	private Color sphereHealthColor;
	private Area playerHealthBar;
	private Color playerHealthColor;
	private Area shrinkBar;
	private Color shrinkBarColor;
	
	private Area subtractArc;
	private Area shrinkAddArc;
	private double angleRad;
	
	public double getCursorRad(){
		return cursorRad;
	}
	
	public void setAngleRad(double angleRad){
		this.angleRad = angleRad;
	}
	
	public Display(double shrinkProp,
			double cursorRad,
			Color cursorColor, Color sphereHealthColor,
			Color playerHealthColor, Color shrinkBarColor){
		this.cursorRad = cursorRad;
		this.cursorColor = new Color(
				cursorColor.getRed(),
				cursorColor.getGreen(),
				cursorColor.getBlue(),
				100
				);
		this.sphereHealthColor = new Color(
				sphereHealthColor.getRed(),
				sphereHealthColor.getGreen(),
				sphereHealthColor.getBlue(),
				100
				);
		this.playerHealthColor = new Color(
				playerHealthColor.getRed(),
				playerHealthColor.getGreen(),
				playerHealthColor.getBlue(),
				100
				);
		this.shrinkBarColor = new Color(
				shrinkBarColor.getRed(),
				shrinkBarColor.getGreen(),
				shrinkBarColor.getBlue(),
				100
				);
		angleRad = cursorRad*Resource.getScreenWidth()/Resource.getHorizontalFOV();
		subtractArc = new Area(new Arc2D.Double(
				Resource.getScreenWidth()/2 - angleRad*4,
				Resource.getScreenHeight()/2 - angleRad*4,
				angleRad*8, angleRad*8,
				0, 360,
				Arc2D.PIE));
		Arc2D.Double sphereArc = new Arc2D.Double(
				Resource.getScreenWidth()/2 - angleRad*5,
				Resource.getScreenHeight()/2 - angleRad*5,
				angleRad*10, angleRad*10,
				0, 180,
				Arc2D.PIE);
		Arc2D.Double playerArc = new Arc2D.Double(
				Resource.getScreenWidth()/2 - angleRad*5.6,
				Resource.getScreenHeight()/2 - angleRad*5.6,
				angleRad*11.2, angleRad*11.2,
				180, 180,
				Arc2D.PIE);
		sphereHealthBar = new Area(sphereArc);
		sphereHealthBar.subtract(new Area(subtractArc));
		playerHealthBar = new Area(playerArc);
		playerHealthBar.subtract(new Area(subtractArc));
		Area shrinkSubtractArc = new Area(new Arc2D.Double(
				Resource.getScreenWidth()/2 - angleRad*5.1,
				Resource.getScreenHeight()/2 - angleRad*5.1,
				angleRad*10.2, angleRad*10.2,
				0, 360,
				Arc2D.PIE));
		shrinkAddArc = new Area(new Arc2D.Double(
				Resource.getScreenWidth()/2 - angleRad*5.6,
				Resource.getScreenHeight()/2 - angleRad*5.6,
				angleRad*11.2, angleRad*11.2,
				180, 180*(shrinkProp - 1),
				Arc2D.PIE));
		shrinkAddArc.subtract(shrinkSubtractArc);
	}
	
	public void calcSphereHealthBar(double sphereHealth){
		Arc2D.Double sphereArc = new Arc2D.Double(
				Resource.getScreenWidth()/2 - angleRad*5,
				Resource.getScreenHeight()/2 - angleRad*5,
				angleRad*10, angleRad*10,
				0, 180*sphereHealth,
				Arc2D.PIE);
		sphereHealthBar = new Area(sphereArc);
		sphereHealthBar.subtract(subtractArc);
		
	}
	
	public void calcPlayerHealthBar(double playerHealth){
		Arc2D.Double playerArc = new Arc2D.Double(
				Resource.getScreenWidth()/2 - angleRad*5.6,
				Resource.getScreenHeight()/2 - angleRad*5.6,
				angleRad*11.2, angleRad*11.2,
				180, 180*playerHealth,
				Arc2D.PIE);
		playerHealthBar = new Area(playerArc);
		playerHealthBar.subtract(new Area(subtractArc));
	}

	public void calcShrinkBar(double shrinks){
		shrinkBar = new Area(new Arc2D.Double(
				Resource.getScreenWidth()/2 - angleRad*5.5,
				Resource.getScreenHeight()/2 - angleRad*5.5,
				angleRad*11, angleRad*11,
				0, 180*shrinks,
				Arc2D.PIE));
		Area shrinkSubtractArc = new Area(new Arc2D.Double(
				Resource.getScreenWidth()/2 - angleRad*5.2,
				Resource.getScreenHeight()/2 - angleRad*5.2,
				angleRad*10.4, angleRad*10.4,
				0, 180,
				Arc2D.PIE));
		shrinkBar.subtract(shrinkSubtractArc);
		shrinkBar.add(shrinkAddArc);
	}
	
	public void drawCursor(Graphics2D g){
		//double newTimeTaken = System.nanoTime()/1000000000.0;
		//double oldTimeTaken = newTimeTaken;
		double angleRad = cursorRad*Resource.getScreenWidth()/Resource.getHorizontalFOV();
		Ellipse2D.Double circle = new Ellipse2D.Double(
				Resource.getScreenWidth()/2 - angleRad,
				Resource.getScreenHeight()/2 - angleRad,
				angleRad*2, angleRad*2);
		g.setColor(cursorColor);
		g.draw(circle);
		g.setColor(sphereHealthColor);
		/*if(level.test){
			System.out.println(0);
			newTimeTaken = System.nanoTime()/1000000000.0;
			System.out.println(newTimeTaken - oldTimeTaken);
			oldTimeTaken = newTimeTaken;
		}*/
		g.fill(sphereHealthBar);
		g.setColor(playerHealthColor);
		g.fill(playerHealthBar);
		g.setColor(shrinkBarColor);
		g.fill(shrinkBar);
	}
}
