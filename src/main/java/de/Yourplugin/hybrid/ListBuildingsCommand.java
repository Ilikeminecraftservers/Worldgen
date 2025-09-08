package de.yourplugin.hybrid;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListBuildingsCommand implements CommandExecutor {

    private final HybridSurvivalConfigPlugin plugin;

    public ListBuildingsCommand(HybridSurvivalConfigPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (plugin.getAllBuildings().isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Es wurden noch keine Gebäude registriert.");
            return true;
        }

        sender.sendMessage(ChatColor.AQUA + "Liste der Gebäude:");
        plugin.getAllBuildings().forEach((address, building) -> {
            String owner = building.getOwner() != null ? building.getOwner() : "Niemand";
            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.WHITE + address + ChatColor.GRAY + " (Besitzer: " + owner + ")");
        });
        return true;
    }
}
