package models.enums;

public enum LifeStage {
	CHILDHOOD(0, 30, 4f, .2f), 
	ADOLESCENCE(31, 50, 10f, .25f), 
	ADULTHOOD(51, 70, 12f, .4f), 
	OLD_AGE(71, 90, 8f, .15f), 
	DEATH;
	
	private int initAge;
	private int endAge;
	private float speed;
	private float percent;
	
	private LifeStage(int initAge, int endAge, float speed, float percent) {
		this.initAge = initAge;
		this.endAge = endAge;
		this.speed = speed;
		this.percent = percent;
	}
	
	private LifeStage() {
		this.initAge = -1;
		this.endAge = -1;
		this.speed = 0;
	}
	
	public int getInitAge() {
		return initAge;
	}
	
	public int getEndAge() {
		return endAge;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public float getPercent() {
		return percent;
	}
}
