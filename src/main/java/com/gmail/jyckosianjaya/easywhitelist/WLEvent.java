package com.gmail.jyckosianjaya.easywhitelist;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class WLEvent implements Listener {
   private EasyWhiteList m;

   public WLEvent(EasyWhiteList m) {
      this.m = m;
   }

   @EventHandler
   public void onConnect(PlayerLoginEvent e) {
      Player p = e.getPlayer();
      UUID id = p.getUniqueId();
      if (p != null && this.m.getStorage().isWhitelisting() && !this.m.getStorage().isUUIDlisted(id)) {
         if (this.m.getStorage().isWhitelisted(p.getName())) {
            this.m.getStorage().addUUID(id, p.getName());
            this.m.getStorage().removeWhitelistOnly(p.getName());
         } else {
            e.disallow(Result.KICK_WHITELIST, this.m.getStorage().getNoWhitelistMsg());
         }
      }
   }
}
