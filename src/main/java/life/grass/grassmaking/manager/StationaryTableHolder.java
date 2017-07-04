package life.grass.grassmaking.manager;

import life.grass.grassmaking.table.StationaryTable;
import org.bukkit.block.Block;

import java.util.*;

public class StationaryTableHolder {
    private static StationaryTableHolder instance;

    private Map<String, StationaryTable> tableMap;

    static {
        instance = new StationaryTableHolder();
    }

    private StationaryTableHolder() {
        tableMap = new HashMap<>();
    }

    public static StationaryTableHolder getInstance() {
        return instance;
    }

    public Optional<StationaryTable> findTable(Block block) {
        return Optional.ofNullable(tableMap.get(generateKey(block)));
    }

    public StationaryTable createTable(Block block, StationaryTable stationaryTable) {
        String key = generateKey(block);
        try {
            tableMap.put(key, stationaryTable);
            return tableMap.get(key);
        } catch (Exception ignore) {
            return null;
        }
    }

    public List<StationaryTable> getTableSet() {
        return new ArrayList(tableMap.values());
    }

    public void remove(Block block) {
        tableMap.remove(generateKey(block));
    }

    private static String generateKey(Block block) {
        return block.getX() + "_" + block.getY() + "_" + block.getZ();
    }
}
