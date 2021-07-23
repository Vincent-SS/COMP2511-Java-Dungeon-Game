package unsw.dungeon;

public class TimeFreezePotion extends Entity{
	private int durability;
	
	public TimeFreezePotion(int x, int y) {
        super(x, y);
        this.durability = 100;
    }
	
	/**
	 * Get the durability of Time Freeze portion
	 * @return
	 */
	public int getDurability() {
		return this.durability;
	}
	
	/**
	 * the durability of this portion will be reduced by 1 everytime the player moves
	 */
	public void reduce() {
		this.durability -= 1;
	}
}