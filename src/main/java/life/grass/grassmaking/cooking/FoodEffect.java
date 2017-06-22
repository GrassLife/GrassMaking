package life.grass.grassmaking.cooking;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public enum FoodEffect {
    HEAVY_STOMACH("胃もたれ") {
        @Override
        public void exert(Player player, int level) {
            player.addPotionEffects(Arrays.asList(
                    new PotionEffect(PotionEffectType.SLOW, 20 * 60 * level, level - 1),
                    new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 60 * level, level + 1)
            ));
        }
    };

    private String displayName;

    FoodEffect(String displayName) {
        this.displayName = displayName;
    }

    public abstract void exert(Player player, int level);

    public String getDisplayName() {
        return displayName;
    }
}
