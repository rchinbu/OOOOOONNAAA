public enum Number {
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        REVERSE,
        SKIP,
        DRAW2,
        DRAW4,
        NONE;

        private static Number[] normal_numbers_list = new Number[]{
                ZERO,
                ONE,
                TWO,
                THREE,
                FOUR,
                FIVE,
                SIX,
                SEVEN,
                EIGHT,
                NINE
        };

        private static Number[] special_nonwild_list = new Number[]{
                REVERSE,
                SKIP,
                DRAW2
        };

        private static Number[] special_wild_list = new Number[] {
                DRAW4,
                NONE
        };

        public static Number[] normal_numbers() {
                return normal_numbers_list;
        }

        public static Number[] special_nonwild() {
                return special_nonwild_list;
        }

        public static Number[] special_wild() {
                return special_wild_list;
        }
}
