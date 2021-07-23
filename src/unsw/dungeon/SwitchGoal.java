package unsw.dungeon;

public class SwitchGoal extends Goal implements Goals {
	
	public SwitchGoal(Dungeon dungeon) {
		super(dungeon);
	}
	
	/**
	 * Check whether Switch goal is achieved
	 */
	public boolean getAchieved() {
		if (dungeon.isAllSwitchOn()) return true;
		return false;
	}
}