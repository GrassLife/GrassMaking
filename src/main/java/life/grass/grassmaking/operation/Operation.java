package life.grass.grassmaking.operation;

import life.grass.grassmaking.GrassMaking;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class Operation {
    private GrassMaking instance;
    private int taskId = -1, timeTick, finishTick;
    private Block block;
    private Location dropLocation;
    private ItemStack result;

    public Operation(Block block) {
        instance = GrassMaking.getInstance();
        this.block = block;
        this.dropLocation = block.getLocation().clone().add(0.5D, 0.1D, 0.5D);
        this.result = new ItemStack(Material.STONE);
    }

    public final void start(int finishTick) {
        if (isOperating()) return;

        onStart();

        this.finishTick = finishTick;
        taskId = instance.getServer().getScheduler().runTaskTimer(instance, this::operate, 0, 1).getTaskId();
    }

    public final void cancel() {
        if (!isOperating()) return;

        onCancel();
        end();
    }

    private void operate() {
        onOperate();

        timeTick++;
        if (finishTick <= timeTick) finish();
    }

    private void finish() {
        onFinish();
        end();

        Item drop = block.getWorld().dropItem(dropLocation, result);
        drop.setVelocity(new Vector(Math.random(), 8, Math.random()).multiply(0.03));
    }

    private void end() {
        onEnd();

        instance.getServer().getScheduler().cancelTask(taskId);
        taskId = -1;
        timeTick = 0;
        finishTick = 0;
    }

    protected void onOperate() {
    }

    protected void onStart() {
    }

    protected void onFinish() {
    }

    protected void onCancel() {
    }

    protected void onEnd() {
    }

    public final boolean isOperating() {
        return taskId != -1;
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

    protected Block getBlock() {
        return block;
    }
}
