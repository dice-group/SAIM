package de.uni_leipzig.simba.saim.gui.widget.form;

import java.util.HashMap;
import de.uni_leipzig.simba.cache.Cache;
import de.uni_leipzig.simba.genetics.selfconfig.GeneticSelfConfigurator;
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
		setGenerations(20);
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
	public HashMap<String,Object> getConfiguartorParams(Configuration config, Cache sC, Cache tC) {
		HashMap<String,Object> params = new HashMap<String, Object>();

		params.put(GeneticSelfConfigurator.pSInfo, config.getSource());
		params.put(GeneticSelfConfigurator.pTInfo, config.getTarget());
		params.put(GeneticSelfConfigurator.pSCache, sC);
		params.put(GeneticSelfConfigurator.pTCache, tC);

		params.put(GeneticSelfConfigurator.pPropMapping, config.propertyMapping);
		System.out.println("Setting property mapping to: \n"+config.propertyMapping);

		params.put(GeneticSelfConfigurator.pBeta, beta);
		params.put(GeneticSelfConfigurator.pGen, generations);
		params.put(GeneticSelfConfigurator.pPop, population);
		params.put(GeneticSelfConfigurator.pCrossover, (float)crossoverRate);
		params.put(GeneticSelfConfigurator.pMutation, (float)mutationRate);
		PseudoMeasures pseudoMeasure = new PseudoMeasures();
		if(this.measure == 1) {
			pseudoMeasure = new ReferencePseudoMeasures();
		}
		params.put(GeneticSelfConfigurator.pMeasure, pseudoMeasure);

		return params;
	}
}
