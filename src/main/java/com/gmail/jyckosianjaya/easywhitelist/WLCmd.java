package com.gmail.jyckosianjaya.easywhitelist;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WLCmd implements CommandExecutor {
   private EasyWhiteList m;
   String prefix = "&6&lE - &e&lWhiteList > &7";

   public WLCmd(EasyWhiteList m) {
      this.m = m;
   }

   public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
      if (!arg0.hasPermission("easywhitelist.admin")) {
         return true;
      } else {
         this.remanage(arg0, arg3);
         return true;
      }
   }

   private void remanage(CommandSender snd, String[] args) {
      if (args.length == 0) {
         Utility.sendMsg(snd, "&a&lWhitelist &7>");
         Utility.sendMsg(snd, "&e> &7/easywl &aadd &f<name>");
         Utility.sendMsg(snd, "&e> &7/easywl &cremove &f<name>");
         Utility.sendMsg(snd, "&e> &7/easywl &flist");
         Utility.sendMsg(snd, "&e> &7/easywl &a&lon");
         Utility.sendMsg(snd, "&e> &7/easywl &c&loff");
         Utility.sendMsg(snd, "&e> &7/easywl &creload");
      } else {
         String names;
         switch (args[0].toLowerCase()) {
            case "reload":
               this.m.getStorage().reload();
               return;
            case "remove":
               if (args.length < 2) {
                  Utility.sendMsg(snd, "&7Please input a name!");
                  return;
               }

               names = args[1];
               this.m.getStorage().removeWhitelist(names);
               Utility.sendMsg(snd, this.prefix + "Whitelist removed for &c" + names);
               return;
            case "on":
               this.m.getStorage().setWhitelist(true);
               Utility.sendMsg(snd, this.prefix + "&fWhitelist is now &a&lON&f!");
               return;
            case "add":
               if (args.length < 2) {
                  Utility.sendMsg(snd, "&7Please input a name!");
                  return;
               }

               names = args[1];
               this.m.getStorage().addWhitelist(names);
               Utility.sendMsg(snd, this.prefix + "Whitelisted &a" + names);
               return;
            case "off":
               this.m.getStorage().setWhitelist(false);
               Utility.sendMsg(snd, this.prefix + "&8Whitelist is &c&lOFF!&8");
               return;
            case "list":
               List neverLogged = this.m.getStorage().getWhiteLists();
               Utility.sendMsg(snd, "&a&lNever logged in: &7" + String.join(", ", neverLogged));
               ArrayList logged = new ArrayList(this.m.getStorage().getUUIDLists().values());
               Utility.sendMsg(snd, "&a&lLogged in: &7" + String.join(", ", logged));
               return;
         }

         Utility.sendMsg(snd, "&a&lWhitelist &7>");
         Utility.sendMsg(snd, "&e> &7/easywl &aadd &f<name>");
         Utility.sendMsg(snd, "&e> &7/easywl &cremove &f<name>");
         Utility.sendMsg(snd, "&e> &7/easywl &flist");
         Utility.sendMsg(snd, "&e> &7/easywl &a&lon");
         Utility.sendMsg(snd, "&e> &7/easywl &c&loff");
         Utility.sendMsg(snd, "&e> &7/easywl &creload");
      }

   }
}
