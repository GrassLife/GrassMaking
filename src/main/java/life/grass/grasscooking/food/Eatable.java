package life.grass.grasscooking.food;

import org.bukkit.ChatColor;

public interface Eatable {
    String EXPIRE_DATE_LORE = ChatColor.RED + "消費期限" + ChatColor.GRAY + ": " + ChatColor.RED;
    String RESTORE_STAMINA_LORE = ChatColor.AQUA + "スタミナ回復量" + ChatColor.GRAY + ": " + ChatColor.AQUA;

    long getExpireDate();

    String getBaseName();

    int getRestoreStamina();

    void setExpireDate(long expireDate);

    void setBaseName(String baseName);

    void setRestoreStamina(int restoreStamina);
}
