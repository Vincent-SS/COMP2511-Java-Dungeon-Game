package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity {

    private Dungeon dungeon;
    private ArrayList<Entity> bag;
    private boolean exitAchieved;
    private boolean killed;
    private boolean haveKey;
    private boolean meat;
    private KeyState keyState;
    private Key key;
    
    
    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
        this.bag = new ArrayList<Entity>();
        this.exitAchieved = false;
        this.killed = false;
        this.keyState = new NoKeyState();
        this.key = null;
        this.haveKey = false;
        this.meat = false;
    }
    
    /**
     * Player moveUp method
     * 
     */
    public void moveUp() {
    	if (exitAchieved == true || killed == true) {
    		System.out.println("Game over!!");
    	} else if (getY() > 0 && validMove(0, -1)) {
        	this.checkInvincibility();
        	this.checkTimeFreeze();
        	this.pickUp_openDoor(this.getX(), this.getY()-1);
        	if(isBoulder(0, -1) instanceof Boulder) {
        		isBoulder(0, -1).boulderPushed(0, -1, this);
        	}
        	y().set(getY() - 1);
        	checkPortal();
        	if (!this.haveTimeFreeze()) {
        		this.invokeEnemyMove();
        	}
        	checkPlayerEnemy();
        	if (dungeon.haveExit()) {checkExit();}
        	else {
        		if (this.dungeon.getGoal().getAchieved()) {
        			killed = true;
        			System.out.println("Game Win");
        		}
        	}
        }
    }
    
    /**
     * Player moveDown method
     */
	public void moveDown() {
    	if (exitAchieved == true || killed == true) {
    		System.out.println("Game over!!");
    	} else if (getY() < dungeon.getHeight() - 1 && validMove(0, 1)) {
        	this.checkInvincibility();
        	this.checkTimeFreeze();
        	this.pickUp_openDoor(this.getX(), this.getY()+1);
        	if(isBoulder(0, 1) instanceof Boulder) {
        		isBoulder(0, 1).boulderPushed(0, 1, this);
        	}
        	y().set(getY() + 1);
        	checkPortal();
        	if (!this.haveTimeFreeze()) {
        		this.invokeEnemyMove();
        	}
        	//if (this.dungeon.getGoal().getAchieved()) System.out.println("CompsiteGoal Achieved");
        	checkPlayerEnemy();
        	if (dungeon.haveExit()) {checkExit();}
        	else {
        		if (this.dungeon.getGoal().getAchieved()) {
        			killed = true;
        			System.out.println("Game Win");
        		}
        	}
        }
    }
	
	/**
	 * Player moveLeft method
	 */
    public void moveLeft() {
    	if (exitAchieved == true || killed == true) {
    		System.out.println("Game over!!");
    	} else if (getX() > 0 && validMove(-1, 0)) {
        	this.checkInvincibility();
        	this.checkTimeFreeze();
        	this.pickUp_openDoor(this.getX()-1, this.getY());
        	if(isBoulder(-1, 0) instanceof Boulder) {
        		isBoulder(-1, 0).boulderPushed(-1, 0, this);
        	}
        	x().set(getX() - 1);
        	checkPortal();
        	if (!this.haveTimeFreeze()) {
        		this.invokeEnemyMove();
        	}
        	//if (this.dungeon.getGoal().getAchieved()) System.out.println("CompsiteGoal Achieved");
        	checkPlayerEnemy();
        	if (dungeon.haveExit()) {checkExit();}
        	else {
        		if (this.dungeon.getGoal().getAchieved()) {
        			killed = true;
        			System.out.println("Game Win");
        		}
        	}
        }
    }
    
    /**
     * Player moveRight method
     */
    public void moveRight() {
    	if (exitAchieved == true || killed == true) {
    		System.out.println("Game over!!");
    	} else if (getX() < dungeon.getWidth() - 1 && validMove(1, 0)) {
        	this.checkInvincibility();
        	this.checkTimeFreeze();
        	this.pickUp_openDoor(this.getX()+1, this.getY());
        	if(isBoulder(1, 0) instanceof Boulder) {
        		isBoulder(1, 0).boulderPushed(1, 0, this);
        	}
        	x().set(getX() + 1);
        	checkPortal();
        	if (!this.haveTimeFreeze()) {
        		this.invokeEnemyMove();
        	}
        	//if (this.dungeon.getGoal().getAchieved()) System.out.println("CompsiteGoal Achieved");
        	checkPlayerEnemy();
        	if (dungeon.haveExit()) {checkExit();}
        	else {
        		if (this.dungeon.getGoal().getAchieved()) {
        			killed = true;
        			System.out.println("Game Win");
        		}
        	}
        }
    }
    
    /**
     * check whether the move is valid
     * @param x
     * @param y
     * @return boolean (valid or not)
     * 
     */
    private boolean validMove(int x, int y) {
    	List<Entity> objects = new ArrayList<Entity>();
    	for (Entity i: this.getDungeon().getEntities()) {
			if (i.getX() == getX()+x && i.getY() == getY()+y) objects.add(i);
		}
		Entity o = dungeon.getEntity(getX()+x, getY()+y);
		if (o instanceof Wall) return false;
		else if (o instanceof ClosedDoor && !idMatched((ClosedDoor) o)) return false;
		
		for (Entity k: objects) {
			if (k instanceof Boulder && !((Boulder) k).boulderValidMove(x, y, this)) {
				return false;
			}
		}
		return true;
	}
    
    /**
     * check whether the key id and door id are matched
     * @param o
     * @return boolean (matched or not)
     */
	private boolean idMatched(ClosedDoor o) {
		if (this.getKeyState() instanceof NoKeyState) return false;
		if (haveKey && this.key.getId() == o.getId()) return true;
		return false;
	}

	/**
	 * Player do the action(pick up sword, invincibility portion, timeFreeze portion, key, etc)
	 * @param x
	 * @param y
	 */
	private void pickUp_openDoor(int x, int y) {
		Entity e = dungeon.getEntity(x, y);
        if ((e instanceof Sword && !this.haveSword()) ||
        	(e instanceof Invincibility && !this.haveInvincibility()) ||
        	(e instanceof TimeFreezePotion && !this.haveTimeFreeze())) {
        	Entity copy = e;
        	this.bag.add(copy);
        	this.dungeon.removeEntity(e);
        	removeImage(e);
        	System.out.println("picked up");
        }
        else if (e instanceof Key && !haveKey) {
        	this.key = (Key)e;
        	this.bag.add(e);
        	removeImage(e);
        	getKeyState().doAction(this, e, dungeon);
        	System.out.println("You picked up a key, id is "+this.key.getId());
        }
        else if (e instanceof Treasure) {
        	this.dungeon.removeEntity(e);
        	removeImage(e);
        	System.out.println("You grabbed a treasure");
        }
        else if (e instanceof Meat) {
        	this.dungeon.removeEntity(e);
        	removeImage(e);
        	this.meat = true;
        }
        else if (e instanceof ClosedDoor && haveKey) {
        	if (((ClosedDoor) e).getId() == key.getId()) {
        		System.out.println("Matched. Door opens!");
        		removeImage(e); addOpenDoor(e.getX(), e.getY());
        		getKeyState().doAction(this, (Entity) e, dungeon);
        	}
        }
	}
	
	/**
	 * once the door opens, update the image to dungeon
	 * @param x
	 * @param y
	 */
	private void addOpenDoor(int x, int y) {
		for (Entity e : dungeon.getEntities()) {
			if (e instanceof OpenDoor) {
				e.x().set(x); e.y().set(y);
			}
		}
	}
	
	/**
	 * remvoe the image
	 * @param e
	 */
	private void removeImage(Entity e) {
		e.x().set(dungeon.getWidth());
    	e.y().set(dungeon.getHeight());
	}
	
	public ArrayList<Entity> getBag() {
		return bag;
	}

	public KeyState getKeyState() {
		return keyState;
	}
	
	/**
	 * check whether the player carries a sword or not
	 * @return
	 */
	public boolean haveSword() {
		if (bag.isEmpty() || bag == null) return false;
		for (Entity e: this.bag) {
			if (e instanceof Sword &&
				((Sword) e).getAvailableHit() > 0) {
				return true;
			}
		}
		return false;
	}

	 /**
	  * check whether the player have invincibility or not
	  * @return
	  */
	public boolean haveInvincibility() {
		if (bag.isEmpty() || bag == null) return false;
		for (Entity e: this.bag) {
			if (e instanceof Invincibility && 
				((Invincibility) e).getDurability() > 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * once player is invincible, the durability reduced by 1 when the player moves
	 */
	private void checkInvincibility() {
		if (this.haveInvincibility()) {
			Entity x = null;
			int num;
    		for(Entity e: this.bag) {
    			if (e instanceof Invincibility) {
    				((Invincibility) e).reduce();
    				x = e;
    				num = ((Invincibility) e).getDurability();
    				System.out.println("Invincibility reduced by 1, remain: "+num);
    			}
    		}
    		if (((Invincibility) x).getDurability() == 0) {
    			this.bag.remove(x);
			}
    	}
	}

	 /**
	  * If the player moves to a portal, he instantly moves to corresponding place
	  */
	 public void checkPortal() {
		 Entity e = dungeon.getEntity(getX(), getY());
		 if (e instanceof Portal) {
			 playerMoveToPortal();
		 }
	 }
	 
	 /**
	  * check the interaction between player and enemy(kill or be killed)
	  */
	 public void checkPlayerEnemy() {
		 Entity e = dungeon.getEntity(getX(), getY());
		 if (e instanceof Enemy && (haveSword() || haveInvincibility())) {
			 System.out.println("You killed an enemy");
			 removeImage(e);
			 dungeon.removeEntity(e);
			 swordOrInvincibilityReduce();
			 if (dungeon.getTotalEnemy() == 0) System.out.println("You killed all enemies!! Enemy Goal completed!");
		 } else if (e instanceof Enemy && !haveSword() && !haveInvincibility() && meat == false) {
			 System.out.println("You are killed by an enemy! Game Over!");
			 removeImage(this);
			 killed = true;
		 } else if (e instanceof Enemy && !haveSword() && !haveInvincibility() && meat == true) {
			 revive();
		 }
	 }
	 
	 /**
	  * reduce durability of invincibility or sword
	  */
	 public void swordOrInvincibilityReduce() {
		 if (!haveInvincibility()) {
			 for (Entity e : bag) {
				 if (e instanceof Sword)
					 ((Sword) e).reduce();
			 }
		 }
	 }
	 public void playerMoveToPortal() {
		 for (Entity e : dungeon.getEntities()) {
			 if (e instanceof Portal && e.getX() != getX() && e.getY() != getY()) {
				 //System.out.println("")
				 x().set(e.getX()); y().set(e.getY());
				 break;
			 }
		 }
	 }

	 public void setKeyState(KeyState state) {
	        this.keyState = state;
	    }

	public void setKey(Entity e) {
		// TODO Auto-generated method stub
		this.key = (Key) e;
	}
	
	/**
	 * After the player moves, the system invokes enemy to do the action
	 */
	private void invokeEnemyMove () {
		 for (Entity i: this.dungeon.getEntities()) {
	     	if (i instanceof Enemy) {
	     		((Enemy) i).PerformMove();
	     	}
	     }
	}
	
	public Key getKey() {
		return this.key;
	}

	public void setHaveKey(boolean b) {
		this.haveKey = b;
	}
	
	public boolean getHaveKey() {
		return this.haveKey;
	}

	public Dungeon getDungeon() {
		// TODO Auto-generated method stub
		return dungeon;
	}
	
	/**
	 * check whether the place is a boulder
	 * @param x
	 * @param y
	 * @return Boulder in that place, or null, if no boulder existed
	 */
	private Boulder isBoulder(int x, int y) {
		 List<Entity> objects = new ArrayList<Entity>();
	
		 for (Entity i: this.getDungeon().getEntities()) {
			 if (i.getX() == getX()+x && i.getY() == getY()+y) objects.add(i);
		 }
		 
		 for (Entity o: objects) {
			 //if (o!=null)System.out.println(o.toString());
			 if (o instanceof Boulder) return ((Boulder) o);
		 }
		 return null;
	 }
	 
	public boolean getPlayerKilled() {
		return killed;
	}
	
	
	/**
	 * Once the player moves to exit, this method is triggered, to check goals
	 * @return
	 */
	public boolean checkExit() {
		Exit exit = null;
		for (Entity e: dungeon.getEntities()) {
			if (e instanceof Exit) {
				exit = (Exit) e;
			}
		}
		if (this.getX() == exit.getX() && 
			this.getY() == exit.getY() &&
			this.dungeon.getGoal().getAchieved()) {return true;}
		else if (this.getX() == exit.getX() && 
				this.getY() == exit.getY() &&
				!this.dungeon.getGoal().getAchieved()) {
					killed = true;
					System.out.println("You exit dungeon without achieving all goals. Wasted");
					return false;
				}
		return false;
	}
	
	/**
	 * Check whether the player got the time freeze portion
	 * @return boolean (has or not)
	 */
	public boolean haveTimeFreeze() {
		if (bag.isEmpty() || bag == null) return false;
		for (Entity e: this.bag) {
			if (e instanceof TimeFreezePotion && 
				((TimeFreezePotion) e).getDurability() > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * check the Time Freeze portion and its durability
	 */
	private void checkTimeFreeze() {
		if (this.haveTimeFreeze()) {
			Entity x = null;
			int num;
    		for(Entity e: this.bag) {
    			if (e instanceof TimeFreezePotion) {
    				((TimeFreezePotion) e).reduce();
    				x = e;
    				num = ((TimeFreezePotion) e).getDurability();
    				System.out.println("TimeFreeze reduced by 1, remain: "+num);
    			}
    		}
    		if (((TimeFreezePotion) x).getDurability() == 0) {
    			this.bag.remove(x);
			}
    	}
	}
	
	/**
	 * Revive the player since the player grabbed a meat
	 */
	private void revive() {
		meat = false; killed = false;
		for (Entity e : dungeon.getEntities()) {
			if (e instanceof GraveStone) {
				this.x().set(e.getX());
				this.y().set(e.getY());
				removeImage(e);
			}
		}
	}

	/**
	 * Player press SPACE button for dropping key action
	 */
	public void dropKey() {
		Entity a = null;
		if (haveKey) {
			haveKey = false;
			/**for (Entity e : dungeon.getEntities()) {
				if (e instanceof Key && e.getX() == dungeon.getWidth() && e.getY() == dungeon.getHeight()) {
					e.x().set(this.getX());
					e.y().set(this.getY());
					a = e;
					break;
				}
			}
			this.key = null;*/
			dungeon.addEntity(key);
			key.x().set(this.getX());
			key.y().set(this.getY());
			this.key = null;
			//dungeon.addEntity(this.key);
			
			getKeyState().doAction(this, a, dungeon);
			//player.
		} else {
			System.out.println("You don't have a key");
		}
	}
}
	
