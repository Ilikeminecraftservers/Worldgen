package de.Yourplugin.hybrid;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListBuildingsCommand implements CommandExecutor {

    private final HybridSurvivalPluginConfig plugin;
    private final BuildingManager buildingManager;

    public ListBuildingsCommand(HybridSurvivalPluginConfig plugin) {
        this.plugin = plugin;
        this.buildingManager = plugin.getBuildingManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (buildingManager.getAllBuildings().isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Es wurden noch keine Gebäude gekauft.");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Alle Gebäude:");
        for (BuildingData building : buildingManager.getAllBuildings()) {
            sender.sendMessage(ChatColor.AQUA + "- " + building.getOwner() +
                    " besitzt ein Gebäude bei: " + building.getLocation().getBlockX() + ", " +
                    building.getLocation().getBlockY() + ", " +
                    building.getLocation().getBlockZ());
        }
        return true;
    }
}
