package life.grass.grassmaking.cooking;

public enum CookingType {
    GRILL(200, 1.5);

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
}
