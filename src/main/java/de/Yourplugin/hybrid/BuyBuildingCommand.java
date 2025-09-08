package de.yourplugin.hybrid;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuyBuildingCommand implements CommandExecutor {

    private final HybridSurvivalConfigPlugin plugin;

    public BuyBuildingCommand(HybridSurvivalConfigPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Nur Spieler können Gebäude kaufen.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.YELLOW + "Benutzung: /buybuilding <Adresse>");
            return true;
        }

        Player player = (Player) sender;
        String address = String.join(" ", args);

        BuildingData existing = plugin.getBuilding(address);
        if (existing != null && existing.getOwner() != null) {
            player.sendMessage(ChatColor.RED + "Dieses Gebäude gehört bereits " + existing.getOwner() + ".");
            return true;
        }

        BuildingData building = new BuildingData(address, player.getName(), 1000.0);
        plugin.addBuilding(building);

        player.sendMessage(ChatColor.GREEN + "Du hast das Gebäude " + address + " für 1000$ gekauft!");
        return true;
    }
}
