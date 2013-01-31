// Represents an Edge between nodes with a given cost in this graph
public class Edge {
	Node from;
	Node to;
	Double cost;
	Edge(Node from, Node to, Double cost) {
		this.from = from;
		this.to = to;
		this.cost = cost;
	}

	Node getOrigin() {
		return this.from;
	}

	Node getDest() {
		return this.to;
	}

	Double getCost() {
		return this.cost;
	}
}