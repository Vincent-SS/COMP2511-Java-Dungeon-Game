package unsw.dungeon;

public class MoveAway implements Movement {
	@Override
	/**
	 * Over ride the move method in Movement to move away from player
	 */
	public void move(Player player, Enemy enemy, Dungeon dun) {
		int Xdis = player.getX()-enemy.getX();
		int Ydis = player.getY()-enemy.getY();
		
		if (Math.abs(Xdis) >= Math.abs(Ydis) && Xdis != 0 && Ydis != 0) {
			if (Ydis > 0) {
				if (enemy.getY() > 0 && enemy.validMove(0, -1)) {
					enemy.y().set(enemy.getY() - 1);
				}
			}
			else {
				if (enemy.getY() < dun.getHeight() - 1 && enemy.validMove(0, 1)) {
					enemy.y().set(enemy.getY() + 1);
				}
			}
		}
		else if (Math.abs(Xdis) < Math.abs(Ydis) && Xdis != 0 && Ydis != 0) {
			if (Xdis > 0) {
				if (enemy.getX() > 0 && enemy.validMove(-1, 0)) {
					enemy.x().set(enemy.getX() - 1);
				}
			}
			else {
				if (enemy.getX() < dun.getWidth() - 1 && enemy.validMove(1, 0)) {
					enemy.x().set(enemy.getX() + 1);
				}
			}
		}
		else if (Xdis == 0) {
			if (Ydis > 0) {
				if (enemy.getY() > 0 && enemy.validMove(0, -1)) {
					enemy.y().set(enemy.getY() - 1);
				}
			}
			else {
				
				if (enemy.getY() < dun.getHeight() - 1 && enemy.validMove(0, 1)) {
					enemy.y().set(enemy.getY() + 1);
				}
			}
		}
		else if (Ydis == 0) {
			if (Xdis > 0) {
				if (enemy.getX() > 0 && enemy.validMove(-1, 0)) {
					enemy.x().set(enemy.getX() - 1);
				}
			}
			else {
				
				if (enemy.getX() < dun.getWidth() - 1 && enemy.validMove(1, 0)) {
					enemy.x().set(enemy.getX() + 1);
				}
			}
		}
	}
}