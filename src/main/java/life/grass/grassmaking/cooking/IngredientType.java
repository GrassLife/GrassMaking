package life.grass.grassmaking.cooking;

public enum IngredientType {
    MEAT(0.4),
    FISH(0.15),
    VEGETABLE(0.08);

    private double oilyMultiple;

    IngredientType(double oilyMultiple) {
        this.oilyMultiple = oilyMultiple;
    }

    public double getOilyMultiple() {
        return oilyMultiple;
    }
}
