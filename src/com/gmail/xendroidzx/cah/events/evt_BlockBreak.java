package com.gmail.xendroidzx.cah.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.gmail.xendroidzx.cah.CAH;

public class evt_BlockBreak implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(CAH.getInstance().players.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		}
	}
	
}
