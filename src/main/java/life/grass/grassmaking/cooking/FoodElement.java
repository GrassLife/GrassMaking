package life.grass.grassmaking.cooking;

public enum FoodElement {
    SWEET("甘さ", "苦さ", FoodEffect.UNKNOWN, FoodEffect.UNKNOWN),
    SPICY("酸っぱさ", "辛さ", FoodEffect.UNKNOWN, FoodEffect.UNKNOWN),
    SALTY("塩辛さ", "淡泊さ", FoodEffect.UNKNOWN, FoodEffect.UNKNOWN),
    SACHI("山の幸", "海の幸", FoodEffect.FULLNESS, FoodEffect.UNKNOWN),
    UMAMI("旨さ", "不味さ", FoodEffect.UNKNOWN, FoodEffect.UNKNOWN),
    HEALTHY("薬効", "有毒", FoodEffect.MEDICINAL, FoodEffect.UNKNOWN);

    private String uprightName, reversedName;
    private FoodEffect uprightEffect, reversedEffect;

    FoodElement(String uprightName, String reversedName, FoodEffect uprightEffect, FoodEffect reversedEffect) {
        this.uprightName = uprightName;
        this.reversedName = reversedName;
        this.uprightEffect = uprightEffect;
        this.reversedEffect = reversedEffect;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public String getUprightName() {
        return uprightName;
    }

    public String getReversedName() {
        return reversedName;
    }

    public FoodEffect getUprightEffect() {
        return uprightEffect;
    }

    public FoodEffect getReversedEffect() {
        return reversedEffect;
    }
}
