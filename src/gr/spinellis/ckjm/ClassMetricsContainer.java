

package gr.spinellis.ckjm;

import java.util.HashMap;
import java.util.Map;

class ClassMetricsContainer {
	private Map<String, ClassMetrics> classMetricsMap = new HashMap<>();

	public ClassMetrics getMetrics(String name) {
		return classMetricsMap.computeIfAbsent(name, k -> new ClassMetrics());
	}

	public Map<String, ClassMetrics> getClassMetricsMap() {
		return classMetricsMap;
	}
}
