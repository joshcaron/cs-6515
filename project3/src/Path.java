// Represents the Path between two Nodes in a Graph, including the total
// traversal cost between the Nodes
interface Path {
  // Return the total cost of this Path
  Double getCost();
}
