/**
 * 
 */
package spatial.test;

import global.AttrOperator;
import global.AttrType;
import heap.FieldNumberOutOfBoundException;
import heap.Heapfile;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;
import iterator.CondExpr;
import iterator.FileScan;
import iterator.FldSpec;
import iterator.JoinsException;
import iterator.PredEvalException;
import iterator.RelSpec;
import iterator.UnknowAttrType;
import iterator.WrongPermat;

import java.io.IOException;
import java.util.ArrayList;

import spatial.bo.SpatialGeometry;
import spatial.util.IntersectionPolygon;
import spatial.util.SpatialAreaUtil;
import spatial.util.SpatialDistanceUtil;
import bufmgr.PageNotReadException;

/**
 * @author Gerard
 * @author Sooraj
 */
public class TestSelectSpatialStmt {
	private static boolean status = true;

	public FileScan processSelect(Heapfile f, String[] projectionPartArr, Where condObj) {
		FileScan fileScan = null;
		CondExpr[] conditionExpression = null;

		short[] Msizes = new short[1];
		Msizes[0] = 30;

		FldSpec[] projectionTypes = new FldSpec[projectionPartArr.length];
		AttrType[] attrTypes = getAttributeTypes();

		for (int i = 0; i < projectionPartArr.length; i++) {
			if (projectionPartArr[i].trim().equalsIgnoreCase("area_id"))
				projectionTypes[i] = new FldSpec(new RelSpec(RelSpec.outer), 1);
			else if (projectionPartArr[i].trim().equalsIgnoreCase("area_name"))
				projectionTypes[i] = new FldSpec(new RelSpec(RelSpec.outer), 2);
			else if (projectionPartArr[i].trim().equalsIgnoreCase("shape"))
				projectionTypes[i] = new FldSpec(new RelSpec(RelSpec.outer), 3);
		}
		if (condObj != null)
			conditionExpression = prepareQuery(condObj);

		try {
			fileScan = new FileScan(f.get_fileName(), attrTypes, Msizes, (short) attrTypes.length,
					(short) projectionTypes.length, projectionTypes, conditionExpression);
		} catch (Exception e) {
			System.err.println("Error in opening connection to file. Error message : " + e.getMessage());
			status = false;
			e.printStackTrace();
		}
		return fileScan;
	}

	private AttrType[] getAttributeTypes() {
		AttrType[] attrTypes = new AttrType[3];
		attrTypes[0] = new AttrType(AttrType.attrInteger);
		attrTypes[1] = new AttrType(AttrType.attrString);
		attrTypes[2] = new AttrType(AttrType.attrSpatialGeometry);
		return attrTypes;
	}

