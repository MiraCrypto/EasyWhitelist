package com.gmail.jyckosianjaya.easywhitelist;

import java.util.Collection;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WLCmd implements CommandExecutor {
   private final EasyWhiteList list;
   String prefix = "&6&lE - &e&lWhiteList > &7";

   public WLCmd(EasyWhiteList list) {
      this.list = list;
   }

   public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
      if (!arg0.hasPermission("easywhitelist.admin")) {
         return true;
      } else {
         this.remanage(arg0, arg3);
         return true;
      }
   }

   private void sendHelp(CommandSender snd) {
      Utility.sendMsg(snd, "&a&lWhitelist &7>");
      Utility.sendMsg(snd, "&e> &7/easywl &aadd &f<name>");
      Utility.sendMsg(snd, "&e> &7/easywl &cremove &f<name>");
      Utility.sendMsg(snd, "&e> &7/easywl &flist");
      Utility.sendMsg(snd, "&e> &7/easywl &a&lon");
      Utility.sendMsg(snd, "&e> &7/easywl &c&loff");
      Utility.sendMsg(snd, "&e> &7/easywl &creload");
   }

   private void remanage(CommandSender snd, String[] args) {
      if (args.length == 0) {
         sendHelp(snd);
      } else {
         String name;
         switch (args[0].toLowerCase()) {
            case "reload":
               this.list.getStorage().reload();
               return;
            case "remove":
               if (args.length < 2) {
                  Utility.sendMsg(snd, "&7Please input a name!");
                  return;
               }

               name = args[1];
               this.list.getStorage().removeWhitelist(name);
               Utility.sendMsg(snd, this.prefix + "Whitelist removed for &c" + name);
               return;
            case "on":
               this.list.getStorage().setEnabled(true);
               Utility.sendMsg(snd, this.prefix + "&fWhitelist is now &a&lON&f!");
               return;
            case "add":
               if (args.length < 2) {
                  Utility.sendMsg(snd, "&7Please input a name!");
                  return;
               }

               name = args[1];
               this.list.getStorage().addName(name);
               Utility.sendMsg(snd, this.prefix + "Whitelisted &a" + name);
               return;
            case "off":
               this.list.getStorage().setEnabled(false);
               Utility.sendMsg(snd, this.prefix + "&8Whitelist is &c&lOFF!&8");
               return;
            case "list":
               List<String> neverLogged = this.list.getStorage().getNames();
               Utility.sendMsg(snd, "&a&lNever logged in: &7" + String.join(", ", neverLogged));

               Collection<String> logged = this.list.getStorage().getUUIDNames();
               Utility.sendMsg(snd, "&a&lLogged in: &7" + String.join(", ", logged));
               return;
         }

         sendHelp(snd);
      }

   }
}
