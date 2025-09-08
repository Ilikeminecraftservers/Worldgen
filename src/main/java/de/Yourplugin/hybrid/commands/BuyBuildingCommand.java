package de.yourplugin.hybrid.commands;

import de.yourplugin.hybrid.BuildingData;
import de.yourplugin.hybrid.HybridSurvivalConfigPlugin;
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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Nur Spieler können Gebäude kaufen.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.YELLOW + "Verwendung: /buybuilding <Adresse>");
            return true;
        }

        String address = String.join(" ", args);
        BuildingData building = plugin.getBuilding(address);

        if (building == null) {
            player.sendMessage(ChatColor.RED + "Kein Gebäude mit Adresse " + address + " gefunden.");
            return true;
        }
        if (building.isPurchased()) {
            player.sendMessage(ChatColor.RED + "Dieses Gebäude gehört bereits jemandem.");
            return true;
        }

        building.setOwner(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "Du hast " + building.getAddress() + " gekauft!");
        return true;
    }
}