	private CondExpr[] prepareQuery(Where condObj) {
		String params[] = condObj.params;
		String paramValues[] = condObj.paramValues;
		String op[] = condObj.op;
		String connector = condObj.connector;
		CondExpr[] conditionExpression = new CondExpr[params.length + 1];
		conditionExpression[params.length] = null;
		if(connector != null && connector.equalsIgnoreCase("OR")){
			conditionExpression[0] = new CondExpr();
			conditionExpression[0].op = new AttrOperator(getAttrOperator(op[0]));
			conditionExpression[0].type1 = new AttrType(AttrType.attrSymbol);
			int offset = 1;
			offset = params[0].contains("area_id") ? 1 : params[0].contains("area_name") ? 2 : 3;
			conditionExpression[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), offset);
			conditionExpression[0].type2 = new AttrType(getAttrType(params[0]));
			if (params[0].contains("area_id"))
				conditionExpression[0].operand2.integer = Integer.parseInt(paramValues[0]);
			else if (params[0].contains("area_name"))
				conditionExpression[0].operand2.string = paramValues[0];
			
			conditionExpression[0].next = new CondExpr();
			conditionExpression[0].next.op = new AttrOperator(getAttrOperator(op[1]));
			conditionExpression[0].next.next = null;
			conditionExpression[0].next.type1 = new AttrType(AttrType.attrSymbol); // rating
			offset = params[1].contains("area_id") ? 1 : params[1].contains("area_name") ? 2 : 3;
			conditionExpression[0].next.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer),offset);
			conditionExpression[0].next.type2 = new AttrType(getAttrType(params[1]));
			if (params[1].contains("area_id"))
				conditionExpression[0].next.operand2.integer = Integer.parseInt(paramValues[1]);
			else if (params[1].contains("area_name"))
				conditionExpression[0].next.operand2.string = paramValues[1];
			
			conditionExpression[1] = null;
			return conditionExpression;
		}
		else{
			for (int i = 0; i < params.length; i++) {
				conditionExpression[i] = new CondExpr();
				conditionExpression[i].next = null;
				conditionExpression[i].op = new AttrOperator(getAttrOperator(op[i]));
				conditionExpression[i].type1 = new AttrType(AttrType.attrSymbol);
				conditionExpression[i].type2 = new AttrType(getAttrType(params[i]));
				int offset = 1;
				offset = params[i].contains("area_id") ? 1 : params[i].contains("area_name") ? 2 : 3;
				conditionExpression[i].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), offset);
				if (params[i].contains("area_id"))
					conditionExpression[i].operand2.integer = Integer.parseInt(paramValues[i]);
				else if (params[i].contains("area_name"))
					conditionExpression[i].operand2.string = paramValues[i];
			}
		}
		return conditionExpression;
	}

	private int getAttrOperator(String operator) {
		int opType = 0;
		if (operator.equals("="))
			opType = AttrOperator.aopEQ;
		else if (operator.equals("!="))
			opType = AttrOperator.aopNE;
		else if (operator.equals("<"))
			opType = AttrOperator.aopLT;
		else if (operator.equals(">"))
			opType = AttrOperator.aopGT;
		else if (operator.equals("<="))
			opType = AttrOperator.aopLE;
		else if (operator.equals("<="))
			opType = AttrOperator.aopGE;
		return opType;
	}

	private int getAttrType(String attribute) {
		int attrType = 0;
		if (attribute.equals("area_id"))
			attrType = AttrType.attrInteger;
		else if (attribute.equals("area_name"))
			attrType = AttrType.attrString;
		return attrType;
	}

	public void outputSelectResult(FileScan fileScan, String[] projections) {
		String outputString = "";
		Tuple recordTuple = null;
		SpatialGeometry spatialGeometry = null;
		try {
			while ((recordTuple = fileScan.get_next()) != null) {
				for (int i = 0; i < projections.length; i++) {
					if (projections[i].trim().equalsIgnoreCase("area_id"))
						outputString += recordTuple.getIntFld(i + 1) + ", ";
					else if (projections[i].trim().equalsIgnoreCase("area_name"))
						outputString += recordTuple.getStrFld(i + 1) + ", ";
					else if (projections[i].trim().equalsIgnoreCase("shape")) {
						spatialGeometry = recordTuple.getSpatialGeometryFld(i + 1);
						if (spatialGeometry != null) {
							outputString += "Spatial Geometry(" + spatialGeometry.getGeometryShape().getShapeId() + ", "
									+ spatialGeometry.getGeometryShape().getShapeName() + ", [";
							for (double d : spatialGeometry.getCoordinates())
								outputString += (int) d + " ";
							outputString = outputString.trim();
							outputString += "]), ";
						}
					}
				}
				if (outputString.trim().endsWith(","))
					outputString = outputString.substring(0, outputString.length() - 2);
				System.out.println(outputString);
				outputString = "";
			}
		} catch (Exception e) {
			System.err.println("Error in displaying result. Error message : " + e.getMessage());
			status = false;
			e.printStackTrace();
		}
		System.out.println();
	}

	public void outputResultWithArea(FileScan fileScan, String[] projections) {
		String outputString = "";
		Tuple recordTuple = null;
		SpatialGeometry spatialGeometry = null;
		try {
			while ((recordTuple = fileScan.get_next()) != null) {
				for (int i = 0; i < projections.length; i++) {
					if (projections[i].trim().equalsIgnoreCase("area_id"))
						outputString += recordTuple.getIntFld(i + 1) + ", ";
					else if (projections[i].trim().equalsIgnoreCase("area_name"))
						outputString += recordTuple.getStrFld(i + 1) + ", ";
					else if (projections[i].trim().equalsIgnoreCase("shape")) {
						spatialGeometry = recordTuple.getSpatialGeometryFld(i + 1);
						if (spatialGeometry != null)
							outputString += SpatialAreaUtil.area(spatialGeometry) + ", ";
					}
				}
				if (outputString.trim().endsWith(","))
					outputString = outputString.trim().substring(0, outputString.length() - 2);
				System.out.println(outputString);
				outputString = "";
			}
		} catch (Exception e) {
			System.err.println("Error in displaying result. Error message : " + e.getMessage());
			status = false;
			e.printStackTrace();
		}
		System.out.println();
	}

	public void outputResultWithDistance(FileScan fileScan1, FileScan fileScan2) {
		Tuple recordTuple = null;
		SpatialGeometry spatialGeometry1 = null;
		SpatialGeometry spatialGeometry2 = null;
		try {
			recordTuple = fileScan1.get_next();
			spatialGeometry1 = recordTuple.getSpatialGeometryFld(1);
			recordTuple = fileScan2.get_next();
			spatialGeometry2 = recordTuple.getSpatialGeometryFld(1);
			new SpatialDistanceUtil().coordinatesOfGeo(spatialGeometry1, spatialGeometry2);
		} catch (Exception e) {
			System.err.println("Error in displaying result. Error message : " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println();
	}

	public void outputResultWithIntersection(FileScan fileScan1, FileScan fileScan2) {
		try {
			IntersectionPolygon ip = new IntersectionPolygon();
			Tuple tuple1 = fileScan1.get_next();
			Tuple tuple2 = fileScan2.get_next();
			double[] fig1Coord = tuple1.getSpatialGeometryFld(1).coordinates;
			double[] fig2Coord = tuple2.getSpatialGeometryFld(1).coordinates;
			String shape1 = tuple1.getSpatialGeometryFld(1).getGeometryShape().getShapeName();
			String shape2 = tuple2.getSpatialGeometryFld(1).getGeometryShape().getShapeName();
			double[][] p1 = null;
			double[][] p2 = null;
			if (shape1.equalsIgnoreCase("rectangle")) {
				p1 = new double[4][2];
				p1[0][0] = fig1Coord[0];
				p1[0][1] = fig1Coord[1];
				p1[1][0] = fig1Coord[1];
				p1[1][1] = fig1Coord[2];
				p1[2][0] = fig1Coord[2];
				p1[2][1] = fig1Coord[3];
				p1[3][0] = fig1Coord[0];
				p1[3][1] = fig1Coord[3];
			} else if (shape1.equalsIgnoreCase("triangle")) {
				p1 = new double[3][2];
				p1[0][0] = fig1Coord[0];
				p1[0][1] = fig1Coord[1];
				p1[1][0] = fig1Coord[2];
				p1[1][1] = fig1Coord[3];
				p1[2][0] = fig1Coord[4];
				p1[2][1] = fig1Coord[5];
			} else if (shape1.equalsIgnoreCase("circle")) {
				p1 = new double[4][2];
				p1[0][0] = fig1Coord[0];
				p1[0][1] = fig1Coord[1];
				p1[1][0] = fig1Coord[2];
				p1[1][1] = fig1Coord[3];
				p1[2][0] = fig1Coord[4];
				p1[2][1] = fig1Coord[5];
				p1[3][0] = 2 * fig1Coord[0] - fig1Coord[2];
				p1[3][1] = fig1Coord[3];
			}
			if (shape2.equalsIgnoreCase("rectangle")) {
				p2 = new double[4][2];
				p2[0][0] = fig2Coord[0];
				p2[0][1] = fig2Coord[1];
				p2[1][0] = fig2Coord[1];
				p2[1][1] = fig2Coord[2];
				p2[2][0] = fig2Coord[2];
				p2[2][1] = fig2Coord[3];
				p2[3][0] = fig2Coord[0];
				p2[3][1] = fig2Coord[3];
			} else if (shape2.equalsIgnoreCase("triangle")) {
				p2 = new double[3][2];
				p2[0][0] = fig2Coord[0];
				p2[0][1] = fig2Coord[1];
				p2[1][0] = fig2Coord[2];
				p2[1][1] = fig2Coord[3];
				p2[2][0] = fig2Coord[4];
				p2[2][1] = fig2Coord[5];
			} else if (shape2.equalsIgnoreCase("circle")) {
				p2 = new double[4][2];
				p2[0][0] = fig2Coord[0];
				p2[0][1] = fig2Coord[1];
				p2[1][0] = fig2Coord[2];
				p2[1][1] = fig2Coord[3];
				p2[2][0] = fig2Coord[4];
				p2[2][1] = fig2Coord[5];
				p2[3][0] = 2 * fig2Coord[0] - fig2Coord[2];
				p2[3][1] = fig2Coord[3];
			}
			ArrayList<double[]> output = ip.getIntersectionPolygon(p1, p2);
			String outputString = "";
			if (output == null) {
				outputString += "These two Geometry Objects are Non Intersecting";
			} else {
				outputString = "Intersection Geometry : (";
				for (double[] d : output) {
					outputString += d[0] + ", " + d[1] + ", ";
				}
				outputString = outputString.trim().substring(0, outputString.length() - 2);
				outputString += ")";
			}
			System.out.println(outputString);
		} catch (Exception e) {
			System.err.println("Error in displaying result. Error message : " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println();
	}
}
