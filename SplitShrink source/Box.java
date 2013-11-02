package edu.ncssm.splitshrink;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;

class Box{
	
	private LineSegment[] edges;
	private Plane[] faces;
	private ArrayList<ArrayList<Integer>> planeEdges;
	private Color lineColor;
	
	private boolean[] isFront;

	public LineSegment[] getEdges() {
		return edges;
	}
	public Plane[] getFaces() {
		return faces;
	}

	public void setEdges(LineSegment[] edges) {
		this.edges = edges;
	}
	public void setFaces(Plane[] faces) {
		this.faces = faces;
	}

	/*public Box(Plane topPlane,
			   Plane frontPlane, Plane leftPlane, Plane backPlane, Plane rightPlane,
			   Plane bottomPlane){
		//numbering conventions are in papers
		//basically, it's top-to-bottom, clockwiseLineSegment[] edges = new LineSegment[12];
		faces = new Plane[6];
		edges = new LineSegment[12];
		faces[0] = topPlane;
		faces[1] = frontPlane;
		faces[2] = leftPlane;
		faces[3] = backPlane;
		faces[4] = rightPlane;
		faces[5] = bottomPlane;
		Point[] points =
			{Box.findIntersection(faces[0], faces[1], faces[2]),
			Box.findIntersection(faces[0], faces[2], faces[3]),
			Box.findIntersection(faces[0], faces[3], faces[4]),
			Box.findIntersection(faces[0], faces[4], faces[1]),
			Box.findIntersection(faces[5], faces[1], faces[2]),
			Box.findIntersection(faces[5], faces[2], faces[3]),
			Box.findIntersection(faces[5], faces[3], faces[4]),
			Box.findIntersection(faces[5], faces[4], faces[1]),
			};
		edges[0] = new LineSegment(points[0], points[1]);
		edges[1] = new LineSegment(points[1], points[2]);
		edges[2] = new LineSegment(points[2], points[3]);
		edges[3] = new LineSegment(points[3], points[0]);
		edges[4] = new LineSegment(points[0], points[4]);
		edges[5] = new LineSegment(points[1], points[5]);
		edges[6] = new LineSegment(points[2], points[6]);
		edges[7] = new LineSegment(points[3], points[7]);
		edges[8] = new LineSegment(points[4], points[5]);
		edges[9] = new LineSegment(points[5], points[6]);
		edges[10] = new LineSegment(points[6], points[7]);
		edges[11] = new LineSegment(points[7], points[4]);
		
	}*/
	
