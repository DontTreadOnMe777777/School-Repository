package assign01;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This tester class assesses the correctness of the MathVector class.
 * 
 * IMPORTANT NOTE: The tests provided to get you started rely heavily on a
 * correctly implemented equals method. Be careful of false positives (i.e.,
 * tests that pass because your equals method incorrectly returns true).
 * 
 * @author Erin Parker & Brandon Walters
 * @version January 9, 2019
 */
class MathVectorJUnitTester {

	private MathVector rowVec, rowVecTranspose, unitVec, sumVec, colVec, colVecTranspose, smallRow, smallRow2, sumVec2,
			largeRow;

	@BeforeEach
	void setUp() throws Exception {
		// Creates a row vector with three elements: 3.0, 1.0, 2.0
		rowVec = new MathVector(new double[][] { { 3, 1, 2 } });

		// Creates a column vector with three elements: 3.0, 1.0, 2.0
		rowVecTranspose = new MathVector(new double[][] { { 3 }, { 1 }, { 2 } });

		// Creates a row vector with three elements: 1.0, 1.0, 1.0
		unitVec = new MathVector(new double[][] { { 1, 1, 1 } });

		// Creates a row vector with three elements: 4.0, 2.0, 3.0
		sumVec = new MathVector(new double[][] { { 4, 2, 3 } });

		// Creates a row vector with three elements: 0.00000000002, 0.00000000002,
		// 0.00000000002
		sumVec2 = new MathVector(new double[][] { { 0.00000000002, 0.00000000002, 0.00000000002 } });

		// Creates a column vector with five elements: -11.0, 2.5, 36.0, -3.14, 7.1
		colVec = new MathVector(new double[][] { { -11 }, { 2.5 }, { 36 }, { -3.14 }, { 7.1 } });

		// Creates a row vector with five elements: -11.0, 2.5, 36.0, -3.14, 7.1
		colVecTranspose = new MathVector(new double[][] { { -11, 2.5, 36, -3.14, 7.1 } });

		// Creates a row vector with three elements: 0.00000000001, 0.00000000001,
		// 0.00000000001
		smallRow = new MathVector(new double[][] { { 0.00000000001, 0.00000000001, 0.00000000001 } });

		// Creates a row vector with three elements: 1000000000, 1000000000, 1000000000
		largeRow = new MathVector(new double[][] { { 1000000000, 1000000000, 1000000000 } });
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	public void smallRowVectorEquality() {
		assertTrue(rowVec.equals(new MathVector(new double[][] { { 3, 1, 2 } })));
	}

	@Test
	public void smallRowVectorInequality() {
		assertFalse(rowVec.equals(unitVec));
	}

	@Test
	public void createVectorFromBadArray() {
		double arr[][] = { { 1, 2 }, { 3, 4 } };
		assertThrows(IllegalArgumentException.class, () -> {
			new MathVector(arr);
		});
		// NOTE: The code above is an example of a lambda expression. See Lab 1 for more
		// info.
	}

	@Test
	public void transposeSmallRowVector() {
		MathVector transposeResult = rowVec.transpose();
		assertTrue(transposeResult.equals(rowVecTranspose));
	}

	@Test
	public void transposeSmallColVector() {
		MathVector transposeResult = colVec.transpose();
		assertTrue(transposeResult.equals(colVecTranspose));
	}

	@Test
	public void addRowAndColVectors() {
		assertThrows(IllegalArgumentException.class, () -> {
			rowVec.add(colVec);
		});
		// NOTE: The code above is an example of a lambda expression. See Lab 1 for more
		// info.
	}

	@Test
	public void addSmallRowVectors() {
		MathVector addResult = rowVec.add(unitVec);
		assertTrue(addResult.equals(sumVec));
	}

	@Test
	public void addVerySmallRowVectors() {
		MathVector addResult = smallRow.add(smallRow);
		assertTrue(addResult.equals(sumVec2));
	}

	@Test
	public void addSmallColVectors() {
		assertThrows(IllegalArgumentException.class, () -> {
			rowVec.add(colVecTranspose);
		});
	}

	@Test
	public void dotProductSmallRowVectors() {
		double dotProdResult = rowVec.dotProduct(unitVec);
		assertEquals(dotProdResult, 3.0 * 1.0 + 1.0 * 1.0 + 2.0 * 1.0);
	}

	@Test
	public void dotProductExtremeSmallVectors() {
		double dotProdResult = smallRow.dotProduct(smallRow);
		assertEquals(dotProdResult,
				0.00000000001 * 0.00000000001 + 0.00000000001 * 0.00000000001 + 0.00000000001 * 0.00000000001);
	}

	@Test
	public void dotProductExtremeLargeVectors() {
		double dotProdResult = largeRow.dotProduct(largeRow);
		assertEquals(dotProdResult,
				1000000000.0 * 1000000000.0 + 1000000000.0 * 1000000000.0 + 1000000000.0 * 1000000000.0);
	}

	@Test
	public void smallRowVectorMagnitude() {
		double vecMagnitude = rowVec.magnitude();
		assertEquals(vecMagnitude, Math.sqrt(3.0 * 3.0 + 1.0 * 1.0 + 2.0 * 2.0));
	}

	@Test
	public void largeRowVectorMagnitude() {
		double vecMagnitude = largeRow.magnitude();
		assertEquals(vecMagnitude,
				Math.sqrt(1000000000.0 * 1000000000.0 + 1000000000.0 * 1000000000.0 + 1000000000.0 * 1000000000.0));
	}

	@Test
	public void smallRowVectorNormalize() {
		MathVector normalVec = rowVec.normalize();
		double magnitude = Math.sqrt(3.0 * 3.0 + 1.0 * 1.0 + 2.0 * 2.0);
		assertTrue(normalVec
				.equals(new MathVector(new double[][] { { 3.0 / magnitude, 1.0 / magnitude, 2.0 / magnitude } })));
	}

	@Test
	public void largeRowVectorNormalize() {
		MathVector normalVec = largeRow.normalize();
		double magnitude = largeRow.magnitude();
		assertTrue(normalVec.equals(new MathVector(
				new double[][] { { 1000000000.0 / magnitude, 1000000000.0 / magnitude, 1000000000.0 / magnitude } })));
	}

	@Test
	public void smallColVectorToString() {
		String resultStr = "-11.0\n2.5\n36.0\n-3.14\n7.1";
		assertEquals(colVec.toString(), resultStr);
	}
	
	@Test
	public void largeRowVectorToString() {
		String resultStr = "1.0E9 1.0E9 1.0E9";
		assertEquals(largeRow.toString(), resultStr);
	}
	
	@Test
	public void extremeSmallColVectorToString() {
		String resultStr = "1.0E-11 1.0E-11 1.0E-11";
		assertEquals(smallRow.toString(), resultStr);
	}

	// STUDENT: Add many, many more unit tests to completely and robustly test your
	// MathVector class.
}