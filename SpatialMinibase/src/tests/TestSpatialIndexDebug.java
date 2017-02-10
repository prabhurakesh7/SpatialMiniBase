/**
 * 
 */
package tests;

import global.AttrOperator;
import global.AttrType;
import global.IndexType;
import global.RID;
import global.SystemDefs;
import heap.Heapfile;
import heap.Scan;
import heap.Tuple;
import index.IndexScan;
import iterator.CondExpr;
import iterator.FldSpec;
import iterator.RelSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import spatial.bo.AreaPopulation;
import spatial.bo.SpatialGeometry;
import spatial.data.GeometryShape;
import btree.BTreeFile;
import btree.MBRKey;

/**
 * @author Gerard
 */
public class TestSpatialIndexDebug
{
	private static final String dbpath         = "./data/"
	                                                   + System.getProperty("user.name")
	                                                   + "SpatialGeometry.minibase-db";
	private boolean             status         = true;
	@SuppressWarnings("rawtypes")
	private Vector              areaPopulation = new Vector();
	private Heapfile            heapFile       = null;
	private RID                 rid            = null;
	private Tuple               t              = null;
	
	public static void main(String[] args)
	{
		TestSpatialIndexDebug spatialIndex = new TestSpatialIndexDebug();
		spatialIndex.createSchema();
	}
	
	public void createSchema()
	{
		createDB();
		createRecords();
		createTable();
		createIndex();
	}
	
