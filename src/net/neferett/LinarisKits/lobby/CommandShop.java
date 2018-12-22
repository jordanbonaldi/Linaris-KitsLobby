package net.neferett.LinarisKits.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.skelerex.LinarisKits.api.GameType;
import com.skelerex.LinarisKits.api.LinarisKitsAPI;
import com.skelerex.LinarisKits.api.User;

public class CommandShop
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Vous devez être un joueur !");
            return true;
        }
        Player player = (Player)sender;
        player.openInventory(getShop(player));
        return true;
    }

    private Inventory getShop(Player player) {
        User user = LinarisKitsAPI.getUser(player);
        Inventory inv = Bukkit.createInventory(player, 27, LinarisKitsLobby.getShopBaseTitle(user));
        inv.setItem(9, GameType.FALLEN_KINGDOM.getItem());
        inv.setItem(10, GameType.TOWERS.getItem());
        inv.setItem(12, GameType.UHC.getItem());
        inv.setItem(13, GameType.SURVIVOR.getItem());
        inv.setItem(14, GameType.SKY_WARS.getItem());
        inv.setItem(16, GameType.RUSHS.getItem());
        inv.setItem(17, GameType.PVP_SWAP.getItem());
        inv.setItem(20, GameType.HUNGER_GAMES.getItem());
        inv.setItem(22, GameType.TOTEM.getItem());
        return inv;
    }
}

