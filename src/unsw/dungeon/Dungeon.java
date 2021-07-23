/**
 *
 */
package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * A dungeon in the interactive dungeon player.
 *
 * A dungeon can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 *
 * @author Robert Clifton-Everest
 *
 */
public class Dungeon {

    private int width, height;
    private List<Entity> entities;
    private Player player;
    private CompsiteGoal goal;
    
    /**]
     * Dungeon Initializer
     * @param width Dungeon width
     * @param height Dungeon Heights
     */
    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
        this.player = null;
        //TODO implement goal initialiser
        //CompsiteGoal g = new CompsiteGoal(this, "AND");
        //TreasureGoal t = new TreasureGoal(this);
        //EnemyGoal e = new EnemyGoal(this);
        //g.goals.add((Goal) t);
        //g.goals.add((Goal) e);
        this.goal = new CompsiteGoal(this, "AND");
    }
    public void setGoal(CompsiteGoal g) {
    	this.goal = g;
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
    	return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    public List<Entity> getEntities() {
    	return entities;
    }
    
    /**
     *Get the entity on certain position 
     * @param i x position
     * @param j y position
     * @return That certain entity
     */
    public Entity getEntity(int i, int j) {
    	for (Entity e : entities) {
    		if (e instanceof Entity && e.getX() == i && e.getY() == j && !(e instanceof Player)) {
    			return e;
    		}
    	}
    	return null;
    }
    
    public void removeEntity(Object e) {
    	entities.remove(e);
    }
    
    public int getTotalTreasure() {
    	int num = 0;
    	for (Entity e : entities) 
    		if (e instanceof Treasure)num++;
    	return num;
    }

	public int getTotalEnemy() {
		int num = 0;
		for (Entity e : entities)
			if (e instanceof Enemy) num++;
		return num;
	}
	
	public boolean isAllSwitchOn() {
		for (Entity e: this.getEntities()) {
			if (e instanceof Switch) {
				if (!((Switch) e).isSwitchOn()) return false;
			}
		}
		return true;
	}
	
	public CompsiteGoal getGoal() {
		return this.goal;
	}

	public int totalTreasure() {
    	int i = 0;
    	for (Entity m: entities) {
    		if (m instanceof Treasure) i++;
    	}
    	return i;
    }
    
    public int totalEnemy() {
    	int i = 0;
    	for (Entity m: entities) {
    		if (m instanceof Enemy) i++;
    	}
    	return i;
    }
    
    public int totalSwitch() {
    	int i = 0;
    	for (Entity m : entities) {
    		if (m instanceof Switch) i++;
    	}
    	return i;
    }
    
    public boolean totalTriggered() {
    	int num = 0;
    	for (Entity m : entities) {
    		if (m instanceof Switch) {
    			for (Entity n : entities) {
    				if (n instanceof Boulder && n.getX() == m.getX() && n.getY() == m.getY()) num++;
    			}
    		}
    	}
    	if (num == totalSwitch()) return true;
    	else return false;
    }
    
    public boolean haveExit() {
    	for (Entity e: this.getEntities()) {
    		if (e instanceof Exit) {
    			return true;
    		}
    	}
    	return false;
    }
}
