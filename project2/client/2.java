// A "Node" is a point on the graph
public interface Node
{	
	// Sets the data contained within the node
	void setData(Object o);
	
	// Returns the data within the node
	Object getData();
}

// Path is used to represent the path between two nodes (linked list)
public interface Path
{
	// Set the head of this path
	void setHead(Node n);
	
	// Get the head of this path
	// Throw a NullPointerException if the head has not been set
	Node getHead();
	
	// Set the tail of the path (rest of the path)
	void setTail(Path p);
	
	// Get the tail of the path
	// Throw a NullPointerException if the tail has not been set
	Path getTail();
	
	// Return true if this Path has a tail
	Boolean hasTail();
	
	// Set the cost from the head Node to the next head Node
	void setCostToNext(Double cost);
	
	// Get the cost from the head Node to the next head Node
	// Throw NullPointerException if there is no next head
	Double getCostToNext();
}

public interface Graph
{
	// Set the cost interval of the graph. NOTE: Also implement this functionality
	// into the constructor of the Graph class
	void setCostInterval(Double low, Double high);
	
	// Get the low cost interval for this graph.
	// Throw a NullPointerException if this has not been set.
	Double getLowCostInterval();

	// Get the high cost interval for this graph.
	// Throw a NullPointerException if this has not been set.
	Double getHighCostInterval();

	// Join two graphs IF they have the same cost interval and disjointed set of
	// nodes.  Throw IllegalArgumentException w/ text if these aren't met.
	Graph joinGraph(Graph obj);
	
	// Calculate the path between two given nodes.  If no path can be found,
	// return null.  Throw IllegalArgumentException if nodes are not in graph.
	Path calculatePath(Node from, Node to);
	
	// Determines if a path exists between the two given nodes
	// Throws IllegalArgumentException if nodes don't exist in the graph.
	Boolean pathExists(Node from, Node to);
	
	// Return shortest path between two nodes.
	// Throw IllegalArgumentException if nodes don't exist in the graph.
	Double pathDistance(Node from, Node to);
	
	// Add an edge to the graph.  Add nodes to graph if they don't already exist.
	// Throw IllegalArgumentException if triangle inequality is violated.
	void addEdge(Node from, Node to, Double cost);
}