package unsw.dungeon;

public class Enemy extends Entity {
	private Dungeon dungeon;
	private Movement movestrats;
	private Player player;
	
	/**
	 * Enemy Initializer
	 * @param x x position
	 * @param y y position
 	 * @param dungeon dungeon that this enemy will be placed
	 */
	public Enemy(int x, int y, Dungeon dungeon) {
        super(x, y);
        this.dungeon = dungeon;
        this.movestrats = new MoveTowards();
        this.player = dungeon.getPlayer();
    }
	
	/**
	 * Perform the "move" action
	 */
	public void PerformMove() {
		if (player.haveInvincibility() || player.haveSword()) {
			this.setMoveStrats(new MoveAway());
		}
		else {
			this.setMoveStrats(new MoveTowards());
		}
		this.movestrats.move(this.player, this, this.dungeon);
	}
	
	/**
	 * Change move strategy according to the situation
	 * @param strats strategy object
	 */
	public void setMoveStrats(Movement strats) {
		this.movestrats = strats;
	}
	
	/**
	 * Check if a move is valid for enemy
	 * @param x target x position
	 * @param y target y position
	 * @return whether the move is valid(Boolean) 
	 */
	public boolean validMove(int x, int y) {
		Object o = dungeon.getEntity(getX()+x, getY()+y);
		if (o instanceof Wall) return false;
		else if (o instanceof ClosedDoor) return false;
		else if (o instanceof Boulder) return false;
		else if (o instanceof Exit) return false;
		else if (o instanceof Enemy) return false;
		else if (o instanceof Portal) return false;
		return true;
	}
}
