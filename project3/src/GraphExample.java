import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class GraphExample implements Graph {
	Double low; // The low value of the cost interval
	Double high; // The high value of the cost interval
	Vector<Node> nodes; // The list of nodes that comprise this graph
	Vector<Edge> edges; // The list of edges that comprise this graph
	
	GraphExample(Double low, Double high) {
		this.low = low;
		this.high = high;
		this.nodes = new Vector<Node>();
		this.edges = new Vector<Edge>();
	}
	
	public void setCostInterval(Double low, Double high) {
		this.low = low;
		this.high = high;
	}

	public Double getLowCostInterval() {
		if (this.low == null) {
			throw new NullPointerException(); 
		} else {
			return this.low;
		}
	}

	public Double getHighCostInterval() {
		if (this.high == null) {
			throw new NullPointerException(); 
		} else {
			return this.high;
		}
	}

	public Vector<Edge> getEdges() {
		return this.edges;
	}

	public Vector<Node> getNodes() {
		return this.nodes;
	}

	public Graph joinGraph(Graph obj) {
		GraphExample other = (GraphExample) obj;
		
		if (!this.low.equals(obj.getLowCostInterval()) || !this.high.equals(obj.getHighCostInterval())) {
			throw new IllegalArgumentException("Cost interval doesn't match");
		}
		
		for(Node n : this.nodes) {
			if (other.hasNode(n.getLabel())) {
				throw new IllegalArgumentException("Nodes overlap");
			}
		}
		
		this.nodes.addAll(other.nodes);
		
		return this;
	}

	public Path calculatePath(Node from, Node to) {
		if (!this.hasNode(from.getLabel()) || !this.hasNode(to.getLabel())) {
			throw new IllegalArgumentException("Nodes not in graph!");
		}
		
		// If from = to, return a path with 0 cost
		if(from.equals(to)) {
			return new PathExample(from, null, 0.0);
		}
		
		// If the path doesn't exist, return null!
		if (!this.pathExists(from, to)) {
			return null;
		}
		
		Map<Node,Vector<Edge>> map = this.generateMap();
		Path p = this.generatePath(from, to, map, new Vector<Node>(), new PathExample());
		
		return p;
	}
	
	public Boolean pathExists(Node from, Node to) {
		if(!this.hasNode(from.getLabel()) || !this.hasNode(to.getLabel())) {
			throw new IllegalArgumentException("Nodes not found");
		}
		//return pathExists(from, to, new Vector<Edge>(this.edges));
		Map<Node, Vector<Edge>> map = this.generateMap();
		return pathExists(from, to, map, new Vector<Node>());
	}
	
	
	public Double pathDistance(Node from, Node to) {
		if(this.hasNode(from.getLabel()) && this.hasNode(to.getLabel())) {
			PathExample p = (PathExample) this.calculatePath(from, to);
			return p.getTotalCost();
		} else {
			throw new IllegalArgumentException("Nodes not found");
		}
	}
	
	public void addEdge(String fromNode, String toNode, Double cost) {
		Node from = new NodeExample(fromNode);
		Node to = new NodeExample(toNode);

		Edge e = new Edge(from, to, cost);
		Boolean f, t; 
		f = this.hasNode(fromNode);
		t = this.hasNode(toNode);
		
		if ( f && t ) {
			if(this.checkTriangularInequality(e)) {
				this.edges.add(e);
				return;
			} else {
				throw new IllegalArgumentException("Edge failed triangular inequality");
			}
		} else if (!(f || t)) {
			this.nodes.add(from);
			this.nodes.add(to);
		} else if (!f) {
			this.nodes.add(from);
		} else {
			this.nodes.add(to);
		}
		
		this.edges.add(e);
	}
	
	public void addNode(String label) {
		Node n = new NodeExample(label);
		this.nodes.add(n);
	}

	public boolean hasNode(String label) {
		Node n = new NodeExample(label);
		return this.nodes.contains(n);
	}
	
	public Node getNode(String label) {
		if (this.hasNode(label)) {
			for (Node n : this.nodes) {
				if (n.getLabel().equals(label)) {
					return n;
				}
			}
			throw new RuntimeException("Node not found in graph");
		} else {
			throw new RuntimeException("Node not found in graph");
		}
	}

	///////////////////////////////////////////////////////////////////////////
	/// 	AUXILIARY METHODS
	///////////////////////////////////////////////////////////////////////////
	
	// Recursive method for generating a Path originating at from and ending at
	// to. Map organizes nodes and their edges. Visited keeps track of the nodes
	// that have already been visited, to prevent looping. CurrentPath is the 
	// path being constructed
	private Path generatePath(Node from, Node to, Map<Node, Vector<Edge>> map,
		Vector<Node> visited, PathExample currentPath) {
		
		Path tail;
		currentPath.setHead(from);
		visited.add(from);
		
		Vector<Edge> fromEdges = map.get(from);
		
		if (from.equals(to)) {
			// you found a path, return
			return currentPath;
		} else {
			for (Edge e : fromEdges) {
				if(visited.contains(e.getDest())) {
					// if the destination of the edge has already been visited,
					// ignore it
					continue;
				} else {
					// otherwise, set the currentPaths cost to this edges cost
					currentPath.setCostToNext(e.getCost());
					// Recur with the destination of this node as "from"
					// if it returns a non-null path, you've found the path
					tail = this.generatePath(e.getDest(), to, map, visited, new PathExample());
					if(tail != null ){
						// You found the path, set it to the currentPath's tail.
						currentPath.setTail(tail);
						return currentPath; // Return the path
					}
				}
			}
			return null;
		}
	}

	// Map nodes to their edges
	private Map<Node,Vector<Edge>> generateMap() {
		Map<Node,Vector<Edge>> map = new HashMap<Node,Vector<Edge>>();
		Vector<Edge> v;
		for (Node n : this.nodes) {
			v = new Vector<Edge>();
			for (Edge e : this.edges) {
				if (e.getOrigin().equals(n)) {
					v.add(e);
				}
			}
			Collections.sort(v, new EdgeComparator());
			map.put(n, v);
		}
		return map;
	}
	
	// Recursive method to determine whether or not a path exists between the 
	// given nodes.
	private Boolean pathExists(Node from, Node to, Map<Node, Vector<Edge>> map,
			Vector<Node> visited) {
		
		Vector<Edge> fromEdges = map.get(from);

		if (from.equals(to)) {
			// you found a path, return
			return true;
		} else {
			for (Edge e : fromEdges) {
				if(visited.contains(e.getDest())) {
					continue;
				} else {
					if(this.pathExists(e.getDest(), to, map, visited)) {
						return true;
					}
				}
			}
			return false;
		}
	}
	
	// Returns true if triangular inequality is not violated. Checks the three
	// possible cases in which it could be violated individually
	private boolean checkTriangularInequality(Edge e) {
		Vector<Edge> pointingToDest = new Vector<Edge>();
		Vector<Edge> pointingFromDest = new Vector<Edge>();
		Vector<Edge> pointingToOrigin = new Vector<Edge>();
		Vector<Edge> pointingFromOrigin = new Vector<Edge>();
		Node origin = e.getOrigin();
		Node dest = e.getDest();
		
		for (Edge edge : this.edges) {
			if (edge.getDest().equals(dest) && edge.getOrigin().equals(origin)) {
				continue;
			}
			if (edge.getDest().equals(dest)) {
				pointingToDest.add(edge);
			}
			if (edge.getOrigin().equals(dest)) {
				pointingFromDest.add(edge);
			}
			if (edge.getDest().equals(origin)) {
				pointingToOrigin.add(edge);
			}
			if (edge.getOrigin().equals(origin)) {
				pointingFromOrigin.add(edge);
			}
		}

		
		// From origin and from destination to same dest
		// CHECK 
		boolean case1 = case1Violated(pointingFromOrigin, pointingFromDest, e);
		
		
		// To origin and to destination from same origin
		// CHECK
		boolean case2 = case2Violated(pointingToOrigin, pointingToDest, e);
		
		// Destination of [from origin] equals origin of [to destination]
		// CHECK
		boolean case3 = case3Violated(pointingFromOrigin, pointingToDest, e);
		
		return !(case1 || case2 || case3);
	}
	
	// Returns true if triangular equality is broken by adding edge 'x' from
	// project 1 specification
	private boolean case1Violated(Vector<Edge> fromOrigin, Vector<Edge> fromDest, Edge e) {
		boolean violated = false;
		for (Edge fo : fromOrigin ) {
			Node dest = fo.getDest();
			for (Edge fd : fromDest ) {
				if (fd.getDest().equals(dest)) {
					violated = !this.inequality(e, fd, fo);
					break;
				}
			}
			if(violated) {
				return violated;
			}
		}
		return violated;
	}
	
	// Returns true if triangular equality is broken by adding edge 'y' from
	// project 1 specification
	private boolean case2Violated(Vector<Edge> toOrigin, Vector<Edge> toDest, Edge e) {
		boolean violated = false;
		for (Edge to : toOrigin ) {
			Node origin = to.getOrigin();
			for (Edge td : toDest ) {
				if (td.getOrigin().equals(origin)) {
					violated = !this.inequality(e, to, td);
					break;
				}
			}
			if(violated) {
				return violated;
			}
		}
		return violated;
	}
	
	// Returns true if triangular equality is broken by adding edge 'z' from
	// project 1 specification
	private boolean case3Violated(Vector<Edge> fromOrigin, Vector<Edge> toDest, Edge e) {
		boolean violated = false;
		for (Edge fo : fromOrigin ) {
			Node dest = fo.getDest();
			for (Edge td : toDest ) {
				if (td.getOrigin().equals(dest)) {
					violated = !this.inequality(fo, td, e);
					break;
				}
			}
			if(violated) {
				return violated;
			}
		}
		return violated;
	}
	
	// returns true if triangular inequality holds
	private boolean inequality(Edge x, Edge y, Edge z) {
		return (x.getCost() + y.getCost()) >= z.getCost();
	}
	
	// Compares edges based on their costs
	private class EdgeComparator implements Comparator<Edge> {

		@Override
		public int compare(Edge e1, Edge e2) {
			return e1.getCost().compareTo(e2.getCost());
		}

	}

}
