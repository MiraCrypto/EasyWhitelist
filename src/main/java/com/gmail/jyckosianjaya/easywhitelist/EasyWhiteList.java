package com.gmail.jyckosianjaya.easywhitelist;

import org.bukkit.plugin.java.JavaPlugin;

public class EasyWhiteList extends JavaPlugin {
   private static EasyWhiteList instance;
   private WLStorage storage;

   public void onEnable() {
      instance = this;
      this.getConfig().options().copyDefaults(true);
      this.saveConfig();
      this.getCommand("easywhitelist").setExecutor(new WLCmd(this));
      this.getServer().getPluginManager().registerEvents(new WLEvent(this), this);
      this.storage = new WLStorage(this);
      Utility.sendConsole("&eE-Whitelist > &7Loaded!");
   }

   public static EasyWhiteList getInstance() {
      return instance;
   }

   public void onDisable() {
      this.storage.saveWhitelists();
   }

   public WLStorage getStorage() {
      return this.storage;
   }
}
