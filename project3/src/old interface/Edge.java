
// Represents an Edge labeled with a real number that connects two Nodes
// of a Graph. The label is the traversal cost between Nodes.
interface Edge {
  // Returns the cost associated with this Edge
  Double getCost();
}