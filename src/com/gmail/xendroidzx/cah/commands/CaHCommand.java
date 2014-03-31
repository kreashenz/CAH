package com.gmail.xendroidzx.cah.commands;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.xendroidzx.cah.CAH;

public class CAHCommand implements CommandExecutor {

	private CAH plugin = CAH.getInstance();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(s instanceof Player) {
			Player p = (Player)s;
			if(cmd.getName().equalsIgnoreCase("cah")) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("help")) {
						if(p.hasPermission("cah.help")) {
							sendHelp(p);
						} else noPerm(p);
					}
					else if(args[0].equalsIgnoreCase("join")) {
						if(p.hasPermission("cah.join")) {
							savePlayerStuff(p);
							plugin.gh.addPlayer(p);
						} else noPerm(p);
					}
					else if(args[0].equalsIgnoreCase("leave")) {
						if(p.hasPermission("cah.leave")) {
							loadPlayerStuff(p);
							plugin.gh.removePlayer(p);
						} else noPerm(p);
					}
					else if(args[0].equalsIgnoreCase("players")) {
						if(p.hasPermission("cah.players")) {
							StringBuilder sb = new StringBuilder();
							for(String f : plugin.players) {
								if(sb.length() > 0) {
									sb.append("§7, ");
								}
								sb.append("§9" + f);
							}
							p.sendMessage("§6Players playing: " + sb.toString() + " §7(§6" + plugin.players.size() + "§7/§c9§7)");
						} else noPerm(p);
					}
					else if(args[0].equalsIgnoreCase("set")) {
						if(p.hasPermission("cah.set")) {
							if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
								plugin.loc.saveMapLocation(p.getLocation());
								p.sendMessage("§6Successfully set map spawn location");
							} else p.sendMessage("§cYou can't set the spawn position in mid-air!");
						} else noPerm(p);
					} else p.sendMessage("§cUnknown subcommand. Use §f/cah help §cto get help!");
				} else sendHelp(p);
			}
			if(cmd.getName().equalsIgnoreCase("czar")) {
				if(p.hasPermission("cah.czar")) {
					p.sendMessage("§6The Czar is§7: §c" + (plugin.czar == null ? "no one" : plugin.czar));
				}
			}
		}
		return true;
	}

	private void sendHelp(Player p) {
		p.sendMessage("§6§o====== §r§cCards Against Humanity §6§o======");
		p.sendMessage("§c/cah help §7- §9Shows this page");
		p.sendMessage("§c/cah join §7- §9Join the CAH game");
		p.sendMessage("§c/cah leave §7- §9Leave the CAH game");
		p.sendMessage("§c/cah players §7- §9Show all players who are playing");
		p.sendMessage("§6/cah set §7- §9Set the spawn area");
		p.sendMessage("§c/czar §7- §9Show who the Czar is");
	}

	private void noPerm(CommandSender s) {
		s.sendMessage("§cYou don't have permission to do that");
	}

	private void savePlayerStuff(Player p) {
		plugin.inv.put(p.getName(), p.getInventory().getContents());
		plugin.armor.put(p.getName(), p.getInventory().getArmorContents());
		plugin.loc.savePlayerLocation(p, p.getLocation());

		p.getInventory().clear();
	}

	private void loadPlayerStuff(Player p) {
		p.teleport(plugin.loc.getPlayerLocation(p));
		p.getInventory().setArmorContents(plugin.armor.get(p.getName()));
		p.getInventory().setContents(plugin.inv.get(p.getName()));

		plugin.inv.remove(p.getName());
		plugin.armor.remove(p.getName());
		plugin.loc.removePlayerLocation(p);
	}

}
