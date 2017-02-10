/**
 * 
 */
package spatial.bo;

import java.util.Arrays;

import spatial.data.GeometryShape;
import spatial.util.SpatialUtil;

/**
 * @author Gerard
 */
public class SpatialGeometry
{
	private GeometryShape geometryShape;
	public double[]       coordinates;
	
	public SpatialGeometry(GeometryShape geometryShape, double[] coordinates)
	{
		this.geometryShape = geometryShape;
		this.coordinates = coordinates;
	}
	
	/**
	 * @return the geometryShape
	 */
	public GeometryShape getGeometryShape()
	{
		return geometryShape;
	}
	
	/**
	 * @return the coordinates
	 */
	public double[] getCoordinates()
	{
		return coordinates;
	}
	
	/**
	 * The function is used to return the signle point that with the help of
	 * dimensions forms the minimum bounded rectangle to generate the index of
	 * the spatial geometry object
	 * 
	 * @return double[]
	 */
	public double[] getMBRIndexKey()
	{
		return SpatialUtil.getMBRIndexKey(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "SpatialGeometry [geometryShape=" + geometryShape
		        + ", coordinates=" + Arrays.toString(coordinates) + "]";
	}
	
}
