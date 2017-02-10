package spatial.util;

import spatial.bo.SpatialGeometry;

public class SpatialDistanceUtil {

	public double distance(double x,double y,double x1,double y1,double x2,double y2){
		double A = x-x1;
		double B = y-y1;
		double C = x2-x1;
		double D = y2-y1;
		
		double dot = A*C + B*D;
		double length = C*C + D*D;
		double param = -1;
		double xx,yy;
		
		if(length!= 0){
			param = dot/length;
		}
		
		if(param<0){
			xx = x1;
			yy = y1;
		}
		else if(param>1){
			xx = x2;
			yy = y2;
		}
		else {
			xx = x1 + param * C;
			yy = y1 + param * D;
		}
	
		double dx = x - xx;
		double dy = y - yy;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	//public void coordinatesOfGeo(double coordinateA[], double coordinateB[], String shape,String shape1)
	public void coordinatesOfGeo(SpatialGeometry spatialGeo1, SpatialGeometry spatialGeo2)
	{
		double mindist = 99999; double currentDist = 0;
		double[] coordinateA = spatialGeo1.coordinates;
		double[] coordinateB = spatialGeo2.coordinates;
		String shape = spatialGeo1.getGeometryShape().getShapeName();
		String shape1 = spatialGeo2.getGeometryShape().getShapeName();
		double Ax1; double Ay1; double Ax3; double Ay3;
		//find co ordinates to form a rectangle
		double Ax2; double Ay2; double Ax4;	double Ay4;
		double Bx1;	double By1; double Bx3; double By3;
		//find co ordinates to form a rectangle
		double Bx2; double By2; double Bx4; double By4;
		
		//we need to fetch co ordinates of the respective shape
		// we expect 4 co ordinates so that we can create 2 rectangles 
		Coordinate[] coordArrA = null; Coordinate[] coordArrB = null;
		if(!shape.equalsIgnoreCase("circle") && !shape1.equalsIgnoreCase("circle")){
			if(shape.equalsIgnoreCase("rectangle")){
				coordArrA = new Coordinate[5];
				Ax1 = coordinateA[0]; Ay1 = coordinateA[1]; Ax3 = coordinateA[2]; Ay3 = coordinateA[3];
				//find co ordinates to form a rectangle
				Ax2 = Ax3; Ay2 = Ay1;
				Ax4 = Ax1; Ay4 = Ay3;
				coordArrA[0] = new Coordinate(Ax1,Ay1); coordArrA[1] = new Coordinate(Ax2,Ay2); coordArrA[2] = new Coordinate(Ax3,Ay3);
				coordArrA[3] = new Coordinate(Ax4,Ay4); coordArrA[4] = new Coordinate(Ax1, Ay1);
			}else if(shape.equalsIgnoreCase("triangle")){
				coordArrA = new Coordinate[4];
				coordArrA[0] = new Coordinate(coordinateA[0], coordinateA[1]);coordArrA[1] = new Coordinate(coordinateA[2],coordinateA[3]);
				coordArrA[2]= new Coordinate(coordinateA[4],coordinateA[5]);coordArrA[3]= new Coordinate(coordinateA[0], coordinateA[1]);
			}
			if(shape1.equalsIgnoreCase("rectangle")){
				Bx1 = coordinateB[0]; By1 = coordinateB[1]; Bx3 = coordinateB[2]; By3 = coordinateB[3];
				//find co ordinates to form a rectangle
				Bx2 = Bx3; By2 = By1;
				Bx4 = Bx1; By4 = By3;
				coordArrB = new Coordinate[4];
				coordArrB[0] = new Coordinate(Bx1, By1); coordArrB[1] = new Coordinate(Bx2,By2);  coordArrB[2] = new Coordinate(Bx3,By3);
				coordArrB[3] = new Coordinate(Bx4,By4);
			}else if(shape1.equalsIgnoreCase("triangle")){
				coordArrB = new Coordinate[4];
				coordArrB[0] = new Coordinate(coordinateB[0],coordinateB[1]); coordArrB[1] = new Coordinate(coordinateB[2],coordinateB[3]);
				coordArrB[2] = new Coordinate(coordinateB[4],coordinateB[5]); coordArrB[3] = new Coordinate(coordinateB[0],coordinateB[1]);
			}
			for(int j=0;j<coordArrB.length;j++){
	 			for(int i=0;i<coordArrA.length-1;i++)
				{
				 currentDist = distance(coordArrB[j].getX(), coordArrB[j].getY(), coordArrA[i].getX(), coordArrA[i].getY(), 
						 coordArrA[i+1].getX(), coordArrA[i+1].getY());
				 if(currentDist<mindist)
					 mindist = currentDist;
				 if(j+1 < coordArrB.length){
					 currentDist = distance(coordArrA[i].getX(), coordArrA[i].getY(), coordArrB[j].getX(), coordArrB[j].getY(), 
							 coordArrB[j+1].getX(), coordArrB[j+1].getY());
					 if(currentDist<mindist)
						 mindist = currentDist; }
				}
			}
			for(int i=0;i<coordArrA.length-1;i++){
				currentDist = distance(coordArrA[i].getX(), coordArrA[i].getY(), coordArrB[coordArrB.length-1].getX(), coordArrB[coordArrB.length-1].getY(), 
						 coordArrB[0].getX(), coordArrB[0].getY());
				 if(currentDist<mindist)
					 mindist = Math.round(currentDist * 100.0) / 100.0;
			}
			System.out.println("The distance between the two geometries is " + Math.round(mindist*100.0)/100.0);
		}
		else if(shape.equalsIgnoreCase("circle") && shape1.equalsIgnoreCase("circle")){
			double x1 = coordinateA[0];
			double y1 = coordinateA[3]; 
			double r1 = coordinateA[2]-x1;
			
			double x2 = coordinateB[0];
			double y2 = coordinateB[3];
			double r2 = coordinateB[2]-x2;
			 
			double dist = Math.sqrt((x2-x1)*(x2-x1)+ (y2-y1)*(y2-y1));
			if(dist!=0.0)
				dist = Math.round((dist-r1-r2)*100.0)/100.0;
			System.out.println("The distance between the two circles is " + Math.round(dist*100.0)/100.0);
		}
		else{
			double r2 = 0;
			if(shape.equalsIgnoreCase("circle")){
				double x1 = coordinateA[0];
				double y1 = coordinateA[3];
				r2 = coordinateA[2]-x1;
				coordArrB = new Coordinate[1];
				coordArrB[0] = new Coordinate(x1,y1);
				if(shape1.equalsIgnoreCase("rectangle")){
					coordArrA = new Coordinate[5];
					Ax1 = coordinateB[0]; Ay1 = coordinateB[1]; Ax3 = coordinateB[2]; Ay3 = coordinateB[3];
					//find co ordinates to form a rectangle
					Ax2 = Ax3; Ay2 = Ay1;
					Ax4 = Ax1; Ay4 = Ay3;
					coordArrA[0] = new Coordinate(Ax1,Ay1); coordArrA[1] = new Coordinate(Ax2,Ay2); coordArrA[2] = new Coordinate(Ax3,Ay3);
					coordArrA[3] = new Coordinate(Ax4,Ay4); coordArrA[4] = new Coordinate(Ax1, Ay1);
				}else if(shape1.equalsIgnoreCase("triangle")){
					coordArrA = new Coordinate[4];
					coordArrA[0] = new Coordinate(coordinateB[0], coordinateB[1]);coordArrA[1] = new Coordinate(coordinateB[2],coordinateB[3]);
					coordArrA[2]= new Coordinate(coordinateB[4],coordinateB[5]);coordArrA[3]= new Coordinate(coordinateB[0], coordinateB[1]);
				}
			}
			if(shape1.equalsIgnoreCase("circle")){
				double x1 = coordinateB[0];
				double y1 = coordinateB[3]; 
				r2 = coordinateB[2]-x1;
				coordArrB = new Coordinate[1];
				coordArrB[0] = new Coordinate(x1,y1);
				if(shape.equalsIgnoreCase("rectangle")){
					coordArrA = new Coordinate[5];
					Ax1 = coordinateA[0]; Ay1 = coordinateA[1]; Ax3 = coordinateA[2]; Ay3 = coordinateA[3];
					//find co ordinates to form a rectangle
					Ax2 = Ax3; Ay2 = Ay1;
					Ax4 = Ax1; Ay4 = Ay3;
					coordArrA[0] = new Coordinate(Ax1,Ay1); coordArrA[1] = new Coordinate(Ax2,Ay2); coordArrA[2] = new Coordinate(Ax3,Ay3);
					coordArrA[3] = new Coordinate(Ax4,Ay4); coordArrA[4] = new Coordinate(Ax1, Ay1);
				}else if(shape.equalsIgnoreCase("triangle")){
					coordArrA = new Coordinate[4];
					coordArrA[0] = new Coordinate(coordinateA[0], coordinateA[1]);coordArrA[1] = new Coordinate(coordinateA[2],coordinateA[3]);
					coordArrA[2]= new Coordinate(coordinateA[4],coordinateA[5]);coordArrA[3]= new Coordinate(coordinateA[0], coordinateA[1]);
				}
			}
			for(int j=0;j<coordArrB.length;j++){
	 			for(int i=0;i<coordArrA.length-1;i++)
				{
				 currentDist = distance(coordArrB[j].getX(), coordArrB[j].getY(), coordArrA[i].getX(), coordArrA[i].getY(), 
						 coordArrA[i+1].getX(), coordArrA[i+1].getY());
				 if(currentDist<mindist)
					 mindist = Math.round(currentDist * 100.0) / 100.0;
				 }
			}
			if(mindist!=0.0)
			mindist = Math.round((mindist - r2) * 100.0)/100.0;
			
			System.out.println("The distance between the two geometries is " + Math.round(mindist*100.0)/100.0);
		}
	}
	
}

