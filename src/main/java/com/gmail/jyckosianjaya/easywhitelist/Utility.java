package com.gmail.jyckosianjaya.easywhitelist;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public final class Utility {
   private static final int CENTER_PX = 154;

   private Utility() {
   }

   public static final Class getClass(String classname) {
      String servversion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

      try {
         return Class.forName("net.minecraft.server." + servversion + "." + classname);
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   private static final List findClasses(File directory, String packageName) throws ClassNotFoundException {
      List classes = new ArrayList();
      if (!directory.exists()) {
         return classes;
      } else {
         File[] files = directory.listFiles();
         File[] var7 = files;
         int var6 = files.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            File file = var7[var5];
            if (file.isDirectory()) {
               assert !file.getName().contains(".");

               classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
               classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
         }

         return classes;
      }
   }

   public static final Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

      assert classLoader != null;

      String path = packageName.replace('.', '/');
      Enumeration resources = classLoader.getResources(path);
      ArrayList dirs = new ArrayList();

      while(resources.hasMoreElements()) {
         URL resource = (URL)resources.nextElement();
         dirs.add(new File(resource.getFile()));
      }

      ArrayList classes = new ArrayList();
      Iterator var7 = dirs.iterator();

      while(var7.hasNext()) {
         File directory = (File)var7.next();
         classes.addAll(findClasses(directory, packageName));
      }

      return (Class[])classes.toArray(new Class[classes.size()]);
   }

   public static final void sendPacket(Player player, Object packet) {
      try {
         Object handle = player.getClass().getMethod("getHandle").invoke(player);
         Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
         playerConnection.getClass().getMethod("sendPacket", getClass("Packet")).invoke(playerConnection, packet);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static final void sendTitle(Player player, int fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
      try {
         Object e;
         Object chatSubtitle;
         Constructor subtitleConstructor;
         Object subtitlePacket;
         if (title != null) {
            title = TransColor(title);
            e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get((Object)null);
            chatSubtitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke((Object)null, "{\"text\":\"" + title + "\"}");
            subtitleConstructor = getClass("PacketPlayOutTitle").getConstructor(getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
            sendPacket(player, subtitlePacket);
            e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get((Object)null);
            chatSubtitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke((Object)null, "{\"text\":\"" + title + "\"}");
            subtitleConstructor = getClass("PacketPlayOutTitle").getConstructor(getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent"));
            subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle);
            sendPacket(player, subtitlePacket);
         }

         if (subtitle != null) {
            subtitle = TransColor(subtitle);
            e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get((Object)null);
            chatSubtitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke((Object)null, "{\"text\":\"" + title + "\"}");
            subtitleConstructor = getClass("PacketPlayOutTitle").getConstructor(getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
            sendPacket(player, subtitlePacket);
            e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get((Object)null);
            chatSubtitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke((Object)null, "{\"text\":\"" + subtitle + "\"}");
            subtitleConstructor = getClass("PacketPlayOutTitle").getConstructor(getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
            sendPacket(player, subtitlePacket);
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public static final void executeConsole(String cmd) {
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
   }

   public static final void sendMsg(Player b, String msg) {
      if (msg.contains("%center%")) {
         sendCenteredMessage(b, msg.replaceAll("%center%", ""));
      } else {
         b.sendMessage(TransColor(msg));
      }

   }

   public static final void sendMsg(CommandSender b, String msg) {
      if (msg.contains("%center%")) {
         if (b instanceof Player) {
            sendCenteredMessage((Player)b, msg.replaceAll("%center%", ""));
         } else {
            b.sendMessage(TransColor(msg.replaceAll("%center%", "")));
         }
      } else {
         b.sendMessage(TransColor(msg));
      }

   }

   public static final void broadcast(String msg) {
      if (msg.contains("%center%")) {
         broadCastCenteredMessage(msg.replaceAll("%center%", ""));
      } else {
         Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
      }

   }

   public static final void sendConsole(String msg) {
      Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
   }

   public static final String TransColor(String c) {
      return ChatColor.translateAlternateColorCodes('&', c);
   }

   public static final void sendCenteredMessage(Player player, String message) {
      if (message != null && !message.equals("")) {
         int messagePxSize = 0;
         boolean previousCode = false;
         boolean isBold = false;
         char[] var8;
         int spaceLength = (var8 = message.toCharArray()).length;

         int toCompensate;
         int c;
         for(toCompensate = 0; toCompensate < spaceLength; ++toCompensate) {
            c = var8[toCompensate];
            if (c == 167) {
               previousCode = true;
            } else if (previousCode) {
               previousCode = false;
               if (c != 108 && c != 76) {
                  isBold = false;
               } else {
                  isBold = true;
               }
            } else {
               DefaultFontInfo dFI = Utility.DefaultFontInfo.getDefaultFontInfo((char)c);
               messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
               ++messagePxSize;
            }
         }

         c = messagePxSize / 2;
         toCompensate = 154 - c;
         spaceLength = Utility.DefaultFontInfo.SPACE.getLength() + 1;
         int compensated = 0;

         StringBuilder sb;
         for(sb = new StringBuilder(); compensated < toCompensate; compensated += spaceLength) {
            sb.append(" ");
         }

         player.sendMessage(ChatColor.translateAlternateColorCodes('&', sb.toString() + message));
      } else {
         sendMsg(player, message);
      }

   }

   public static final void broadCastCenteredMessage(String message) {
      if (message != null && !message.equals("")) {
         int messagePxSize = 0;
         boolean previousCode = false;
         boolean isBold = false;
         char[] var7;
         int spaceLength = (var7 = message.toCharArray()).length;

         int toCompensate;
         int c;
         for(toCompensate = 0; toCompensate < spaceLength; ++toCompensate) {
            c = var7[toCompensate];
            if (c == 167) {
               previousCode = true;
            } else if (previousCode) {
               previousCode = false;
               if (c != 108 && c != 76) {
                  isBold = false;
               } else {
                  isBold = true;
               }
            } else {
               DefaultFontInfo dFI = Utility.DefaultFontInfo.getDefaultFontInfo((char)c);
               messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
               ++messagePxSize;
            }
         }

         c = messagePxSize / 2;
         toCompensate = 154 - c;
         spaceLength = Utility.DefaultFontInfo.SPACE.getLength() + 1;
         int compensated = 0;

         StringBuilder sb;
         for(sb = new StringBuilder(); compensated < toCompensate; compensated += spaceLength) {
            sb.append(" ");
         }

         Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', sb.toString() + message));
      } else {
         Bukkit.getServer().broadcastMessage("");
      }

   }

   public static final String[] TransColor(String[] c) {
      String strf = "";
      int length = c.length;
      int cr = 0;
      String[] var7 = c;
      int var6 = c.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         String str = var7[var5];
         ++cr;
         if (cr != length) {
            strf = strf + str + ";";
         } else {
            strf = strf + str;
         }
      }

      strf = TransColor(strf);
      return strf.split(";");
   }

   public static final List TransColor(List strlist) {
      for(int x = 0; x < strlist.size(); ++x) {
         strlist.set(x, TransColor((String)strlist.get(x)));
      }

      return strlist;
   }

   public static final void PlaySoundAt(World w, Location p, Sound s, Float vol, Float pit) {
      w.playSound(p, s, vol, pit);
   }

   public static final void PlaySound(Player p, Sound s, Float vol, Float pit) {
      p.playSound(p.getLocation(), s, vol, pit);
   }

   public static final ArrayList near(Entity loc, int radius) {
      ArrayList nearby = new ArrayList();
      Iterator var4 = loc.getNearbyEntities((double)radius, (double)radius, (double)radius).iterator();

      while(var4.hasNext()) {
         Entity e = (Entity)var4.next();
         if (e instanceof Player) {
            nearby.add((Player)e);
         }
      }

      return nearby;
   }

   public static final void PlayParticle(World world, Location loc, Effect particle, int count) {
      world.playEffect(loc, particle, count);
   }

   public static final void spawnParticle(World world, Particle particle, Location loc, Double Xoff, Double Yoff, Double Zoff, int count) {
      world.spawnParticle(particle, loc.getX(), loc.getY(), loc.getZ(), count, Xoff, Yoff, Zoff);
   }

   public static final String normalizeTime(int seconds) {
      int sec = seconds;
      int min = 0;
      int hour = 0;

      int day;
      for(day = 0; sec >= 60; sec -= 60) {
         ++min;
      }

      while(min >= 60) {
         ++hour;
         min -= 60;
      }

      while(hour >= 24) {
         ++day;
         hour -= 24;
      }

      if (sec == 0 && min == 0 && hour == 0 && day == 0) {
         return "&a&lZERO!";
      } else if (min == 0 && hour == 0 && day == 0) {
         return sec + " Seconds";
      } else if (hour == 0 && day == 0 && min > 0) {
         return min + " Minutes " + sec + " Seconds";
      } else if (day == 0 && hour > 0) {
         return hour + " Hours " + min + " Minutes " + sec + " Seconds";
      } else {
         return day > 0 ? day + " Days " + hour + " Hours " + min + " Minutes " + sec + " Seconds" : "&a&lZERO!";
      }
   }

   public static final String normalizeTime2(int seconds) {
      int sec = seconds;
      int min = 0;
      int hour = 0;

      int day;
      for(day = 0; sec >= 60; sec -= 60) {
         ++min;
      }

      while(min >= 60) {
         ++hour;
         min -= 60;
      }

      while(hour >= 24) {
         ++day;
         hour -= 24;
      }

      if (sec == 0 && min == 0 && hour == 0 && day == 0) {
         return "&a&lZERO!";
      } else if (min == 0 && hour == 0 && day == 0) {
         return sec + " sec";
      } else if (hour == 0 && day == 0 && min > 0) {
         return min + " min " + sec + " sec";
      } else if (day == 0 && hour > 0) {
         return hour + " h " + min + " min " + sec + " sec";
      } else {
         return day > 0 ? day + " day " + hour + " h " + min + " min " + sec + " sec" : "&a&lZERO!";
      }
   }

   public static final boolean isEmpty(Inventory inv) {
      int size = inv.getSize();

      for(int i = 0; i < size; ++i) {
         if (inv.getItem(i) == null) {
            return true;
         }
      }

      return false;
   }

   public static final boolean isEmpty(PlayerInventory inv) {
      return isEmpty(inv);
   }

   public static enum DefaultFontInfo {
      A('A', 5),
      a('a', 5),
      B('B', 5),
      b('b', 5),
      C('C', 5),
      c('c', 5),
      D('D', 5),
      d('d', 5),
      E('E', 5),
      e('e', 5),
      F('F', 5),
      f('f', 4),
      G('G', 5),
      g('g', 5),
      H('H', 5),
      h('h', 5),
      I('I', 3),
      i('i', 1),
      J('J', 5),
      j('j', 5),
      K('K', 5),
      k('k', 4),
      L('L', 5),
      l('l', 1),
      M('M', 5),
      m('m', 5),
      N('N', 5),
      n('n', 5),
      O('O', 5),
      o('o', 5),
      P('P', 5),
      p('p', 5),
      Q('Q', 5),
      q('q', 5),
      R('R', 5),
      r('r', 5),
      S('S', 5),
      s('s', 5),
      T('T', 5),
      t('t', 4),
      U('U', 5),
      u('u', 5),
      V('V', 5),
      v('v', 5),
      W('W', 5),
      w('w', 5),
      X('X', 5),
      x('x', 5),
      Y('Y', 5),
      y('y', 5),
      Z('Z', 5),
      z('z', 5),
      NUM_1('1', 5),
      NUM_2('2', 5),
      NUM_3('3', 5),
      NUM_4('4', 5),
      NUM_5('5', 5),
      NUM_6('6', 5),
      NUM_7('7', 5),
      NUM_8('8', 5),
      NUM_9('9', 5),
      NUM_0('0', 5),
      EXCLAMATION_POINT('!', 1),
      AT_SYMBOL('@', 6),
      NUM_SIGN('#', 5),
      DOLLAR_SIGN('$', 5),
      PERCENT('%', 5),
      UP_ARROW('^', 5),
      AMPERSAND('&', 5),
      ASTERISK('*', 5),
      LEFT_PARENTHESIS('(', 4),
      RIGHT_PERENTHESIS(')', 4),
      MINUS('-', 5),
      UNDERSCORE('_', 5),
      PLUS_SIGN('+', 5),
      EQUALS_SIGN('=', 5),
      LEFT_CURL_BRACE('{', 4),
      RIGHT_CURL_BRACE('}', 4),
      LEFT_BRACKET('[', 3),
      RIGHT_BRACKET(']', 3),
      COLON(':', 1),
      SEMI_COLON(';', 1),
      DOUBLE_QUOTE('"', 3),
      SINGLE_QUOTE('\'', 1),
      LEFT_ARROW('<', 4),
      RIGHT_ARROW('>', 4),
      QUESTION_MARK('?', 5),
      SLASH('/', 5),
      BACK_SLASH('\\', 5),
      LINE('|', 1),
      TILDE('~', 5),
      TICK('`', 2),
      PERIOD('.', 1),
      COMMA(',', 1),
      SPACE(' ', 3),
      DEFAULT('a', 4);

      private char character;
      private int length;

      private DefaultFontInfo(char character, int length) {
         this.character = character;
         this.length = length;
      }

      public final char getCharacter() {
         return this.character;
      }

      public final int getLength() {
         return this.length;
      }

      public final int getBoldLength() {
         return this == SPACE ? this.getLength() : this.length + 1;
      }

      public static final DefaultFontInfo getDefaultFontInfo(char c) {
         DefaultFontInfo[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            DefaultFontInfo dFI = var4[var2];
            if (dFI.getCharacter() == c) {
               return dFI;
            }
         }

         return DEFAULT;
      }
   }
}
