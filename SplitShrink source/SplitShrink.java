package edu.ncssm.splitshrink;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.io.IOException;
import java.io.InputStream;

import edu.ncssm.splitshrink.Box.Plane;

public class SplitShrink {

	public static void main(String[] args) {
		Resource.setHorizontalFOV(Math.PI/2);
		Resource.setHorSensitivity(60);
		Resource.setVertSensitivity(60);
		Resource.setRollSensitivity(2);
		Resource.setAngularDistance(Math.PI/20);
		Resource.setMinEdge(10);
		Resource.setViewDist(10);
		Resource.setMaxFrameTime(1/60.0);
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Resource.setScreenWidth(device.getDisplayMode().getWidth());
		Resource.setScreenHeight(device.getDisplayMode().getHeight());
		
		for(int i = 0; i < 256; i++){
			Resource.setKeyState(i, KeyState.UP);
		}for(int i = 0; i < 3; i++){
			Resource.setMouseState(i, KeyState.UP);
		}

		Plane[] faces1 = {new Box.Plane(0, 1, 0, 120),
						 new Box.Plane(0, 0, 1, 120),
						 new Box.Plane(1, 0, 0, 120),
						 new Box.Plane(0, 0, -1, 120),
						 new Box.Plane(-1, 0, 0, 120),
						 new Box.Plane(0, -1, 0, 120),
						 
						 new Box.Plane(0, 1, 1, 120),
						 new Box.Plane(1, 1, 0, 120),
						 new Box.Plane(0, 1, -1, 120),
						 new Box.Plane(-1, 1, 0, 120),
						 
						 new Box.Plane(1, 0, 1, 120),
						 new Box.Plane(1, 0, -1, 120),
						 new Box.Plane(-1, 0, -1, 120),
						 new Box.Plane(-1, 0, 1, 120),
						 
						 new Box.Plane(0, -1, 1, 120),
						 new Box.Plane(1, -1, 0, 120),
						 new Box.Plane(0, -1, -1, 120),
						 new Box.Plane(-1, -1, 0, 120),
		};
		int[][] pointFaces1 = {
				{0, 6, 7},
				{0, 7, 8},
				{0, 8, 9},
				{0, 9, 6},
				{6, 1, 10},
				{6, 10, 7},
				{7, 10, 2},
				{7, 2, 11},
				{7, 11, 8},
				{8, 11, 3},
				{8, 3, 12},
				{8, 12, 9},
				{9, 12, 4},
				{9, 4, 13},
				{9, 13, 6},
				{6, 13, 1},
				{14, 1, 10},
				{14, 10, 15},
				{15, 10, 2},
				{15, 2, 11},
				{15, 11, 16},
				{16, 11, 3},
				{16, 3, 12},
				{16, 12, 17},
				{17, 12, 4},
				{17, 4, 13},
				{17, 13, 14},
				{14, 13, 1},
				{5, 14, 15},
				{5, 15, 16},
				{5, 16, 17},
				{5, 17, 14}
			  };
		int[][] linePoints1 = {
				{0, 1},
				{1, 2},
				{2, 3},
				{3, 0},
				
				{0, 5},
				{1, 8},
				{2, 11},
				{3, 14},
				
				{4, 5},
				{5, 6},
				{6, 7},
				{7, 8},
				{8, 9},
				{9, 10},
				{10, 11},
				{11, 12},
				{12, 13},
				{13, 14},
				{14, 15},
				{15, 4},
				
				{4, 16},
				{6, 18},
				{7, 19},
				{9, 21},
				{10, 22},
				{12, 24},
				{13, 25},
				{15, 27},
				
				{16, 17},
				{17, 18},
				{18, 19},
				{19, 20},
				{20, 21},
				{21, 22},
				{22, 23},
				{23, 24},
				{24, 25},
				{25, 26},
				{26, 27},
				{27, 16},
				
				{17, 28},
				{20, 29},
				{23, 30},
				{26, 31},
				
				{28, 29},
				{29, 30},
				{30, 31},
				{31, 28}
			  };
		Box box1 = new Box(faces1, pointFaces1, linePoints1, Crayola.white);
		
		Plane[] faces2 = {new Box.Plane(0, 1, 0, 70),
						 new Box.Plane(0, 0, 1, 70),
						 new Box.Plane(-1, 0, 0, 70),
						 new Box.Plane(0, 0, -1, 70),
						 new Box.Plane(1, 0, 0, 70),
						 new Box.Plane(0, -1, 0, 70)};
		int[][] pointFaces2 = {
				  {0, 1, 2},
				  {0, 2, 3},
				  {0, 3, 4},
				  {0, 4, 1},
				  {5, 1, 2},
				  {5, 2, 3},
				  {5, 3, 4},
				  {5, 4, 1}
			  };
		int[][] linePoints2 = {
				  {0, 1},
				  {1, 2},
				  {2, 3},
				  {3, 0},
				  {0, 4},
				  {1, 5},
				  {2, 6},
				  {3, 7},
				  {4, 5},
				  {5, 6},
				  {6, 7},
				  {7, 4}
			  };
		Box box2 = new Box(faces2, pointFaces2, linePoints2, Crayola.white);
	
		Sphere[] spheres1 = new Sphere[1];
		Sphere[] spheres2 = new Sphere[27];
		spheres1[0] = new ConstAccelSphere(Crayola.white, 10, 20, 0, 0, 0);
		int index = 0;
		for(int i = -1; i < 2; i++){
			for(int j = -1; j < 2; j++){
				for(int k = -1; k < 2; k++){
					spheres2[index] = new ConstAccelSphere(Crayola.randomFrom(Crayola.blueGreen, Crayola.blueBell, Crayola.yellowGreen), 10, 10, 30*i, 30*j, 30*k);
					index++;
				}
			}
		}
		Level level1 = new Level(
				"LEVEL 1", //title
				1, 1, //wbCOR, bbCOR
				.4, 1, //wpCOR, bpCOR
				100, 1,//splitSpeed, splitAdd
				10, 0, 4, //gameShrinks, shrinks, maxShrinks
				2000, //initHealth
				box2, //box
				Crayola.black, //bgColor
				300, //fogDist
				new Player(Crayola.red, 200, 1, 10, 0, 0, -80, new Point(0, 0, 1), new Point(0, 1, 0), 500, 300), //player
				spheres1, //spheres
				CollisionType.COLORBASED //collisionType
				);
		
		Level level2 = new Level(
				"LEVEL 2", //title
				1, 1, //wbCOR, bbCOR
				.4, 1, //wpCOR, bpCOR
				100, 1,//splitSpeed, splitAdd
				20, 0, 4, //gameShrinks, shrinks, maxShrinks
				1000, //initHealth
				box1, //box
				Crayola.black, //bgColor
				300, //fogDist
				new Player(Crayola.red, 200, 1, 10, 0, 0, -80, new Point(0, 0, 1), new Point(0, 1, 0), 500, 300), //player
				spheres2, //spheres
				CollisionType.COLORBASED //collisionType
				);
		Level[] levels = {level1, level2};
		
		FullScreenWindow fullscreen = null;
		try{
			fullscreen = new FullScreenWindow();
			Robot r = new Robot();
			final InputStream stream;
			stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("res/edu.ncssm.splitshrink.8bit.ttf");
			Font bitFont = Font.createFont(Font.TRUETYPE_FONT, stream);
			
			//Levels and text screens
			TextScreen textScreen;
			for(Level level:levels){
				boolean won;
				do{
					textScreen = new TextScreen(level.getTitle(), 80f, bitFont, Crayola.white, Crayola.black);
					textScreen.go(fullscreen);
					won = level.clone().go(fullscreen, r);
					if(won){
						textScreen = new TextScreen("LEVEL COMPLETE", 80f, bitFont, Crayola.white, Crayola.black);
						textScreen.go(fullscreen);
					}else{
						textScreen = new TextScreen("TRY AGAIN", 80f, bitFont, Crayola.white, Crayola.black);
						textScreen.go(fullscreen);
					}
				}while(!won);
			}
			textScreen = new TextScreen("CONGRATULATIONS!", 80f, bitFont, Crayola.white, Crayola.black);
			textScreen.go(fullscreen);
			
			System.exit(0);
			
		} catch (AWTException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}finally{
			fullscreen.getDevice().setFullScreenWindow(null);
		}
	}
}
