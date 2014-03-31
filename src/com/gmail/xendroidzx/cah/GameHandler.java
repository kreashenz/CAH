package com.gmail.xendroidzx.cah;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.gmail.xendroidzx.cah.utils.Utils;

public class GameHandler {

	private HashMap<String, WhiteCard[]> deck = new HashMap<String, WhiteCard[]>();
	public HashMap<String, Integer> scores = new HashMap<String, Integer>(), answers = new HashMap<String, Integer>();
	public HashMap<String, ItemStack> submittedCard = new HashMap<String, ItemStack>();

	private CAH plugin = CAH.getInstance();
	public boolean isPlaying = false, countdown = false, canJoin = (plugin.players.size() < 8 && plugin.players.size() >= 2);

	private BlackCard black = plugin.blacks.get(new Random().nextInt(plugin.blacks.size()));
	private WhiteCard white1, white2, white3, white4;

	public int maxAnswers = black.getAnswers(), time = 15;

	public void addPlayer(Player p) {
		if(!plugin.players.contains(p.getName())) {
			if(!canJoin) {
				if(plugin.loc.getMapLocation() != null) {
					plugin.players.add(p.getName());
					answers.put(p.getName(), 0);
					Bukkit.broadcastMessage("§6" + p.getName() + " §chas joined the game! §7(§6" + plugin.players.size() + "§7/§c8§7)");
					p.teleport(plugin.loc.getMapLocation());
					if(plugin.players.size() >= 2 && !(countdown && isPlaying)) {
						startTimer();
					}
					if(plugin.players.size() == 8 && !(countdown && isPlaying)) {
						start();
					}
				} else p.sendMessage("§cThere's no spawn location set! Uh oh!");
			} else p.sendMessage("§cSorry, you can't join now, the game is full!");
		} else p.sendMessage("§cYou are already in the game!");
	}

	public void removePlayer(Player p) {
		if(plugin.players.contains(p.getName())) {
			plugin.players.remove(p.getName());
			Bukkit.broadcastMessage("§6" + p.getName() + " §chas left the game! §7(§6" + plugin.players.size() + "§7/§c8§7)");
			if(plugin.players.size() <= 1) {
				end();
				reset();
			}
		} else p.sendMessage("§cYou can't leave, because you never joined!");
	}

	public void start() {
		chooseStartingCzar();
		for(String s : plugin.players) {
			Player p = Bukkit.getPlayerExact(s);
			if(!p.getName().equals(plugin.getCzar().getName())) {
				dealCards(p, false);
			}
		}
	}

	public void end() {
		if(!isPlaying) {
			for(String s : plugin.players) {
				Player p = Bukkit.getPlayerExact(s);
				clearPlayer(p);
			}
		}
	}

