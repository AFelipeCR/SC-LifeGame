package lifegame;

import models.entities.PopulationArea;

public class LifeGame {
	public static void main(String[] args) {
		new Thread(new PopulationArea()).start();
	}
}