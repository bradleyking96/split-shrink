package edu.ncssm.splitshrink;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Arrays;

class Level implements Cloneable{

	private String title;
	private double timeStep,
	               wbCOR, bbCOR,
	               wpCOR, bpCOR,
	               splitSpeed,
	               volume,
	               volumeDecrease,
	               initVolume,
	               health,
	               initHealth;
	public int gameShrinks,
    		   shrinks,
    		   maxShrinks,
    		   splitAdd;
	private Point playerPos;
	private double[][] playerLOS;
	private Player player;
	private Box box;
	private Display display;
	private Color bgColor;
	private double fogDist;
	private ArrayList<Sphere> spheres;
	private Sphere[] sortedSpheres;
	private CollisionType collisionType;
	private boolean won;
	
	public String getTitle(){
		return title;
	}
	
	public double getTimeStep() {
		return timeStep;
	}	
	
	public void setTimeStep(double timeStep){
		this.timeStep = timeStep;
	}
	
	public ArrayList<Sphere> getSpheres(){
		return spheres;
	}
	
	public Level(
			String title,
			double wbCOR, double bbCOR,
			double wpCOR, double bpCOR,
			double splitSpeed, int splitAdd,
			int gameShrinks, int shrinks, int maxShrinks,
			double initHealth,
			Box box,
			Color bgColor,
			double fogDist,
			Player player,
			Sphere[] spheres,
			CollisionType collisionType
			){
		this.title = title;
		this.timeStep = 0.001;
		this.wbCOR = wbCOR;
		this.bbCOR = bbCOR;
		this.wpCOR = wpCOR;
		this.bpCOR = bpCOR;
		this.splitSpeed = splitSpeed;
		this.splitAdd = splitAdd;
		this.gameShrinks = gameShrinks;
		this.shrinks = shrinks;
		this.maxShrinks = maxShrinks;
		this.player = player;
		this.playerPos = player.getPlayerPos();
		this.playerLOS = player.getLOSMatrix();
		this.box = box;
		this.display = new Display(
				(double) maxShrinks/(double) gameShrinks, 
				Math.PI/100,
				Crayola.white,
				Crayola.white,
				Crayola.white,
				Crayola.white);
		this.display.calcShrinkBar(shrinks/gameShrinks);
		this.bgColor = bgColor;
		this.fogDist = fogDist;
		this.spheres = new ArrayList<Sphere>();
		initVolume = 0;
		for(int i = 0; i < spheres.length; i++){
			this.spheres.add(spheres[i]);
			initVolume += Math.pow(spheres[i].getRadius(), 3);
		}
		volume = initVolume;
		volumeDecrease = initVolume/gameShrinks;
		this.initHealth = initHealth;
		this.health = initHealth;
		this.collisionType = collisionType;
		won = false;
	}
	
	@Override
	public Level clone(){
		Sphere[] newSpheres = new Sphere[spheres.size()];
		for(int i = 0; i < newSpheres.length; i++){
			newSpheres[i] = spheres.get(i).clone();
		}
		return new Level(
				title,
				wbCOR, bbCOR,
				wpCOR, bpCOR,
				splitSpeed, splitAdd,
				gameShrinks, shrinks, maxShrinks,
				initHealth,
				box,
				bgColor,
				fogDist,
				player.clone(),
				newSpheres,
				collisionType
				);
	}

	public Sphere[] sortSpheres(){
		sortedSpheres = new Sphere[spheres.size() + 1];
		sortedSpheres = Arrays.copyOf(spheres.toArray(sortedSpheres), spheres.size() + 1);
		sortedSpheres[spheres.size()] = player;
		Arrays.sort(sortedSpheres);
		return sortedSpheres;
	}
	
