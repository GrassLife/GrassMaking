package life.grass.grassmaking.food;

import life.grass.grassitem.JsonHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Cuisine extends Food {

    protected Cuisine(ItemStack item) {
        super(item);
    }

    @Override
    public String getName() {
        return JsonHandler.getGrassJson(item).getDynamicValue("CuisineName").getAsOverwritedString().orElse("料理");
    }

    @Override
    public int getWeight() {
        return JsonHandler.getGrassJson(item).getDynamicValue("Weight").getAsMaskedInteger().orElse(10);
    }

    public static Cuisine makeCuisine(ItemStack item) {
        return new Cuisine(JsonHandler.putUniqueName(item, "Cuisine"));
    }

    public void setType(Material material) {
        item.setType(material);
    }

    public void setName(String name) {
        JsonHandler.putDynamicData(item, "CuisineName", name);
    }
}
