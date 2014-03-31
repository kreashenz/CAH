package com.gmail.xendroidzx.cah;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.xendroidzx.cah.commands.CAHCommand;
import com.gmail.xendroidzx.cah.events.evt_BlockBreak;
import com.gmail.xendroidzx.cah.events.evt_BlockPlace;
import com.gmail.xendroidzx.cah.events.evt_PlayerDropItem;
import com.gmail.xendroidzx.cah.events.evt_PlayerInteract;

public class CAH extends JavaPlugin {

	private static CAH clazz;
	private CAHCommand cmd;

	public String czar;

	public List<String> players;
	public List<WhiteCard> whites;
	public List<BlackCard> blacks;

	public HashMap<String, ItemStack[]> armor = new HashMap<String, ItemStack[]>();
	public HashMap<String, ItemStack[]> inv = new HashMap<String, ItemStack[]>();

	public GameHandler gh;
	public LocHandler loc;

	@Override
	public void onEnable() {
		clazz = this;
		cmd = new CAHCommand();
		loc = new LocHandler();

		saveResource("white.txt", false);
		saveResource("black.txt", false);
		saveDefaultConfig();

		load();
		players = new ArrayList<String>();

		getCommand("cah").setExecutor(cmd);
		getCommand("czar").setExecutor(cmd);

		gh = new GameHandler();

		getServer().getPluginManager().registerEvents(new evt_PlayerDropItem(), this);
		getServer().getPluginManager().registerEvents(new evt_PlayerInteract(), this);
		getServer().getPluginManager().registerEvents(new evt_BlockPlace(), this);
		getServer().getPluginManager().registerEvents(new evt_BlockBreak(), this);
	}

	public static CAH getInstance() {
		return clazz;
	}

	private void load() {
		whites = Collections.synchronizedList(new ArrayList<WhiteCard>());
		blacks = new ArrayList<BlackCard>();

		File white = new File(getDataFolder(), "white.txt"), black = new File(getDataFolder(), "black.txt");

		BufferedReader reader;

		String line;

		try {
			reader = new BufferedReader(new FileReader(white));
			while((line = reader.readLine()) != null) {
				whites.add(new WhiteCard(line));
			}
			reader.close();

			reader = new BufferedReader(new FileReader(black));
			while((line = reader.readLine()) != null) {
				blacks.add(new BlackCard(line));
			}
			reader.close();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}

	public Player getCzar() {
		return Bukkit.getPlayerExact(czar);
	}

}
