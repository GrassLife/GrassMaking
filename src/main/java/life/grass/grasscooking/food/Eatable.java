package life.grass.grasscooking.food;

import org.bukkit.ChatColor;

public interface Eatable {
    String EXPIRE_DATE_LORE = ChatColor.RED + "消費期限" + ChatColor.GRAY + ": " + ChatColor.RED;
    String RESTORE_STAMINA_LORE = ChatColor.AQUA + "スタミナ回復量" + ChatColor.GRAY + ": " + ChatColor.AQUA;
    String RESTORE_EFFECTIVE_STAMINA_LORE = ChatColor.GREEN + "有効スタミナ回復量" + ChatColor.GRAY + ": " + ChatColor.GREEN;

    long getExpireDate();

    String getBaseName();

    int getRestoreStamina();

    int getRestoreEffectiveStamina();

    void setExpireDate(long expireDate);

    void setBaseName(String baseName);

    void setRestoreStamina(int restoreStamina);

    void setRestoreEffectiveStamina(int restoreEffectiveStamina);
}
