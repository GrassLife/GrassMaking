package life.grass.grassmaking.cooking;

public enum CookingType {
    GRILL(200, 1.5) {
        @Override
        public double getCalorieMultiple(IngredientType ingredientType) {
            switch (ingredientType) {
                case MEAT:
                case FISH:
                case VEGETABLE:
                    return 1.3;
                default:
                    return 1;
            }
        }
    },
    CUTTING(60, 0.8) {
        @Override
        public double getCalorieMultiple(IngredientType ingredientType) {
            switch (ingredientType) {
                case MEAT:
                    return 0.6;
                case FISH:
                    return 1.3;
                case VEGETABLE:
                    return 1;
                default:
                    return 1;
            }
        }
    };

    private int weightDivider;
    private double foodElementMultiple;

    CookingType(int weightDivider, double foodElementMultiple) {
        this.weightDivider = weightDivider;
        this.foodElementMultiple = foodElementMultiple;
    }

    public int getWeightDivider() {
        return weightDivider;
    }

    public double getFoodElementMultiple() {
        return foodElementMultiple;
    }

    public abstract double getCalorieMultiple(IngredientType ingredientType);
}
