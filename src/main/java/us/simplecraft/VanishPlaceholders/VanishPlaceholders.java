package us.simplecraft.VanishPlaceholders;

import de.myzelyam.api.vanish.VanishAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.UUID;

public class VanishPlaceholders extends PlaceholderExpansion {

    static String[] plugins = {"SuperVanish", "PremiumVanish"};
    String pluginName;
    private boolean sv;

    @Override
    public boolean canRegister() {
        for (String e : plugins) {
            if (Bukkit.getPluginManager().getPlugin(e) != null) {
                pluginName = e;
                return init();
            }
        }
        Bukkit.getLogger().warning("Vanish Placeholders - Could not find a supported plugin!");
        return false;
    }

    @Override
    public String getIdentifier() {
        return "vanish";
    }

    @Override
    public String getAuthor() {
        return "cookieman768";
    }

    @Override
    public String getVersion() {
        return "2.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (identifier.equals("count")) {
            return String.valueOf(canSee(p));
        }
        if (identifier.equals("playercount")) {
            return String.valueOf(Bukkit.getOnlinePlayers().size() - vanishedPlayers(p));
        }
        if (identifier.equals("vanished")) {
            return String.valueOf(isVanished(p));
        }
        return null;
    }

    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    private int vanishedPlayers(Player p) {
        if (sv) {
            int i = 0;
            for (UUID e : VanishAPI.getInvisiblePlayers()) {
                if (Bukkit.getPlayer(e) == null) {
                    i++;
                    continue;
                }
                if (!VanishAPI.canSee(p, Bukkit.getPlayer(e))) {
                    i++;
                }
            }
            return i;
        }
        return 0;
    }

    private int canSee(Player p) {
        if (sv) {
            int i = 0;
            for (UUID e : VanishAPI.getInvisiblePlayers()) {
                if (Bukkit.getPlayer(e) == null) continue;
                if (VanishAPI.canSee(p, Bukkit.getPlayer(e))) {
                    i++;
                }
            }
            return i;
        }
        return 0;
    }

    private boolean init() {
        if (pluginName.equals("SuperVanish") || pluginName.equals("PremiumVanish")) {
            sv = true;
            Bukkit.getLogger().info("Vanish Placeholders - Hooked into SuperVanish or PremiumVanish");
            return true;
        }
        Bukkit.getLogger().warning("Vanish Placeholders - Unable to Hook!");
        return false;
    }
}

