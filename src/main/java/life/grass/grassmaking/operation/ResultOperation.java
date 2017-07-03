package life.grass.grassmaking.operation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class ResultOperation extends Operation {
    private Block block;
    private Location dropLocation;
    private ItemStack result;

    public ResultOperation(Block block) {
        super(block);

        this.block = block;
        this.dropLocation = block.getLocation().clone().add(0.5D, 0.1D, 0.5D);
        this.result = new ItemStack(Material.STONE);
    }

    @Override
    protected void onFinish() {
        super.onFinish();

        Item drop = block.getWorld().dropItem(dropLocation, result);
        drop.setVelocity(new Vector(Math.random(), 8, Math.random()).multiply(0.03));
    }

    public Location getDropLocation() {
        return dropLocation;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setDropLocation(Location dropLocation) {
        this.dropLocation = dropLocation;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }
}
