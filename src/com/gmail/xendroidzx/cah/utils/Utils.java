package com.gmail.xendroidzx.cah.utils;

import java.util.Arrays;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {

	public static ItemStack setName(ItemStack i, String name) {
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(name);
		i.setItemMeta(m);
		return i;
	}

	public static ItemStack setLore(ItemStack i, String... lore) {
		ItemMeta m = i.getItemMeta();
		m.setLore(Arrays.asList(lore));
		i.setItemMeta(m);
		return i;
	}

	public static String replace(String string, char letter, Object... words) {
		String result = String.format(string.replace(String.valueOf(letter), "%s"), words);
		return result;
	}

}
