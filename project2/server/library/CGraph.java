import java.util.ArrayList;
import java.util.Collections;

public class CGraph implements Graph {

	private ArrayList<CNode> nodes;
	private Double low_cost;
	private Double high_cost;
	
	public CGraph(Double low, Double high)
	{
		this.nodes = new ArrayList<CNode>();
		this.low_cost = low;
		this.high_cost = high;
	}
	
	@Override
	public void addNode(String label) {
		CNode n = new CNode(label);
		nodes.add(n);
	}

	@Override
	public boolean hasNode(String label) {
		for (Node n : this.nodes)
		{
			if (n.getLabel() == label)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public Node getNode(String label) {
		for (Node n : this.nodes)
		{
			if (n.getLabel() == label)
			{
				return n;
			}
		}
		throw new RuntimeException("Node does not exist in graph");
	}

	@Override
	public void addEdge(Node origin, Node dest, Double cost) {
		if (cost < this.low_cost || cost > this.high_cost)
		{
			// Not valid - doesn't fall in the cost interval
			throw new RuntimeException("Cost is not in this graph's cost interval");
		}
		CNode c_origin = null;
		for (CNode cn : this.nodes)
		{
			if (cn.getLabel() == origin.getLabel())
			{
				c_origin = cn;
				break;
			}
		}
		if (c_origin == null)
		{
			throw new RuntimeException("Origin node was not added to graph prior to attempted edge add");
		}
		if (c_origin.hasInequality((CNode)dest, this, cost))
		{
			throw new RuntimeException("Add violates triangle inequality");
		}
		else
		{
			c_origin.addEdge(new CEdge(origin, dest, cost));
		}
	}


	@Override
	public boolean pathExists(Node origin, Node dest) {
		CNode c_origin = null;
		for (CNode cn : this.nodes)
		{
			if (cn.getLabel() == origin.getLabel())
			{
				c_origin = cn;
				break;
			}
		}
		if (c_origin == null)
		{
			throw new RuntimeException("Origin does not exist in graph");
		}
		ArrayList<CEdge> edges = new ArrayList<CEdge>();
		for (CEdge edge : c_origin.getEdges())
		{
			edges.add(edge);
		}
		ArrayList<CEdge> visited = new ArrayList<CEdge>();
		
		while (!edges.isEmpty())
		{
			CEdge e = edges.get(0);
			if (visited.contains(e))
			{
				edges.remove(0);
				continue;
			}
			visited.add(e);
			if (e.getTo().getLabel().equals(dest.getLabel()))
			{
				// We've found it!
				return true;
			}
			else
			{
				for (CNode cn : this.nodes)
				{
					if (cn.getLabel().equals(e.getTo().getLabel()))
					{
						edges.addAll(cn.getEdges());
						break;
					}
				}
				edges.remove(0);
			}
		}
		return false;
	}

	@Override
	public Double pathCost(Node origin, Node dest) {
		// TODO Auto-generated method stub
		CPath p = (CPath)this.getPath(origin, dest);
		Double d = 0.0;
		
		if (p.getCost() == null)
		{
			throw new RuntimeException("Path does not exist");
		}
		
		while (p.getCost() != null)
		{
			d += p.getCost();
			p = p.tail;
		}
		
		return d;
	}

	@Override
	public Path getPath(Node origin, Node dest) {
		
		CNode c_from = (CNode)origin;
		
		c_from.minDistance = 0.0;
		
		ArrayList<CNode> queue = new ArrayList<CNode>();
		queue.add(c_from);
		
		while (!queue.isEmpty())
		{
			CNode node = queue.get(0);
			queue.remove(0);
			for (CEdge edge : node.getEdges())
			{
				CNode v = (CNode)edge.getTo();
				
				Double cost = edge.getCost();
				Double distance = node.minDistance + cost;
				if (distance < v.minDistance)
				{
					queue.remove(v);
					v.minDistance = distance;
					v.previous = node;
					v.distance = cost;
					queue.add(v);
				}
			}
		}
		
		ArrayList<CNode> path = new ArrayList<CNode>();
		for (CNode node = (CNode)dest; node != null; node = node.previous)
		{
			path.add(node);
		}
		Collections.reverse(path);
		
		CPath topmost_path = null;
		for (int x = 0; x < path.size(); x++)
		{
			CNode n = path.get(x);
			
			if (topmost_path == null)
			{
				topmost_path = new CPath(n, null);
			}
			else
			{
				topmost_path.setTail(new CPath(n, null));
			}
		}
		
		if (topmost_path.tail == null)
		{
			throw new RuntimeException("Path does not exist");
		}
		
		return topmost_path;
	}

	@Override
	public void joinGraph(Graph other) {
		if (other instanceof CGraph)
		{
			CGraph othergraph = (CGraph)other;
			if (this.high_cost.equals(othergraph.high_cost) &&
				this.low_cost.equals(othergraph.low_cost))
			{
				for (Node n : othergraph.nodes)
				{
					for (Node n2 : this.nodes)
					{
						if (n.getLabel().equals(n2.getLabel()))
						{
							// Disjointed set of notes
							throw new RuntimeException("Disjointed set of nodes because: node " + n.getLabel() + " and node " + n2.getLabel());
						}
					}
				}
				this.nodes.addAll(othergraph.nodes);
			}
			else
			{
				throw new RuntimeException("Graphs do not have the same cost interval");
			}
		}
	}
	
	public boolean isDirectlyConnected(CNode a, CNode b)
	{
		for (CEdge e : a.getEdges())
		{
			if (e.getTo() == b)
			{
				return true;
			}
		}
		for (CEdge e : b.getEdges())
		{
			if (e.getTo() == a)
			{
				return true;
			}
		}
		return false;
	}
	
	public Double directConnectCost(CNode a, CNode b)
	{
		for (CEdge e: a.getEdges())
		{
			if (e.getTo() == b)
			{
				return e.getCost();
			}
		}
		for (CEdge e: b.getEdges())
		{
			if (e.getTo() == a)
			{
				return e.getCost();
			}
		}
		return null;
	}

}
