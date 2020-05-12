import java.io.*;
public enum Color implements Serializable {
        RED, YELLOW, BLUE, GREEN, WILD;

		public static final String ANSI_RESET = "\u001B[0m";
		public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
		public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
		public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
		public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
		public static final String ANSI_RED = "\u001B[31m";
		public static final String ANSI_GREEN = "\u001B[32m";
		public static final String ANSI_YELLOW = "\u001B[33m";
		public static final String ANSI_BLUE = "\u001B[34m";

        
        private static Color[] normal_colors_list = new Color[]{RED, YELLOW, BLUE, GREEN};

        public static Color[] normal_colors() {
                return normal_colors_list;
        }
        
        public static String getPrintableColor(Color color) {
			if (color == Color.RED) {
				return ANSI_RED_BACKGROUND + "RED" + ANSI_RESET;
			} else if (color == Color.BLUE) {
				return ANSI_BLUE_BACKGROUND + "BLUE" + ANSI_RESET;	
			} else if (color == Color.GREEN) {
				return ANSI_GREEN_BACKGROUND + "GREEN" + ANSI_RESET;
			} else if (color == Color.YELLOW) {
				return ANSI_YELLOW_BACKGROUND + "YELLOW" + ANSI_RESET;
			} else {
				return ANSI_RED + "W" + ANSI_BLUE + "I" + ANSI_GREEN + "L" + ANSI_YELLOW + "D" + ANSI_RESET;
			}
        }
}
