package com.gmail.jyckosianjaya.easywhitelist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;

public class WLStorage {
   private final EasyWhiteList plugin;
   private final ArrayList<String> nameList = new ArrayList<>();
   private ConfigurationSection uuidList;
   private boolean enabled = false;
   private String noWhitelistMsg = "";

   public WLStorage(EasyWhiteList plugin) {
      this.plugin = plugin;
      this.reload();
   }

   public void reload() {
      this.plugin.reloadConfig();
      FileConfiguration config = this.plugin.getConfig();
      
      this.nameList.clear();
      this.nameList.addAll(config.getStringList("whitelisted"));
      
      this.uuidList = config.getConfigurationSection("uuidlisted");
      if (this.uuidList == null) {
         this.uuidList = config.createSection("uuidlisted");
      }
      
      this.enabled = config.getBoolean("whitelist");
      this.noWhitelistMsg = Utility.TransColor(config.getString("no_whitelisted"));

      Utility.sendConsole("&e&lEasyWhitelist > &7Config reloaded.");
   }

   public void saveWhitelists() {
      FileConfiguration config = this.plugin.getConfig();
      config.set("whitelisted", this.nameList);
      config.set("whitelist", this.enabled);
      config.set("uuidlisted", this.uuidList);
      this.plugin.saveConfig();
   }

   public boolean isNameListed(String name) {
      return this.nameList.contains(name.toLowerCase());
   }

   public void addName(String name) {
      if (this.getUUIDFromName(name) != null) {
         return;
      }
      String key = name.toLowerCase();
      if (!this.nameList.contains(key)) {
         this.nameList.add(key);
         this.saveWhitelists();
      }
   }

   public void removeName(String name) {
      String key = name.toLowerCase();
      if (this.nameList.contains(key)) {
         this.nameList.remove(key);
         this.saveWhitelists();
      }
   }

   public void addUUID(UUID id, String name) {
      this.uuidList.set(id.toString(), name);
      this.saveWhitelists();
   }

   public void removeUUID(@Nonnull String uuid) {
      this.uuidList.set(uuid, null);
      this.saveWhitelists();
   }

   public void removeWhitelist(String name) {
      this.removeName(name);

      String uuid = this.getUUIDFromName(name);
      if (uuid != null) {
         this.removeUUID(uuid);
      }
   }

   public void setEnabled(boolean onoff) {
      this.enabled = onoff;
      this.saveWhitelists();
   }

   public ArrayList<String> getNames() {
      return this.nameList;
   }

   public Map<String, Object> getUUIDLists() {
      return this.uuidList.getValues(false);
   }

   public Collection<String> getUUIDNames() {
      return getUUIDLists().values().stream().map(Object::toString).toList();
   }

   public String getNoWhitelistMsg() {
      return this.noWhitelistMsg;
   }

   public boolean isUUIDlisted(UUID id) {
      if (this.uuidList == null) {
         return false;
      }
      return this.uuidList.get(id.toString()) != null;
   }

   public String getUUIDFromName(String name) {
      for (String uuid: uuidList.getKeys(false)) {
         if (name.equalsIgnoreCase(uuidList.getString(uuid))) {
            return uuid;
         }
      }
      return null;
   }

   // Upon player login, transfer from name list to uuid list.
   public boolean onLogin(UUID id, String name) {
      if (!enabled) {
         return true;
      }
      if (isUUIDlisted(id)) {
         return true;
      }
      if (isNameListed(name)) {
         addUUID(id, name);
         removeName(name);
         return true;
      }
      return false;
   }
}
