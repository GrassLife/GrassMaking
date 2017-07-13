package life.grass.grassmaking.ui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class SlotPart {
    private boolean canMove, canDrop;
    private ItemStack item;
    private String tag;

    public SlotPart(boolean canMove, boolean canDrop, String tag) {
        this(canMove, canDrop, tag, null, 0, null, null);
    }

    public SlotPart(boolean canMove, boolean canDrop, String tag, Material material, int data, String name, String description) {
        this.canMove = canMove;
        this.canDrop = canDrop;
        this.tag = tag;

        if (material == null) {
            this.item = null;
        } else {
            this.item = new ItemStack(material, 1, ((short) data));
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(name == null ? " " : name);
            meta.setLore(description == null ? new ArrayList<>() : Collections.singletonList(description));

            this.item.setItemMeta(meta);
        }
    }

    public SlotPart(boolean canMove, boolean canDrop, String tag, ItemStack item) {
        this.canMove = canMove;
        this.canDrop = canDrop;
        this.tag = tag;
        this.item = item;
    }

    public boolean canMove() {
        return canMove;
    }

    public boolean canDrop() {
        return canDrop;
    }

    public Optional<ItemStack> getItemStack() {
        return Optional.ofNullable(item == null || item.getType() == Material.AIR ? null : item);
    }

    public Optional<String> getTag() {
        return Optional.ofNullable(tag);
    }
}
