package unsw.dungeon;

import java.util.ArrayList;

public class CompsiteGoal extends Goal implements Goals {
	public ArrayList<Goal> goals;
	public String logic;
	
	/**
	 * Composite Goal initializer
	 * @param dungeon
	 * @param logic goal logic for the composite goal
	 */
	public CompsiteGoal(Dungeon dungeon, String logic) {
		super(dungeon);
		this.goals = new ArrayList<Goal>();
		this.logic = logic;
	}
	
	/**
	 * check if this composite goal is achieved
	 */
	public boolean getAchieved() {
		switch (this.logic) {
		case "AND":
			for (Goal g: this.goals) {
				if (g instanceof TreasureGoal) {
					if (!((TreasureGoal) g).getAchieved()) return false;
				}
				else if (g instanceof SwitchGoal) {
					if (!((SwitchGoal) g).getAchieved()) return false;
				}
				else if (g instanceof EnemyGoal) {
					if (!((EnemyGoal) g).getAchieved()) return false;
				}
				else if (g instanceof CompsiteGoal) {
					if (!((CompsiteGoal) g).getAchieved()) return false;
				}
			}
			return true;
		case "OR":
			for (Goal g: this.goals) {
				if (g instanceof TreasureGoal) {
					if (((TreasureGoal) g).getAchieved()) return true;
				}
				else if (g instanceof SwitchGoal) {
					if (((SwitchGoal) g).getAchieved()) return true;
				}
				else if (g instanceof EnemyGoal) {
					if (((EnemyGoal) g).getAchieved()) return true;
				}
				else if (g instanceof CompsiteGoal) {
					if (((CompsiteGoal) g).getAchieved()) return true;
				}
			}
			return false;
		}
		return false;
	}
}