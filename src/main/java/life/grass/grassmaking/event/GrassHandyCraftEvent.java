package life.grass.grassmaking.event;

import life.grass.grassmaking.handcrafting.Recipe;
import life.grass.grassmaking.table.handcrafting.HandyCraftingTable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class GrassHandyCraftEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private HandyCraftingTable handyCraftingTable;
    private Recipe recipe;
    private ItemStack result;

    public GrassHandyCraftEvent(Player player, HandyCraftingTable handyCraftingTable, Recipe recipe) {
        this.player = player;
        this.handyCraftingTable = handyCraftingTable;
        this.recipe = recipe;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public HandyCraftingTable getHandyCraftingTable() {
        return handyCraftingTable;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }
}
