package com.gmail.xendroidzx.cah;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.gmail.xendroidzx.cah.utils.Utils;

public class GameHandler {

	public HashMap<String, Integer> scores = new HashMap<String, Integer>(), answers = new HashMap<String, Integer>();
	private HashMap<String, WhiteCard[]> deck = new HashMap<String, WhiteCard[]>();
	public HashMap<String, ItemStack> submittedCard = new HashMap<String, ItemStack>();

	private CAH plugin = CAH.getInstance();
	public boolean isPlaying = false, canJoin = (plugin.players.size() < 8 && plugin.players.size() >= 3);

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
					if(plugin.players.size() >= 3) {
						startFutureGameTimer();
					}
					if(plugin.players.size() == 8) {
						start();
						shutdown();
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

	private ScheduledExecutorService serv = null;
	private ScheduledFuture<?> s = null;

	private void startFutureGameTimer() {
		serv = Executors.newScheduledThreadPool(1);
		s = serv.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if(plugin.players.size() >= 3) {
					if(time == 120) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c2 minutes§6!");
					}
					if(time == 60) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c1 minute§6!");
					}
					if(time == 30) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c30 seconds§6!");
					}
					if(time == 15) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c15 seconds§6!");
					}
					if(time <= 10 && time > 1) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c" + time + " seconds§6!");
					}
					if(time == 1) {
						Bukkit.broadcastMessage("§6New CAH game starting §cnow!");
						start();
						shutdown();
					}
					time--;
				} else {
					time = 120;
					shutdown();
				}
			}
		}, 0L, 1L, TimeUnit.SECONDS);
	}

	private void shutdown() {
		serv.shutdown();
		s.cancel(true);
	}

	/*private int startGameTimer() {
		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
			@Override
			public void run() {
				if(plugin.players.size() >= 3) {
					if(time == 120) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c2 minutes§6!");
					}
					if(time == 60) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c1 minute§6!");
					}
					if(time == 30) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c30 seconds§6!");
					}
					if(time == 15) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c15 seconds§6!");
					}
					if(time <= 10 && time > 1) {
						Bukkit.broadcastMessage("§6New CAH game starting in §c" + time + " seconds§6!");
					}
					if(time == 1) {
						Bukkit.broadcastMessage("§6New CAH game starting §cnow!");
						Bukkit.getScheduler().cancelTask(getTaskId());
						start();
					}
					time--;
				} else {
					time = 120;
					this.cancel();
				}
			}
		}, 0L, 20L);

		return id;
	}*/	

	private void chooseStartingCzar() {
		int i = new Random().nextInt(plugin.players.size());
		String p = plugin.players.get(i);
		plugin.czar = p;
	}

	private void chooseCards(Player p) {
		if(canJoin) {
			for(int i = 0; i < plugin.players.size(); i++) {
				white1 = plugin.whites.get(new Random().nextInt(plugin.whites.size()));
				white2 = plugin.whites.get(new Random().nextInt(plugin.whites.size()));
				white3 = plugin.whites.get(new Random().nextInt(plugin.whites.size()));
				white4 = plugin.whites.get(new Random().nextInt(plugin.whites.size()));
			}
			deck.put(p.getName(), new WhiteCard[] {white1, white2, white3, white4});
		}
	}

	private void dealCards(Player p, boolean isCzar) {
		black = plugin.blacks.get(new Random().nextInt(plugin.blacks.size()));

		PlayerInventory pi = p.getInventory();
		ItemStack b = Utils.setName(new ItemStack(Material.WOOL, 1, (byte) 15), "§8" + black.getText());
		pi.setItem(8, b);
		if(!isCzar) {
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
		Player p = plugin.getCzar();
		PlayerInventory pi = p.getInventory();
		ItemStack white = Utils.setLore(submittedCard.get(fromPlayer.getName()), "§b" + fromPlayer.getName());
		pi.addItem(white);
		submittedCard.remove(fromPlayer.getName());
		answers.put(p.getName(), answers.get(p.getName()) + 1);
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
			p.teleport(plugin.pLoc.get(p.getName()));
			removePlayer(p);
		}
		plugin.pLoc.clear();
		plugin.players.clear();
		plugin.armor.clear();
		plugin.czar = null;
		plugin.inv.clear();

		plugin.gh = new GameHandler();
	}

}
