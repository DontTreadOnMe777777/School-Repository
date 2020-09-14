package assign07;

/**
 * This class represents an edge between a source vertex and a destination
 * vertex in a directed graph.
 * 
 * The source of this edge is the Vertex whose object has an adjacency list
 * containing this edge.
 * 
 * @author Brandon Walters and Brandon Ernst
 * @version March 1, 2019
 */
public class Edge<T> {

	// destination of this directed edge
	private Vertex<T> dst;

	public Edge(Vertex<T> dst) {
		this.dst = dst;
	}

	public Vertex<T> getOtherVertex() {
		return this.dst;
	}

	public T toData() {
		return this.dst.getData();
	}
}