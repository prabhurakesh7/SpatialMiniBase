/**
 * 
 */
package spatial.bo;

/**
 * Defination of the AreaPopulation Schema
 * 
 * @author Gerard
 */
public class AreaPopulation
{
	public static final String TABLE_NAME = "Population";
	private int                areaPopulationId;
	private String             areaPopulationName;
	private SpatialGeometry    areaPopulationGeometry;
	
	public AreaPopulation(int areaPopulationId, String areaPopulationName,
	        SpatialGeometry areaPopulationGeometry)
	{
		super();
		this.areaPopulationId = areaPopulationId;
		this.areaPopulationName = areaPopulationName;
		this.areaPopulationGeometry = areaPopulationGeometry;
	}
	
	/**
	 * @return the areaPopulationId
	 */
	public int getAreaPopulationId()
	{
		return areaPopulationId;
	}
	
	/**
	 * @return the areaPopulationName
	 */
	public String getAreaPopulationName()
	{
		return areaPopulationName;
	}
	
	/**
	 * @return the areaPopulationGeometry
	 */
	public SpatialGeometry getAreaPopulationGeometry()
	{
		return areaPopulationGeometry;
	}
	
}
