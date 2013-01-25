
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