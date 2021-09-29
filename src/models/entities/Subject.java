package models.entities;

import java.util.Random;

import environment.Parameters;
import models.enums.LifeStage;

public class Subject {
	protected Point position;
	protected LifeStage stage;
	protected boolean isFemale;
	protected boolean isGivingBirth;
	protected int age;
	protected int virusTime;
	protected int incubationTime;
	private float angle;
	
	public Subject(int age, boolean isFemale, LifeStage stage, float angle, Point position) {
		this.age = age;
		this.isFemale = isFemale;
		this.stage = stage;
		this.angle = angle;
		this.position = position;
		this.virusTime = -1;
		this.incubationTime = -1;
		this.isGivingBirth = false;
	}
	
	public Subject(Subject clone) {
		this.age = clone.age;
		this.isFemale = clone.isFemale;
		this.stage = clone.stage;
		this.angle = clone.angle;
		this.position = clone.position;
		this.virusTime = clone.virusTime;
		this.incubationTime = clone.incubationTime;
		this.isGivingBirth = clone.isGivingBirth;
	}
	
	public void step(double timeUnit) {
		Random r = new Random();
		this.age++;
		
		if(this.age > this.stage.getEndAge())
			if(this.stage != LifeStage.DEATH)
				this.stage = LifeStage.values()[this.stage.ordinal() + 1];
		
		if(this.hasVirus()) {
			this.virusTime--;
			
			if(r.nextFloat() <= 0.1)
				this.stage = LifeStage.DEATH;
		}
		
		if(this.incubationTime >= 0) {
			this.incubationTime--;
			
			if(this.incubationTime == 0)
				this.isGivingBirth = true;
		}
		
		this.position.x += Math.cos(Math.toRadians(this.angle)) * this.stage.getSpeed() * timeUnit;
		this.position.y += Math.sin(Math.toRadians(this.angle)) * this.stage.getSpeed() * timeUnit;
		
		if(this.position.x > Parameters.MAP_WIDTH) {
			this.position.x = Parameters.MAP_WIDTH * 2  - this.position.x;
			this.angle = 90 + r.nextInt(180);
		}
		
		if(this.position.x < 0) {
			this.position.x = -this.position.x;
			this.angle = -90 + r.nextInt(180);
		}
		
		if(this.position.y > Parameters.MAP_HEIGHT) {
			this.position.y = Parameters.MAP_HEIGHT * 2  - this.position.y;
			this.angle = -r.nextInt(180);
		}
		
		if(this.position.y < 0) {
			this.position.y = -this.position.y;
			this.angle =  r.nextInt(180);
		}
		
	}
	
	public void step() {
		this.step(1);
	}
	
	public int getAge() {
		return age;
	}
	
	public boolean isFemale() {
		return isFemale;
	}
	
	public LifeStage getStage() {
		return stage;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public String toText(){
		return "Age: %s\t%s\tGender: %s\t%s\t%s"
			.formatted(
				this.age,
				this.stage.toString().substring(0, 3),
				this.isFemale ? 'F' : 'M',
				this.hasVirus() ? "Virus" : "",
				this.position.toText()
			);
	}
	
	public void setVirusTime(int virusTime) {
		this.virusTime = virusTime;
	}
	
	public void setIncubationTime(int incubationTime) {
		this.incubationTime = incubationTime;
	}
	
	public boolean hasVirus() {
		return this.virusTime >= 0;
	}
	
	public void setGivingBirth(boolean isGivingBirth) {
		this.isGivingBirth = isGivingBirth;
	}
	
	public void kill() {
		this.stage = LifeStage.DEATH;
	}
	
	public boolean isGivingBirth() {
		return isGivingBirth;
	}
}
