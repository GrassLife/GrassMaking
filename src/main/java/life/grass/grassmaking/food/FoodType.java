package life.grass.grassmaking.food;

public enum FoodType {
    CUISINE,
    INGREDIENT_MEAT;

    @Override
    public String toString() {
        return name();
    }
}
