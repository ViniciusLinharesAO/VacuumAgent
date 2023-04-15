package ai.utils;

public class Address {
	private int x;
	private int y;

	public Address(int spaceX, int spaceY) {
		this.x = spaceX;
		this.y = spaceY;
	}

	public int getX() {
		return this.x;
	}

	public int setX(int newSpaceX) {
		this.x = newSpaceX;
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int setY(int newSpaceY) {
		this.y = newSpaceY;
		return this.y;
	}
}