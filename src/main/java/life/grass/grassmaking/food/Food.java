package life.grass.grassmaking.food;

import javafx.util.converter.LocalDateTimeStringConverter;
import life.grass.grassitem.GrassItem;
import life.grass.grassmaking.tag.CookingTag;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public abstract class Food {
    protected static final String EXPIRE_DATE_LORE;
    protected static final String RESTORE_AMOUNT_LORE;
    protected static final String SEPARATOR_LORE;

    private ItemStack item;
    private FoodType foodType;
    private int weight;
    private LocalDateTime expireDate;
    private int restoreAmount;
    private Map<FoodElement, Integer> elementMap;
    private Map<FoodEffect, Integer> effectMap;

    static {
        EXPIRE_DATE_LORE = ChatColor.RED + "消費期限" + ChatColor.GRAY + ": " + ChatColor.RED;
        RESTORE_AMOUNT_LORE = ChatColor.AQUA + "スタミナ回復量" + ChatColor.GRAY + ": " + ChatColor.AQUA;
        SEPARATOR_LORE = ChatColor.GRAY + "-----------------------";
    }

    /* package */ Food(ItemStack item) {
        this.item = item;

        GrassItem grassItem = new GrassItem(item);
        FoodType itemFoodType = grassItem.hasNBT(CookingTag.FOOD_TYPE) ? FoodType.valueOf((String) grassItem.getNBT(CookingTag.FOOD_TYPE).get()) : FoodType.UNKNOWN;

        this.foodType = itemFoodType;
        this.elementMap = new HashMap<>();
        this.effectMap = new HashMap<>();
        this.weight = 1;
        if (itemFoodType == FoodType.UNKNOWN) {
            this.expireDate = LocalDateTime.now();
            this.restoreAmount = 1;
        } else {
            this.expireDate = LocalDateTime.parse((String) grassItem.getNBT(CookingTag.EXPIRE_DATE).get());
            this.restoreAmount = (int) grassItem.getNBT(CookingTag.RESTORE_AMOUNT).get();
            grassItem.getNBT(CookingTag.ELEMENT)
                    .ifPresent(obj -> ((Map<String, String>) obj)
                            .forEach((element, level) -> elementMap.put(FoodElement.valueOf(element), Integer.valueOf(level)))
                    );
        }

        updateItem();
    }

    protected void updateItem() {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList(
                EXPIRE_DATE_LORE + new LocalDateTimeStringConverter().toString(expireDate),
                RESTORE_AMOUNT_LORE + restoreAmount
        ));

        if (!elementMap.isEmpty()) lore.addAll(Arrays.asList(" ", SEPARATOR_LORE));
        elementMap.forEach((key, value) -> {
                    if (value == 0) return;
                    String name = value > 0 ? key.getUprightName() : key.getReversedName();
                    String element = ChatColor.DARK_GRAY + " * " + ChatColor.YELLOW + name + ChatColor.GRAY + ": ";

                    ChatColor color;
                    for (int i = 1; i <= Math.abs(value); i++) {
                        switch (i) {
                            case 1:
                                color = ChatColor.AQUA;
                                break;
                            case 2:
                                color = ChatColor.GREEN;
                                break;
                            case 3:
                                color = ChatColor.YELLOW;
                                break;
                            case 4:
                                color = ChatColor.GOLD;
                                break;
                            case 5:
                                color = ChatColor.RED;
                                break;
                            default:
                                color = ChatColor.BLACK;
                        }
                        element += color + ChatColor.BOLD.toString() + "*";
                    }

                    lore.add(element);
                }
        );

        meta.setLore(lore);

        item.setItemMeta(meta);

        GrassItem grassItem = new GrassItem(item);
        grassItem.setNBT(CookingTag.EXPIRE_DATE, expireDate.toString());
        grassItem.setNBT(CookingTag.RESTORE_AMOUNT, restoreAmount);
        grassItem.setNBT(CookingTag.ELEMENT, elementMap);

        this.item = grassItem.toItemStack();
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
        updateItem();
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
        updateItem();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if (0 < weight) this.weight = weight;
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
        if (5 < level) level = 5;
        else if (level < -5) level = -5;

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
