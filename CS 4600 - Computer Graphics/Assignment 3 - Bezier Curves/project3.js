// [TO-DO] Complete the implementation of the following class and the vertex shader below.

class CurveDrawer {
	constructor()
	{
		this.prog = InitShaderProgram( curvesVS, curvesFS );
		// [TO-DO] Other initializations should be done here.

		// Get the ids of the uniform variables in the shaders
		this.mvp = gl.getUniformLocation( this.prog, 'mvp' );

		// Create the vertex buffer object
		this.buffer = gl.createBuffer();

		// [TO-DO] This is a good place to get the locations of attributes and uniform variables.
		
		// Initialize the attribute buffer
		this.steps = 100;
		var tv = [];
		for ( var i=0; i<this.steps; ++i ) {
			tv.push( i / (this.steps-1) );
		}
		// [TO-DO] This is where you can create and set the contents of the vertex buffer object
		// for the vertex attribute we need.

		// Binds the tv values to the vertex buffer
		gl.bindBuffer(gl.ARRAY_BUFFER, this.buffer);
		gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(tv), gl.STATIC_DRAW);
	}
	setViewport( width, height )
	{
		// Compute the orthographic projection matrix and send it to the shader
		var trans = [ 2/width,0,0,0,  0,-2/height,0,0, 0,0,1,0, -1,1,0,1 ];
		gl.useProgram( this.prog );
		gl.uniformMatrix4fv( this.mvp, false, trans );
		// [TO-DO] This is where we should set the transformation matrix.
		// [TO-DO] Do not forget to bind the program before you set a uniform variable value.
	}
	updatePoints( pt )
	{
		gl.useProgram( this.prog );

		var point0 = gl.getUniformLocation( this.prog, 'p0' );
		var point1 = gl.getUniformLocation( this.prog, 'p1' );
		var point2 = gl.getUniformLocation( this.prog, 'p2' );
		var point3 = gl.getUniformLocation( this.prog, 'p3' );

		// Instead of throwing the point values into a buffer, we can use them to set the values of our uniform variables
		gl.uniform2f(point0, pt[0].getAttribute("cx"), pt[0].getAttribute("cy"));
		gl.uniform2f(point1, pt[1].getAttribute("cx"), pt[1].getAttribute("cy"));
		gl.uniform2f(point2, pt[2].getAttribute("cx"), pt[2].getAttribute("cy"));
		gl.uniform2f(point3, pt[3].getAttribute("cx"), pt[3].getAttribute("cy"));

		// [TO-DO] The control points have changed, we must update corresponding uniform variables.
		// [TO-DO] Do not forget to bind the program before you set a uniform variable value.
		// [TO-DO] We can access the x and y coordinates of the i^th control points using
		// var x = pt[i].getAttribute("cx");
		// var y = pt[i].getAttribute("cy");
	}
	draw()
	{
		gl.useProgram( this.prog );

		var tArray = gl.getAttribLocation( this.prog, 't');

		gl.bindBuffer( gl.ARRAY_BUFFER, this.buffer );
		gl.vertexAttribPointer( tArray, 1, gl.FLOAT, false, 0, 0 );
		gl.enableVertexAttribArray( tArray );
		gl.drawArrays( gl.LINE_STRIP, 0, 100 );
		// [TO-DO] This is where we give the command to draw the curve.
		// [TO-DO] Do not forget to bind the program and set the vertex attribute.
	}
}

// Vertex Shader
var curvesVS = `
	attribute float t;
	uniform mat4 mvp;
	uniform vec2 p0;
	uniform vec2 p1;
	uniform vec2 p2;
	uniform vec2 p3;
	void main()
	{
		// [TO-DO] Replace the following with the proper vertex shader code
		vec2 finalPoint;
		finalPoint.x = (((1.0 - t) * (1.0 - t) * (1.0 - t)) * p0.x) + (3.0 * ((1.0 - t) * (1.0 - t)) * t * p1.x) + (3.0 * (1.0 - t) * (t * t) * p2.x) + ((t * t * t) * p3.x);
		finalPoint.y = (((1.0 - t) * (1.0 - t) * (1.0 - t)) * p0.y) + (3.0 * ((1.0 - t) * (1.0 - t)) * t * p1.y) + (3.0 * (1.0 - t) * (t * t) * p2.y) + ((t * t * t) * p3.y); 
		gl_Position = mvp * vec4(finalPoint,0,1);
	}
`;

// Fragment Shader
var curvesFS = `
	precision mediump float;
	void main()
	{
		gl_FragColor = vec4(1,0,0,1);
	}
`;