
public class Feature implements Comparable<Object> {
	String name;
	int feature;
	int sfeature;
	
	public Feature(String name, int feature, int superFeature) {
		this.name = name;
		this.feature = feature;
		this.sfeature = superFeature;
	}
	
	public String getName() {
		return name;
	}
	
	public int getFeature() {
		return feature;
	}
	
	public int getSuper() {
		return sfeature;
	}

	@Override
	public int compareTo(Object o) {
		return ((Feature)o).name.length() - this.name.length();
	}
}
