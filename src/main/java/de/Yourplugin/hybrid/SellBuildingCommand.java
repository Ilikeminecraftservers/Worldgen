package de.yourplugin.hybrid;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SellBuildingCommand implements CommandExecutor {

    private final HybridSurvivalConfigPlugin plugin;
    private final BuildingManager buildingManager;

    public SellBuildingCommand(HybridSurvivalConfigPlugin plugin) {
        this.plugin = plugin;
        this.buildingManager = plugin.getBuildingManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Nur Spieler können Gebäude verkaufen.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.YELLOW + "Benutzung: /sellbuilding <Adresse>");
            return true;
        }

        String address = String.join(" ", args);
        BuildingData building = buildingManager.getBuilding(address);

        if (building == null) {
            player.sendMessage(ChatColor.RED + "Kein Gebäude unter dieser Adresse.");
            return true;
        }

        String owner = building.getOwner();
        if (owner == null || !owner.equalsIgnoreCase(player.getName())) {
            player.sendMessage(ChatColor.RED + "Du besitzt dieses Gebäude nicht.");
            return true;
        }

        buildingManager.removeBuilding(address);
        player.sendMessage(ChatColor.GREEN + "Du hast das Gebäude '" + address + "' verkauft.");
        return true;
    }
}