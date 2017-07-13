package life.grass.grassmaking.manager;

import life.grass.grassmaking.table.BlockTable;
import org.bukkit.block.Block;

import java.util.*;

public class BlockTableHolder {
    private static BlockTableHolder instance;

    private Map<String, BlockTable> tableMap;

    static {
        instance = new BlockTableHolder();
    }

    private BlockTableHolder() {
        tableMap = new HashMap<>();
    }

    public static BlockTableHolder getInstance() {
        return instance;
    }

    public Optional<BlockTable> findTable(Block block) {
        return Optional.ofNullable(tableMap.get(generateKey(block)));
    }

    public BlockTable createTable(Block block, BlockTable blockTable) {
        String key = generateKey(block);
        try {
            tableMap.put(key, blockTable);
            return tableMap.get(key);
        } catch (Exception ignore) {
            return null;
        }
    }

    public List<BlockTable> getTableList() {
        return new ArrayList(tableMap.values());
    }

    public void remove(Block block) {
        tableMap.remove(generateKey(block));
    }

    private static String generateKey(Block block) {
        return block.getX() + "_" + block.getY() + "_" + block.getZ();
    }
}
