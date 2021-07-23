package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Boulder extends Entity {
	/**
	 * Boulder initializer
	 * @param x x position
	 * @param y y position
	 */
	public Boulder(int x, int y) {
        super(x, y);
    }

	/**
	 * Check whether a boulder is on a switch, turn switch on if yes, off if no
	 * @param x target x position
	 * @param y target y position
	 * @param player the player entity in the dungeon
	 */
	public void boulderPushed(int x, int y, Player player) {
		if (this.boulderValidMove(x, y, player)) {
			for (Entity i : player.getDungeon().getEntities()) {
				if (i.getX() == getX() && i.getY() == getY() && i instanceof Switch) {
					((Switch) i).setSwitchOn(false);
					System.out.println("switched off");
				}
			}
			x().set(getX() + x);
			y().set(getY() + y);
			for (Entity i: player.getDungeon().getEntities()) {
				if (i.getX() == getX() && i.getY() == getY() && i instanceof Switch) {
					((Switch) i).setSwitchOn(true);
					System.out.println("switched on");
				}
			}
		}
	}
	
	/**
	 * Check if the target coordinate is valid for boulder
	 * @param x target x position
	 * @param y target y position
	 * @param player the player entity in the dungeon
	 * @return
	 */
	public boolean boulderValidMove(int x, int y, Player player) {
		int positionX = getX() + x, positionY = getY() + y;
		List<Entity> objects = new ArrayList<Entity>();
		
		for (Entity i: player.getDungeon().getEntities()) {
			if (i.getX() == positionX && i.getY() == positionY) objects.add(i);
		}
		
		for (Entity o: objects) {
			if (o instanceof Wall || o instanceof Boulder || o instanceof ClosedDoor) return false;
		}
		return true;
	}
}