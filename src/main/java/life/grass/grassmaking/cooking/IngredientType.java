package life.grass.grassmaking.cooking;

public enum IngredientType {
    MEAT(1.5),
    FISH(1.0),
    VEGETABLE(0.5);

    private double oilyMultiple;

    IngredientType(double oilyMultiple) {
        this.oilyMultiple = oilyMultiple;
    }

    public double getOilyMultiple() {
        return oilyMultiple;
    }
}
