package de.uni_leipzig.simba.saim.gui.widget.form;
/**
 * Bean used by the form to configure the MeshBased SelfConfigurator;
 * @author Lyko
 *
 */
public class SelfConfigMeshBasedBean {
	
	private int gridPoints;
	private int iterations;
	private double beta;
	private double minCoverage;
	
	public SelfConfigMeshBasedBean() {
		setDefaultValues();
	}
	
	private void setDefaultValues() {
		setGridPoints(5);
		setIterations(5);
		setBeta(0.5d);
		setMinCoverage(0.6d);
	}

	public int getGridPoints() {
		return gridPoints;
	}

	public void setGridPoints(int gridPoints) {
		this.gridPoints = gridPoints;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	public double getMinCoverage() {
		return minCoverage;
	}

	public void setMinCoverage(double minCoverage) {
		this.minCoverage = minCoverage;
	}
	
}
