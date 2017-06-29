package life.grass.grassmaking.cooking;

public enum IngredientType {
    MEAT(200),
    FISH(550),
    VEGETABLE(850);

    private int oilyPerCalorie;

    IngredientType(int oilyPerCalorie) {
        this.oilyPerCalorie = oilyPerCalorie;
    }

    public int getOilyPerCalorie() {
        return oilyPerCalorie;
    }
}
