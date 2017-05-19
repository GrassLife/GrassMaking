package life.grass.grasscooking.food;

import javafx.util.converter.LocalDateTimeStringConverter;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public abstract class Food {
    protected static final String EXPIRE_DATE_LORE
            = ChatColor.RED + "消費期限" + ChatColor.GRAY + ": " + ChatColor.RED;
    protected static final String RESTORE_AMOUNT_LORE
            = ChatColor.AQUA + "スタミナ回復量" + ChatColor.GRAY + ": " + ChatColor.AQUA;

    private ItemStack item;
    private LocalDateTime expireDate;
    private int restoreAmount;
    private Map<FoodElement, Integer> elementMap;
    private Map<FoodEffect, Integer> effectMap;

    public Food(ItemStack item) {
        this.item = item;
        this.expireDate = LocalDateTime.now();
        this.restoreAmount = 20;
        this.elementMap = new HashMap<>();
        this.effectMap = new HashMap<>();

        setExpireDate(expireDate.plusMinutes(40));

        updateItem();
    }

    protected void updateItem() {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList(
                EXPIRE_DATE_LORE + new LocalDateTimeStringConverter().toString(expireDate),
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

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate.minusMinutes(expireDate.getMinute() % 10).truncatedTo(ChronoUnit.MINUTES);
        updateItem();
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

    public String getFoodType() {
        return getClass().getSimpleName();
    }
}
