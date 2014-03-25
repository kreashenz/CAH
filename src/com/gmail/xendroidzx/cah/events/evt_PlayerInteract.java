package com.gmail.xendroidzx.cah.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.xendroidzx.cah.CAH;

public class evt_PlayerInteract implements Listener {

	private CAH plugin = CAH.getInstance();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(plugin.players.contains(p.getName())) {
			ItemStack i = p.getItemInHand();
			if(i != null && i.getType() == Material.WOOL && i.hasItemMeta()) {
				if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					plugin.gh.submittedCard.put(p.getName(), i);
					plugin.gh.addCardToCzar(p);
					if(plugin.gh.maxAnswers == plugin.gh.answers.get(p.getName())) {
						plugin.gh.clearPlayer(p);
					}
				}
			}
		}
	}

}
