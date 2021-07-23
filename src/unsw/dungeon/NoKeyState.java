package unsw.dungeon;

import java.util.ArrayList;

public class NoKeyState implements KeyState {

	/**
	 * The action will be triggered once the player has no key, and it will be switched to carry key state
	 */
	@Override
	public void doAction(Player player, Entity e, Dungeon dungeon) {
		// TODO Auto-generated method stub
		player.setKeyState(new CarryKeyState());
		player.getBag().add(e);
		player.setHaveKey(true);
		player.setKey(e);
		//dungeon.removeEntity(e);
	}

}
