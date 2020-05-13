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

        public static String getPrintableNumber(Color color, Number number) {
                        if (color == Color.RED) {
                                if (number == Number.ZERO) {
                                        return ANSI_RED_BACKGROUND + "ZERO" + ANSI_RESET;
                                } else if (number == Number.ONE) {
                                        return ANSI_RED_BACKGROUND + "ONE" + ANSI_RESET;
                                } else if (number == Number.TWO) {
                                        return ANSI_RED_BACKGROUND + "TWO" + ANSI_RESET;
                                } else if (number == Number.THREE) {
                                        return ANSI_RED_BACKGROUND + "THREE" + ANSI_RESET;
                                } else if (number == Number.FOUR) {
                                        return ANSI_RED_BACKGROUND + "FOUR" + ANSI_RESET;
                                } else if (number == Number.FIVE) {
                                        return ANSI_RED_BACKGROUND + "FIVE" + ANSI_RESET;
                                } else if (number == Number.SIX) {
                                        return ANSI_RED_BACKGROUND + "SIX" + ANSI_RESET;
                                } else if (number == Number.SEVEN) {
                                        return ANSI_RED_BACKGROUND + "SEVEN" + ANSI_RESET;
                                } else if (number == Number.EIGHT) {
                                        return ANSI_RED_BACKGROUND + "EIGHT" + ANSI_RESET;
                                } else if (number == Number.NINE) {
                                        return ANSI_RED_BACKGROUND + "NINE" + ANSI_RESET;
                                } else if (number == Number.NONE) {
                                        return ANSI_RED_BACKGROUND + "NONE" + ANSI_RESET;
                                } else if (number == Number.SKIP) {
                                        return ANSI_RED_BACKGROUND + "SKIP" + ANSI_RESET;
                                } else if (number == Number.DRAW2) {
                                        return ANSI_RED_BACKGROUND + "DRAW2" + ANSI_RESET;
                                } else if (number == Number.DRAW4) {
                                        return ANSI_RED_BACKGROUND + "DRAW4" + ANSI_RESET;
                                } else {
                                        return ANSI_RED_BACKGROUND + "REVERSE" + ANSI_RESET;
                                }
                        } else if (color == Color.YELLOW) {
                                if (number == Number.ZERO) {
                                        return ANSI_YELLOW_BACKGROUND + "ZERO" + ANSI_RESET;
                                } else if (number == Number.ONE) {
                                        return ANSI_YELLOW_BACKGROUND + "ONE" + ANSI_RESET;
                                } else if (number == Number.TWO) {
                                        return ANSI_YELLOW_BACKGROUND + "TWO" + ANSI_RESET;
                                } else if (number == Number.THREE) {
                                        return ANSI_YELLOW_BACKGROUND + "THREE" + ANSI_RESET;
                                } else if (number == Number.FOUR) {
                                        return ANSI_YELLOW_BACKGROUND + "FOUR" + ANSI_RESET;
                                } else if (number == Number.FIVE) {
                                        return ANSI_YELLOW_BACKGROUND + "FIVE" + ANSI_RESET;
                                } else if (number == Number.SIX) {
                                        return ANSI_YELLOW_BACKGROUND + "SIX" + ANSI_RESET;
                                } else if (number == Number.SEVEN) {
                                        return ANSI_YELLOW_BACKGROUND + "SEVEN" + ANSI_RESET;
                                } else if (number == Number.EIGHT) {
                                        return ANSI_YELLOW_BACKGROUND + "EIGHT" + ANSI_RESET;
                                } else if (number == Number.NINE) {
                                        return ANSI_YELLOW_BACKGROUND + "NINE" + ANSI_RESET;
                                } else if (number == Number.NONE) {
                                        return ANSI_YELLOW_BACKGROUND + "NONE" + ANSI_RESET;
                                } else if (number == Number.SKIP) {
                                        return ANSI_YELLOW_BACKGROUND + "SKIP" + ANSI_RESET;
                                } else if (number == Number.DRAW2) {
                                        return ANSI_YELLOW_BACKGROUND + "DRAW2" + ANSI_RESET;
                                } else if (number == Number.DRAW4) {
                                        return ANSI_YELLOW_BACKGROUND + "DRAW4" + ANSI_RESET;
                                } else {
                                        return ANSI_YELLOW_BACKGROUND + "REVERSE" + ANSI_RESET;
                                }
                        } else if (color == Color.BLUE) {
                                if (number == Number.ZERO) {
                                        return ANSI_BLUE_BACKGROUND + "ZERO" + ANSI_RESET;
                                } else if (number == Number.ONE) {
                                        return ANSI_BLUE_BACKGROUND + "ONE" + ANSI_RESET;
                                } else if (number == Number.TWO) {
                                        return ANSI_BLUE_BACKGROUND + "TWO" + ANSI_RESET;
                                } else if (number == Number.THREE) {
                                        return ANSI_BLUE_BACKGROUND + "THREE" + ANSI_RESET;
                                } else if (number == Number.FOUR) {
                                        return ANSI_BLUE_BACKGROUND + "FOUR" + ANSI_RESET;
                                } else if (number == Number.FIVE) {
                                        return ANSI_BLUE_BACKGROUND + "FIVE" + ANSI_RESET;
                                } else if (number == Number.SIX) {
                                        return ANSI_BLUE_BACKGROUND + "SIX" + ANSI_RESET;
                                } else if (number == Number.SEVEN) {
                                        return ANSI_BLUE_BACKGROUND + "SEVEN" + ANSI_RESET;
                                } else if (number == Number.EIGHT) {
                                        return ANSI_BLUE_BACKGROUND + "EIGHT" + ANSI_RESET;
                                } else if (number == Number.NINE) {
                                        return ANSI_BLUE_BACKGROUND + "NINE" + ANSI_RESET;
                                } else if (number == Number.NONE) {
                                        return ANSI_BLUE_BACKGROUND + "NONE" + ANSI_RESET;
                                } else if (number == Number.SKIP) {
                                        return ANSI_BLUE_BACKGROUND + "SKIP" + ANSI_RESET;
                                } else if (number == Number.DRAW2) {
                                        return ANSI_BLUE_BACKGROUND + "DRAW2" + ANSI_RESET;
                                } else if (number == Number.DRAW4) {
                                        return ANSI_BLUE_BACKGROUND + "DRAW4" + ANSI_RESET;
                                } else {
                                        return ANSI_BLUE_BACKGROUND + "REVERSE" + ANSI_RESET;
                                }
                        } else if (color == Color.GREEN) {
                                if (number == Number.ZERO) {
                                        return ANSI_GREEN_BACKGROUND + "ZERO" + ANSI_RESET;
                                } else if (number == Number.ONE) {
                                        return ANSI_GREEN_BACKGROUND + "ONE" + ANSI_RESET;
                                } else if (number == Number.TWO) {
                                        return ANSI_GREEN_BACKGROUND + "TWO" + ANSI_RESET;
                                } else if (number == Number.THREE) {
                                        return ANSI_GREEN_BACKGROUND + "THREE" + ANSI_RESET;
                                } else if (number == Number.FOUR) {
                                        return ANSI_GREEN_BACKGROUND + "FOUR" + ANSI_RESET;
                                } else if (number == Number.FIVE) {
                                        return ANSI_GREEN_BACKGROUND + "FIVE" + ANSI_RESET;
                                } else if (number == Number.SIX) {
                                        return ANSI_GREEN_BACKGROUND + "SIX" + ANSI_RESET;
                                } else if (number == Number.SEVEN) {
                                        return ANSI_GREEN_BACKGROUND + "SEVEN" + ANSI_RESET;
                                } else if (number == Number.EIGHT) {
                                        return ANSI_GREEN_BACKGROUND + "EIGHT" + ANSI_RESET;
                                } else if (number == Number.NINE) {
                                        return ANSI_GREEN_BACKGROUND + "NINE" + ANSI_RESET;
                                } else if (number == Number.NONE) {
                                        return ANSI_GREEN_BACKGROUND + "NONE" + ANSI_RESET;
                                } else if (number == Number.SKIP) {
                                        return ANSI_GREEN_BACKGROUND + "SKIP" + ANSI_RESET;
                                } else if (number == Number.DRAW2) {
                                        return ANSI_GREEN_BACKGROUND + "DRAW2" + ANSI_RESET;
                                } else if (number == Number.DRAW4) {
                                        return ANSI_GREEN_BACKGROUND + "DRAW4" + ANSI_RESET;
                                } else {
                                        return ANSI_GREEN_BACKGROUND + "REVERSE" +  ANSI_RESET;
                                }
                        } else {
                                return number.toString();
                }
        }
        public static String getPrintableColor(Color color) {
                        if (color == Color.RED) {
                                return ANSI_RED_BACKGROUND + "RED " + ANSI_RESET;
                        } else if (color == Color.BLUE) {
                                return ANSI_BLUE_BACKGROUND + "BLUE " + ANSI_RESET;
                        } else if (color == Color.GREEN) {
                                return ANSI_GREEN_BACKGROUND + "GREEN " + ANSI_RESET;
                        } else if (color == Color.YELLOW) {
                                return ANSI_YELLOW_BACKGROUND + "YELLOW " + ANSI_RESET;
                        } else {
                                return ANSI_RED + "W" + ANSI_BLUE + "I" + ANSI_GREEN + "L" + ANSI_YELLOW + "D" + ANSI_RESET + " ";
                        }
        }
}
