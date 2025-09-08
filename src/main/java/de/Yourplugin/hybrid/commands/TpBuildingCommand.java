package de.yourplugin.hybrid.commands;

import de.yourplugin.hybrid.BuildingData;
import de.yourplugin.hybrid.HybridSurvivalConfigPlugin;
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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Nur Spieler können sich teleportieren.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.YELLOW + "Verwendung: /tpbuilding <Adresse>");
            return true;
        }

        String address = String.join(" ", args);
        BuildingData building = plugin.getBuilding(address);

        if (building == null) {
            player.sendMessage(ChatColor.RED + "Kein Gebäude mit Adresse " + address + " gefunden.");
            return true;
        }

        Location loc = building.getLocation();
        player.teleport(loc);
        player.sendMessage(ChatColor.GREEN + "Teleportiert zu " + building.getAddress());
        return true;
    }
}