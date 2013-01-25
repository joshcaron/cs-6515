
public class Mainclass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		CGraphFactory cfg = new CGraphFactory();
		
		CGraph g = (CGraph)cfg.createGraph(0.0, 100.0);
		
		g.addNode("a");
		g.addNode("b");
		g.addNode("c");
		g.addNode("d");
		g.addNode("e");

		if (g.hasNode("a"))
		{
			System.out.println("test worked!");
		}
		
		Node a = g.getNode("a");
		Node b = g.getNode("b");
		Node c = g.getNode("c");
		Node d = g.getNode("d");
		Node e = g.getNode("e");
		
		/*g.addEdge(a, b, 25.0);
		g.addEdge(a, b, 50.0);
		g.addEdge(a, b, 100.0);
		g.addEdge(a, b, 5.0);
		
		g.addEdge(b, c, 25.0);
		g.addEdge(c, a, 1.0);
		
		g.addEdge(c, e, 5.0);
		g.addEdge(c, d, 10.0);
		g.addEdge(c, a, 25.0);
		
		g.addEdge(d, e, 5.0);*/
		
		g.addEdge(a, b, 10.0);
		g.addEdge(b, c, 15.0);
		g.addEdge(c, a, 6.0);
		
		g.getPath(a, c);
		
		CGraph g2 = (CGraph)cfg.createGraph(0.0, 100.0);
		
		g2.addNode("p");
		
		g2.joinGraph(g);
		
		/*
		if (g.pathExists(a, d))
		{
			System.out.println("Yes!");
		}
		else
		{
			System.out.println("NO!");
		}*/
		
		/*CPath p = (CPath) g.getPath(a, e);
		System.out.println("Stop at " + p.head.getLabel() + ", will take " + p.getCost() + " to get there");
		while (p.tail != null)
		{
			p = p.tail;
			System.out.println("Stop at " + p.head.getLabel() + ", will take " + p.getCost() + " to get there");
		}
		
		if (g.pathCost(a, e) == 35.0)
		{
			System.out.println("The test worked!");
		}*/
	}

}
