package life.grass.grassmaking.table;

import life.grass.grassmaking.ui.SlotPart;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Table implements InventoryHolder {
    private static final int DEFAULT_TABLE_SIZE = 54;
    private static final SlotPart PADDING_SLOT_PART = new SlotPart(false, null, Material.STAINED_GLASS_PANE, 15, null, null);

    private Inventory inventory;
    private Map<Integer, SlotPart> slotPartMap = new HashMap<>();

    private boolean isInitialized = false;

    protected abstract String getTitle();

    public void initInventory() {
        inventory = Bukkit.createInventory(this, getTableSize(), getTitle());

        for (int i = 0; i < getTableSize(); i++) {
            if (!slotPartMap.containsKey(i)) slotPartMap.put(i, PADDING_SLOT_PART);
            inventory.setItem(i, slotPartMap.get(i).getItemStack().orElse(null));
        }

        isInitialized = true;
    }

    public List<Integer> collectTagSlotList(String tag) {
        List<Integer> slotList = new ArrayList<>();
        slotPartMap.forEach((slot, slotPart) -> {
            if (slotPart.getTag().orElse("EMPTY").equalsIgnoreCase(tag)) {
                slotList.add(slot);
            }
        });

        return slotList;
    }

    public Inventory getInventory() {
        if (!isInitialized) initInventory();

        return inventory;
    }

    public Map<Integer, SlotPart> getSlotPartMap() {
        return slotPartMap;
    }

    public SlotPart getSlotPart(int slot) {
        return slotPartMap.get(slot);
    }

    public void addSlotPart(int slot, SlotPart slotPart) {
        this.slotPartMap.put(slot, slotPart);
    }

    public int getTableSize() {
        return DEFAULT_TABLE_SIZE;
    }
}
