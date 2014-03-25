package com.gmail.xendroidzx.cah;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class LocHandler {
	
	private CAH plugin = CAH.getInstance();
	
	private FileConfiguration conf = plugin.getConfig();
	
	public void saveMapLocation(Location loc) {
		conf.set("location.world", loc.getWorld().getName());
		conf.set("location.x", loc.getBlockX());
		conf.set("location.y", loc.getBlockY());
		conf.set("location.z", loc.getBlockZ());
		
		plugin.saveConfig();
	}
	
	public Location getMapLocation() {
		return new Location(
				Bukkit.getWorld(conf.getString("location.world")),
				conf.getInt("location.x"),
				conf.getInt("location.y"),
				conf.getInt("location.z")
				);
	}

}
