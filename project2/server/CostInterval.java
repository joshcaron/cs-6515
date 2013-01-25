import java.util.Iterator;
import java.util.Vector;


public class CostInterval {
	Double min;
	Double max;
	
	CostInterval(Vector<Double> e) {
		Iterator<Double> it = e.iterator();
		if (!it.hasNext()) {
			this.min = 0.0;
			this.max = 0.0;
		} else {
			this.min = this.max = it.next();
			while(it.hasNext()) {
				Double i = it.next();
				if (i < this.min) {
					this.min = i;
				} 
				if (i > this.max) {
					this.max = i;
				}
			}
		}
	}
	
	Double getMin() {
		return this.min;
	}
	
	Double getMax() {
		return this.max;
	}
}
