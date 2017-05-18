package life.grass.grasscooking.food;

public enum FoodElement {
    SWEET("甘さ") {
        @Override
        public FoodElement getConflict() {
            return FoodElement.BITTER;
        }
    },
    BITTER("苦さ") {
        @Override
        public FoodElement getConflict() {
            return FoodElement.SWEET;
        }
    },

    TART("酸っぱさ") {
        @Override
        public FoodElement getConflict() {
            return FoodElement.SPICY;
        }
    },
    SPICY("辛さ") {
        @Override
        public FoodElement getConflict() {
            return FoodElement.TART;
        }
    },

    SALTY("塩辛さ") {
        @Override
        public FoodElement getConflict() {
            return FoodElement.PLAIN;
        }
    },
    PLAIN("淡泊さ") {
        @Override
        public FoodElement getConflict() {
            return FoodElement.SALTY;
        }
    },

    UMAMI("旨さ") {
        @Override
        public FoodElement getConflict() {
            return FoodElement.BRACKISH;
        }
    },
    BRACKISH("不味さ") {
        @Override
        public FoodElement getConflict() {
            return FoodElement.UMAMI;
        }
    };

    private String displayName;

    FoodElement(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    abstract FoodElement getConflict();
}
