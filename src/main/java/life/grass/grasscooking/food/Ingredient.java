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
    private int restoreStamina, restoreEffectiveStamina;

    private Ingredient(ItemStack item, Map<Element, Integer> elementMap, long expireDate, String baseName, int restoreStamina, int restoreEffectiveStamina) {
        super(item, elementMap);

        this.expireDate = expireDate;
        this.baseName = baseName;
        this.restoreStamina = restoreStamina;
        this.restoreEffectiveStamina = restoreEffectiveStamina;

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + baseName);
        item.setItemMeta(meta);

        updateItem();
    }

    public static Ingredient fromItemStack(ItemStack item) {
        // TODO: read and write NBT
        Map<Element, Integer> elementMap = new HashMap<>();
        long expireDate = -1;
        String baseName = item.getType().toString();
        int restoreStamina = 1;
        int restoreEffectiveStamina = 1;

        return new Ingredient(item, elementMap, expireDate, baseName, restoreStamina, restoreEffectiveStamina);
    }

    public static Ingredient fromItemStack(ItemStack item, Map<Element, Integer> elementMap, long expireDate, String baseName, int restoreStamina, int restoreEffectiveStamina) {
        return new Ingredient(item, elementMap, expireDate, baseName, restoreStamina, restoreEffectiveStamina);
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
    public int getRestoreEffectiveStamina() {
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
    public void setRestoreEffectiveStamina(int restoreEffectiveStamina) {
        this.restoreEffectiveStamina = restoreEffectiveStamina;
        updateItem();
    }

    @Override
    protected void updateItem() {
        super.updateItem();

        ItemMeta meta = item.getItemMeta();

        List<String> lore = Arrays.asList(
                EXPIRE_DATE_LORE + "--",
                RESTORE_STAMINA_LORE + restoreStamina,
                RESTORE_EFFECTIVE_STAMINA_LORE + restoreEffectiveStamina);
        elementMap.forEach((key, value) -> lore.add(ELEMENT_PREFIX_LORE + key.toString() + ELEMENT_SEPARATOR_LORE + value));
        meta.setLore(lore);

        item.setItemMeta(meta);
    }
}
