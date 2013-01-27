
public class CEdge implements Edge {
	
	private Node origin;
	private Node destination;
	private Double cost;
	
	public CEdge(Node from, Node to, Double cost)
	{
		this.origin = from;
		this.destination = to;
		this.cost = cost;
	}
	
	public Node getFrom()
	{
		return origin;
	}
	
	public Node getTo()
	{
		return destination;
	}

	@Override
	public Double getCost() {
		return cost;
	}
}
