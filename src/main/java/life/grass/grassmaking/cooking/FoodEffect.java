package life.grass.grassmaking.cooking;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;

public enum FoodEffect {
    MEDICINAL("体力回復") {
        @Override
        public void exert(Player player, int level) {
            level = Math.abs(level);
            if (level < 1) return;

            player.addPotionEffects(Collections.singletonList(
                    new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 5 + (int) (1.5 * level) - 1, level / 2)
            ));
        }
    },
    FULLNESS("満足感") {
        @Override
        public void exert(Player player, int level) {
            level = Math.abs(level);
            if (level < 1) return;

            player.addPotionEffects(Collections.singletonList(
                    new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 40 + 5 * level, level / 2)
            ));
        }
    },
    ANALGESIC("鎮痛") {
        @Override
        public void exert(Player player, int level) {
            level = Math.abs(level);
            if (level < 1) return;

            player.addPotionEffects(Collections.singletonList(
                    new PotionEffect(PotionEffectType.ABSORPTION, 20 * 45, level)
            ));
        }
    },
    HEAVY_STOMACH("胃もたれ") {
        @Override
        public void exert(Player player, int level) {
            if (level < 1) return;

            player.addPotionEffects(Arrays.asList(
                    new PotionEffect(PotionEffectType.SLOW, 20 * 60 * level, level - 1),
                    new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 60 * level, level)
            ));
        }
    },
    UNKNOWN("効果なし") {
        @Override
        public void exert(Player player, int level) {
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
