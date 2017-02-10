package spatial.test;

/**
 * @author Sooraj
 */

import iterator.FileScan;
import global.GlobalConst;
import heap.Heapfile;
import heap.Tuple;

public class Query implements GlobalConst{

	static TestSpatialCreateInsertStmt cr_ins = new TestSpatialCreateInsertStmt();
	static TestSelectSpatialStmt select = new TestSelectSpatialStmt();
	static TestSpatialIndex index = new TestSpatialIndex();
	static Heapfile f = null;
	
	void processQuery(String query){
		while(query.contains("  "))
			query = query.replace("  ", " ").trim();				//Iteratively remove double spaces with single space
		if(query.charAt(query.length()-1) == ';')
			query = query.substring(0, query.length() - 1).trim(); 	// Remove trailing ';' symbol and leading/trailing spaces
		String[] queryParts = query.split(" ");
		if(queryParts[0].trim().equalsIgnoreCase("CREATE")) 
			Query.parseCreateQuery(query);							// Parse Create query
		else if(queryParts[0].trim().equalsIgnoreCase("INSERT")) 	// Parse Insert query
			Query.parseInsertQuery(query);
		else if(queryParts[0].trim().equalsIgnoreCase("SELECT"))	// Parse Select query
			Query.parseSelectQuery(query);
		else
			showImproperQueryError(); 								// No other starting keyword supported other than the above 3
	}
	
	private static void showImproperQueryError(){
		System.err.println("Query not in proper format");
		showExtraLine();
	}
	
	private static void showExtraLine(){
		System.out.println();
	}

	private static void parseCreateQuery(String query){
		String[] queryParts = query.trim().split(" ");
		if(queryParts[1].trim().equalsIgnoreCase("TABLE")){				
			int tableIndex = (query.trim().indexOf("TABLE")!=-1) ? query.trim().indexOf("TABLE") : query.trim().indexOf("table");
			String tableName = query.trim().substring(tableIndex+5, query.indexOf("(")).trim();
			f = cr_ins.createTable(tableName);							//	Create Table
			showExtraLine();
		}
		else if(queryParts[1].trim().equalsIgnoreCase("INDEX")){		
			index.createIndex(f, cr_ins.getTupleRecord());				// Create Index
			showExtraLine();
		}
		else															
			showImproperQueryError();									// Not a supported query
	}
	
