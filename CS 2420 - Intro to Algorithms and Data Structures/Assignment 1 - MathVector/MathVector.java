package assign01;

/**
 * This class represents a simple row or column vector of numbers. In a row
 * vector, the numbers are written horizontally (i.e., along the columns). In a
 * column vector, the numbers are written vertically (i.e., along the rows).
 * 
 * @author Erin Parker & Brandon Walters
 * @version January 9, 2019
 */
public class MathVector {

	// 2D array to hold the numbers of the vector, either along the columns or rows
	private double[][] data;
	// set to true for a row vector and false for a column vector
	private boolean isRowVector;
	// count of elements in the vector
	private int vectorSize;

	/**
	 * Creates a new row or column vector. For a row vector, the input array is
	 * expected to have 1 row and a positive number of columns, and this number of
	 * columns represents the vector's length. For a column vector, the input array
	 * is expected to have 1 column and a positive number of rows, and this number
	 * of rows represents the vector's length.
	 * 
	 * @param data - a 2D array to hold the numbers of the vector
	 * @throws IllegalArgumentException if the numbers of rows and columns in the
	 *                                  input 2D array is not compatible with a row
	 *                                  or column vector
	 */
	public MathVector(double[][] data) {
		if (data.length == 0)
			throw new IllegalArgumentException("Number of rows must be positive.");
		if (data[0].length == 0)
			throw new IllegalArgumentException("Number of columns must be positive.");

		if (data.length == 1) {
			// This is a row vector with length = number of columns.
			this.isRowVector = true;
			this.vectorSize = data[0].length;
		} else if (data[0].length == 1) {
			// This is a column vector with length = number of rows.
			this.isRowVector = false;
			this.vectorSize = data.length;
		} else
			throw new IllegalArgumentException("Either the number of rows or the number of columns must be 1.");

		// Create the array and copy data over.
		if (this.isRowVector)
			this.data = new double[1][vectorSize];
		else
			this.data = new double[vectorSize][1];
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[0].length; j++) {
				this.data[i][j] = data[i][j];
			}
		}
	}

	/**
	 * Determines whether this vector is "equal to" another vector, where equality
	 * is defined as both vectors being row (or both being column), having the same
	 * vector length, and containing the same numbers in the same positions.
	 * 
	 * @param other - another vector to compare
	 */
	public boolean equals(Object other) {
		if (!(other instanceof MathVector))
			return false;

		MathVector otherVec = (MathVector) other;

		// Check if both vectors are same type
		if (this.isRowVector && !otherVec.isRowVector || !this.isRowVector && otherVec.isRowVector) {
			return false;
		}

		// Check if vectors are same size
		else if (this.vectorSize != otherVec.vectorSize) {
			return false;
		}

		// Check if each number is equal across both vectors
		else {
			for (int i = 0; i < this.data.length; i++) {
				for (int j = 0; j < this.data[0].length; j++) {
					if (this.data[i][j] != otherVec.data[i][j]) {
						return false;
					}
				}
			}
			return true;
		}
	}

	/**
	 * Generates and returns a new vector that is the transposed version of this
	 * vector.
	 */
	public MathVector transpose() {

		// If row, create a column vector and copy data over
		if (this.isRowVector) {
			MathVector transposed = new MathVector(new double[this.vectorSize][1]);
			for (int i = 0; i < this.vectorSize; i++) {
				transposed.data[i][0] = this.data[0][i];
			}
			return transposed;
		}

		// If column, create a row vector and copy data over
		else {
			MathVector transposed = new MathVector(new double[1][this.vectorSize]);
			for (int i = 0; i < this.vectorSize; i++) {
				transposed.data[0][i] = this.data[i][0];
			}
			return transposed;
		}
	}

	/**
	 * Generates and returns a new vector representing the sum of this vector and
	 * another vector.
	 * 
	 * @param other - another vector to be added to this vector
	 * @throws IllegalArgumentException if the other vector and this vector are not
	 *                                  both row vectors of the same length or
	 *                                  column vectors of the same length
	 */
	public MathVector add(MathVector other) {

		// Checks for exceptions
		if (this.vectorSize != other.vectorSize || this.isRowVector && !other.isRowVector
				|| !this.isRowVector && other.isRowVector) {
			throw new IllegalArgumentException("Vectors do not match.");
		}

		MathVector addedVector = new MathVector(this.data);

		// Adds vectors together
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[0].length; j++) {
				addedVector.data[i][j] = addedVector.data[i][j] + other.data[i][j];
			}
		}
		return addedVector;
	}

	/**
	 * Computes and returns the dot product of this vector and another vector.
	 * 
	 * @param other - another vector to be combined with this vector to produce the
	 *              dot product
	 * @throws IllegalArgumentException if the other vector and this vector are not
	 *                                  both row vectors of the same length or
	 *                                  column vectors of the same length
	 */
	public double dotProduct(MathVector other) {

		// Checks for exceptions
		if (this.vectorSize != other.vectorSize || this.isRowVector && !other.isRowVector
				|| !this.isRowVector && other.isRowVector) {
			throw new IllegalArgumentException("Vectors do not match.");
		}

		MathVector dotProductVector = new MathVector(this.data);

		// Multiplies two vectors together
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[0].length; j++) {
				dotProductVector.data[i][j] = dotProductVector.data[i][j] * other.data[i][j];
			}
		}

		double sum = 0;

		// Adds all numbers in the vector together
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[0].length; j++) {
				sum = sum + dotProductVector.data[i][j];
			}
		}
		return sum;
	}

	/**
	 * Computes and returns this vector's magnitude (also known as a vector's
	 * length) .
	 */
	public double magnitude() {

		MathVector magnitudeVector = new MathVector(this.data);

		// Squares all numbers in the vector
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[0].length; j++) {
				magnitudeVector.data[i][j] = magnitudeVector.data[i][j] * magnitudeVector.data[i][j];
			}
		}

		double sum = 0;

		// Adds all numbers in the vector together
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[0].length; j++) {
				sum = sum + magnitudeVector.data[i][j];
			}
		}
		return Math.sqrt(sum);
	}

	/**
	 * Generates and returns a normalized version of this vector.
	 */
	public MathVector normalize() {

		MathVector normalizedVector = new MathVector(this.data);

		// Takes magnitude of vector
		double vectorMagnitude = normalizedVector.magnitude();

		// Divides each number in the vector by the magnitude
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[0].length; j++) {
				normalizedVector.data[i][j] = normalizedVector.data[i][j] / vectorMagnitude;
			}
		}
		return normalizedVector;
	}

	/**
	 * Generates and returns a textual representation of this vector. For example,
	 * "1.0 2.0 3.0 4.0 5.0" for a sample row vector of length 5 and "1.0 2.0 3.0
	 * 4.0 5.0" for a sample column vector of length 5. In both cases, notice the
	 * lack of a newline or space after the last number.
	 */
	public String toString() {

		String stringToPrint = "";
		
		// If row, prints accordingly
		if (this.isRowVector) {
			for (int i = 0; i < this.data.length; i++) {
				for (int j = 0; j < this.data[0].length; j++) {
					stringToPrint = stringToPrint + this.data[i][j] + " ";
				}
			}
			return stringToPrint.trim();
		}
		
		// If column, prints appropriately
		else {
			for (int i = 0; i < this.data.length; i++) {
				for (int j = 0; j < this.data[0].length; j++) {
					stringToPrint = stringToPrint + this.data[i][j] + "\n";
				}
			}
			return stringToPrint.trim();
		}
	}
}