	/**
	 * 
	 */
	private void createIndex()
	{
		AttrType[] attrType = new AttrType[3];
		attrType[0] = new AttrType(AttrType.attrInteger);
		attrType[1] = new AttrType(AttrType.attrString);
		attrType[2] = new AttrType(AttrType.attrSpatialGeometry);
		short[] attrSize = new short[2];
		attrSize[0] = 32;
		attrSize[1] = 32;
		// create an scan on the heapfile
		Scan scan = null;
		try
		{
			scan = new Scan(heapFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Runtime.getRuntime().exit(1);
		}
		
		// create the index file
		BTreeFile btf = null;
		try
		{
			btf = new BTreeFile("RTreeIndex", AttrType.attrSpatialGeometry, 32,
			        1/* delete */);
			btf.traceFilename("./data/hello.txt");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Runtime.getRuntime().exit(1);
		}
		
		System.out.println("RTreeIndex created successfully.\n");
		
		rid = new RID();
		double[] key = null;
		Tuple temp = null;
		
		try
		{
			temp = scan.getNext(rid);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		while (temp != null)
		{
			t.tupleCopy(temp);
			
			try
			{
				key = t.getSpatialGeometryFld(3).getMBRIndexKey();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				btf.insert(new MBRKey(key), rid);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				temp = scan.getNext(rid);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// close the file scan
		scan.closescan();
		
		System.out.println("RTreeIndex file created successfully.\n");
		
		FldSpec[] projlist = new FldSpec[3];
		RelSpec rel = new RelSpec(RelSpec.outer);
		projlist[0] = new FldSpec(rel, 1);
		projlist[1] = new FldSpec(rel, 2);
		projlist[2] = new FldSpec(rel, 3);
		
		// set up an identity selection
		CondExpr[] expr = new CondExpr[2];
		expr[0] = new CondExpr();
		expr[0].op = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].type2 = new AttrType(AttrType.attrSpatialGeometry);
		expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 3);
		expr[0].operand2.double_array = new double[] { 1, 1, 2, 2 };
		expr[0].next = null;
		expr[1] = null;
		
		// start index scan
		IndexScan iscan = null;
		try
		{
			short[] Msizes = new short[1];
			Msizes[0] = 30;
			iscan = new IndexScan(new IndexType(IndexType.B_Index),
			        "areapopulation.tbl", "RTreeIndex", attrType, Msizes, 3, 3,
			        projlist, expr, 2, false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		int count = 0;
		t = null;
		String outval = null;
		
		try
		{
			t = iscan.get_next();
			if (t != null)
			{
				System.out.println();
				int areapopulationId = t.getIntFld(1);
				String areapopulationName = t.getStrFld(2);
				SpatialGeometry areapopulationGeometry = t
				        .getSpatialGeometryFld(3);
				System.out.println(areapopulationId + ", " + areapopulationName
				        + ", " + areapopulationGeometry);
			}
			else
			{
				System.out.println("NO-RECORDS");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		// clean up
		try
		{
			iscan.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		System.err
		        .println("------------------- TEST 1 completed ---------------------\n");
		
	}
	
	private void createDB()
	{
		new SystemDefs(dbpath, 1000, 100, "Clock");
	}
	
	/**
* 
*/
	@SuppressWarnings("unchecked")
	private void createRecords()
	{
		for (int index = 0; index < 30; index++)
		{
			double[] forestAreaCoordinates = new double[] { 1.0 + index,
			        1.0 + index, 2.0 + index, 2.0 + index };
			
			areaPopulation.addElement(new AreaPopulation(index, "Forest Area",
			        new SpatialGeometry(GeometryShape.RECTANGLE,
			                forestAreaCoordinates)));
			
		}
		double[] forestAreaCoordinates = new double[] { 9, 9, 11, 11 };
		areaPopulation.addElement(new AreaPopulation(22, "Forest Area",
		        new SpatialGeometry(GeometryShape.RECTANGLE,
		                forestAreaCoordinates)));
		/*
		 * double[] forestAreaCoordinates = new double[] { 5, 5, 9, 9 };
		 * areaPopulation.addElement(new AreaPopulation(1, "Forest Area",
		 * new SpatialGeometry(GeometryShape.RECTANGLE,
		 * forestAreaCoordinates)));
		 * double[] forestAreaCoordinates1 = new double[] { 5, 5, 7, 7 };
		 * areaPopulation.addElement(new AreaPopulation(2, "Forest Area",
		 * new SpatialGeometry(GeometryShape.RECTANGLE,
		 * forestAreaCoordinates1)));
		 */
	}
	
	private void createTable()
	{
		/* Create column types for area population */
		AttrType[] columnTypes = new AttrType[3];
		columnTypes[0] = new AttrType(AttrType.attrInteger);
		columnTypes[1] = new AttrType(AttrType.attrString);
		columnTypes[2] = new AttrType(AttrType.attrSpatialGeometry);
		
		/* Taken as is from existing minibase code */
		short[] Ssizes = new short[1];
		Ssizes[0] = 30;
		
		insertHeaderAndRecords(columnTypes, Ssizes);
		
	}
	
	/**
	 * @param columnTypes
	 * @param Ssizes
	 */
	private void insertHeaderAndRecords(AttrType[] columnTypes, short[] Ssizes)
	{
		t = insertTupleHeader(columnTypes, Ssizes);
		
		List<RID> rids = insertRecords(t, columnTypes, Ssizes);
		if (status)
		{
			System.out.println("Insert succesfull RID : " + rids);
		}
		else
		{
			System.out.println("Insert erorr");
		}
	}
	
	/**
	 * @param columnTypes
	 *            {@link AttrType}
	 * @param Ssizes
	 *            short
	 * @return {@link Tuple}
	 */
	private Tuple insertTupleHeader(AttrType[] columnTypes, short[] Ssizes)
	{
		Tuple tupleRecord = new Tuple();
		setTupleHeader(columnTypes, Ssizes, tupleRecord);
		return tupleRecord;
	}
	
	/**
	 * @param columnTypes
	 * @param Ssizes
	 * @param tupleRecord
	 * @return
	 */
	private Tuple setTupleHeader(AttrType[] columnTypes, short[] Ssizes,
	        Tuple tupleRecord)
	{
		try
		{
			tupleRecord.setHdr((short) 3, columnTypes, Ssizes);
		}
		catch (Exception e)
		{
			System.err
			        .println("Error in creating tuple header information Message "
			                + e.getMessage());
			status = false;
			e.printStackTrace();
		}
		return tupleRecord;
	}
	
	/**
	 * @param tupleRecord
	 *            {@link Tuple}
	 * @param ssizes
	 * @param columnTypes
	 * @return {@link RID}
	 */
	private List<RID> insertRecords(Tuple tupleRecord, AttrType[] columnTypes,
	        short[] ssizes)
	{
		try
		{
			heapFile = new Heapfile("areapopulation.tbl");
		}
		catch (Exception e)
		{
			System.err.println("Error in creating heapfile Message : "
			        + e.getMessage());
			status = false;
			e.printStackTrace();
		}
		
		int size = tupleRecord.size();
		Tuple tuple = new Tuple(size);
		
		tuple = setTupleHeader(columnTypes, ssizes, tuple);
		
		List<RID> rids = new ArrayList<RID>();
		for (int index = 0; index < areaPopulation.size(); index++)
		{
			try
			{
				tuple.setIntFld(1, ((AreaPopulation) areaPopulation
				        .elementAt(index)).getAreaPopulationId());
				tuple.setStrFld(2, ((AreaPopulation) areaPopulation
				        .elementAt(index)).getAreaPopulationName());
				tuple.setSpatialGeomtryFld(3, ((AreaPopulation) areaPopulation
				        .elementAt(index)).getAreaPopulationGeometry());
				rid = heapFile.insertRecord(tuple.returnTupleByteArray());
				rids.add(rid);
			}
			catch (Exception e)
			{
				System.err
				        .println("Heap file error in creating tuple with records Message : "
				                + e.getMessage());
				status = false;
				e.printStackTrace();
			}
		}
		return rids;
	}
	
}
