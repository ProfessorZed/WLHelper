package me.zed.helper;

import net.minecraft.server.v1_8_R3.CommandExecute;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Random;

public class Domination extends CommandExecute implements CommandExecutor, Listener {

    CTF plugin;

    Domination(CTF plugin) {
        this.plugin = plugin;
    }


    public String disabled = ChatColor.GREEN + "Dom Helper:" + ChatColor.RED + " Disabled";
    public String enabled = ChatColor.GREEN + "Dom Helper:" + ChatColor.DARK_GREEN + " Enabled";

    public static ItemStack farm = new ItemStack(Material.PAPER);
    public static ItemMeta farmeta = farm.getItemMeta();
    public static ItemStack bs = new ItemStack(Material.PAPER);
    public static ItemMeta bsmeta = bs.getItemMeta();
    public static ItemStack lm = new ItemStack(Material.PAPER);
    public static ItemMeta lmeta = lm.getItemMeta();
    public static ItemStack mines = new ItemStack(Material.PAPER);
    public static ItemMeta minesmeta = mines.getItemMeta();
    public static ItemStack stables = new ItemStack(Material.PAPER);
    public static ItemMeta stablesmeta = stables.getItemMeta();

    public static ItemStack current = new ItemStack(Material.ENCHANTED_BOOK);
    public static ItemMeta currentmeta = current.getItemMeta();

    public ItemStack domHelper = new ItemStack(Material.PAPER);
    public ItemMeta domMeta = domHelper.getItemMeta();

    //God, please forgive me that I have sinned..

    public void addHelperDom(Inventory inv, Player p) {
        Inventory pinv = p.getInventory();
        pinv.addItem(farm);
        pinv.addItem(bs);
        pinv.addItem(lm);
        pinv.addItem(mines);
        pinv.addItem(stables);
        pinv.setItem(6, current);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("Dom")) {
                domMeta.setDisplayName(disabled);
                domHelper.setItemMeta(domMeta);
                p.getInventory().setItem(8, domHelper);
            }
        }
        return false;
    }

    Color getColor(int i) {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }

        return c;
    }

    public void spawnfw(Player p, Location loc) {
        Firework fw = (Firework) p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwmeta = fw.getFireworkMeta();

        Random ran = new Random();

        int r = ran.nextInt(4) + 1;
        Type type = Type.BALL;

        if (r == 1)
            type = Type.BALL;
        if (r == 2)
            type = Type.BALL_LARGE;
        if (r == 3)
            type = Type.BURST;
        if (r == 4)
            type = Type.CREEPER;
        if (r == 5)
            type = Type.STAR;

        int r1 = ran.nextInt(17) + 1;
        int r2 = ran.nextInt(17) + 1;
        Color c1 = getColor(r1);
        Color c2 = getColor(r2);

        FireworkEffect effect = FireworkEffect.builder().flicker(ran.nextBoolean()).withColor(c1).withFade(c2)
                .with(type).trail(ran.nextBoolean()).build();

        fwmeta.addEffect(effect);

        int rp = ran.nextInt(2) + 1;
        fwmeta.setPower(rp);

        fw.setFireworkMeta(fwmeta);
    }

    public void currentFirework(Player player, Location loc) {
        new BukkitRunnable() {
            int i = 5;
            public void run() {
                i--;
                spawnfw(player, loc);
                if (i <= 0) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20 * 5);
    }

    public HashMap<Player, ItemStack[]> invSave = new HashMap<Player, ItemStack[]>();
    public HashMap<Player, ItemStack[]> armorSave = new HashMap<Player, ItemStack[]>();

    boolean helperOpened = false;

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        String halp = plugin.wlname + ChatColor.RED + " is requesting immediate assistance at " + ChatColor.GREEN.toString() + ChatColor.BOLD;
        if (helperOpened == false && e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand() != null && p.getItemInHand().getItemMeta().getDisplayName().equals(disabled)) {
                invSave.put(p, p.getInventory().getContents());
                armorSave.put(p, p.getInventory().getArmorContents());
                p.getInventory().clear();
                addHelperDom(p.getInventory(), p);
                domMeta.setDisplayName(enabled);
                domHelper.setItemMeta(domMeta);
                p.getInventory().setItem(8, domHelper);
                helperOpened = true;
            }
        } else if (helperOpened == true && e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand() != null && p.getItemInHand().getItemMeta().getDisplayName().equals(enabled)) {
                p.getInventory().clear();
                domMeta.setDisplayName(disabled);
                domHelper.setItemMeta(domMeta);
                p.getInventory().setContents(invSave.get(p));
                p.getInventory().setArmorContents(armorSave.get(p));
                p.getInventory().setItem(8, domHelper);
                p.updateInventory();
                helperOpened = false;
            }
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand() != null && p.getItemInHand().equals(current)) {
                currentFirework(p, p.getLocation());
                p.sendMessage(ChatColor.RED + "Activated aid request to your location. The spawned " + ChatColor.GREEN + "firework signals" + ChatColor.RED + " will allow your teammates to spot you!");
            } else if (p.getItemInHand() != null && p.getItemInHand().equals(farm)){
                p.sendMessage(halp + "Farm");
            } else if (p.getItemInHand() != null && p.getItemInHand().equals(mines)){
                p.sendMessage(halp + "Mines");
            } else if (p.getItemInHand() != null && p.getItemInHand().equals(lm)){
                p.sendMessage(halp + "Lumbermill");
            } else if (p.getItemInHand() != null && p.getItemInHand().equals(bs)){
                p.sendMessage(halp + "Blacksmith");
            } else if (p.getItemInHand() != null && p.getItemInHand().equals(stables)){
                p.sendMessage(halp + "Stables");
            }
        }
    }
}