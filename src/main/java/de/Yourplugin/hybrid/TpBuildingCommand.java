package de.yourplugin.hybrid;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpBuildingCommand implements CommandExecutor {

    private final HybridSurvivalConfigPlugin plugin;

    public TpBuildingCommand(HybridSurvivalConfigPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Nur Spieler können Gebäude teleportieren.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.YELLOW + "Benutzung: /tpbuilding <Adresse>");
            return true;
        }

        Player player = (Player) sender;
        String address = String.join(" ", args);

        BuildingData building = plugin.getBuilding(address);
        if (building == null) {
            player.sendMessage(ChatColor.RED + "Kein Gebäude mit dieser Adresse gefunden.");
            return true;
        }

        Location loc = building.getLocation();
        if (loc == null) {
            player.sendMessage(ChatColor.RED + "Für dieses Gebäude wurde noch kein Standort gesetzt.");
            return true;
        }

        player.teleport(loc);
        player.sendMessage(ChatColor.GREEN + "Teleportiert zu " + address + ".");
        return true;
    }
}
