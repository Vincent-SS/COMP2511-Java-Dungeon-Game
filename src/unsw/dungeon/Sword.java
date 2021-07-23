package unsw.dungeon;


public class Sword extends Entity {
	private int availableHit;
	
	public Sword(int x, int y) {
        super(x, y);
        this.availableHit = 5;
    }
	
	public int getAvailableHit() {
		return this.availableHit;
	}
	
	public void reduce() {
		if (availableHit != 0) availableHit -= 1;
		else availableHit = 0;
	}
}
