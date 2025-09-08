package de.yourplugin.hybrid;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SellBuildingCommand implements CommandExecutor {

    private final HybridSurvivalConfigPlugin plugin;

    public SellBuildingCommand(HybridSurvivalConfigPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Nur Spieler können Gebäude verkaufen.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.YELLOW + "Benutzung: /sellbuilding <Adresse>");
            return true;
        }

        Player player = (Player) sender;
        String address = String.join(" ", args);

        BuildingData building = plugin.getBuilding(address);
        if (building == null || !player.getName().equalsIgnoreCase(building.getOwner())) {
            player.sendMessage(ChatColor.RED + "Du besitzt dieses Gebäude nicht.");
            return true;
        }

        plugin.getAllBuildings().remove(address);
        player.sendMessage(ChatColor.GREEN + "Du hast das Gebäude " + address + " verkauft!");
        return true;
    }
}
