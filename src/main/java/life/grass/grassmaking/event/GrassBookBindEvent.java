package life.grass.grassmaking.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class GrassBookBindEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private ItemStack result;

    public GrassBookBindEvent() {
        result = null;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public ItemStack getResult() {
        return result;
    }
}
