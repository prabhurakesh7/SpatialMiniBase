/**
 * 
 */
package spatial.test;

import global.AttrType;
import global.RID;
import global.SystemDefs;
import heap.Heapfile;
import heap.Tuple;

import java.util.Vector;

import spatial.bo.AreaPopulation;
import spatial.bo.SpatialGeometry;
import spatial.data.GeometryShape;

/**
 * @author Gerard
 * @author Sooraj
 */
public class TestSpatialCreateInsertStmt
{
	private static final String dbpath = "./data/" + System.getProperty("user.name") + "SpatialGeometry.minibase-db";
	private boolean status = true;
	Tuple tupleRecord = null;
	@SuppressWarnings("rawtypes")
	private Vector areaPopulation = new Vector();
	
	void createDB()
	{
		new SystemDefs(dbpath, 1000, 100, "Clock");
	}
	
	Heapfile createTable(String tableName){
		Heapfile heapFile = null;
		
		//Create column types for area population 
		AttrType[] columnTypes = new AttrType[3];
		columnTypes[0] = new AttrType(AttrType.attrInteger);
		columnTypes[1] = new AttrType(AttrType.attrString);
		columnTypes[2] = new AttrType(AttrType.attrSpatialGeometry);
	
		//Taken as is from existing minibase code 
		short[] Ssizes = new short[1];
		Ssizes[0] = 30;
		
		tupleRecord = insertTupleHeader(columnTypes, Ssizes);
		try{
			heapFile = new Heapfile(tableName + ".tbl");
		}catch (Exception e)
		{
			System.err.println("Error in creating heapfile Message : " + e.getMessage());
			status = false;
			e.printStackTrace();
		}
		System.out.println("Table '" + tableName + "' created successfully!");
		return heapFile;
	}
	
	private Tuple insertTupleHeader(AttrType[] columnTypes, short[] Ssizes)
	{
		Tuple tupleRecord = new Tuple();
		return setTupleHeader(columnTypes, Ssizes, tupleRecord);
	}
	
	private Tuple setTupleHeader(AttrType[] columnTypes, short[] Ssizes, Tuple tupleRecord)
	{
		try{
			tupleRecord.setHdr((short) 3, columnTypes, Ssizes);
		}catch (Exception e){
			System.err.println("Error in creating tuple header information Message " + e.getMessage());
			status = false;
			e.printStackTrace();
		}
		return tupleRecord;
	}

	@SuppressWarnings("unchecked")
	void insertRecord(Heapfile heapfile, int areaPopulationId, String areaPopulationName, int shapeId, String[] coordParts)
	{
		GeometryShape shape;
		switch(shapeId){
		case(1):
			shape = GeometryShape.RECTANGLE; break;
		case(2):
			shape = GeometryShape.CIRCLE; break;
		case(3):
			shape = GeometryShape.TRIANGLE; break;
		default:
			shape = GeometryShape.POINT; break;
		}
		
		double[] areaCoordinates = new double[coordParts.length];
		for(int i=0;i<coordParts.length;i++)
			areaCoordinates[i] = Double.parseDouble(coordParts[i]);
		
		areaPopulation.addElement(new AreaPopulation(areaPopulationId, areaPopulationName, new SpatialGeometry(shape, areaCoordinates)));
	
		//Create column types for area population 
		AttrType[] columnTypes = new AttrType[3];
		columnTypes[0] = new AttrType(AttrType.attrInteger);
		columnTypes[1] = new AttrType(AttrType.attrString);
		columnTypes[2] = new AttrType(AttrType.attrSpatialGeometry);
	
		//Taken as is from existing minibase code 
		short[] Ssizes = new short[1];
		Ssizes[0] = 30;
		RID rid = insertRecordToTable(heapfile, tupleRecord, columnTypes, Ssizes);
		
		if (status)
		{
			System.out.println("Insert successful!"); // RID : " + rid);
		}
		else
		{
			System.out.println("Insert error");
		}
	}
	
	/**
	 * @param tupleRecord {@link Tuple}
	 * @param ssizes
	 * @param columnTypes
	 * @return {@link RID}
	 */
	private RID insertRecordToTable(Heapfile heapfile, Tuple tupleRecord, AttrType[] columnTypes, short[] ssizes)
	{
		RID rid = null;
		int size = tupleRecord.size();
		Tuple tuple = new Tuple(size);
		
		tuple = setTupleHeader(columnTypes, ssizes, tuple);
		try
		{
			tuple.setIntFld(1, ((AreaPopulation) areaPopulation.elementAt(areaPopulation.size() - 1)).getAreaPopulationId());
			tuple.setStrFld(2, ((AreaPopulation) areaPopulation.elementAt(areaPopulation.size() - 1)).getAreaPopulationName());
			tuple.setSpatialGeomtryFld(3, ((AreaPopulation) areaPopulation.elementAt(areaPopulation.size() - 1)).getAreaPopulationGeometry());
			rid = heapfile.insertRecord(tuple.returnTupleByteArray());
		}
		catch (Exception e)
		{
			System.err.println("Heap file error in creating tuple with record Message : " + e.getMessage());
			status = false;
			e.printStackTrace();
		}
		return rid;
	}
	
	/**
	 * @return the tupleRecord
	 */
	public Tuple getTupleRecord()
	{
		return tupleRecord;
	}
}
