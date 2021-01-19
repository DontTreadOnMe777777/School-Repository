// Returns a 3x3 transformation matrix as an array of 9 values in column-major order.
// The transformation first applies scale, then rotation, and finally translation.
// The given rotation value is in degrees.
function GetTransform( positionX, positionY, rotation, scale )
{
	var matrix = [1, 0, 0, 0, 1, 0, 0, 0, 1];

	// Transformation matrix
	var transform = [1, 0, 0, 0, 1, 0, positionX, positionY, 1];

	// Convert degrees given to radians, then create rotation matrix
	var radians = (rotation * (Math.PI/180)) * -1;
	var rotation = [Math.cos(radians), Math.sin(radians) * -1, 0, Math.sin(radians), Math.cos(radians), 0, 0, 0, 1];

	// Scale matrix
	var scale = [scale, 0, 0, 0, scale, 0, 0, 0, 1];

	// Perform matrix multiplication in the order described (scale, rotation, translation)
	matrix = matrixMultiplication(scale, matrix);
	matrix = matrixMultiplication(rotation, matrix);
	matrix = matrixMultiplication(transform, matrix);

	return matrix;
}

// Returns a 3x3 transformation matrix as an array of 9 values in column-major order.
// The arguments are transformation matrices in the same format.
// The returned transformation first applies trans1 and then trans2.
function ApplyTransform( trans1, trans2 )
{
	return matrixMultiplication(trans2, trans1);
}

function matrixMultiplication(matrixA, matrixB)
{
	var result = [1, 0, 0, 0, 1, 0, 0, 0, 1];

	result[0] = matrixA[0] * matrixB[0] + matrixA[3] * matrixB[1] + matrixA[6] * matrixB[2];
	result[1] = matrixA[1] * matrixB[0] + matrixA[4] * matrixB[1] + matrixA[7] * matrixB[2];
	result[2] = matrixA[2] * matrixB[0] + matrixA[5] * matrixB[1] + matrixA[8] * matrixB[2];

	result[3] = matrixA[0] * matrixB[3] + matrixA[3] * matrixB[4] + matrixA[6] * matrixB[5];
	result[4] = matrixA[1] * matrixB[3] + matrixA[4] * matrixB[4] + matrixA[7] * matrixB[5];
	result[5] = matrixA[2] * matrixB[3] + matrixA[5] * matrixB[4] + matrixA[8] * matrixB[5];

	result[6] = matrixA[0] * matrixB[6] + matrixA[3] * matrixB[7] + matrixA[6] * matrixB[8];
	result[7] = matrixA[1] * matrixB[6] + matrixA[4] * matrixB[7] + matrixA[7] * matrixB[8];
	result[8] = matrixA[2] * matrixB[6] + matrixA[5] * matrixB[7] + matrixA[8] * matrixB[8];

	return result;
}
