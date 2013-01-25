// Specification of a component that manages directed, planar Graphs

// Specifies a method to create instances of the Graph interface
interface GraphFactory {
	
  // Create a new instance of a empty Graph (with no Nodes or Edges) with 
  // the given cost interval. Throws an exception if low is greater than high.
  Graph createGraph(Double low, Double high);
}