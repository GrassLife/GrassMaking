package life.grass.grassmaking.food;

public enum FoodElement {
    SWEET("甘味", "甘さ", "苦さ"),
    SPICY("酸味", "酸っぱさ", "辛さ"),
    SALTY("塩味", "塩辛さ", "淡泊さ"),
    UMAMI("旨味", "旨さ", "不味さ");

    private String elementName, uprightName, reversedName;

    FoodElement(String name, String uprightName, String reversedName) {
        this.elementName = name;
        this.uprightName = uprightName;
        this.reversedName = reversedName;
    }

    @Override
    public String toString() {
        return elementName;
    }

    public String getUprightName() {
        return uprightName;
    }

    public String getReversedName() {
        return reversedName;
    }
}
