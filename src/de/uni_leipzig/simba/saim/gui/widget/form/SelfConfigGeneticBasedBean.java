package de.uni_leipzig.simba.saim.gui.widget.form;


import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.genetics.learner.UnSupervisedLearnerParameters;
import de.uni_leipzig.simba.saim.core.Configuration;
import de.uni_leipzig.simba.selfconfig.PseudoMeasures;
import de.uni_leipzig.simba.selfconfig.ReferencePseudoMeasures;
/**
 * Bean to generate form for Genetic-based self-configurations.
 * @author Lyko
 */
public class SelfConfigGeneticBasedBean {
	private double beta;
	private int measure;
	private int population;
	private int generations;
	private double mutationRate;
	private double crossoverRate;


	/**
	 * Constructor. Calls setting default values.
	 */
	public SelfConfigGeneticBasedBean() {
		setDefaultValues();
	}
	/**
	 * Sets default values except the measure!.
	 */
	public void setDefaultValues() {
		setBeta(1.0);
		setPopulation(10);
		setGenerations(10);
		setMutationRate(0.4f);
		setCrossoverRate(0.4f);
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	public int getMeasure() {
		return measure;
	}

	public void setMeasure(int measure) {
		this.measure = measure;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getGenerations() {
		return generations;
	}

	public void setGenerations(int generations) {
		this.generations = generations;
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public double getCrossoverRate() {
		return crossoverRate;
	}

	public void setCrossoverRate(double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}

	/**
	 * Method to get parameter for the Genetic based self configurator.
	 * @param config Needed to set KBInfos and PropertyMappings.
	 * @param sC separated to update GUI.
	 * @param tC separated to update GUI.
	 * @return HashMap<String,Object> params: the parameter based on the current values of the bean to learn with the Genetic Based Selfconfigurator.
	 */
	public UnSupervisedLearnerParameters getConfiguartorParams(Configuration config, Cache sC, Cache tC) {
		UnSupervisedLearnerParameters params = new UnSupervisedLearnerParameters(config.getLimesConfiReader(), config.propertyMapping);
//		params.put(GeneticSelfConfigurator.pSInfo, config.getSource());
//		params.put(GeneticSelfConfigurator.pTInfo, config.getTarget());
//		params.put(GeneticSelfConfigurator.pSCache, sC);
//		params.put(GeneticSelfConfigurator.pTCache, tC);
		params.setGenerations(generations);
		params.setPopulationSize(population);
		params.setCrossoverRate((float)crossoverRate);
		params.setMutationRate((float)mutationRate);
		PseudoMeasures pseudoMeasure = new PseudoMeasures();
		if(this.measure == 1) {
			pseudoMeasure = new ReferencePseudoMeasures();
		}
		params.setPseudoFMeasure(pseudoMeasure);
		params.setPFMBetaValue(beta);
	
		return params;
	}
}
