public enum Color {
        RED, YELLOW, BLUE, GREEN, WILD;

        private static Color[] normal_colors_list = new Color[]{RED, YELLOW, BLUE, GREEN};

        public static Color[] normal_colors() {
                return normal_colors_list;
        }
}
