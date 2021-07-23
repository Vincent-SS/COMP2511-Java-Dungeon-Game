package unsw.dungeon;

public class Switch extends Entity {
	private boolean SwitchOn;
	
	public Switch(int x, int y) {
        super(x, y);
        this.setSwitchOn(false);
    }

	public boolean isSwitchOn() {
		return SwitchOn;
	}

	public void setSwitchOn(boolean switchOn) {
		SwitchOn = switchOn;
	}	
}