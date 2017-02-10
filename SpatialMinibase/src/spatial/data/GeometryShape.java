/**
 * 
 */
package spatial.data;

/**
 * @author Gerard
 */
public enum GeometryShape
{
	/* Rectangle stores x1, y1, x2, y2 */
	RECTANGLE(1, "rectangle", 4),
	
	/* Circle stores x1,y1(bottom) x2,y2(right) x3,y3(top) */
	CIRCLE(2, "circle", 6),
	
	/* Triangle stores x1, y1, x2, y2, x3, y3 */
	TRIANGLE(3, "triangle", 6), 
	 
	/* Point stores x1,y1 */
	POINT(4, "point", 2);
	
	private int    shapeId;
	private String shapeName;
	private int    numberOfDimensionCoordinates;
	
	/**
     * 
     */
	private GeometryShape(int shapeId, String shapeName,
	        int numberOfDimensionCoordinates)
	{
		this.shapeId = shapeId;
		this.shapeName = shapeName;
		this.numberOfDimensionCoordinates = numberOfDimensionCoordinates;
	}
	
	/**
	 * @return the shapeId
	 */
	public int getShapeId()
	{
		return shapeId;
	}
	
	/**
	 * @return the shapeName
	 */
	public String getShapeName()
	{
		return shapeName;
	}
	
	/**
	 * @return the numberOfDimensionCoordinates
	 */
	public int getNumberOfDimensionCoordinates()
	{
		return numberOfDimensionCoordinates;
	}
	
}
