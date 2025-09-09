package de.yourplugin.hybrid;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ListBuildingsCommand implements CommandExecutor {

    private final HybridSurvivalConfigPlugin plugin;
    private final BuildingManager buildingManager;

    public ListBuildingsCommand(HybridSurvivalConfigPlugin plugin) {
        this.plugin = plugin;
        this.buildingManager = plugin.getBuildingManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Map<String, BuildingData> map = buildingManager.getAllBuildingsMap();
        if (map.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Es wurden noch keine Gebäude registriert.");
            return true;
        }

        sender.sendMessage(ChatColor.AQUA + "Gebäude-Liste:");
        map.forEach((addr, b) -> {
            String owner = b.getOwner() == null ? "Niemand" : b.getOwner();
            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.WHITE + addr + ChatColor.GRAY + " (Besitzer: " + owner + ")");
        });
        return true;
    }
}