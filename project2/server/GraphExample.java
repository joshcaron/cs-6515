
public class GraphExample implements Graph {
	Double minCost;
	Double maxCost;
	
	GraphExample (Double min, Double max) {
		this.minCost = min;
		this.maxCost = max;
		
	}
	
	  // Adds a Node to the graph
	  public void addNode(String label) {
	}
	  
	  // Does the Graph contain a Node with the given label?
	  public boolean hasNode(String label) {
		return false;
	}
	  
	  // Returns the Node with the given label in this Graph. If it does not exist,
	  // throws an exception
	  public Node getNode(String label) {
		return null;
	}
	  
	  // Adds an edge from origin to dest with the given cost. Throws an exception
	  // if cost is not in cost interval or triangular inequality is violated.
	  public void addEdge(Node origin, Node dest, Double cost) {
	}
		
	  // Does the path from origin to dest exist?
	  public boolean pathExists(Node origin, Node dest) {
		return false;
	}
		
	  // Returns the cost of the cheapest path from origin to dest. 
	  // If path does not exist, throws an exception.
	  public Double pathCost(Node origin, Node dest) {
		return null;
	}
	  
	  // Returns a Path object that represents the path from origin to dest and
	  // the cost associated with it. If path does not exist, throws an exception.
	  public Path getPath(Node origin, Node dest) {
		return null;
	}
	  
	  // Adds other to this Graph. Throws exception if this and other don't have
	  // the same cost interval or the nodes in this and other aren't disjoint
	  public void joinGraph(Graph other) {
	}	
}




