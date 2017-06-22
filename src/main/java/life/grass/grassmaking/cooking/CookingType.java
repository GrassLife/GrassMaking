package life.grass.grassmaking.cooking;

public enum CookingType {
    GRILL(200, 1.3, 1.5);

    private int weightDivider;
    private double oilyMultiple;
    private double foodElementMultiple;

    CookingType(int weightDivider, double oilyMultiple, double foodElementMultiple) {
        this.weightDivider = weightDivider;
        this.oilyMultiple = oilyMultiple;
        this.foodElementMultiple = foodElementMultiple;
    }

    public int getWeightDivider() {
        return weightDivider;
    }

    public double getOilyMultiple() {
        return oilyMultiple;
    }

    public double getFoodElementMultiple() {
        return foodElementMultiple;
    }
}
