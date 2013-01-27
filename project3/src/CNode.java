import java.util.ArrayList;

public class CNode implements Node {
	
	private String data;
	
	private ArrayList<CEdge> edges;
	
	public CNode(String data)
	{
		this.data = data;
		this.edges = new ArrayList<CEdge>();
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return data;
	}
	
	public void addEdge(CEdge edge)
	{
		this.edges.add(edge);
	}
	
	public ArrayList<CEdge> getEdges()
	{
		return this.edges;
	}
	
	public Double minDistance = Double.MAX_VALUE;
	public CNode previous = null;
	public Double distance = 0.0;
	
	public boolean hasInequality(CNode to, CGraph g, Double c){
		for (CEdge e : to.getEdges())
		{
			Double b = g.directConnectCost((CNode) e.getTo(), this);
			if (b == null)
			{
				continue;
			}
			else
			{
				Double a = e.getCost();
				if ((a+c)<=b || (a+b)<=c || (b+c)<=a)
				{
					return true;
				}
			}
		}
		return false;
	}

}
