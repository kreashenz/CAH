package com.gmail.xendroidzx.cah.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.gmail.xendroidzx.cah.CAH;

public class evt_PlayerDropItem implements Listener {

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		if(CAH.getInstance().players.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		}
	}

}
