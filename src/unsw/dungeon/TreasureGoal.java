package unsw.dungeon;

public class TreasureGoal extends Goal implements Goals {
	
	public TreasureGoal(Dungeon dungeon) {
		super(dungeon);
	}
	
	/**
	 * Check whether treasure goal is achieved
	 */
	public boolean getAchieved() {
		if (dungeon.getTotalTreasure() == 0) return true;
		return false;
	}
}