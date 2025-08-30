package me.simplicitee.project.addons;

import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ProjectCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ProjectAddons.instance.version());
			return true;
		} else if (args.length == 1 && sender instanceof Player) {
			if (args[0].equalsIgnoreCase("active")) {
				for (CoreAbility ability : CoreAbility.getAbilitiesByInstances()) {
					if (ability.getPlayer().getUniqueId() == ((Player) sender).getUniqueId()) {
						sender.sendMessage(ability.getElement().getColor() + ability.getName() + ChatColor.WHITE + " : " + (System.currentTimeMillis() - ability.getStartTime()));
					}
				}
			}
			return true;
		}
		
		return false;
	}

}
