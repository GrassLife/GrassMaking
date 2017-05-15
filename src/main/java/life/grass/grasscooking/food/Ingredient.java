package life.grass.grasscooking.food;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ingredient extends FoodMaterial implements Eatable {
    private long expireDate;
    private String baseName;
    private int restoreStamina;

    private Ingredient(ItemStack item, FoodType foodType, Map<FoodElement, Integer> elementMap, int size, long expireDate, String baseName, int restoreStamina) {
        super(item, foodType, elementMap, size);

        this.expireDate = expireDate;
        this.baseName = baseName;
        this.restoreStamina = restoreStamina;

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + baseName);
        item.setItemMeta(meta);

        updateItem();
    }

    public static Ingredient fromItemStack(ItemStack item) {
        // TODO: read and write NBT
        FoodType foodType = null;
        Map<FoodElement, Integer> elementMap = new HashMap<>();
        int size = 10;
        long expireDate = -1;
        String baseName = item.getType().toString();
        int restoreStamina = 20;

        return new Ingredient(item, foodType, elementMap, size, expireDate, baseName, restoreStamina);
    }

    public static Ingredient fromItemStack(ItemStack item, FoodType foodType, Map<FoodElement, Integer> elementMap, int size, long expireDate, String baseName, int restoreStamina) {
        return new Ingredient(item, foodType, elementMap, size, expireDate, baseName, restoreStamina);
    }

    public static boolean verifyIngredient(ItemStack item) {
        // TODO: change
        return true;
    }

    @Override
    public long getExpireDate() {
        return expireDate;
    }

    @Override
    public String getBaseName() {
        return baseName;
    }

    @Override
    public int getRestoreStamina() {
        return 1;
    }

    @Override
    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
        updateItem();
    }

    @Override
    public void setBaseName(String baseName) {
        this.baseName = baseName;
        updateItem();
    }

    @Override
    public void setRestoreStamina(int restoreStamina) {
        this.restoreStamina = restoreStamina;
        updateItem();
    }

    @Override
    protected void updateItem() {
        super.updateItem();

        ItemMeta meta = item.getItemMeta();

        List<String> lore = Arrays.asList(
                EXPIRE_DATE_LORE + "--",
                RESTORE_STAMINA_LORE + restoreStamina);
        elementMap.forEach((key, value) -> lore.add(ELEMENT_PREFIX_LORE + key.toString() + ELEMENT_SEPARATOR_LORE + value));
        meta.setLore(lore);

        item.setItemMeta(meta);
    }
}
