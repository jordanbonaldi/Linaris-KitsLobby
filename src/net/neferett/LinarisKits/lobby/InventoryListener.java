package net.neferett.LinarisKits.lobby;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.skelerex.LinarisKits.api.GameType;
import com.skelerex.LinarisKits.api.Kit;
import com.skelerex.LinarisKits.api.KitLevel;
import com.skelerex.LinarisKits.api.LinarisKitsAPI;
import com.skelerex.LinarisKits.api.User;

public class InventoryListener
implements Listener {
    private HashMap<UUID, GameType> m_playersInv = new HashMap<UUID, GameType>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        User user = LinarisKitsAPI.getUser(player);
        Inventory inv = event.getInventory();
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        ItemStack item = event.getCurrentItem();
        if (inv.getName().contains(LinarisKitsLobby.getShopBaseTitle(user))) {
            GameType game = GameType.getGameType(item);
            event.setCancelled(true);
            if (game == null) {
                this.updateInv(player, this.getShopGame(game, player));
                return;
            }
            this.m_playersInv.put(player.getUniqueId(), game);
            this.updateInv(player, this.getShopGame(game, player));
        } else if (this.isGameShop(inv.getName(), user)) {
            event.setCancelled(true);
            GameType game = this.m_playersInv.get(player.getUniqueId());
            if (event.getClick() == ClickType.RIGHT) {
                Kit kit = LinarisKitsAPI.getKit(game, item);
                if (!kit.hasPerm(player)) {
                    player.sendMessage("§f[§6Boutique§8] §cVous n'avez pas la permission d'ameliorez ce kit !");
                    return;
                }
                int level = user.getLevelForKit(kit);
                if (level < 5) {
                    KitLevel tobuy = kit.getLevel(level + 1);
                    if (tobuy.getPrice() > user.getCoins()) {
                        player.sendMessage("§8[§6Boutique§8] §cVous n'avez pas assez de coins pour acheter cette amélioration !");
                    } else {
                        user.subCoins(tobuy.getPrice());
                        user.addLevelForKit(kit);
                        player.sendMessage("§8[§6Boutique§8] §6Vous avez debloqué " + kit.getName() + " §6au niveau " + Integer.toString(level + 1));
                    }
                } else {
                    player.sendMessage("§8[§6Boutique§8] §cAucune amélioration n'est disponnible pour ce kit !");
                }
            } else {
                player.sendMessage("§8[§6Boutique§8] §cUtilisez le §eclique droit §cpour §avalider §cvotre achat !");
            }
            this.updateInv(player, this.getShopGame(game, player));
        }
    }

    private Inventory getShopGame(GameType game, Player player) {
        User user = LinarisKitsAPI.getUser(player);
        Inventory inv = Bukkit.createInventory(player, 9, LinarisKitsLobby.getShopGameTitle(game, user));
        int i = 0;
        for (Kit kit : LinarisKitsAPI.getKitsForGame(game)) {
            int level = user.getLevelForKit(kit);
            ItemStack item = kit.getItem();
            ItemMeta meta = item.getItemMeta();
            LinkedList<String> lore = new LinkedList<String>();
            lore.add((Object)ChatColor.RESET + kit.getDescription());
            lore.add("");
            lore.add("§r§6Niveau actuel : §e" + level + "/5");
            if (level == 0) {
                lore.add("§r§cNon possédé");
            } else {
                lore.addAll(kit.getLevel(level).getLore());
            }
            if (level < 5) {
                lore.add("");
                lore.add("§r§6Prochain niveau : §e" + kit.getLevel(level + 1).getPrice() + " §6coins");
                lore.addAll(kit.getLevel(level + 1).getLore());
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(i++, kit.getItem());
        }
        return inv;
    }

    private boolean isGameShop(String title, User user) {

        for(GameType games : GameType.values())
            if (LinarisKitsLobby.getShopGameTitle(games, user).equalsIgnoreCase(title)) {
                return true;
            }
        
        return false;
    }

    private void updateInv(final Player player, final Inventory inv) {
        player.closeInventory();
        Bukkit.getScheduler().scheduleSyncDelayedTask(LinarisKitsLobby.getInstance(), new Runnable(){

            @Override
            public void run() {
                player.openInventory(inv);
            }
        });
    }

}

