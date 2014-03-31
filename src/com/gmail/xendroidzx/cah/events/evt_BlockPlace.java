package com.gmail.xendroidzx.cah.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.gmail.xendroidzx.cah.CAH;

public class evt_BlockPlace implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(CAH.getInstance().players.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		}
	}

}
