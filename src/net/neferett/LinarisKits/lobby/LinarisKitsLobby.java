package net.neferett.LinarisKits.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.skelerex.LinarisKits.api.GameType;
import com.skelerex.LinarisKits.api.User;

public class LinarisKitsLobby
extends JavaPlugin {
    public static final String shop_base_title = "Shop (§6<coins> §r§bcoins§r)";
    public static final String shop_game_title = "Shop <game> (§6<coins> §bcoins§r)";
    private static LinarisKitsLobby instance;

    public static LinarisKitsLobby getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents((Listener)new InventoryListener(), (Plugin)this);
        this.getCommand("shop").setExecutor((CommandExecutor)new CommandShop());
    }

    public static String getShopBaseTitle(User user) {
        return "Shop (§6<coins> §r§bcoins§r)".replaceAll("<coins>", Integer.toString(user.getCoins()));
    }

    public static String getShopGameTitle(GameType game, User user) {
        return "Shop <game> (§6<coins> §bcoins§r)".replaceAll("<coins>", Integer.toString(user.getCoins())).replaceAll("<game>", ChatColor.stripColor((String)game.getShortName()));
    }
}

