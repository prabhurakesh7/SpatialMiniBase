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
public class SpatialUtil
{
	/**
	 * Based on the shape type returns {@link SpatialGeometry}
	 * 
	 * @param dataInputStream
	 *            {@link DataInputStream}
	 * @return
	 * @throws IOException
	 */
	public static SpatialGeometry getGeometryShape(
	        DataInputStream dataInputStream) throws IOException
	{
		SpatialGeometry spatialGeometry = null;
		short shapeType = dataInputStream.readShort();
		switch (shapeType)
			{
				case 1:// case for rectangle
					spatialGeometry = rectangleShape(dataInputStream);
					break;
				case 2:// case for circle
					spatialGeometry = circleShape(dataInputStream);
					break;
				case 3:// case for triangle
					spatialGeometry = triangleShape(dataInputStream);
					break;
				default:
					break;
			}
		return spatialGeometry;
	}
	
	/**
	 * @param dataInputStream
	 * @return
	 * @throws IOException
	 */
	private static SpatialGeometry circleShape(DataInputStream dataInputStream)
	        throws IOException
	{
		double x1 = dataInputStream.readDouble();
		double y1 = dataInputStream.readDouble();
		double x2 = dataInputStream.readDouble();
		double y2 = dataInputStream.readDouble();
		double x3 = dataInputStream.readDouble();
		double y3 = dataInputStream.readDouble();
		double[] coordinates = { x1, y1, x2, y2, x3, y3 };
		SpatialGeometry spatialGeometry = new SpatialGeometry(
		        GeometryShape.CIRCLE, coordinates);
		return spatialGeometry;
	}
	
	private static SpatialGeometry triangleShape(DataInputStream dataInputStream)
	        throws IOException
	{
		double x1 = dataInputStream.readDouble();
		double y1 = dataInputStream.readDouble();
		double x2 = dataInputStream.readDouble();
		double y2 = dataInputStream.readDouble();
		double x3 = dataInputStream.readDouble();
		double y3 = dataInputStream.readDouble();
		double[] coordinates = { x1, y1, x2, y2, x3, y3 };
		SpatialGeometry spatialGeometry = new SpatialGeometry(
		        GeometryShape.TRIANGLE, coordinates);
		return spatialGeometry;
	}
	
	/**
	 * Returns rectangle shape
	 * 
	 * @param dataInputStream
	 *            {@link DataInputStream}
	 * @return
	 * @throws IOException
	 */
	private static SpatialGeometry rectangleShape(
	        DataInputStream dataInputStream) throws IOException
	{
		double x1, y1, x2, y2;
		x1 = dataInputStream.readDouble();
		y1 = dataInputStream.readDouble();
		x2 = dataInputStream.readDouble();
		y2 = dataInputStream.readDouble();
		double[] coordinates = { x1, y1, x2, y2 };
		SpatialGeometry spatialGeometry = new SpatialGeometry(
		        GeometryShape.RECTANGLE, coordinates);
		return spatialGeometry;
		
	}
	
	/**
	 * @param spatialGeometry
	 * @return
	 */
	public static double[] getMBRIndexKey(SpatialGeometry spatialGeometry)
	{
		double[] index = null;
		switch (spatialGeometry.getGeometryShape().getShapeId())
			{
				case 1:// case for rectangle
					return spatialGeometry.getCoordinates();
				case 2:// case for circle
					return getCircleCoordinates(spatialGeometry);
				case 3://case for triangle;
					return getTriangleCoordinates(spatialGeometry);
				default:
					break;
			}
		
		return index;
	}
	/**
	 * @param spatialGeometry
	 * @return
	 */
	private static double[] getTriangleCoordinates(
	        SpatialGeometry spatialGeometry)
	{
		double[] coordinates = spatialGeometry.getCoordinates();
		double x1 = coordinates[0];
		double y1 = coordinates[1];
		double x2 = coordinates[2];
		double y2 = coordinates[3];
		double x3 = coordinates[4];
		double y3 = coordinates[5];
		
		double smallestX = Math.min(x1, Math.min(x2, x3));
		double largextX = Math.max(x1, Math.max(x2, x3));
		double smallestY = Math.min(y1, Math.min(y2, y3));
		double largextY = Math.max(y1, Math.max(y2, y3));
		
		double[] mbrCoordinates = new double[] { smallestX, smallestY,
		        largextX, largextY };
		return mbrCoordinates;
	}
	
	/**
	 * @param spatialGeometry
	 * @return
	 */
	private static double[] getCircleCoordinates(SpatialGeometry spatialGeometry)
	{
		double[] coordinates = spatialGeometry.getCoordinates();
		double bottomX = coordinates[0];
		double bottomY = coordinates[1];
		
		double topY = coordinates[5];
		double topX = coordinates[4];
		double radius = (topY - bottomY) / 2;
		
		double x1 = bottomX - radius;
		double y1 = bottomY;
		double x2 = topX + radius;
		double y2 = topY;
		
		double[] mbrCoordinates = new double[] { x1, y1, x2, y2 };
		return mbrCoordinates;
	}
	
}
