package unsw.dungeon;

public class Invincibility extends Entity{
	private int durability;
	/**
	 * Invincible posion initializer
	 * @param x x position
	 * @param y y position
	 */
	public Invincibility(int x, int y) {
        super(x, y);
        this.durability = 10;
    }	
	
	public int getDurability() {
		return this.durability;
	}
	
	/**
	 * reduce durability by 1
	 */
	public void reduce() {
		this.durability -= 1;
	}
}
