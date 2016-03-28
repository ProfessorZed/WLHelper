package me.zed.helper;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Helper extends JavaPlugin implements Listener {

    public String disabled = ChatColor.GREEN + "Helper:" + ChatColor.RED + " Disabled";
    public String enabled = ChatColor.GREEN + "Helper:" + ChatColor.DARK_GREEN + " Enabled";

    public List<String> helplore = new ArrayList<>();
    public List<String> cominglore = new ArrayList<>();
    public List<String> flaglore = new ArrayList<>();


    ItemStack help = new ItemStack(Material.PAPER);
    ItemMeta helpmeta = help.getItemMeta();
    ItemStack coming = new ItemStack(Material.PAPER);
    ItemMeta comingmeta = coming.getItemMeta();
    ItemStack care = new ItemStack(Material.PAPER);
    ItemMeta caremeta = care.getItemMeta();
    ItemStack flag = new ItemStack(Material.PAPER);
    ItemMeta flagmeta = flag.getItemMeta();

    ItemStack helper = new ItemStack(Material.PAPER);
    ItemMeta helpermeta = helper.getItemMeta();

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        helpmeta.setDisplayName(ChatColor.GREEN + "Help me!");
        helplore.add(ChatColor.RED + "Clicking on this item will request aid at your location.");
        helpmeta.setLore(helplore);
        help.setItemMeta(helpmeta);

        comingmeta.setDisplayName(ChatColor.GREEN + "I'm on my way!");
        cominglore.add(ChatColor.RED + "Clicking on this item will notify your teammates that");
        cominglore.add(ChatColor.RED + "you're on your way to them.");
        comingmeta.setLore(cominglore);
        coming.setItemMeta(comingmeta);

        flagmeta.setDisplayName(ChatColor.GREEN + "Flag carrier help!");
        String flaglore1 = ChatColor.GOLD + "Clicking this item will request immediate ";
        String flaglore3 = ChatColor.GOLD + "assistance for you to cary the flag safely.";
        String flaglore2 = ChatColor.RED + "**Can only be activated if you're carrying the flag!**";
        flaglore.add(flaglore1);
        flaglore.add(flaglore3);
        flaglore.add(flaglore2);
        flagmeta.setLore(flaglore);
        flag.setItemMeta(flagmeta);
    }

    public String wlname = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "75" + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Mage" + ChatColor.DARK_GRAY + "]"
            + ChatColor.AQUA + "[MVP" + ChatColor.RED + "+" + ChatColor.AQUA + "] Professor_Zed";

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("helper")) {
                helpermeta.setDisplayName(disabled);
                helper.setItemMeta(helpermeta);
                p.getInventory().setItem(8, helper);
            }
        }
        return false;
    }

    public Player p;

    public HashMap<Player, ItemStack[]> invSave = new HashMap<Player, ItemStack[]>();

    public HashMap<Player, ItemStack[]> armorSave = new HashMap<Player, ItemStack[]>();


    public void addHelper(Inventory inv, Player p) {
        Inventory pinv = p.getInventory();
        pinv.addItem(help);
        pinv.addItem(coming);
        pinv.addItem(flag);
    }


    boolean helperOpened = false;

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        helpermeta.setDisplayName(disabled);
        helper.setItemMeta(helpermeta);
        if (helperOpened == false && e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand() != null && p.getItemInHand().getItemMeta().getDisplayName().equals(disabled)) {
                invSave.put(p, p.getInventory().getContents());
                armorSave.put(p, p.getInventory().getArmorContents());
                p.getInventory().clear();
                addHelper(p.getInventory(), p);
                helpermeta.setDisplayName(enabled);
                helper.setItemMeta(helpermeta);
                p.getInventory().setItem(8, helper);
                helperOpened = true;

            }
        } else if (helperOpened == true && e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand() != null && p.getItemInHand().getItemMeta().getDisplayName().equals(enabled)) {
                p.getInventory().clear();
                helpermeta.setDisplayName(disabled);
                helper.setItemMeta(helpermeta);
                p.getInventory().setContents(invSave.get(p));
                p.getInventory().setArmorContents(armorSave.get(p));
                p.updateInventory();
                helperOpened = false;
            }
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            double x = p.getLocation().getBlockX();
            double y = p.getLocation().getBlockY();
            double z = p.getLocation().getBlockZ();
            if (p.getItemInHand() != null && p.getItemInHand().equals(help)) {
                p.sendMessage(wlname + ChatColor.RED + " is requesting aid at " + "X:" + ChatColor.WHITE + x + ChatColor.RED + " Y:" + ChatColor.WHITE
                        + y + ChatColor.RED + " Z:" + ChatColor.WHITE + z);
            } else if (p.getItemInHand() != null && p.getItemInHand().equals(coming)) {
                p.sendMessage(wlname + ChatColor.GREEN + " is on the way!");
            } else if (p.getItemInHand() != null && p.getItemInHand().equals(flag)) {
                p.sendMessage(wlname + ChatColor.GREEN + " ⚑ Enemy flag acquired. Requesting immediate assistance at " + "X:" + ChatColor.WHITE + x + ChatColor.GREEN + " Y:" + ChatColor.WHITE
                        + y + ChatColor.GREEN + " Z:" + ChatColor.WHITE + z);
            }
        }
    }
}