	private static void parseInsertQuery(String query){
		String[] queryParts = query.trim().split(" ");
		if(queryParts[1].trim().equalsIgnoreCase("INTO") 
				&& (queryParts[3].trim().contains("VALUES") || queryParts[3].trim().contains("values"))){ 				//Check for keywords
			String tempPart = "";
			tempPart = query.trim().substring(query.indexOf("(")).trim();
			int areaPopulationId = Integer.parseInt(tempPart.trim().substring(1, tempPart.indexOf(",")).trim());		//Extract AreaId
			tempPart = tempPart.trim().substring(tempPart.indexOf(",") + 1).trim();
			String areaPopulationName = tempPart.trim().substring(0, tempPart.indexOf(",")).replace("'", "").trim();	//Extract AreaName
			tempPart = tempPart.trim().substring(tempPart.indexOf("SDO_GEOMETRY") + 12, tempPart.length()).trim();				
			int shapeId = Integer.parseInt(tempPart.trim().substring(tempPart.trim().indexOf("(") + 1, tempPart.trim().indexOf(",")).trim()); //Extract shapeid
			tempPart = tempPart.trim().substring(tempPart.trim().indexOf(",") + 1, tempPart.trim().indexOf(")")).replace("(","").trim();
			String[] coordParts = tempPart.trim().split(",");															//Extract coordinates
			
			/* Integrity Constraint Check Begins */
			String[] projectionPartArr= {"area_id"};
			String[] param = {"area_id"}; String[] op = {"="}; String[] paramValue = {areaPopulationId+""};
			Where condObj = new Where(param, paramValue, op, null);
			FileScan tempFileScan = select.processSelect(f, projectionPartArr, condObj);	//Search heapfile for record with same areaid
			try {
				Tuple tuple = tempFileScan.get_next();
				if(tuple!=null){	
					System.out.println("Record already exists with same AreaId!");
					showExtraLine();
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			/* Integrity Constraint Check Ends */
			
			cr_ins.insertRecord(f, areaPopulationId, areaPopulationName, shapeId, coordParts);		//Call insert
			showExtraLine();
		}
		else
			showImproperQueryError();
	}
	
	private static void parseSelectQuery(String query) {

		FileScan fileScan = null;
		String[] projectionPartArr = null;
		Where condObj = null;
		if(query.contains(" WHERE ") || query.contains(" where ")){
			condObj = getConditionParts(query);
		}
		projectionPartArr = getProjectionParts(query);
		if(query.contains("SDO_GEOM.SDO_AREA")){
			for(int i=0; i<projectionPartArr.length; i++)
				if(projectionPartArr[i].contains("SDO_GEOM.SDO_AREA"))
					projectionPartArr[i] = projectionPartArr[i].substring(projectionPartArr[i].indexOf("(")+1, projectionPartArr[i].indexOf(")"));
			fileScan = select.processSelect(f, projectionPartArr, condObj);
			select.outputResultWithArea(fileScan, projectionPartArr);
			fileScan.close();
		}
		else if(query.contains("SDO_GEOM.SDO_INTERSECTION")){
			FileScan fileScan1 = null;
			FileScan fileScan2 = null;
			String wherePart = query.substring(query.indexOf("WHERE") + 5).trim();
			String[] conditions = wherePart.split("AND");
			String paramValue1 = conditions[0].substring(conditions[0].indexOf("=")+1).trim().replace("'","");
			String paramValue2 = conditions[1].substring(conditions[1].indexOf("=")+1).trim().replace("'","");
			String[] projectionArr = {"shape"};
			String[] params = {"area_name"};
			String[] op = {"="};
			String[] paramValues1 = {paramValue1}; String[] paramValues2 = {paramValue2};
			Where condObj1 = new Where(params, paramValues1, op, null);
			Where condObj2 = new Where(params, paramValues2, op, null);
			fileScan1 = select.processSelect(f, projectionArr, condObj1);
			fileScan2 = select.processSelect(f, projectionArr, condObj2);
			select.outputResultWithIntersection(fileScan1, fileScan2);
			fileScan1.close();
			fileScan2.close();
		}
		else if(query.contains("SDO_GEOM.SDO_DISTANCE")){
			FileScan fileScan1 = null;
			FileScan fileScan2 = null;
			String wherePart = query.substring(query.indexOf("WHERE") + 5).trim();
			String[] conditions = wherePart.split("AND");
			String paramValue1 = conditions[0].substring(conditions[0].indexOf("=")+1).trim().replace("'","");
			String paramValue2 = conditions[1].substring(conditions[1].indexOf("=")+1).trim().replace("'","");
			String[] projectionArr = {"shape"};
			String[] params = {"area_name"};
			String[] op = {"="};
			String[] paramValues1 = {paramValue1}; String[] paramValues2 = {paramValue2};
			Where condObj1 = new Where(params, paramValues1, op, null);
			Where condObj2 = new Where(params, paramValues2, op, null);
			fileScan1 = select.processSelect(f, projectionArr, condObj1);
			fileScan2 = select.processSelect(f, projectionArr, condObj2);
			select.outputResultWithDistance(fileScan1, fileScan2);
			fileScan1.close();
			fileScan2.close();
		}
		else if((query.substring(query.indexOf("SELECT") + 6, query.indexOf("FROM")).trim().contains("*"))
				&& (query.substring(query.indexOf("WHERE") + 5).trim().contains("shape IN"))){
			String wherepart = query.substring(query.indexOf("WHERE") + 5).trim();
			String coords = wherepart.substring(wherepart.indexOf("IN")+2).trim();
			coords = coords.replace("(", ""); coords = coords.replace(")", ""); coords = coords.trim();
			String[] coordsArr = coords.split(",");
			double[] mbrCoordinates = new double[coordsArr.length];
			for(int i=0;i<coordsArr.length;i++)
				mbrCoordinates[i] = Double.parseDouble(coordsArr[i]);
			TestSpatialIndex index = new TestSpatialIndex();
			try {
				index.fetchRecordUsingIndexing(mbrCoordinates, f.get_fileName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println();
		}
		else{
			fileScan = select.processSelect(f, projectionPartArr, condObj);
			select.outputSelectResult(fileScan, projectionPartArr);
			fileScan.close();
		}
	}
	
	private static Where getConditionParts(String query) {
		String wherePart = query.substring(query.indexOf("WHERE") + 5).trim();
		if(wherePart != ""){
			String operator = "", connector= "";
			if(wherePart.trim().contains(" OR "))
				connector = "OR";
			else if(wherePart.trim().contains(" AND "))
				connector = "AND";
			else
				connector = null;
			String conditions[] = wherePart.split("OR|AND");
			String params[] = new String[conditions.length]; String paramValues[] = new String[conditions.length]; String op[] = new String[conditions.length];
			for(int i=0;i<conditions.length;i++){
				operator = conditions[i].trim().contains("=")?"=":conditions[i].trim().contains("!=")?"!=":conditions[i].trim().contains("<")?"<":
					conditions[i].trim().contains(">")?">":conditions[i].trim().contains(">=")?">=":conditions[i].trim().contains("<=")?"<=":"unidentified";
				if(operator.equalsIgnoreCase("unidentified"))
					return null;
				op[i] = operator;
				params[i] = conditions[i].split(operator)[0].trim();
				paramValues[i] = conditions[i].split(operator)[1].trim().replace("'", "");	
			}
			return new Where(params, paramValues, op, connector) ;
		}
		else 
			return null;
	}

	private static String[] getProjectionParts(String query){
		String[] projectionPartArr = {"area_id", "area_name", "shape"};
		String projectionPart = query.substring(query.indexOf("SELECT") + 6, query.indexOf("FROM")).trim();
		if(!projectionPart.contains("*")){
			projectionPartArr = projectionPart.split(",");
		}
		return projectionPartArr;
	}
}
class Where{
	public Where(String[] _params, String[] _paramValues, String[] _op, String _connector) {
		this.params = _params;
		this.paramValues = _paramValues;
		this.op = _op;
		this.connector = _connector;
	}
	
	String params[];
	String paramValues[];
	String op[];
	String connector;
}

