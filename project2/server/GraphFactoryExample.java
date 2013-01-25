

public class GraphFactoryExample implements GraphFactory {

	public Graph createGraph(Double low, Double high) {
		// TODO Auto-generated method stub
		return new GraphExample(low, high);
	}

}
