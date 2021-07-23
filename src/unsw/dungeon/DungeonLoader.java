package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Loads a dungeon from a .json file.
 *
 * By extending this class, a subclass can hook into entity creation. This is
 * useful for creating UI elements with corresponding entities.
 *
 * @author Robert Clifton-Everest
 *
 */
public abstract class DungeonLoader {

    private JSONObject json;

    public DungeonLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("dungeons/" + filename)));
    }

    /**
     * Parses the JSON to create a dungeon.
     * @return
     */
    public Dungeon load() {
        int width = json.getInt("width");
        int height = json.getInt("height");

        Dungeon dungeon = new Dungeon(width, height);

        JSONArray jsonEntities = json.getJSONArray("entities");

        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(dungeon, jsonEntities.getJSONObject(i));
        }
        JSONObject jsonGoalCondition = json.getJSONObject("goal-condition");
        this.loadGoals(dungeon, jsonGoalCondition, dungeon.getGoal());
        System.out.println(dungeon.getGoal().logic+" | "+dungeon.getGoal().goals);
        System.out.println(jsonGoalCondition);
        return dungeon;
    }

    private void loadEntity(Dungeon dungeon, JSONObject json) {
        String type = json.getString("type");
        int x = json.getInt("x");
        int y = json.getInt("y");

        Entity entity = null;
        switch (type) {
        case "player":
            Player player = new Player(dungeon, x, y);
            dungeon.setPlayer(player);
            onLoad(player);
            entity = player;
            break;
        case "wall":
            Wall wall = new Wall(x, y);
            onLoad(wall);
            entity = wall;
            break;
        // TODO Handle other possible entities
        case "exit":
        	Exit exit = new Exit(x, y);
        	onLoad(exit);
        	entity = exit;
        	break;
        case "treasure":
        	Treasure treasure = new Treasure(x, y);
        	onLoad(treasure);
        	entity = treasure;
        	break;
        case "door":
        	int id = json.getInt("id");
        	OpenDoor openDoor = new OpenDoor(x, y);
        	onLoad(openDoor);
        	ClosedDoor closedDoor = new ClosedDoor(x, y, id);
        	onLoad(closedDoor);
        	entity = closedDoor;
        	break;
        case "key":
        	id = json.getInt("id");
        	Key key = new Key(x, y, id);
        	onLoad(key);
        	entity = key;
        	break;
        case "boulder":
        	Boulder boulder = new Boulder(x, y);
        	onLoad(boulder);
        	entity = boulder;
            break;
        case "switch":
        	Switch switches = new Switch(x, y);
        	onLoad(switches);
        	entity = switches;
        	break;
        case "enemy":
			Enemy enemy = new Enemy(x, y, dungeon);
        	onLoad(enemy);
        	entity = enemy;
        	break;
        case "sword":
        	Sword sword = new Sword(x, y);
        	onLoad(sword);
        	entity = sword;
        	break;
        case "invincibility":
        	Invincibility invincibility = new Invincibility(x, y);
        	onLoad(invincibility);
        	entity = invincibility;
        	break;
        case "portal":
        	Portal portal = new Portal(x, y);
        	onLoad(portal);
        	entity = portal;
        	break;
        case "time":
        	TimeFreezePotion timeFreezepotion = new TimeFreezePotion(x, y);
        	onLoad(timeFreezepotion);
        	entity = timeFreezepotion;
        	break;
        
	    case "grave":
	    	GraveStone graveStone = new GraveStone(x, y);
	    	onLoad(graveStone);
	    	entity = graveStone;
	    	break;
        case "meat":
        	Meat meat = new Meat(x, y);
        	onLoad(meat);
        	entity = meat;
        	break;
        }
        dungeon.addEntity(entity);
    }

    private void loadGoals(Dungeon dungeon, JSONObject json, CompsiteGoal cg) {
    	String logic = json.getString("goal");
    	switch (logic) {
    	case "AND":
    		CompsiteGoal ComGoal = new CompsiteGoal(dungeon, logic);
    		JSONArray subGoals = json.getJSONArray("subgoals");
    		for (Object n: subGoals) {
    			this.loadGoals(dungeon,(JSONObject) n, ComGoal);
    		}
    		cg.goals.add(ComGoal);
    		break;
    	case "OR":
    		CompsiteGoal ComGoal1 = new CompsiteGoal(dungeon, logic);
    		JSONArray subGoals1 = json.getJSONArray("subgoals");
    		for (Object n: subGoals1) {
    			this.loadGoals(dungeon,(JSONObject) n, ComGoal1);
    		}
    		cg.goals.add(ComGoal1);
    		System.out.println(ComGoal1.goals);
    		break;
    	case "treasure":
    		TreasureGoal treasGoal = new TreasureGoal(dungeon);
    		cg.goals.add(treasGoal);
    		System.out.println(treasGoal.toString());
    		break;
    	case "enemies":
    		EnemyGoal enemyGoal = new EnemyGoal(dungeon);
    		cg.goals.add(enemyGoal);
    		System.out.println(enemyGoal.toString());
    		break;
    	case "boulders":
    		SwitchGoal switchGoal = new SwitchGoal(dungeon);
    		cg.goals.add(switchGoal);
    		System.out.println(switchGoal.toString());
    		break;
    	}
    }
    
    public abstract void onLoad(Entity player);

    public abstract void onLoad(Wall wall);

    // TODO Create additional abstract methods for the other entities
    public abstract void onLoad(Exit exit);
	public abstract void onLoad(Treasure treasure);
	public abstract void onLoad(ClosedDoor closedDoor);
	public abstract void onLoad(OpenDoor openDoor);
	public abstract void onLoad(Key key);
	public abstract void onLoad(Boulder boulder);
	public abstract void onLoad(Switch switches);
	public abstract void onLoad(Enemy enemy);
	public abstract void onLoad(Sword sword);
	public abstract void onLoad(Invincibility invincibility);
	public abstract void onLoad(Portal portal);
	public abstract void onLoad(TimeFreezePotion timeFreezePotion);
	public abstract void onLoad(GraveStone graveStone);
	public abstract void onLoad(Meat meat);
}
