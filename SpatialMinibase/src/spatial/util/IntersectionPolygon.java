package spatial.util;
//Name  : Rakesh Prabhu
//Arizona State University
import java.util.*;

public class IntersectionPolygon {
	List<double[]> poly1, poly2, intersection;

	public static void main(String[] args) {
		double[][] P1 = { { 40, 150 }, { 150, 50 }, { 450, 170 }, { 400, 350 }, { 156, 300 }, { 50, 250 }, { 5, 230 } };
		double[][] P2 = { { 100, 100 }, { 320, 100 }, { 200, 400 } };
		IntersectionPolygon f = new IntersectionPolygon();
		ArrayList<double[]> intersectionPolygon = f.getIntersectionPolygon(P1, P2);
	/*	for (double[] inter : intersectionPolygon)
			System.out.println(inter[0] + "===>" + inter[1]);*/
	}

	public ArrayList<double[]> getIntersectionPolygon(double[][] polyOne, double[][] polyTwo) {

		poly1 = new ArrayList<double[]>(Arrays.asList(polyOne));
		intersection = new ArrayList<double[]>(poly1);
		poly2 = new ArrayList<double[]>(Arrays.asList(polyTwo));
		return clipThePoly();
	}

	// Clip the Polygon
	private ArrayList<double[]> clipThePoly() {
		int lFir = poly2.size();
		for (int i = 0; i < lFir; i++) {

			int lSec = intersection.size();
			List<double[]> input = intersection;
			intersection = new ArrayList<double[]>(lSec);

			double[] A = poly2.get((i + lFir - 1) % lFir);
			double[] B = poly2.get(i);

			for (int j = 0; j < lSec; j++) {

				double[] P = input.get((j + lSec - 1) % lSec);
				double[] Q = input.get(j);

				if (checkItItsIn(A, B, Q)) {
					if (!checkItItsIn(A, B, P)) // adds the co ordinates of the
												// outer or inner one
						intersection.add(intersection(A, B, P, Q));
					intersection.add(Q);
				} else if (checkItItsIn(A, B, P)) // adds the co ordinates of
													// the point of intersection
					intersection.add(intersection(A, B, P, Q));
			}
		}
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		if (intersection.size() == 0)
			return null;
/*		System.out.println(intersection.size());
*/		for (int i = 0; i < intersection.size(); i++) {// gets the outer co
														// ordinates of the
														// bounding box
			double[] res = intersection.get(i);
			/*
			 * System.out.println(res[0] + "===>" + res[1]);
			 */
			minX = Math.min(res[0], minX);
			maxX = Math.max(res[0], maxX);
			minY = Math.min(res[1], minY);
			maxY = Math.max(res[1], maxY);
		}
		ArrayList<double[]> result2 = new ArrayList<double[]>(intersection.size());
		result2.add(new double[] { minX, minY });
		//result2.add(new double[] { maxX, minY });
		result2.add(new double[] { maxX, maxY });
		//result2.add(new double[] { minX, maxY });
		return result2;

	}

	private double[] intersection(double[] a, double[] b, double[] p, double[] q) {

		double B2 = p[0] - q[0];
		double A2 = q[1] - p[1];
		double C2 = A2 * p[0] + B2 * p[1];
		double B1 = a[0] - b[0];
		double A1 = b[1] - a[1];
		double C1 = A1 * a[0] + B1 * a[1];
		double inter = A1 * B2 - A2 * B1;
		double xCor = (B2 * C1 - B1 * C2) / inter;
		double yCor = (A1 * C2 - A2 * C1) / inter;

		return new double[] { xCor, yCor };
	}
	// check if the area is inside the other
	private boolean checkItItsIn(double[] first, double[] second, double[] third) { 
		return (first[0] - third[0]) * (second[1] - third[1]) > (first[1] - third[1]) * (second[0] - third[0]);
	}

}