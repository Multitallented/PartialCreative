/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redcastlemedia.multitallented.bukkit.partialcreative;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author danielmiller
 */
public class PCListener implements Listener {
  private final PartialCreative plugin;
  public PCListener(PartialCreative plugin) {
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    plugin.handleQuittingPlayer(event.getPlayer());
  }
  
  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event) {
    try {
      if (event.isCancelled() || event.getInventory().equals(event.getPlayer().getInventory()) ||
              !PartialCreative.isPlayerInMode((Player) event.getPlayer())) {
        return;
      }
    } catch (Exception e) {
      return;
    }
    event.setCancelled(true);
    ((Player) event.getPlayer()).sendMessage(ChatColor.GRAY + "[PartialCreative] You can't do that while in /pcm");
  }
  
  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    if (event.isCancelled() || !PartialCreative.isPlayerInMode(event.getPlayer())) {
      return;
    }
    event.setCancelled(true);
    event.getPlayer().sendMessage(ChatColor.GRAY + "[PartialCreative] You can't do that while in /pcm");
  }
  
  @EventHandler
  public void onPlayerPickupItem(PlayerPickupItemEvent event) {
    if (event.isCancelled() || !PartialCreative.isPlayerInMode(event.getPlayer())) {
      return;
    }
    event.setCancelled(true);
  }
  
  @EventHandler
  public void onPlayerEggThrowEvent(PlayerEggThrowEvent event) {
    if (event.isHatching() && PartialCreative.isPlayerInMode(event.getPlayer())) {
      event.setHatching(false);
    }
  }
  
  
  
  
}
