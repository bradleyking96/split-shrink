package edu.ncssm.splitshrink;

import java.lang.Math;

class Resource {
	
	private static double horizontalFOV,
	viewDist,
	horSensitivity, vertSensitivity, rollSensitivity,
	angularDistance, minEdge,
	maxFrameTime;
	private static int screenWidth, screenHeight;
	private static KeyState[] pressedKeys = new KeyState[256];
	private static double[] mousePos = new double[2];
	private static KeyState[] mouseClick = new KeyState[3];
	
	
	public static double getHorizontalFOV() {
		return horizontalFOV;
	}
	public static double getViewDist() {
		return viewDist;
	}
	public static double getHorSensitivity() {
		return horSensitivity;
	}
	public static double getVertSensitivity() {
		return vertSensitivity;
	}
	public static double getRollSensitivity() {
		return rollSensitivity;
	}
	public static double getAngularDistance() {
		return angularDistance;
	}
	public static double getMinEdge() {
		return minEdge;
	}
	public static double getMaxFrameTime(){
		return maxFrameTime;
	}
	public static int getScreenWidth() {
		return screenWidth;
	}
	public static int getScreenHeight() {
		return screenHeight;
	}
	public static KeyState getKeyState(int i) {
		return pressedKeys[i];
	}
	public static KeyState getMouseState(int i) {
		return mouseClick[i];
	}
	public static double[] getMousePos() {
		return mousePos;
	}

	
	public static void setHorizontalFOV(double horizontalFOV) {
		Resource.horizontalFOV = horizontalFOV;
	}
	public static void setViewDist(double viewDist) {
		Resource.viewDist = viewDist;
	}
	public static void setHorSensitivity(double horSensitivity) {
		Resource.horSensitivity = horSensitivity;
	}
	public static void setVertSensitivity(double vertSensitivity) {
		Resource.vertSensitivity = vertSensitivity;
	}
	public static void setRollSensitivity(double rollSensitivity) {
		Resource.rollSensitivity = rollSensitivity;
	}
	public static void setAngularDistance(double angularDistance) {
		Resource.angularDistance = angularDistance;
	}
	public static void setMinEdge(double minEdge) {
		Resource.minEdge = minEdge;
	}
	public static void setMaxFrameTime(double maxFrameTime){
		Resource.maxFrameTime = maxFrameTime;
	}
	public static void setScreenWidth(int screenWidth) {
		Resource.screenWidth = screenWidth;
	}
	public static void setScreenHeight(int screenHeight) {
		Resource.screenHeight = screenHeight;
	}
	public static void setKeyState(int i, KeyState pressed) {
		Resource.pressedKeys[i] = pressed;
	}
	public static void setMouseState(int i, KeyState pressed) {
		Resource.mouseClick[i] = pressed;
	}
	public static void setMousePos(double[] mousePos) {
		Resource.mousePos = mousePos;
	}
	public static void setMousePos(double mouseXPos, double mouseYPos) {
		Resource.mousePos[0] = mouseXPos;
		Resource.mousePos[1] = mouseYPos;
	}
	
	public static double[] solveQuadratic(double a, double b, double c){
		double[] solution = new double[2];
		double foo, bar;
		if(a == 0){
			foo = -c/b;
			bar = 0;
		}else{
			foo = -b/(2*a);
			bar = Math.pow(b*b - 4*a*c, .5)/(2*a);
		}
		solution[0] = foo; solution[1] = bar;
		return solution;
	}
		
	public static Point adjustPos(double x, double y, double z, double[][] playerLOS){
		double newX = playerLOS[0][0]*x + playerLOS[0][1]*y + playerLOS[0][2]*z;
		double newY = playerLOS[1][0]*x + playerLOS[1][1]*y + playerLOS[1][2]*z;
		double newZ = playerLOS[2][0]*x + playerLOS[2][1]*y + playerLOS[2][2]*z;
		Point foo = new Point(newX, newY, newZ);
        return foo;
	}
	
    public static double[] calcAnglePos(Point adjustedPos){
        double x = adjustedPos.getX();
        double y = adjustedPos.getY();
        double z = adjustedPos.getZ();
        double[] pos = new double[2];
        if(z >= 0){
            pos[0] = Math.atan2(x, Math.pow((y*y + z*z), .5)); pos[1] = Math.atan2(y, Math.pow((x*x + z*z), .5));
        }else{
            pos[0] = Math.atan2(x, -Math.pow((y*y + z*z), .5)); pos[1] = Math.atan2(y, Math.pow((x*x + z*z), .5));
        }
        return pos;
    }
    
    public static double[] calcRealPos(double[] anglePosition){
    	double[] realPos = {anglePosition[0]*screenWidth/horizontalFOV + screenWidth/2, 
                		    -anglePosition[1]*screenWidth/horizontalFOV + screenHeight/2};
        return realPos;
    }
    
    public static Point rotate(Point axis, Point point, double q){
    	double x = point.getX();
    	double y = point.getY();
    	double z = point.getZ();
    	double a = axis.getX();
    	double b = axis.getY();
    	double c = axis.getZ();
    	double a2 = a*a;
    	double b2 = b*b;
    	double c2 = c*c;
    	double sinq = Math.sin(q);
    	double cosq = Math.cos(q);
    	if(a == 0 && b == 0){
    		if(c < 0){
        		point.setX(cosq*x + sinq*y);
        		point.setY(-sinq*x + cosq*y);
        		point.setZ(z);
    		}else{
        		point.setX(cosq*x - sinq*y);
        		point.setY(sinq*x + cosq*y);
        		point.setZ(z);
    		}
    	}else{
        	point.setX(
        			  (((a2*c2 + b2)/(a2 + b2))*cosq + a2)*x
        			+ (a*b*(1 - cosq) - c*sinq)*y
        			+ (a*c*(1 - cosq) + b*sinq)*z
        			);
        	point.setY(
        			  (a*b*(1 - cosq) + c*sinq)*x
        			+ (((b2*c2 + a2)/(a2 + b2))*cosq + b2)*y
        			+ (b*c*(1 - cosq) - a*sinq)*z
        			);
        	point.setZ(
        			  (a*c*(1 - cosq) - b*sinq)*x
        			+ (b*c*(1 - cosq) + a*sinq)*y
        			+ ((a2 + b2)*cosq + c2)*z
        			);
    	}
    	return point;
    }
}
