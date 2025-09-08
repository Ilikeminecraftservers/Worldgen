package de.yourplugin.hybrid.commands;

import de.yourplugin.hybrid.BuildingData;
import de.yourplugin.hybrid.HybridSurvivalConfigPlugin;
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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "Liste aller Gebäude:");
        for (BuildingData b : plugin.getBuildings().values()) {
            String status = b.isPurchased() ? ChatColor.RED + " (Besetzt)" : ChatColor.GREEN + " (Frei)";
            sender.sendMessage(ChatColor.AQUA + b.getAddress() + status);
        }
        return true;
    }
}