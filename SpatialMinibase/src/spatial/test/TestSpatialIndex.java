/**
 * 
 */
package spatial.test;

import global.AttrOperator;
import global.AttrType;
import global.IndexType;
import global.RID;
import heap.Heapfile;
import heap.Scan;
import heap.Tuple;
import index.IndexException;
import index.IndexScan;
import iterator.CondExpr;
import iterator.FldSpec;
import iterator.RelSpec;

import java.io.IOException;

import spatial.bo.SpatialGeometry;
import btree.BTreeFile;
import btree.MBRKey;

/**
 * @author Gerard
 */
public class TestSpatialIndex
{
	private RID   rid         = null;
	private Tuple tupleRecord = null;
	
	public void createIndex(Heapfile heapfile, Tuple tupleRecord)
	{
		this.tupleRecord = tupleRecord;
		Scan scan = null;
		BTreeFile btf = null;
		try
		{
			scan = new Scan(heapfile);
			System.out.println("RTreeIndex scan successful.");
			btf = new BTreeFile("RTreeIndex", AttrType.attrSpatialGeometry, 32,
			        1/* delete */);
			System.out.println("RTreeIndex file creation successful.");
		}
		catch (Exception e)
		{
			System.out
			        .println("Error in RTree scanning records from heap file OR Rtree file creation failed Message : "
			                + e.getMessage());
			e.printStackTrace();
			Runtime.getRuntime().exit(1);
		}
		
		rid = new RID();
		double[] key = null;
		Tuple temp = null;
		
		try
		{
			temp = scan.getNext(rid);
		}
		catch (Exception e)
		{
			System.out
			        .println("Error in scanning records from heap file Message : "
			                + e.getMessage());
			e.printStackTrace();
			Runtime.getRuntime().exit(1);
		}
		try
		{
			while (temp != null)
			{
				tupleRecord.tupleCopy(temp);
				
				key = tupleRecord.getSpatialGeometryFld(3).getMBRIndexKey();
				btf.insert(new MBRKey(key), rid);
				temp = scan.getNext(rid);
			}
		}
		catch (Exception e)
		{
			System.out
			        .println("Error in inserting index records - MBRKEY | slotno - pageno in RTree file Message : "
			                + e.getMessage()
			                + " for record : "
			                + rid
			                + " for key " + key);
			e.printStackTrace();
		}
		finally
		{
			if (scan != null)
				scan.closescan();
		}
		
		System.out.println("RTreeIndex created successfully.");
	}
	
	public void fetchRecordUsingIndexing(double[] mbrCoordinates,
	        String tableName) throws IndexException, IOException
	{
		AttrType[] attrType = getAttrTypes();
		FldSpec[] projlist = getProjectionList();
		CondExpr[] expr = getConditions(mbrCoordinates);
		
		// start index scan
		IndexScan iscan = null;
		try
		{
			short[] Msizes = new short[1];
			Msizes[0] = 30;
			iscan = new IndexScan(new IndexType(IndexType.B_Index), tableName,
			        "RTreeIndex", attrType, Msizes, 3, 3, projlist, expr, 2,
			        false);
		}
		catch (Exception e)
		{
			System.out
			        .println("Error in index scanning records with projection and condition expressions Message : "
			                + e.getMessage());
			e.printStackTrace();
		}
		
		tupleRecord = null;
		try
		{
			tupleRecord = iscan.get_next();
			if(tupleRecord == null)
				System.out.println("NO-RECORDS");
			else{
				while(tupleRecord != null)
				{
					int areapopulationId = tupleRecord.getIntFld(1);
					String areapopulationName = tupleRecord.getStrFld(2);
					SpatialGeometry areapopulationGeometry = tupleRecord
					        .getSpatialGeometryFld(3);
					System.out.println(areapopulationId + ", " + areapopulationName
					        + ", " + areapopulationGeometry);
					tupleRecord = iscan.get_next();
				}
			}
		}
		catch (Exception e)
		{
			System.out
			        .println("Error in fethcing records from Indexing spatial coordinates Messasge : "
			                + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (iscan != null)
				iscan.close();
		}
		
	}
	
	/**
	 * @param mbrCoordinates
	 * @return
	 */
	private CondExpr[] getConditions(double[] mbrCoordinates)
	{
		CondExpr[] expr = new CondExpr[2];
		expr[0] = new CondExpr();
		expr[0].op = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].type2 = new AttrType(AttrType.attrSpatialGeometry);
		expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 3);
		expr[0].operand2.double_array = new double[] { mbrCoordinates[0],
		        mbrCoordinates[1], mbrCoordinates[2], mbrCoordinates[3] };
		expr[0].next = null;
		expr[1] = null;
		return expr;
	}
	
	/**
	 * @return
	 */
	private FldSpec[] getProjectionList()
	{
		FldSpec[] projlist = new FldSpec[3];
		RelSpec rel = new RelSpec(RelSpec.outer);
		projlist[0] = new FldSpec(rel, 1);
		projlist[1] = new FldSpec(rel, 2);
		projlist[2] = new FldSpec(rel, 3);
		return projlist;
	}
	
	/**
	 * @return
	 */
	private AttrType[] getAttrTypes()
	{
		AttrType[] attrType = new AttrType[3];
		attrType[0] = new AttrType(AttrType.attrInteger);
		attrType[1] = new AttrType(AttrType.attrString);
		attrType[2] = new AttrType(AttrType.attrSpatialGeometry);
		return attrType;
	}
}
