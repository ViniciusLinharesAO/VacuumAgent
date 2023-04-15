package ai.utils;

public class Consts {
	public enum spaceState {
		UNKNOWN (0),
		CLEAN (1),
		OBSTACLE (2);

		private int state;

		spaceState(int state) {
			this.state = state;
		}

		public int getValue() {
            return this.state;
        }
	}
}