	public void splitOrShrink(){
		double minDist = Double.MAX_VALUE;
		boolean hit = false;
		int index = -1;
		if(Resource.getMouseState(0) == KeyState.ONCE){
			for(int i = 0; i < spheres.size(); i++){
				Sphere sphere = spheres.get(i);
				if(display.getCursorRad() + sphere.getAngleRad() > sphere.getAngleDist()){
					double sphereDist = sphere.getPosition().dist(player.getPosition());
					if(sphereDist < minDist && sphere.getDist() > player.getDist()){//closer than other sphere, but not behind player
						minDist = sphereDist;
						hit = true;
						index = i;
					}
				}
			}
			if(hit && shrinks > 0){
				Resource.setMouseState(0, KeyState.UP);
				shrinks -= 1;
				if(shrinks < 0){
					shrinks = 0;
				}
				double sphereVolume = Math.pow(spheres.get(index).getRadius(), 3);
	            if(sphereVolume <= volumeDecrease*1.2){
	            	spheres.set(index, spheres.get(spheres.size() - 1));
	            	spheres.remove(spheres.size() - 1);
	            	volume -= sphereVolume;
	            }else{
	                double newMass = ((sphereVolume - volumeDecrease)/sphereVolume)*spheres.get(index).getMass();
	                double newRadius = (Math.pow(newMass, 1/3.0)/Math.pow(spheres.get(index).getMass(), 1/3.0))*spheres.get(index).getRadius();
	                spheres.get(index).setMass(newMass);
	                spheres.get(index).setRadius(newRadius);
	                volume -= volumeDecrease;
	            }
	    		display.calcSphereHealthBar(volume/initVolume);
	    		display.calcShrinkBar((double) shrinks/(double) gameShrinks);
			}
		}else if(Resource.getMouseState(2) == KeyState.ONCE){
			for(int i = 0; i < spheres.size(); i++){
				Sphere sphere = spheres.get(i);
				if(display.getCursorRad() + sphere.getAngleRad() > sphere.getAngleDist()){
					double sphereDist = sphere.getPosition().dist(player.getPosition());
					if(sphereDist < minDist && sphere.getDist() > player.getDist()){//closer than other sphere, but not behind player
						minDist = sphereDist;
						hit = true;
						index = i;
					}
				}
			}
			if(hit && spheres.size() < 100){
				Resource.setMouseState(2, KeyState.UP);
				shrinks += 1;
				if(shrinks > maxShrinks){
					shrinks = maxShrinks;
				}
				display.calcShrinkBar((double) shrinks/(double) gameShrinks);
                Sphere oldSphere = spheres.get(index);
                double sphereVolume = Math.pow(oldSphere.getRadius(), 3);
                if(sphereVolume < volumeDecrease/50.0){
	            	spheres.set(index, spheres.get(spheres.size() - 1));
	            	spheres.remove(spheres.size() - 1);
	            	volume -= sphereVolume;
		    		display.calcSphereHealthBar(volume/initVolume);
                }else{
                    Point vel = new Point((Math.random() - 1), (Math.random() - 1), (Math.random() - 1));
                    vel.normalize();
                    Point pos = vel.mult(oldSphere.getRadius());
                    vel = vel.mult(splitSpeed);
                    double vX = vel.getX();
                    double vY = vel.getY();
                    double vZ = vel.getZ();
                    double pX = pos.getX();
                    double pY = pos.getY();
                    double pZ = pos.getZ();
                    Sphere sphere1 = oldSphere.createSphere(
                    		oldSphere.getColor(),
                    		oldSphere.getMass()/2, oldSphere.getRadius()/Math.pow(2, 1/3.0), 
                    		oldSphere.getXPos() + pX, oldSphere.getYPos() + pY, oldSphere.getZPos() + pZ,
                    		oldSphere.getXVel() + vX, oldSphere.getYVel() + vY, oldSphere.getZVel() + vZ,
                    		oldSphere.getXAccel(), oldSphere.getYAccel(), oldSphere.getZAccel()
                    		);
                    Sphere sphere2 = oldSphere.createSphere(
                    		oldSphere.getColor(),
                    		oldSphere.getMass()/2, oldSphere.getRadius()/Math.pow(2, 1/3.0), 
                    		oldSphere.getXPos() - pX, oldSphere.getYPos() - pY, oldSphere.getZPos() - pZ,
                    		oldSphere.getXVel() - vX, oldSphere.getYVel() - vY, oldSphere.getZVel() - vZ,
                    		oldSphere.getXAccel(), oldSphere.getYAccel(), oldSphere.getZAccel()
                    		);
	            	spheres.set(index, spheres.get(spheres.size() - 1));
	            	spheres.remove(spheres.size() - 1);
    	            spheres.add(sphere1);
    	            spheres.add(sphere2);
                }
			}
		}
	}
	
