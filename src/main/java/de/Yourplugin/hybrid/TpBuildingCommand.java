package de.Yourplugin.hybrid;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpBuildingCommand implements CommandExecutor {

    private final HybridSurvivalPluginConfig plugin;
    private final BuildingManager buildingManager;

    public TpBuildingCommand(HybridSurvivalPluginConfig plugin) {
        this.plugin = plugin;
        this.buildingManager = plugin.getBuildingManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können sich teleportieren.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Benutzung: /tpbuilding <Adresse>");
            return true;
        }

        String address = args[0];
        BuildingData building = buildingManager.getBuilding(address);

        if (building == null) {
            player.sendMessage(ChatColor.RED + "Kein Gebäude mit dieser Adresse gefunden!");
            return true;
        }

        Location loc = building.getLocation();
        player.teleport(loc);
        player.sendMessage(ChatColor.GREEN + "Du wurdest zum Gebäude bei '" + address + "' teleportiert.");
        return true;
    }
}
