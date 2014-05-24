// This snippet is borrowed, but I couldn't find the source to credit when I googled bits of the code, so I am sorry anonymous donor.

package com.ne0nx3r0.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemStackConvertorRI2 {
    public static String fromItemStack(ItemStack is,boolean includeName) {
        StringBuilder f = new StringBuilder();
        f.append("type=").append(is.getType()).append(";");
        if (is.getDurability() != 0)
           f.append("dura=").append(is.getDurability()).append(";");
        f.append("amount=").append(is.getAmount()).append(";");
        
        if (!is.getEnchantments().isEmpty()) {
           f.append("enchantments=");
           int in = 1;
           for (Map.Entry<Enchantment, Integer> key : is.getEnchantments().entrySet()) {
              f.append(key.getKey().getName()).append(":").append(key.getValue());
              if (in != is.getEnchantments().size()) {
                 f.append("&");
              }
              in++;
           }
           f.append(";");
        }
        else if(is.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta bookmeta = (EnchantmentStorageMeta) is.getItemMeta();
            
            if(bookmeta.hasStoredEnchants()) {
                f.append("enchantments_book=");


                int in = 1;
                int last = bookmeta.getStoredEnchants().size();
                
                for (Entry<Enchantment, Integer> enchantment : bookmeta.getStoredEnchants().entrySet()) {

                    f.append(enchantment.getKey().getName()).append(":").append(enchantment.getValue());
                    
                    if(in != last){
                        f.append("&");
                    }

                    in++;
                }
            }
            
            f.append(";");
        }
        
        if (is.hasItemMeta()) {
           ItemMeta m = is.getItemMeta();
           if (includeName && m.hasDisplayName()) {
              f.append("name=").append(m.getDisplayName().replace(";","&#59")).append(";");
           }
           if (m instanceof LeatherArmorMeta) {
              LeatherArmorMeta me = (LeatherArmorMeta) m;
              int r = me.getColor().getRed();
              int g = me.getColor().getGreen();
              int b = me.getColor().getBlue();
              f.append("rgb=").append(r).append(",").append(g).append(",").append(b);
           }
           if (m.hasLore()) {
              f.append("lore=");
              StringBuilder lore = new StringBuilder();
              for (String s : m.getLore()) {
                 lore.append("line:").append(s.replace(";","&#59"));
              }
              f.append(lore.toString().replaceFirst("line:", ""));
           }
           if (m instanceof SkullMeta) {
              SkullMeta me = (SkullMeta) m;
              if (me.hasOwner())
                 f.append("owner=").append(me.getOwner().replace(";","&#59"));
           }
        }
        return f.toString();
    }
    
    // TODO: Clean up this mess...
    public static ItemStack fromString(String s) {
      Material type = Material.AIR;
      short dura = 0;
      int amount = 1;
      Map<Enchantment, Integer> enchants = new HashMap<>();
      String cName = null;
      String[] rgb = null;
      List<String> lore = new ArrayList<>();
      String owner = null;
      
      ItemStack is = new ItemStack(type, amount);

      for (String d : s.split(";")) {
         String[] id = d.split("=");
         if (id[0].equalsIgnoreCase("type")) {
            is.setType(Material.getMaterial(id[1]));
         } else if (id[0].equalsIgnoreCase("dura")) {
            dura = Short.parseShort(id[1]);
         } else if (id[0].equalsIgnoreCase("amount")) {
            is.setAmount(Integer.parseInt(id[1]));
         }
         else if(id[0].equalsIgnoreCase("enchantments_book")) {
            EnchantmentStorageMeta bookmeta = (EnchantmentStorageMeta) is.getItemMeta();
                
            for (String en : id[1].split("&")) {
               String[] ench = en.split(":");
               
               bookmeta.addStoredEnchant(Enchantment.getByName(ench[0]), Integer.parseInt(ench[1]), true);
            }
            
            is.setItemMeta(bookmeta);
         }
         else if (id[0].equalsIgnoreCase("enchantments")) {
            for (String en : id[1].split("&")) {
               String[] ench = en.split(":");
               enchants.put(Enchantment.getByName(ench[0]), Integer.parseInt(ench[1]));
            }
         } else if (id[0].equalsIgnoreCase("name")) {
            cName = id[1].replace("&#59",";");
         } else if (id[0].equalsIgnoreCase("rgb")) {
            rgb = id[1].split(",");
         } else if (id[0].equalsIgnoreCase("lore")) {
            lore = Arrays.asList(id[1].replace("&#59",";").split("line:"));
         } else if (id[0].equalsIgnoreCase("owner")) {
            owner = id[1].replace("&#59",";");
         }
      }
      
      if (dura != 0) {
         is.setDurability(dura);
      }
      ItemMeta m = is.getItemMeta();
      if (cName != null)
         m.setDisplayName(cName);
      if (rgb != null)
         ((LeatherArmorMeta) m).setColor(Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
      if (!lore.isEmpty())
         m.setLore(lore);
      if (owner != null)
         ((SkullMeta) m).setOwner(owner);
      is.setItemMeta(m);
      
      if(!enchants.isEmpty()){
        is.addUnsafeEnchantments(enchants);
      }
      
      return is;
    }
}
