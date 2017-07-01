package life.grass.grassmaking.event;

import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.table.cooking.Cooker;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GrassCookEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Cooker cooker;
    private CookingType cookingType;
    private List<ItemStack> ingredientList;
    private List<ItemStack> seasoningList;
    private ItemStack result;

    public GrassCookEvent(Cooker cooker, CookingType cookingType, List<ItemStack> ingredientList, List<ItemStack> seasoningList) {
        this.cooker = cooker;
        this.cookingType = cookingType;
        this.ingredientList = ingredientList;
        this.seasoningList = seasoningList;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Cooker getCooker() {
        return cooker;
    }

    public CookingType getCookingType() {
        return cookingType;
    }

    public List<ItemStack> getIngredientList() {
        return ingredientList;
    }

    public List<ItemStack> getSeasoningList() {
        return seasoningList;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }
}
