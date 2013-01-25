// Represents a directed, planar Graph 
interface Graph {

  // Adds a Node to the graph
  void addNode(String label);
  
  // Does the Graph contain a Node with the given label?
  boolean hasNode(String label);
  
  // Returns the Node with the given label in this Graph. If it does not exist,
  // throws an exception
  Node getNode(String label);
  
  // Adds an edge from origin to dest with the given cost. Throws an exception
  // if cost is not in cost interval or triangular inequality is violated.
  void addEdge(Node origin, Node dest, Double cost);
	
  // Does the path from origin to dest exist?
  boolean pathExists(Node origin, Node dest);
	
  // Returns the cost of the cheapest path from origin to dest. 
  // If path does not exist, throws an exception.
  Double pathCost(Node origin, Node dest);
  
  // Returns a Path object that represents the path from origin to dest and
  // the cost associated with it. If path does not exist, throws an exception.
  Path getPath(Node origin, Node dest);
  
  // Adds other to this Graph. Throws exception if this and other don't have
  // the same cost interval or the nodes in this and other aren't disjoint
  void joinGraph(Graph other);
}