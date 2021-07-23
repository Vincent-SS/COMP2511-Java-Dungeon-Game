package unsw.dungeon;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
//import junit.framework.Assert;
//import junit.framework.TestCase;

public class DungeonTest {
	// *******************************************
	// Test Player Move up, down, left and right
	
	// This won't change the Player's position
	// Since there's a Wall blocked
	@Test
	public void testPlayerMoveUpFail() {
		System.out.println("Testing Player's Movement");
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 0,0); 
		Wall wall = new Wall(0,1);
		dungeon.addEntity(wall);
		dungeon.addEntity(player);
		player.moveUp();
		assert(player.getX() == 0);
		assert(player.getY() == 0);
		
	}
	// This will success
	@Test
	public void testPlayerMoveLeftSuccess() {
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 1,1);
		dungeon.addEntity(player);
		player.moveLeft();
		assert(player.getX() == 0);
		assert(player.getY() == 1);
	}
	
	// This won't change the Player's position
	// Since there are a boulder before a wall
	@Test
	public void testPlayerMoveDownFail() {
		Dungeon dungeon = new Dungeon(6, 6);
		Player player = new Player(dungeon, 1, 1);
		Boulder boulder = new Boulder(2,1);
		Wall wall = new Wall(3,1);
		dungeon.addEntity(wall);
		dungeon.addEntity(boulder);
		dungeon.addEntity(player);
		player.moveRight();
		assert(player.getX() == 1);
		assert(player.getY() == 1);
		System.out.println("Passed");
	}
	
	// *******************************************
	// Test Player can collect treasure
	
	// The dungeon contains 2 treasure initially
	// and all picked up by player,
	// so total treasure is 0 eventually
	@Test
	public void testPlayerCollectTreasure1() {
		System.out.println("Testing Player collects treasure");
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 1,2);
		Exit exit = new Exit(2,5);
		dungeon.addEntity(player);
		dungeon.addEntity(exit);
		dungeon.addEntity(new Treasure(1,3));
		dungeon.addEntity(new Treasure(1,4));
		assert(dungeon.totalTreasure() == 2);
		player.moveDown();
		player.moveDown();
		assert(dungeon.totalTreasure() == 0);
	}
	
	@Test
	public void testPlayerCollectTreasure2() {
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 2,2);
		dungeon.addEntity(player);
		dungeon.addEntity(new Treasure(2,3));
		dungeon.addEntity(new Treasure(1,4));
		assert(dungeon.totalTreasure() == 2);
		player.moveDown();
		assert(dungeon.totalTreasure() == 1);
		System.out.println("Passed");
	}
	
	// *******************************************
	// Test Player can pick up a key
	
	// The Player has no key
	// So he/she picks up the key
	@Test
	public void testPlayerPickKeySuccess() {
		System.out.println("Testing Player picks up key");
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 2,2);
		assert(player.getKeyState() instanceof NoKeyState);
		Exit exit = new Exit(2, 5);
		dungeon.addEntity(player);
		dungeon.addEntity(exit);
		dungeon.addEntity(new Key(2,4,1));
		player.moveDown();
		player.moveDown();
		assert(player.getKeyState() instanceof CarryKeyState);
	}
	
	// The Player carries a key with id 1
	// So he/she cannot pick up the key with id 2
	@Test
	public void testPlayerPickKeyFail() {
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 2,2);
		assert(player.getKeyState() instanceof NoKeyState);
		dungeon.addEntity(player);
		Key key = new Key(2,4,1);
		Exit exit = new Exit(2,5);
		dungeon.addEntity(key);
		dungeon.addEntity(exit);
		player.moveDown();
		player.moveDown();
		assert(player.getHaveKey());
		assert(player.getKey().getId() == 1);
		// add a new key with id 2, but player still hold id1
		dungeon.addEntity(new Key(2,5,2));
		player.moveDown();
		assert(player.getKey().getId() == 1);
		System.out.println("Passed");
	}
	
	// *******************************************
	// Test Player opens the door with specific key
	@Test
	public void testOpenDoorSuccess() {
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 2,2);
		Exit exit = new Exit(2,5);
		dungeon.addEntity(player);
		dungeon.addEntity(exit);
		dungeon.addEntity(new Key(2,3,1));
		player.moveDown();
		ClosedDoor closedDoor = new ClosedDoor(2,4,1);
		dungeon.addEntity(closedDoor);
		assert(dungeon.getEntities().contains(closedDoor) == true);
		player.moveDown();
		assert(dungeon.getEntities().contains(closedDoor) == false);
	}
	
	// Player cannot open the door with a wrong key
	@Test
	public void testOpenDoorFail() {
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 2,2);
		dungeon.addEntity(player);
		dungeon.addEntity(new Key(2,3,1));
		player.moveDown();
		ClosedDoor closedDoor = new ClosedDoor(2,4,2);
		dungeon.addEntity(closedDoor);
		assert(dungeon.getEntities().contains(closedDoor) == true);
		player.moveDown();
		assert(dungeon.getEntities().contains(closedDoor) == true);
	}
	// *******************************************
	// Test Player can move boulders
	@Test
	public void testMoveBouldersSuccess() {
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 2,2);
		Boulder boulder = new Boulder(2,3);
		dungeon.addEntity(player);
		dungeon.addEntity(boulder);
		player.moveDown();
		assert(dungeon.getEntities().contains(boulder) == true);
		for (Entity e:dungeon.getEntities()) if (e instanceof Boulder) assert(e.getX() == 2 && e.getY() == 4);
	}
	
	// Test Player cannot move boulders since there exists adjacent boulders
	@Test
	public void testMoveBouldersFail1() {
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 2,2);
		Boulder boulder1 = new Boulder(2,3);
		Boulder boulder2 = new Boulder(2,4);
 		dungeon.addEntity(player);
		dungeon.addEntity(boulder1);
		dungeon.addEntity(boulder2);
		player.moveDown();
		assert(dungeon.getEntities().contains(boulder1) == true);
	}
	
	// Test Player cannot move boulders since there exits wall
	@Test
	public void testMoveBouldersFail2() {
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 2,2);
		Boulder boulder = new Boulder(2,3);
		Wall wall = new Wall(2,4);
 		dungeon.addEntity(player);
		dungeon.addEntity(boulder);
		dungeon.addEntity(wall);
		player.moveDown();
		assert(dungeon.getEntities().contains(boulder) == true);
	}
	
	
	// *******************************************
	// Test floor switch can be triggered while there's a boulder
	@Test
	public void testTriggerSuccess() {
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 2,2);
		Boulder boulder = new Boulder(2,3);
		Switch switc = new Switch(2,4);
		dungeon.addEntity(player);
		dungeon.addEntity(boulder);
		dungeon.addEntity(switc);
		player.moveDown();
		assert(dungeon.isAllSwitchOn() == true);
	}
	
	// Test once all floor switch been triggered, the player cannot move
	@Test
	public void testTriggerFail() {
		Dungeon dungeon = new Dungeon(6,6);
		Player player = new Player(dungeon, 2,2);
		Boulder boulder = new Boulder(2,3);
		Switch switc = new Switch(2,4);
		dungeon.addEntity(player);
		dungeon.addEntity(boulder);
		dungeon.addEntity(switc);
		player.moveDown();
		assert(dungeon.isAllSwitchOn() == true);
		player.moveDown();
		assert(dungeon.isAllSwitchOn() == true);
	}
	
	// *******************************************
	// Test Player can kill the Enemy
	
	// Kill the enemy by sword
	@Test
	public void testKillbySword() {
		Dungeon dungeon = new Dungeon(1,5);
		Player player = new Player(dungeon, 1,1);
		Enemy enemy = new Enemy(1,5,dungeon);
		Sword sword = new Sword(1,2);
		dungeon.addEntity(player);
		dungeon.addEntity(sword);
		player.moveDown();
		player.moveDown();
		player.moveDown();
		player.moveDown();
		assert(!dungeon.getEntities().contains(enemy));
	}
	
	// Kill the enemy by invincibility potion
	@Test
	public void testKillbyPotion() {
		Dungeon dungeon = new Dungeon(6,6);
		Player player = new Player(dungeon, 3,1);
		Invincibility inv = new Invincibility(3,2);
		Enemy enemy = new Enemy(4,3, dungeon);
		dungeon.addEntity(player);
		dungeon.addEntity(inv);
		player.moveDown();
		player.moveRight();
		for (Entity e:dungeon.getEntities()) assert(e.equals((Entity) enemy) == false);
	}
	
	// *******************************************
	// Test Player can pick up sword
	@Test
	public void testPickSwordSuccess() {
		Dungeon dungeon = new Dungeon(6,6);
		Player player = new Player(dungeon, 3,1);
		Sword sword = new Sword(3,2);
		dungeon.addEntity(player);
		dungeon.addEntity(sword);
		player.moveDown();
		for (Entity e:player.getBag()) if (e instanceof Sword) assert(((Sword) e).getAvailableHit() == 5);
	}
	
	// Test Player can only have one sword
	// the sword remains still since player carries one
	@Test
	public void testPickSwordFail() {
		Dungeon dungeon = new Dungeon(6,6);
		Player player = new Player(dungeon, 3,1);
		Sword sword = new Sword(3,2);
		dungeon.addEntity(player);
		dungeon.addEntity(sword);
		player.moveDown();
		Sword sword2 = new Sword(3,3);
		dungeon.addEntity(sword2);
		player.moveDown();
		for (Entity e:dungeon.getEntities()) if (e instanceof Sword) assert(e.getX() == 3 && e.getY() == 3);
	}
	
	// *******************************************
	// Test Player can pick up Invisible Potion
	@Test
	public void testPickPotionSuccess() {
		Dungeon dungeon = new Dungeon(6,6);
		Player player = new Player(dungeon, 3,1);
		Invincibility inv = new Invincibility(3,2);
		dungeon.addEntity(player);
		dungeon.addEntity(inv);
		player.moveDown();
		//assert(player.getAvalableInv() == 5);
		for (Entity e:player.getBag()) if (e instanceof Invincibility) assert(((Invincibility) e).getDurability() == 10);
	}
	
	// Test Player can only have one potion
	// the potion remains still since player carries one
	@Test
	public void testPickPotionFail() {
		Dungeon dungeon = new Dungeon(6,6);
		Player player = new Player(dungeon, 3,1);
		Invincibility invinci = new Invincibility(3,2);
		dungeon.addEntity(player);
		dungeon.addEntity(invinci);
		player.moveDown();
		Invincibility invinci2 = new Invincibility(3,3);
		dungeon.addEntity(invinci2);
		player.moveDown();
		for (Entity e:dungeon.getEntities()) if (e instanceof Invincibility) assert(e.getX() == 3 && e.getY() == 3);
	}
	
	// *******************************************
	// Test Enemy can kill the player
	@Test
	public void testKillPlayer() {
		Dungeon dungeon = new Dungeon(6,6);
		Player player = new Player(dungeon, 3,1);
		dungeon.setPlayer(player);
		dungeon.addEntity(player);
		Enemy enemy = new Enemy(4,2,dungeon);
		dungeon.addEntity(enemy);
		assert(player.getPlayerKilled() == false);
		player.moveDown();
		assert(player.getPlayerKilled() == true);
	}
	
	// Test Player can push a boulder
	@Test
	public void testPushBoulder() {
		Dungeon dungeon = new Dungeon(5,5);
		Player player = new Player(dungeon, 2,2);
		Boulder boulder = new Boulder(2,3);
		dungeon.addEntity(player);
		dungeon.addEntity(boulder);
		player.moveDown();
		for (Entity e:dungeon.getEntities()) if (e instanceof Boulder) assert(e.getX() == 2 && e.getY() ==4);
	}
	
	// *******************************************
	// Test exit goal success
	@Test
	public void testexitsuccess() {
		Dungeon dungeon = new Dungeon(3,3);
		Player player = new Player(dungeon, 1,1);
		Exit exit = new Exit (3,3);
		dungeon.addEntity(player);
		dungeon.addEntity(exit);
		player.moveRight();
		player.moveRight();
		player.moveDown();
		player.moveDown();
		assert(dungeon.getGoal().getAchieved() == true);
	}
	// Test exit goal fail
	@Test
	public void testexitfail() {
		Dungeon dungeon = new Dungeon(3,3);
		Player player = new Player(dungeon, 1,1);
		Exit exit = new Exit (3,3);
		dungeon.addEntity(player);
		dungeon.addEntity(exit);
		player.moveRight();
		player.moveDown();
		assert(!player.checkExit());
	}

	// *******************************************
	// Test collect treasure goal success
	@Test
	public void testcollectsuccess() {
		Dungeon dungeon = new Dungeon(9,9);
		Player player = new Player(dungeon, 5,5);
		Treasure Treasure1 = new Treasure(5,6);
		Treasure Treasure2 = new Treasure(5,7);
		dungeon.addEntity(player);
		dungeon.addEntity(Treasure1);
		dungeon.addEntity(Treasure2);
		CompsiteGoal g = new CompsiteGoal(dungeon, "AND");
		TreasureGoal t = new TreasureGoal(dungeon);
		g.goals.add((Goal) t);
		player.moveDown();
		player.moveDown();
		player.moveDown();
		assert(dungeon.getGoal().getAchieved());
	}
	// Test collect treasure goal fail
	@Test
	public void testcollectfail() {
		Dungeon dungeon = new Dungeon(9,9);
		Player player = new Player(dungeon, 5,5);
		Treasure Treasure1 = new Treasure(5,6);
		Treasure Treasure2 = new Treasure(5,7);
		dungeon.addEntity(new Exit(1,1));
		dungeon.addEntity(player);
		dungeon.addEntity(Treasure1);
		dungeon.addEntity(Treasure2);
		CompsiteGoal g = new CompsiteGoal(dungeon, "AND");
		EnemyGoal e = new EnemyGoal(dungeon);
		TreasureGoal t = new TreasureGoal(dungeon);
		g.goals.add((Goal) e);
		g.goals.add((Goal) t);
		player.moveUp();
		player.moveUp();
		player.moveUp();
		dungeon.setGoal(g);
		assert(dungeon.getTotalTreasure() > 0);
		assert(!dungeon.getGoal().getAchieved());
	}
	// *******************************************
	// Test boulder goal success
	@Test
	public void testbouldersuccess() {
		Dungeon dungeon = new Dungeon(9,9);
		Player player = new Player(dungeon, 5,5);
		Switch switch1 = new Switch(5,3);
		Switch switch2 = new Switch(5,7);
		Boulder boulder1 = new Boulder(5,4);
		Boulder boulder2 = new Boulder(5,6);
		dungeon.addEntity(player);
		dungeon.addEntity(switch1);
		dungeon.addEntity(switch2);
		dungeon.addEntity(boulder1);
		dungeon.addEntity(boulder2);
		CompsiteGoal g = new CompsiteGoal(dungeon, "AND");
		SwitchGoal b = new SwitchGoal(dungeon);
		g.goals.add((Goal)b);
		player.moveUp();
		player.moveDown();
		player.moveDown();
		assert(dungeon.getGoal().getAchieved());
	}
	// Test boulder goal fail
	@Test
	public void testboulderfail() {
		Dungeon dungeon = new Dungeon(9,9);
		Player player = new Player(dungeon, 5,5);
		Switch switch1 = new Switch(5,3);
		Switch switch2 = new Switch(1,7);
		Boulder boulder1 = new Boulder(2,7);
		Boulder boulder2 = new Boulder(3,6);
		dungeon.addEntity(player);
		dungeon.addEntity(switch1);
		dungeon.addEntity(switch2);
		dungeon.addEntity(boulder1);
		dungeon.addEntity(boulder2);
		CompsiteGoal g = new CompsiteGoal(dungeon, "AND");
		SwitchGoal b = new SwitchGoal(dungeon);
		g.goals.add((Goal)b);
		player.moveDown();
		player.moveDown();
		player.moveDown();
		assert(!dungeon.totalTriggered());
	}
}