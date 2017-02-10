/**
 * 
 */
package btree;

import java.util.Arrays;

/**
 * @author Gerard
 */
public class MBRKey extends KeyClass
{
	/** Stores co-ordinates with ',' seperated with x1,y1,x2,y2 */
	private double[] indexKey;
	
	/**
	 * Class constructor
	 * 
	 * @param value
	 *            the value of the integer key to be set
	 */
	public MBRKey(double[] coordinates)
	{
		this.indexKey = new double[4];
		this.indexKey = coordinates;
	}
	
	/**
	 * @return
	 */
	public double[] getKey()
	{
		return this.indexKey;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "MBRKey [indexKey=" + Arrays.toString(indexKey) + "]";
	}
	
}
