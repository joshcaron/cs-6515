
public class NodeExample implements Node {
	String label;
	
	
	NodeExample(String label) {
		this.label = label;
	}
	
	public void setLabel(String o) {
		this.label = o;
	}

	public String getLabel() {
		return this.label;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			return this.getLabel().equals(((NodeExample) o).getLabel());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.label.hashCode();
	}

}
