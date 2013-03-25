package de.uni_leipzig.simba.saim.gui.widget.form;

import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.selfconfig.DisjunctiveMeshSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.LinearMeshSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.Measure;
import de.uni_leipzig.simba.selfconfig.MeshBasedSelfConfigurator;
import de.uni_leipzig.simba.selfconfig.PseudoMeasures;
import de.uni_leipzig.simba.selfconfig.ReferencePseudoMeasures;
/**
 * Bean used by the form to configure the MeshBased SelfConfigurator;
 * @author Lyko
 */
public class SelfConfigMeshBasedBean {
	private int classifierName;
	private int gridPoints;
	private int iterations;
	private double beta;
	private double minCoverage;
	private int measure;

	public SelfConfigMeshBasedBean() {
		setDefaultValues();
	}

	private void setDefaultValues() {
		setGridPoints(3);
		setIterations(3);
		setBeta(1.0d);
		setMinCoverage(0.6d);
		setClassifierName(0);
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

	/**
	 * Method to create the classifier according to its id.
	 * @param id
	 * @param sourceCache
	 * @param targetCache
	 * @param minCoverage
	 * @param beta
	 * @return
	 */
	public MeshBasedSelfConfigurator getConfigurator(int id, Cache sourceCache, Cache targetCache, double minCoverage, double beta) {
		MeshBasedSelfConfigurator bsc;
		switch(id) {
			case 0:
				bsc = new MeshBasedSelfConfigurator(sourceCache, targetCache, minCoverage, beta);
				break;
			case 1:
				bsc = new LinearMeshSelfConfigurator(sourceCache, targetCache, minCoverage, beta);
				break;
			case 2:
				bsc = new DisjunctiveMeshSelfConfigurator(sourceCache, targetCache, minCoverage, beta);
				break;
			default:
				bsc = new MeshBasedSelfConfigurator(sourceCache, targetCache, minCoverage, beta);
				break;
		}
		bsc.ITERATIONS_MAX = iterations;
		Measure measureS = new PseudoMeasures();
		if(measure != 0)
			measureS = new ReferencePseudoMeasures();
		bsc.setMeasure(measureS);
		return bsc;
	}

	public int getClassifierName() {
		return classifierName;
	}

	public void setClassifierName(int classifierName) {
		System.out.println("Setting classifier name to:"+classifierName);
		this.classifierName = classifierName;
	}
	
	public int getMeasure() {
		return measure;
	}

	public void setMeasure(int measure) {
		this.measure = measure;
	}

}
