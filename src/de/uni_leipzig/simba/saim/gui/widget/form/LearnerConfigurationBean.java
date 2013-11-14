package de.uni_leipzig.simba.saim.gui.widget.form;

import de.uni_leipzig.simba.genetics.learner.SupervisedLearnerParameters;
/**
 * Bean to automatically build form for genetic learner configuration.
 * @author Lyko
 */
public class LearnerConfigurationBean {
	private int generations;
	private int population;
	private double mutationRate;
	private double crossoverRate;
	private int numberOfInqueriesPerRun;

	public LearnerConfigurationBean() {
		setDefaultValues();
	}

	/**
	 * To set some default values.
	 */
	private void setDefaultValues() {
		setGenerations(20);
		setPopulation(10);
		setMutationRate(0.4d);
		setCrossoverRate(0.4d);
		setNumberOfInqueriesPerRun(10);
	}

	public int getGenerations() {
		return generations;
	}
	public void setGenerations(int generations) {
		this.generations = generations;
	}
	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}
	public double getMutationRate() {
		return mutationRate;
	}
	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}
	public int getNumberOfInqueriesPerRun() {
		return numberOfInqueriesPerRun;
	}
	public void setNumberOfInqueriesPerRun(int numberOfInqueriesPerRun) {
			this.numberOfInqueriesPerRun = numberOfInqueriesPerRun;
	}

	public double getCrossoverRate() {
		return crossoverRate;
	}

	public void setCrossoverRate(double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}

	@Override
	public String toString() {
		return "BEAN: gens="+generations+" pop="+population+" mutationRate="+mutationRate+" inquieries="+numberOfInqueriesPerRun;
	}

	/**
	 * Generate params HashMap used by leraner.
	 * @return
	 */
	public SupervisedLearnerParameters createParams() {
		SupervisedLearnerParameters params = new SupervisedLearnerParameters(null, null);
		params.setPopulationSize(getPopulation());
		params.setGenerations(getGenerations());
		params.setCrossoverRate((float)getCrossoverRate());
	    params.setMutationRate((float)getMutationRate());
		params.setTrainingDataSize(getNumberOfInqueriesPerRun());
		return params;
	}


}
