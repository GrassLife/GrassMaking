package life.grass.grassmaking.manager;

import life.grass.grassmaking.generator.Generator;
import life.grass.grassmaking.generator.RawBeefGenerator;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GeneratorManager {
    private Map<Material, Generator> generatorMap;

    public GeneratorManager() {
        generatorMap = new HashMap<>();

        generatorMap.put(Material.RAW_BEEF, new RawBeefGenerator());
    }

    public Optional<Generator> findGenerator(Material material) {
        return Optional.ofNullable(generatorMap.getOrDefault(material, null));
    }
}
