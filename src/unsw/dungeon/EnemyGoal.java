package unsw.dungeon;

public class EnemyGoal extends Goal implements Goals {
	
	public EnemyGoal(Dungeon dungeon) {
		super(dungeon);
	}
	
	/**
	 * Check if kill enemy goal is achieved
	 */
	public boolean getAchieved() {
		if (dungeon.getTotalEnemy() == 0) return true;
		return false;
	}
}