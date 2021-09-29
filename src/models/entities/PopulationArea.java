package models.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import environment.Parameters;
import models.enums.LifeStage;
import utils.IAnonFunc;

public class PopulationArea implements Runnable, Parameters {
	private Random rnd;
	private List<Subject> population;
	private IAnonFunc subjectsCallback;

	public PopulationArea() {
		this.rnd = new Random();
		this.population = new ArrayList<>();
		this.initPopulation();
	}
	
	public PopulationArea(IAnonFunc subjectsCallback) {
		this.rnd = new Random();
		this.population = new ArrayList<>();
		this.initPopulation();
		this.subjectsCallback = subjectsCallback;
	}

	private void initPopulation() {
		for (LifeStage stage : LifeStage.values()) {
			for (int i = 0; i < INIT_POPULATION * stage.getPercent(); i++) {
				this.population
						.add(new Subject(stage.getInitAge() + this.rnd.nextInt(stage.getEndAge() - stage.getInitAge()),
								this.rnd.nextBoolean(), stage, this.rnd.nextFloat() * 360,
								new Point(this.rnd.nextInt(MAP_WIDTH), this.rnd.nextInt(MAP_HEIGHT))));
			}
		}
	}

	private Subject generateChildSubject() {
		LifeStage stage = LifeStage.CHILDHOOD;

		return new Subject(stage.getInitAge(), this.rnd.nextBoolean(), stage, this.rnd.nextFloat() * 360,
				new Point(this.rnd.nextInt(MAP_WIDTH), this.rnd.nextInt(MAP_HEIGHT)));
	}

	@Override
	public void run() {
		boolean isAlive = true;
		Map<String, List<Subject>> positions;
		List<Subject> deadSubjects = new ArrayList<>();
		List<Subject> bornedSubjects = new ArrayList<>();
		int time = 0;

		while (isAlive) {
			positions = new HashMap<>();
			System.out.println("%s, %s".formatted(time++, this.population.size()));

			for (Subject s : this.population) {
				if (s.isGivingBirth())
					bornedSubjects.add(this.generateChildSubject());

				if (positions.containsKey(s.getPosition().toText())) {
					List<Subject> l = positions.get(s.getPosition().toText());
					List<Subject> deadAuxSubjects = new ArrayList<>();
					boolean isSDead = false;

					for (Subject sAux : l) {
						if (s.hasVirus())
							sAux.setVirusTime(VIRUS_TIME);

						if (sAux.hasVirus())
							s.setVirusTime(VIRUS_TIME);

						if (s.getStage().equals(LifeStage.ADOLESCENCE) && sAux.getStage().equals(LifeStage.ADOLESCENCE)
								&& !s.isFemale() && !sAux.isFemale()) {
							if (this.rnd.nextBoolean()) {
								isSDead = true;
								s.kill();
								break;
							} else {
								deadAuxSubjects.add(sAux);
								deadSubjects.add(sAux);
							}
						} else if (s.getStage().equals(LifeStage.ADULTHOOD)
								&& sAux.getStage().equals(LifeStage.ADULTHOOD)) {
							if (Boolean.logicalXor(s.isFemale(), sAux.isFemale())) {
								if (s.isFemale())
									s.setIncubationTime(INCUBATION_TIME);
								else
									sAux.setIncubationTime(INCUBATION_TIME);
							} else if (s.isFemale() && sAux.isFemale()) {
								s.setVirusTime(VIRUS_TIME);
								sAux.setVirusTime(VIRUS_TIME);
							}
						}
					}

					l.removeAll(deadAuxSubjects);

					if (!isSDead)
						l.add(s);
				} else {
					List<Subject> l = new ArrayList<>();
					l.add(s);
					positions.put(s.getPosition().toText(), l);
				}

				s.step();

				if (s.getStage().equals(LifeStage.DEATH)) {
					deadSubjects.add(s);
					continue;
				}
			}

			this.population.removeAll(deadSubjects);
			deadSubjects.clear();
			this.population.addAll(bornedSubjects);
			bornedSubjects.clear();

			if (this.population.size() == 0)
				isAlive = false;
			
			if(this.subjectsCallback != null)
				this.subjectsCallback.f(this.population);

			try {
				Thread.sleep(TIME_UNIT);
			} catch (InterruptedException e) {
			}
		}
		
		System.out.println("Fin");
	}
}
