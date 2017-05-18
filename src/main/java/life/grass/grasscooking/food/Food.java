package life.grass.grasscooking.food;

import javafx.util.converter.LocalDateTimeStringConverter;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public abstract class Food {
    protected static final String EXPIRE_DATE_LORE
            = ChatColor.RED + "消費期限" + ChatColor.GRAY + ": " + ChatColor.RED;
    protected static final String RESTORE_AMOUNT_LORE
            = ChatColor.AQUA + "スタミナ回復量" + ChatColor.GRAY + ": " + ChatColor.AQUA;

    private World world;
    private ItemStack item;
    private long expireDate;
    private int restoreAmount;
    private Map<FoodElement, Integer> elementMap;
    private Map<FoodEffect, Integer> effectMap;

    public Food(World world, ItemStack item) {
        this.world = world;
        this.item = item;
        this.restoreAmount = 20;
        this.elementMap = new HashMap<>();
        this.effectMap = new HashMap<>();
        entryExpireDate(60 * 30);

        updateItem();
    }

    protected void updateItem() {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList(
                EXPIRE_DATE_LORE + new LocalDateTimeStringConverter().toString(calcExpireDate()),
                RESTORE_AMOUNT_LORE + restoreAmount
        ));
        meta.setLore(lore);

        item.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
        updateItem();
    }

    public long getExpireDate() {
        return expireDate;
    }

    public LocalDateTime calcExpireDate() {
        long time = getExpireDate() - world.getFullTime();
        LocalDateTime date = LocalDateTime.now();

        return date.plusSeconds(time / 72);
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
        updateItem();
    }

    public void entryExpireDate(int seconds) {
        setExpireDate(world.getFullTime() + seconds * 72);
    }

    public int getRestoreAmount() {
        return restoreAmount;
    }

    public void setRestoreAmount(int restoreAmount) {
        this.restoreAmount = restoreAmount;
        updateItem();
    }

    public Map<FoodElement, Integer> getElementMap() {
        return elementMap;
    }

    public int getElementLevel(FoodElement element) {
        return elementMap.getOrDefault(element, 0);
    }

    public void setElement(FoodElement element, int level) {
        elementMap.put(element, level);
        updateItem();
    }

    public void increaseElement(FoodElement element, int level) {
        setElement(element, getElementLevel(element) + level);
        updateItem();
    }

    public Map<FoodEffect, Integer> getEffectMap() {
        return effectMap;
    }

    public int getEffectLevel(FoodEffect effect) {
        return effectMap.getOrDefault(effect, 0);
    }

    public void setEffect(FoodEffect effect, int level) {
        effectMap.put(effect, level);
        updateItem();
    }

    public void increaseEffect(FoodEffect effect, int level) {
        setEffect(effect, getEffectLevel(effect) + level);
    }
}
