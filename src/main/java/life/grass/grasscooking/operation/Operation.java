package life.grass.grasscooking.operation;

import life.grass.grasscooking.GrassCooking;
import org.bukkit.block.Block;

public abstract class Operation {
    private GrassCooking instance;
    private int taskId = -1, timeTick, finishTick;
    private Block block;

    public Operation(Block block) {
        instance = GrassCooking.getInstance();
        this.block = block;
    }

    public final void start(int finishTick) {
        if (isOperating()) return;

        onStart();

        this.finishTick = finishTick;
        taskId = instance.getServer().getScheduler().runTaskTimer(instance, this::operate, 0, 4).getTaskId();
    }

    public final void cancel() {
        onCancel();
        end();
    }

    public final boolean isOperating() {
        return taskId != -1;
    }

    protected Block getBlock() {
        return block;
    }

    private void operate() {
        onOperate();

        timeTick++;
        if (finishTick <= timeTick) finish();
    }

    private void finish() {
        onFinish();
        end();
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
}
