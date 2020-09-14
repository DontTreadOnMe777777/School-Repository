package assign07;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents a vertex (AKA node) in a directed graph. The vertex is
 * generic, assumes that anything can be stored in the vertex.
 * 
 * @author Brandon Walters and Brandon Ernst
 * @version March 1, 2019
 */
public class Vertex<T> implements Comparable<Vertex<T>>{

	// used to id the Vertex
	private T data;

	// adjacency list
	private LinkedList<Edge<T>> adj;
	
	private double distance;
	
	private Vertex<T> previousVertex;
	
	private int indegree;
	
	private boolean visited;

	public Vertex(T data) {
		this.data = data;
		this.adj = new LinkedList<Edge<T>>();
	}

	public T getData() {
		return data;
	}

	public void addEdge(Vertex<T> otherVertex) {
		adj.add(new Edge<T>(otherVertex));
	}
	
	public Iterator<Edge<T>> edges() {
		return adj.iterator();
	}
	
	public LinkedList<Edge<T>> getEdges() {
		return adj;
	}

	public String toString() {
		String s = "Vertex " + data + " adjacent to ";
		Iterator<Edge<T>> itr = adj.iterator();
		while(itr.hasNext())
			s += itr.next() + "  ";
		return s;
	}

	public void setDistanceFromStart(double distance) {
		this.setDistance(distance);
	}

	public double getDistanceFromStart() {
		return this.distance;
	}
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Vertex<T> getPreviousVertex() {
		return previousVertex;
	}

	public void setPreviousVertex(Vertex<T> previousVertex) {
		this.previousVertex = previousVertex;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public int calculateIndegree (HashMap<T, Vertex<T>> verticesMap, Vertex<T> vertex) {
		vertex.indegree = 0;
		for(Vertex<T> verticesToCheck : verticesMap.values()) {
			LinkedList<Edge<T>> linkedListEdges = verticesToCheck.getEdges();
			for (Edge<T> e : linkedListEdges) {
				if (e.getOtherVertex().equals(vertex)) {
					vertex.indegree++;
				}
			}
		}
		return vertex.indegree;
	}
	
	public void setIndegree (int newIndegree) {
		this.indegree = newIndegree;
	}
	
	public int getIndegree () {
		return indegree;
	}
	
	@Override
	public int compareTo (Vertex<T> other) {
		return Double.compare(this.distance, other.distance);
	}
}