	public Box(Plane[] faces, int[][] pointFaces, int[][] linePoints, Color lineColor){
		this.faces = faces;
		this.lineColor = lineColor;
		Point[] points = new Point[pointFaces.length];
		for(int i = 0; i < pointFaces.length; i++){
			points[i] = Box.findIntersection(faces[pointFaces[i][0]],
											 faces[pointFaces[i][1]],
											 faces[pointFaces[i][2]]
											 );
		}
		edges = new LineSegment[linePoints.length];
		for(int i = 0; i < linePoints.length; i++){
			edges[i] = new LineSegment(points[linePoints[i][0]],
									   points[linePoints[i][1]]
									   );
		}
		planeEdges = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < faces.length; i++){
			planeEdges.add(new ArrayList<Integer>());
		}
		for(int i = 0; i < edges.length; i++){
			for(int plane1:pointFaces[linePoints[i][0]]){
				for(int plane2:pointFaces[linePoints[i][1]]){
					if(plane1 == plane2){
						planeEdges.get(plane1).add(i);
					}
				}
			}
		}
		isFront = new boolean[edges.length];
		Arrays.fill(isFront, false);
	}
	
	public static Point findIntersection(Plane plane1, Plane plane2, Plane plane3){
		//intersection of 2 planes to line, then intersection of line with remaining plane
		double a1 = plane1.getA(); double b1 = plane1.getB(); double c1 = plane1.getC(); double k1 = plane1.getK();
		double a2 = plane2.getA(); double b2 = plane2.getB(); double c2 = plane2.getC(); double k2 = plane2.getK();
		double a3 = plane3.getA(); double b3 = plane3.getB(); double c3 = plane3.getC(); double k3 = plane3.getK();
		//find line intersection of plane1 and plane2
		double dx = b1*c2 - c1*b2;
		double dy = c1*a2 - a1*c2;
		double dz = a1*b2 - b1*a2;
		double x0, y0, z0;
		if(a1*b2 == a2*b1){
			x0 = 0;
			y0 = (c2*k1 - c1*k2)/(b2*c1 - b1*c2);
			z0 = (b2*k1 - b1*k2)/(b1*c2 - b2*c1);
		}else{
			x0 = (b2*k1 - b1*k2)/(a2*b1 - a1*b2);
			y0 = (a2*k1 - a1*k2)/(a1*b2 - a2*b1);
			z0 = 0;
		}
		//find point on line on plane3
		double t = -(a3*x0 + b3*y0 + c3*z0 + k3)/(a3*dx + b3*dy + c3*dz);
		double x = x0 + dx*t;
		double y = y0 + dy*t;
		double z = z0 + dz*t;
		Point point = new Point(x, y, z);
		return point;
	}
	
	static class Plane {
	
		//plane with equation ax + by + cz + k = 0
		private double a, b, c, k;
		
		public Plane(double a, double b, double c, double k){
			//<a, b, c> : perpendicular vector **from** origin **to** plane
			//k : distance from origin to plane
			this.a = -a/Math.pow(a*a + b*b + c*c, .5); //normalize; makes more useful
			this.b = -b/Math.pow(a*a + b*b + c*c, .5);
			this.c = -c/Math.pow(a*a + b*b + c*c, .5);
			this.k = k; //k is distance from origin
			if(this.k < 0){ //make k >= 0
				this.a *= -1;
				this.b *= -1;
				this.c *= -1;
				this.k *= -1;
			}
		}
		
		public double getA() {
			return a;
		}
		public double getB() {
			return b;
		}
		public double getC() {
			return c;
		}
		public double getK() {
			return k;
		}

		public void setValues(double a, double b, double c, double k){
			this.a = a/Math.pow(a*a + b*b + c*c, .5);
			this.b = b/Math.pow(a*a + b*b + c*c, .5);
			this.c = c/Math.pow(a*a + b*b + c*c, .5);
			this.k = k/Math.pow(a*a + b*b + c*c, .5);
			if(k < 0){
				this.a *= -1;
				this.b *= -1;
				this.c *= -1;
				this.k *= -1;
			}
		}
	}
	
	static class LineSegment{
		//parametrically defined line of form x = x0 + (x1 - x0)*t, etc.
		//from t = 0 to t = 1
		double x0, y0, z0;
		double x1, y1, z1;
		
		public LineSegment(Point point1, Point point2){
			this.x0 = point1.getX();
			this.y0 = point1.getY();
			this.z0 = point1.getZ();
			this.x1 = point2.getX();
			this.y1 = point2.getY();
			this.z1 = point2.getZ();
		}
		
		public double getLength(){
			double length = Math.pow((x1 - x0)*(x1 - x0) + (y1 - y0)*(y1 - y0) + (z1 - z0)*(z1 - z0), .5);
			return length;
		}

		public double getX0() {
			return x0;
		}
		public double getY0() {
			return y0;
		}
		public double getZ0() {
			return z0;
		}
		public double getX1() {
			return x1;
		}
		public double getY1() {
			return y1;
		}
		public double getZ1() {
			return z1;
		}
		
		public void setX0(double x0) {
			this.x0 = x0;
		}
		public void setY0(double y0) {
			this.y0 = y0;
		}
		public void setZ0(double z0) {
			this.z0 = z0;
		}
		public void setX1(double x1) {
			this.x1 = x1;
		}
		public void setY1(double y1) {
			this.y1 = y1;
		}
		public void setZ1(double z1) {
			this.z1 = z1;
		}
	}
	
	public void findFrontLines(Point playerPos){
		Arrays.fill(isFront, false);
		double a, b, c, k, x, y, z;
		x = playerPos.getX();
		y = playerPos.getY();
		z = playerPos.getZ();
		for(int i = 0; i < faces.length; i++){
			a = faces[i].getA();
			b = faces[i].getB();
			c = faces[i].getC();
			k = faces[i].getK();
			if(a*x + b*y + c*z + k < 0){
				for(int j = 0; j < planeEdges.get(i).size(); j++){
					isFront[planeEdges.get(i).get(j)] = true;
				}
			}
		}
	}
	
	public void drawBox(Graphics2D g, Point playerSpherePos, Point playerPos, double[][] playerLOS, double angularDistance, Color bgColor, double fogDist, boolean front){
		for(int j = 0; j < edges.length; j++){
			if(isFront[j] == front){
				g.setColor(Color.black);
				//adjusted start and end points
				Point point0 = Resource.adjustPos(edges[j].getX0() - playerPos.getX(),
												  edges[j].getY0() - playerPos.getY(),
												  edges[j].getZ0() - playerPos.getZ(),
												  playerLOS);
				Point point1 = Resource.adjustPos(edges[j].getX1() - playerPos.getX(),
												  edges[j].getY1() - playerPos.getY(),
												  edges[j].getZ1() - playerPos.getZ(),
												  playerLOS);
				double dx = point1.getX() - point0.getX();
				double dy = point1.getY() - point0.getY();
				double dz = point1.getZ() - point0.getZ();
				double x = point0.getX();
				double y = point0.getY();
				double z = point0.getZ();
				double i = 0;
				Point startAdjustPos = new Point(x, y, z);
				double[] startAnglePos = Resource.calcAnglePos(startAdjustPos);
				double[] endAnglePos = new double[2];
				double maxAdder = Resource.getMinEdge()/edges[j].getLength();
				while(i < 1){
					//re-evaluate i
					double dThetaXdt = (y*y + z*z - 2*x*(y + z)*(dy + dz))/((x*x + y*y + z*z)*Math.pow((y*y + z*z), .5));
		            double dThetaYdt = (x*x + z*z - 2*y*(x + z)*(dx + dz))/((x*x + y*y + z*z)*Math.pow((y*y + z*z), .5));
		            double adder = angularDistance/(Math.pow((dThetaXdt*dThetaXdt + dThetaYdt*dThetaYdt), .5));
		            if(adder > maxAdder){adder = maxAdder;}
		            i += adder;
		            if(i > 1){
		            	i = 1;
		                x = point0.getX() + dx;
		                y = point0.getY() + dy;
		                z = point0.getZ() + dz;
		            }else{
		                x += adder*dx;
		                y += adder*dy;
		                z += adder*dz;
		            }
		            
		            //find end position
					Point endAdjustPos = new Point(x, y, z);
					endAnglePos = Resource.calcAnglePos(endAdjustPos);
					//draw line
					if (Math.abs(startAnglePos[0] - endAnglePos[0]) < Math.PI && Math.abs(startAnglePos[1] - endAnglePos[1]) < Math.PI //no wraparound
		                    && !(startAdjustPos.getZ() < 0 && endAdjustPos.getZ() < 0)){ //lines behind viewer are not drawn
		            		double[] realStart = Resource.calcRealPos(startAnglePos);
		            		double[] realEnd = Resource.calcRealPos(endAnglePos);
		            		double dist = endAdjustPos.dist(Resource.adjustPos(playerSpherePos.getX() - playerPos.getX(),
		            														   playerSpherePos.getY() - playerPos.getY(),
		            														   playerSpherePos.getZ() - playerPos.getZ(),
		            														   playerLOS));
		            		double c1, c2;
		            		if(dist > fogDist){
		            			c2 = 1;
		            			c1 = 0;
		            		}else{
		            			c2 = dist/fogDist;
		            			c1 = 1 - c2;
		            		}
		            		g.setColor(new Color((int) ((lineColor.getRed()*c1 + bgColor.getRed()*c2)/(c1 + c2)),
		            							 (int) ((lineColor.getGreen()*c1 + bgColor.getGreen()*c2)/(c1 + c2)),
		            							 (int) ((lineColor.getBlue()*c1 + bgColor.getBlue()*c2)/(c1 + c2)),
		            							 lineColor.getAlpha()
		            							 ));
		            		g.draw(new Line2D.Double(realStart[0], realStart[1], realEnd[0], realEnd[1]));
					}
					//set start as old end
					startAdjustPos = new Point(x, y, z);
					startAnglePos = Resource.calcAnglePos(endAdjustPos);
				}
			}
		}
	}
}