	public void startTimer() {
		new Runnable() {
			public int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
			@Override
			public void run() {
				if (time >= 1) {
					time--;
					countdown = true;
					if(time == 60) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c1 minute§6!");
					}
					if(time == 30 || time == 15 || time == 10 || (time <= 5 && time > 1)) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c" + time + " seconds§6!");
					}
					if(time == 1) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c1 second§6!");
					}
				} else {
					start();
					countdown = false;
					isPlaying = true;
					Bukkit.getScheduler().cancelTask(taskID);
					Bukkit.broadcastMessage("§6New CAH game starting §cnow§6!");
				}
			}
		};
	}

	private void chooseStartingCzar() {
		int i = new Random().nextInt(plugin.players.size());
		String p = plugin.players.get(i);
		plugin.czar = p;
		plugin.getCzar().sendMessage("§6You are the Czar! §7You must wait till everyone has submitted a"
				+ " §7card then you must choose which is your favourite; usually the one that makes you laugh the most!");
		dealCards(plugin.getCzar(), true);
	}

	private void chooseCards(Player p) {
		for(int i = 0; i < plugin.players.size(); i++) {
			white1 = plugin.whites.get(new Random().nextInt(plugin.whites.size()));
			white2 = plugin.whites.get(new Random().nextInt(plugin.whites.size()));
			white3 = plugin.whites.get(new Random().nextInt(plugin.whites.size()));
			white4 = plugin.whites.get(new Random().nextInt(plugin.whites.size()));
		}
		deck.put(p.getName(), new WhiteCard[] {white1, white2, white3, white4});
	}

	private void dealCards(Player p, boolean isCzar) {
		PlayerInventory pi = p.getInventory();
		ItemStack b = Utils.setName(new ItemStack(Material.WOOL, 1, (byte) 15), "§8" + black.getText());
		pi.setItem(8, b);
		if(isCzar == false) {
			chooseCards(p);
			pi.addItem(
					Utils.setName(new ItemStack(Material.WOOL), "§f" + white1.getText()),
					Utils.setName(new ItemStack(Material.WOOL), "§f" + white2.getText()),
					Utils.setName(new ItemStack(Material.WOOL), "§f" + white3.getText()),
					Utils.setName(new ItemStack(Material.WOOL), "§f" + white4.getText())
					);
		}
	}

	public void addCardToCzar(Player fromPlayer) {
		HashMap<String, ItemStack> stored = new HashMap<String, ItemStack>();
		if(maxAnswers > 1) {
			if(!(answers.get(fromPlayer.getName()) == maxAnswers)) {
				answers.put(fromPlayer.getName(), answers.get(fromPlayer.getName()) + 1);
				ItemStack move = submittedCard.get(fromPlayer.getName());
				stored.put(fromPlayer.getName(), move);
				submittedCard.remove(fromPlayer.getName());
			} else {
				ItemStack w1 = stored.get(fromPlayer.getName()), w2 = submittedCard.get(fromPlayer.getName());
				multipleCardStuff(fromPlayer, w1, w2);
				clearPlayer(fromPlayer);
			}
		} else {
			if(stored.containsKey(fromPlayer.getName())) stored.remove(fromPlayer.getName());
			singleCardStuff(fromPlayer);
			answers.put(fromPlayer.getName(), answers.get(fromPlayer.getName()) + 1);
			clearPlayer(fromPlayer);
		}
	}

	private void singleCardStuff(Player p) {
		Player czar = plugin.getCzar();
		PlayerInventory pi = czar.getInventory();

		String lore = black.getText();
		lore = Utils.replace(lore, '_', submittedCard.get(p.getName()).getItemMeta().getDisplayName());

		ItemStack i = Utils.setName(Utils.setLore(submittedCard.get(p.getName()), lore), "§b" + p.getName());
		pi.addItem(i);
		submittedCard.remove(p.getName());
	}

	private void multipleCardStuff(Player p, ItemStack... cards) {
		Player czar = plugin.getCzar();
		PlayerInventory pi = czar.getInventory();

		ItemStack a = null;
		String lore = black.getText();
		for(ItemStack i : cards) {
			lore = Utils.replace(lore, '_', i.getItemMeta().getDisplayName());
			a = Utils.setName(Utils.setLore(i, lore), "§b" + p.getName());
		}

		pi.addItem(a);
	}

	@SuppressWarnings("deprecation")
	public void clearPlayer(Player p) {
		for(ItemStack i : p.getInventory().getContents()) {
			if(i != null && i.getType() == Material.WOOL && i.getData().getData() == 0 && i.hasItemMeta()) {
				i.setType(Material.AIR);
			}
		}
	}

	public void addScore(Player p, int score, boolean setCzar) {
		if(setCzar) {
			plugin.czar = p.getName();
		}
		scores.put(p.getName(), score);
	}

	public void reset() {
		for(String s : plugin.players) {
			Player p = Bukkit.getPlayerExact(s);
			p.teleport(plugin.loc.getPlayerLocation(p));
			plugin.loc.removePlayerLocation(p);
			removePlayer(p);
		}
		plugin.players.clear();
		plugin.armor.clear();
		plugin.inv.clear();
		plugin.czar = null;

		plugin.gh = new GameHandler();
	}

}
