
public class NodeExample implements Node {
	Object data;
	
	
	NodeExample(Object data) {
		this.data = data;
	}
	
	public void setData(Object o) {
		this.data = o;
	}

	public Object getData() {
		return this.data;
	}

}
