package com.gmail.xendroidzx.cah;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.xendroidzx.cah.commands.CaHCommand;

public class CAH extends JavaPlugin {

	private static CAH clazz;

	public String czar;

	public List<String> players;

	private List<WhiteCard> whites;
	private List<BlackCard> blacks;

	public GameHandler gh;

	public void onEnable() {
		clazz = this;

		gh = new GameHandler();

		saveResource("white.txt", false);
		saveResource("black.txt", false);
		saveDefaultConfig();

		load();

		getCommand("cah").setExecutor(new CaHCommand());
	}

	public static CAH getInstance() {
		return clazz;
	}

	private void load() {
		whites = Collections.synchronizedList(new ArrayList<WhiteCard>());
		blacks = new ArrayList<BlackCard>();

		File white = new File(getDataFolder(), "white.txt"), black = new File(getDataFolder(), "black.txt");

		Scanner scanner;
		String line;

		try {
			scanner = new Scanner(white);
			while((line = scanner.nextLine()) != null) {
				whites.add(new WhiteCard(line));
			}
			scanner.close();

			scanner = new Scanner(black);
			while((line = scanner.nextLine()) != null) {
				blacks.add(new BlackCard(line));
			}
			scanner.close();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}

	public Player getCzar() {
		return Bukkit.getPlayerExact(czar);
	}

}
