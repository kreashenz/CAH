package com.gmail.xendroidzx.cah.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.xendroidzx.cah.CAH;

public class CaHCommand implements CommandExecutor {

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
					} else if(args[0].equalsIgnoreCase("join")) {
						if(p.hasPermission("cah.join")) {

						} else noPerm(p);
					} else if(args[0].equalsIgnoreCase("leave")) {
						if(p.hasPermission("cah.leave")) {

						} else noPerm(p);
					} else tell(p, "§cUnknown subcommand. Use §f/cah help §cto get help!");
				} else sendHelp(p);
			}
			if(cmd.getName().equalsIgnoreCase("czar")) {
				if(p.hasPermission("cah.czar")) {
					tell(p, "§6The Czar is§7: §c" + plugin.getCzar().getName());
				}
			}
		}
		return true;
	}

	private void sendHelp(Player p) {
		tell(p, "§6§o====== §r§cCards Against Humanity §6§o======");
		tell(p, "§c/cah help §7- §9Shows this page");
		tell(p, "§c/cah join §7- §9Join the CAH game");
		tell(p, "§c/cah leave §7- §9Leave the CAH game");
		tell(p, "§c/cah players §7- §9Show all players who are playing");
		tell(p, "§c/czar §7- §9Show who the Czar is");
	}

	private void tell(CommandSender s, String msg) {
		s.sendMessage(msg);
	}

	private void noPerm(CommandSender s) {
		tell(s, "§cYou don't have permission to do that");
	}

}
