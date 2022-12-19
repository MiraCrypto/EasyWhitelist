package com.gmail.jyckosianjaya.easywhitelist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class WLStorage {
   private EasyWhiteList m;
   private ArrayList whitelists = new ArrayList();
   private ConfigurationSection uuidlists;
   private boolean WhitelistEnabled = false;
   private String nowhitelistmsg = "";

   public WLStorage(EasyWhiteList m) {
      this.m = m;
      this.reload();
   }

   public void reload() {
      this.m.reloadConfig();
      FileConfiguration config = this.m.getConfig();
      this.whitelists = new ArrayList(config.getStringList("whitelisted"));
      this.uuidlists = config.getConfigurationSection("uuidlisted");
      this.WhitelistEnabled = config.getBoolean("whitelist");
      this.nowhitelistmsg = Utility.TransColor(config.getString("no_whitelisted"));
      if (this.uuidlists == null) {
         this.uuidlists = config.createSection("uuidlisted");
      }

      Utility.sendConsole("&e&lEasyWhitelist > &7Config reloaded.");
   }

   public void saveWhitelists() {
      FileConfiguration config = this.m.getConfig();
      config.set("whitelisted", this.whitelists);
      config.set("whitelist", this.isWhitelisting());
      config.set("uuidlisted", this.uuidlists);
      this.m.saveConfig();
   }

   public boolean isWhitelisted(String name) {
      return this.whitelists.contains(name.toLowerCase());
   }

   public void addWhitelist(String name) {
      if (this.getUUIDFromName(name) == null) {
         if (!this.whitelists.contains(name.toLowerCase())) {
            this.whitelists.add(name.toLowerCase());
            this.saveWhitelists();
         }

      }
   }

   public void addUUID(UUID id, String name) {
      this.uuidlists.set(id.toString(), name);
      this.saveWhitelists();
   }

   public void removeUUIDlist(String uuid) {
      if (uuid != null) {
         this.uuidlists.set(uuid, (Object)null);
         this.saveWhitelists();
      }

   }

   public void removeWhitelist(String name) {
      if (this.whitelists.contains(name.toLowerCase())) {
         this.whitelists.remove(name.toLowerCase());
         this.saveWhitelists();
      }

      this.removeUUIDlist(this.getUUIDFromName(name));
   }

   public void removeWhitelistOnly(String name) {
      if (this.whitelists.contains(name.toLowerCase())) {
         this.whitelists.remove(name.toLowerCase());
         this.saveWhitelists();
      }

   }

   public void setWhitelist(Boolean onoff) {
      this.WhitelistEnabled = onoff;
      this.saveWhitelists();
   }

   public ArrayList getWhiteLists() {
      return this.whitelists;
   }

   public Map getUUIDLists() {
      return this.uuidlists.getValues(false);
   }

   public boolean isWhitelisting() {
      return this.WhitelistEnabled;
   }

   public String getNoWhitelistMsg() {
      return this.nowhitelistmsg;
   }

   public boolean isUUIDlisted(UUID id) {
      if (this.uuidlists == null) {
         return false;
      } else {
         return this.uuidlists.get(id.toString()) != null;
      }
   }

   public String getUUIDFromName(String name) {
      Iterator var2 = this.uuidlists.getKeys(false).iterator();

      String key;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         key = (String)var2.next();
      } while(!name.equalsIgnoreCase(this.uuidlists.getString(key)));

      return key;
   }
}
