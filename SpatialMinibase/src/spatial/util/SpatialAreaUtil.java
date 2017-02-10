/**
 * 
 */
package spatial.util;

import java.io.DataInputStream;
import java.io.IOException;

import spatial.bo.SpatialGeometry;
import spatial.data.GeometryShape;

/**
 * @author Gerard
 */
public class SpatialAreaUtil
{
	/**
	 * @param spatialGeometry
	 * @return
	 */
	public static double area(SpatialGeometry spatialGeometry)
	{
		double area = 0;
		switch (spatialGeometry.getGeometryShape().getShapeId())
		{
			case 1:
				area = rectangleArea(spatialGeometry.getCoordinates());
				break;
			case 2:
				area = circleArea(spatialGeometry.getCoordinates());
				break;
			case 3:
				area = triangleArea(spatialGeometry.getCoordinates());
				break;
			default:
				break;
		}
		return Math.round(area * 100.0) / 100.0;
	}
	
	/**
	 * @param coordinates
	 * @return
	 */
	private static double circleArea(double[] coordinates)
	{
		double x1 = coordinates[0];
		double y1 = coordinates[1];
		double x3 = coordinates[4];
		double y3 = coordinates[5];
		double diameter = (y3-y1);
		double radius = diameter / 2;
		double area = 3.14 * radius * radius;
		return area;
	}
	
	/**
	 * As we know to calculate the area we need at the most 2-vertices and that is 4-coordinates
	 * @param coordinates {@link Double}
	 * @return {@link Double}
	 */
	private static double rectangleArea(double[] coordinates)
	{
		double x1 = coordinates[0];
		double y1 = coordinates[1];
		double x2 = coordinates[2];
		double y2 = coordinates[3];
		double length = x2 - x1;
		double width = y2 - y1;
		double area = length * width;
		if (area < 0)
			area = area * -1;
		return area;
	}
	
	private static double triangleArea(double[] coordinates){
		double x1 = coordinates[0];
		double y1 = coordinates[1];
		double x2 = coordinates[2];
		double y2 = coordinates[3];
		double x3 = coordinates[4];
		double y3 = coordinates[5];
		
		double s,a,b,c;
		a = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
		b = Math.sqrt((x3-x2)*(x3-x2) + (y3-y2)*(y3-y2));
		c = Math.sqrt((x1-x3)*(x1-x3) + (y1-y3)*(y1-y3));
		s = (a + b + c)/2;
		double area = Math.round(Math.sqrt(s*(s-a)*( s-b)*(s-c)));
		return area;				
	}
}
