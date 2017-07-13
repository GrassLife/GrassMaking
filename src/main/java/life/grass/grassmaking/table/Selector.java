package life.grass.grassmaking.table;

public abstract class Selector extends Table {
    public static final String SELECTING_TAG = "Selecting";

    public abstract void onPressSelecting(int slot);
}
