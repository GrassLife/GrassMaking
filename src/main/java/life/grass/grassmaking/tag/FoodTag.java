package life.grass.grassmaking.tag;

import life.grass.grassitem.GrassNBTTag;

import java.util.HashMap;

public enum FoodTag implements GrassNBTTag {
    RESTORE_AMOUNT("RestoreAmount", String.class),
    EXPIRE_DATE("ExpireDate", String.class),
    WEIGHT("Weight", String.class),
    ELEMENT("Element", HashMap.class);

    private String key;
    private Class clazz;

    FoodTag(String key, Class clazz) {
        this.key = key;
        this.clazz = clazz;
    }

    @Override
    public String getKey() {
        return "Food/" + key;
    }

    @Override
    public Class getValueClass() {
        return clazz;
    }
}
