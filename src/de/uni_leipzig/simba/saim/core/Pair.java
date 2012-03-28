package de.uni_leipzig.simba.saim.core;

public class Pair<T> {
	private T a;
	private T b;

	public T getA() {
		return a;
	}

	public T getB() {
		return b;
	}
	
	public void setA(T a) {
		this.a = a;
	}
	
	public void setB(T b) {
		this.b = b;
	}

	public Pair(T a, T b) {
		super();
		this.a = a;
		this.b = b;
	}
	
	public String toString() {
		return a.toString()+" - "+b.toString();
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(o.getClass().equals(this.getClass())) {
			@SuppressWarnings("unchecked")
			Pair<String> oP = (Pair<String>) o;
			if(oP.getA().equals(a) && oP.getB().equals(b))
				return true;
			return false;
		}
		return false;
	}
	
	
}
