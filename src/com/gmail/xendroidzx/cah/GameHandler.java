package com.gmail.xendroidzx.cah;

import java.util.Random;

import org.bukkit.entity.Player;

public class GameHandler {

	private CAH plugin = CAH.getInstance();
	private boolean isPlaying = false, canJoin = plugin.players.size() < 9;

	public void addPlayer(Player p) {
		if(isPlaying) {
			if(plugin.players.size() < 9) {
				plugin.players.add(p.getName());
			}
		}
	}

	public void start() {
		chooseStartingCzar();
	}

	private void chooseStartingCzar() {
		int i = new Random().nextInt(plugin.players.size());
		String p = plugin.players.get(i);
		plugin.czar = p;
	}

}
