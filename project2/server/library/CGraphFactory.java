
public class CGraphFactory implements GraphFactory {

	@Override
	public Graph createGraph(Double low, Double high) {
		Graph g = new CGraph(low, high);
		return g;
	}

}
