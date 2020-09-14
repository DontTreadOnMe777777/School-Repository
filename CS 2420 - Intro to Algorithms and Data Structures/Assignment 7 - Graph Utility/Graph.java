package assign07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Represents a directed graph (a set of vertices and a set of edges). The graph
 * is generic.
 * 
 * 
 * @author Brandon Walters and Brandon Ernst
 * @version March 1, 2019
 */
public class Graph<T> {

	// the graph -- a set of vertices (generic data mapped to Vertex instance)
	HashMap<T, Vertex<T>> verticesMap;
	
	private int previousIndegree;

	/**
	 * Constructs an empty graph.
	 */
	public Graph() {
		verticesMap = new HashMap<T, Vertex<T>>();
	}

	/**
	 * Adds to the graph an edge from the vertex with name "name1" to the vertex
	 * with name "name2". The edge is unweighted. If either
	 * vertex does not already exist in the graph, it is added.
	 */
	public void addEdge(T name1, T name2) {
		Vertex<T> vertex1;
		if(verticesMap.containsKey(name1))
			vertex1 = verticesMap.get(name1);
		else {
			vertex1 = new Vertex<T>(name1);
			verticesMap.put(name1, vertex1);
		}

		Vertex<T> vertex2;
		if(verticesMap.containsKey(name2))
			vertex2 = verticesMap.get(name2);
		else {
			vertex2 = new Vertex<T>(name2);
			verticesMap.put(name2, vertex2);
		}

		vertex1.addEdge(vertex2);
	}
	
	public boolean depthFirstSetup () {
		Set<Vertex<T>> visitedVertices = new HashSet<Vertex<T>>();
		Set<Vertex<T>> completedVertices = new HashSet<Vertex<T>>();
		for (Vertex<T> vertex : verticesMap.values()) {
			if (depthFirstSearch(vertex, visitedVertices, completedVertices)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean depthFirstSearch (Vertex<T> vertex, Set<Vertex<T>> visitedVertices, Set<Vertex<T>> completedVertices) {
		if (visitedVertices.contains(vertex) && !completedVertices.contains(vertex)) {
			return true;
		}
		
		visitedVertices.add(vertex);
		
		for (Edge<T> e : vertex.getEdges()) {
			if (depthFirstSearch(e.getOtherVertex(), visitedVertices, completedVertices)) {
				return true;
			}
		}
		completedVertices.add(vertex);
		return false;
	}
	
	public boolean breadthFirstSearch (Vertex<T> startVertex, T srcData, T dstData) {
		for (Vertex<T> vertex : verticesMap.values()) {
			vertex.setVisited(false);
		}
		GenericQueue<Vertex<T>> queue = new GenericQueue<Vertex<T>>();
		startVertex.setVisited(true);
		queue.add(startVertex);
		
		while (!queue.isEmpty()) {
			Vertex<T> vertexToCheck = queue.remove();
			for (Edge<T> e : vertexToCheck.getEdges()) {
				Vertex<T> destinationVertex = e.getOtherVertex();
				if (!destinationVertex.isVisited()) {
					destinationVertex.setVisited(true);
					if (destinationVertex.getData() == dstData) {
						return true;
					}
					queue.add(destinationVertex);
				}
				else {
				}
			}
		}
		return false;
	}
	
	public List<T> topologicalSort () {
		GenericQueue<Vertex<T>> queue = new GenericQueue<Vertex<T>>();
		List<Vertex<T>> topologicalOrderList = new ArrayList<Vertex<T>>();
		
		for (Vertex<T> vertex : verticesMap.values()) {
			vertex.setVisited(false);
			if (vertex.calculateIndegree(verticesMap, vertex) == 0) {
				queue.add(vertex);
			}
		}
		while (!queue.isEmpty()) {
			Vertex<T> vertexToAdd = queue.remove();
			previousIndegree = vertexToAdd.getIndegree();
			topologicalOrderList.add(vertexToAdd);
			for (Edge<T> e : vertexToAdd.getEdges()) {
				Vertex<T> newVertex = e.getOtherVertex();
				if (newVertex.calculateIndegree(verticesMap, newVertex) - previousIndegree - 1 == 0 && !newVertex.isVisited()) {
					queue.add(newVertex);
					newVertex.setVisited(true);
				}
			}
			
		}
		return (List<T>) topologicalOrderList;
	}

	/**
	 * Uses Dijkstra's algorithm to determine the shortest paths from the
	 * starting vertex to every other vertex in the graph.
	 * 
	 * Use Java's PriorityQueue class to find the "unvisited vertex with
	 * smallest distance from s". In order to associate each vertex with a
	 * priority (related to its distance from s), you must either use the
	 * Comparable interface or the Comparator interface. See the API for
	 * PriorityQueue, and ask the course staff if you need help.
	 */
	public void dijkstras(String startName) {
		// get starting vertex
		Vertex<T> start = verticesMap.get(startName);
		if(start == null)
			throw new UnsupportedOperationException("Vertex " + startName + " does not exist.");

		// set up priority queue of vertices prioritized by smallest distance
		// from
		// start (CHANGE if using Comparator)
		PriorityQueue<Vertex<T>> pq = new PriorityQueue<Vertex<T>>();

		// for all vertices: set distance from start to INF
		for(Vertex<T> v : verticesMap.values())
			v.setDistanceFromStart(Double.POSITIVE_INFINITY);

		start.setDistanceFromStart(0);
		pq.offer(start);

		// while there are vertices left to consider . . .
		while(!pq.isEmpty()) {
			// get the unvisited vertex with the smallest distance from start
			Vertex<T> x = pq.poll();

			// for all of the edges from vertex x . . .
			for(Edge<T> e : x.getEdges()) {
				// get the destination vertex for the edge and consider the cost
				// of this
				// path
				Vertex<T> dest = e.getOtherVertex();
				//double cost = e.getWeight() + x.getDistanceFromStart();

				// if the cost of this path is smaller than the previously
				// considered
				// path, update
				/*if(dest.getDistanceFromStart() > cost) {
					dest.setDistanceFromStart(cost);
					dest.setPreviousVertex(x);
					// if the destination vertex has not been visited, add to
					// the priority
					// queue
					if(!dest.isVisited())
						pq.offer(dest);
				}*/
			}

			x.setVisited(true);
		}

		// print the results of the algorithm
		for(Vertex<T> v : verticesMap.values()) {
			System.out.print("Shortest path from " + start.getData() + " to " + v.getData() + " has length "
					+ v.getDistanceFromStart() + " and is ");
			String s = "";
			while(!v.getData().equals(start.getData())) {
				s = " --> " + v.getData() + s;
				v = v.getPreviousVertex();
			}
			System.out.println(start.getData() + s);
		}
	}
}