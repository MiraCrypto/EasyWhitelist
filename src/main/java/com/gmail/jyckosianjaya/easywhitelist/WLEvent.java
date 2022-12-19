package com.gmail.jyckosianjaya.easywhitelist;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class WLEvent implements Listener {
   private final EasyWhiteList list;

   public WLEvent(EasyWhiteList list) {
      this.list = list;
   }

   @EventHandler
   public void onConnect(PlayerLoginEvent e) {
      Player p = e.getPlayer();
      UUID id = p.getUniqueId();
      if (!list.getStorage().onLogin(id, p.getName())) {
         e.disallow(Result.KICK_WHITELIST, this.list.getStorage().getNoWhitelistMsg());
      }
   }
}
