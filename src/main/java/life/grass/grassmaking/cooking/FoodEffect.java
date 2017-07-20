package life.grass.grassmaking.cooking;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;

public enum FoodEffect {
    MEDICINAL("体力回復") {
        @Override
        public void exert(Player player, int level, int calorie) {
            level = Math.abs(level);
            if (level < 1) return;

            player.addPotionEffects(Collections.singletonList(
                    new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * calorie / 30, level - 1)
            ));
        }
    },
    FULLNESS("満足感") {
        @Override
        public void exert(Player player, int level, int calorie) {
            level = Math.abs(level);
            if (level < 1) return;

            player.addPotionEffects(Collections.singletonList(
                    new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * calorie / 2, level / 2 - 1)
            ));
        }
    },
    ANALGESIC("鎮痛") {
        @Override
        public void exert(Player player, int level, int calorie) {
            level = Math.abs(level);
            if (level < 1) return;

            player.addPotionEffects(Collections.singletonList(
                    new PotionEffect(PotionEffectType.ABSORPTION, 20 * calorie / 5, level)
            ));
        }
    },
    EUPHORIA("多幸感") {
        @Override
        public void exert(Player player, int level, int calorie) {
            if (level < 1) return;

            player.addPotionEffects(Collections.singletonList(
                    new PotionEffect(PotionEffectType.SPEED, 20 * calorie / 18, level - 1)
            ));
        }
    },
    HEAVY_STOMACH("胃もたれ") {
        @Override
        public void exert(Player player, int level, int calorie) {
            if (level < 1) return;

            player.addPotionEffects(Arrays.asList(
                    new PotionEffect(PotionEffectType.SLOW, 20 * 60 * level, level - 1),
                    new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 45 * level, level)
            ));
        }
    },
    UNKNOWN("効果なし") {
        @Override
        public void exert(Player player, int level, int calorie) {
        }
    };


    private String displayName;

    FoodEffect(String displayName) {
        this.displayName = displayName;
    }

    public abstract void exert(Player player, int level, int calorie);

    public String getDisplayName() {
        return displayName;
    }
}
