package life.grass.grasscooking.food;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cuisine implements Eatable {
    private static final String EFFECT_PREFIX_LORE = ChatColor.DARK_GRAY + "  * " + ChatColor.GOLD;
    private static final String EFFECT_SEPARATOR_LORE = ChatColor.GRAY + ": " + ChatColor.GOLD;

    private ItemStack item;
    private long expireDate;
    private String baseName;
    private int restoreStamina, restoreEffectiveStamina;
    private Map<FoodEffect, Integer> foodEffectMap;

    private Cuisine(ItemStack item, long expireDate, String baseName, int restoreStamina, int restoreEffectiveStamina, Map<FoodEffect, Integer> foodEffectMap) {
        this.item = item;
        this.expireDate = expireDate;
        this.baseName = baseName;
        this.restoreStamina = restoreStamina;
        this.restoreEffectiveStamina = restoreEffectiveStamina;
        this.foodEffectMap = foodEffectMap;

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + baseName);
        item.setItemMeta(meta);

        updateItem();
    }

    public static Cuisine fromItemStack(ItemStack item) {
        // TODO: read and write NBT
        long expireDate = -1;
        String baseName = item.getType().toString();
        int restoreStamina = 1;
        int restoreEffectiveStamina = 1;
        Map<FoodEffect, Integer> foodEffectMap = new HashMap<>();

        return new Cuisine(item, expireDate, baseName, restoreStamina, restoreEffectiveStamina, foodEffectMap);
    }

    public static Cuisine fromItemStack(ItemStack item, long expireDate, String baseName, int restoreStamina, int restoreEffectiveStamina, Map<FoodEffect, Integer> foodEffectMap) {
        return new Cuisine(item, expireDate, baseName, restoreStamina, restoreEffectiveStamina, foodEffectMap);
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
        return restoreStamina;
    }

    @Override
    public int getRestoreEffectiveStamina() {
        return restoreEffectiveStamina;
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
    public void setRestoreEffectiveStamina(int restoreEffectiveStamina) {
        this.restoreEffectiveStamina = restoreEffectiveStamina;
        updateItem();
    }

    public ItemStack getItem() {
        return item;
    }

    private void updateItem() {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = Arrays.asList(
                EXPIRE_DATE_LORE + "--",
                RESTORE_STAMINA_LORE + restoreStamina,
                RESTORE_EFFECTIVE_STAMINA_LORE + restoreEffectiveStamina);
        foodEffectMap.forEach((key, value) -> lore.add(EFFECT_PREFIX_LORE + key.toString() + EFFECT_SEPARATOR_LORE + value));
        meta.setLore(lore);

        item.setItemMeta(meta);
    }
}
