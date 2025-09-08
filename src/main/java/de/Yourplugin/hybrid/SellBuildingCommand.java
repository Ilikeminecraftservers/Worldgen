package de.Yourplugin.hybrid;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SellBuildingCommand implements CommandExecutor {

    private final HybridSurvivalPluginConfig plugin;
    private final BuildingManager buildingManager;

    public SellBuildingCommand(HybridSurvivalPluginConfig plugin) {
        this.plugin = plugin;
        this.buildingManager = plugin.getBuildingManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können Gebäude verkaufen.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Benutzung: /sellbuilding <Adresse>");
            return true;
        }

        String address = args[0];
        BuildingData building = buildingManager.getBuilding(address);

        if (building == null || !player.getName().equalsIgnoreCase(building.getOwner())) {
            player.sendMessage(ChatColor.RED + "Dir gehört dieses Gebäude nicht!");
            return true;
        }

        buildingManager.removeBuilding(address);
        player.sendMessage(ChatColor.GREEN + "Du hast dein Gebäude bei '" + address + "' verkauft!");
        return true;
    }
}
