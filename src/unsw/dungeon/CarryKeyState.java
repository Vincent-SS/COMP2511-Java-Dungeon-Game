package unsw.dungeon;

public class CarryKeyState implements KeyState {
	public CarryKeyState() {
		
	}
	
	/**
	 * The action will be triggered once the player is carrying a key, and it will be switched to no key state
	 */
	@Override
	public void doAction(Player player, Entity e, Dungeon dungeon) {
		// TODO Auto-generated method stub
		player.setKeyState(new NoKeyState());
		player.setHaveKey(false);
		player.getBag().remove(player.getKey());
		dungeon.removeEntity(e);
	}
}
