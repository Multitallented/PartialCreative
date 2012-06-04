package redcastlemedia.multitallented.bukkit.partialcreative;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class PartialCreative extends JavaPlugin {
  private static HashMap<Player, ArrayList<ItemStack>> previousItems = new HashMap<Player, ArrayList<ItemStack>>();
  private static HashSet<Player> playerModes = new HashSet<Player>();
  
  @Override
  public void onDisable() {
    System.out.println("[PartialCreative] has been disabled.");
    
    for (Player p : playerModes) {
      if (previousItems.containsKey(p)) {
        setPlayerInventory(p, previousItems.get(p));
      }
    }
    
  }

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(new PCListener(this), this);

    System.out.println("[PartialCreative] has been enabled.");
  }
  
  @Override
  public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {
    if (label.equals("pc") && (cs instanceof Player)) {
      Player p = (Player) cs;
      if (playerModes.contains(p)) {
        ArrayList<ItemStack> oldItems = previousItems.containsKey(p) ? previousItems.get(p) : new ArrayList<ItemStack>();
        previousItems.put(p, storeInventory(p));
        setPlayerInventory(p, oldItems);
        p.sendMessage(ChatColor.GRAY + "[PartialCreative] You are now in partial creative mode.");
      } else {
        ArrayList<ItemStack> oldItems = previousItems.containsKey(p) ? previousItems.get(p) : new ArrayList<ItemStack>();
        previousItems.put(p, storeInventory(p));
        setPlayerInventory(p, oldItems);
        p.sendMessage(ChatColor.GRAY + "[PartialCreative] You are now in non-partial creative mode.");
      }
      return true;
    }
    return true;
  }
  
  public static boolean isPlayerInMode(Player p) {
    return playerModes.contains(p);
  }
  
  private ArrayList<ItemStack> storeInventory(Player p) {
    ArrayList<ItemStack> iss = new ArrayList<ItemStack>();
    PlayerInventory pi = p.getInventory();
    iss.addAll(Arrays.asList(pi.getArmorContents()));
    iss.addAll(Arrays.asList(pi.getContents()));
    return iss;
  }
  
  private void setPlayerInventory(Player p, ArrayList<ItemStack> oldItems) {
    PlayerInventory pi = p.getInventory();
    pi.clear();
    pi.setHelmet(oldItems.get(0));
    pi.setChestplate(oldItems.get(1));
    pi.setLeggings(oldItems.get(2));
    pi.setBoots(oldItems.get(3));
    for (int i = 4; i< oldItems.size(); i++) {
      try {
        pi.addItem(oldItems.get(i));
      } catch (NullPointerException npe) {
        
      }
    }
  }
  
  public void handleQuittingPlayer(Player p) {
    if (!playerModes.contains(p)) {
      return;
    }
    if (previousItems.containsKey(p)) {
      ArrayList<ItemStack> oldItems = storeInventory(p);
      setPlayerInventory(p, previousItems.get(p));
      previousItems.put(p, oldItems);
    }
    playerModes.remove(p);
  }
}