	public void updateSpheres(){
		for(int i = 0; i < spheres.size(); i++){
			spheres.get(i).calcAccel(this);
			spheres.get(i).update(timeStep, box, wbCOR, playerPos);
		}
		player.calcAccel(this);
		player.update(timeStep, box, wpCOR, playerPos);
	}
	
	public void checkCollisions(){
		Sphere sphere1, sphere2;
		for(int i = 0; i < spheres.size() - 1; i++){
			for(int j = i + 1; j < spheres.size(); j++){
				sphere1 = spheres.get(i);
				sphere2 = spheres.get(j);
				if(sphere1.getPosition().dist(sphere2.getPosition())
						< sphere1.getRadius() + sphere2.getRadius()){
					sphere1.collide(spheres, i, j, bbCOR, collisionType);
				}
			}
		}
		for(int i = 0; i < spheres.size(); i++){
				sphere1 = spheres.get(i);
				if(sphere1.getPosition().dist(player.getPosition())
						< sphere1.getRadius() + player.getRadius()){
						double damage = player.collide(spheres, -1, i, bpCOR, collisionType);
						health -= damage ;
						if(health < 0){
							display.calcPlayerHealthBar(0);
							health = 0;
						}else{
							display.calcPlayerHealthBar(health/initHealth);
						}
						
				}
		}
	}
	
	public void doPhysics(){
		splitOrShrink();
		updateSpheres();
		checkCollisions();
		playerLOS = player.getLOSMatrix();
		playerPos = player.getPlayerPos();
	}

	public void render(Graphics2D g) {
		g.setBackground(bgColor);
		g.clearRect(0, 0, Resource.getScreenWidth(), Resource.getScreenHeight());
		box.findFrontLines(playerPos);
		box.drawBox(g, player.getPosition(), playerPos, playerLOS, Resource.getAngularDistance(), bgColor, fogDist, false);
		for(Sphere sphere:sortSpheres()){
			sphere.draw(g, player.getPosition(), playerPos, playerLOS, bgColor, fogDist);
		}
		box.drawBox(g, player.getPosition(), playerPos, playerLOS, Resource.getAngularDistance(), bgColor, fogDist, true);
		display.drawCursor(g);
	}
	
	public boolean go(FullScreenWindow fullscreen, Robot r) throws InterruptedException{
		r.mouseMove((int) (Resource.getScreenWidth()/2.0), (int) (Resource.getScreenHeight()/2.0));
		long time = System.nanoTime();
		long oldTime = time;
		fullscreen.requestFocus();
		while(true){
			if(fullscreen.hasFocus()){
				java.awt.Point mousePos = MouseInfo.getPointerInfo().getLocation();
				r.mouseMove((int) (Resource.getScreenWidth()/2.0), (int) (Resource.getScreenHeight()/2.0));
				Resource.setMousePos(mousePos.getX() - Resource.getScreenWidth()/2.0,
								 	 mousePos.getY() - Resource.getScreenHeight()/2.0);
				doPhysics();
				fullscreen.renderingLoop(this);
				time = System.nanoTime();
				timeStep = (time - oldTime)/(1000000000.0);
				if(timeStep < Resource.getMaxFrameTime()){
					long sleepTime = (long) ((Resource.getMaxFrameTime() - timeStep)*1000);
					Thread.sleep(sleepTime);
					timeStep = Resource.getMaxFrameTime();
				}
				oldTime = System.nanoTime();
				//System.out.println(1/timeStep);
				if(Resource.getKeyState(27) != KeyState.UP){
					System.exit(0);
				}
				if(spheres.size() == 0){
					won = true;
					break;
				}
				if(health == 0){
					won = false;
					break;
				}
			}else{
				Thread.sleep(100L);
				time = System.nanoTime();
			}
		}
		Resource.setMouseState(2, KeyState.UP);
		Resource.setMouseState(0, KeyState.UP);
		return won;
	}
}