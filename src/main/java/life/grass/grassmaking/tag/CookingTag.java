package life.grass.grassmaking.tag;

import life.grass.grassitem.GrassNBTTag;

import java.util.HashMap;

public enum CookingTag implements GrassNBTTag {
    EXPIRE_DATE("ExpireDate", String.class),
    RESTORE_AMOUNT("RestoreAmount", Integer.class),
    ELEMENT("Element", HashMap.class);

    private String key;
    private Class clazz;

    CookingTag(String key, Class clazz) {
        this.key = key;
        this.clazz = clazz;
    }

    @Override
    public String getKey() {
        return "Cooking/" + key;
    }

    @Override
    public Class getValueClass() {
        return clazz;
    }
}
