package de.yourplugin.hybrid.commands;

import de.yourplugin.hyybrid.BuildingData;
import de.yourplugin.hybrid.HybridSurvivalConfigPlugin;
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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Nur Spieler können Gebäude verkaufen.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.YELLOW + "Verwendung: /sellbuilding <Adresse>");
            return true;
        }

        String address = String.join(" ", args);
        BuildingData building = plugin.getBuilding(address);

        if (building == null) {
            player.sendMessage(ChatColor.RED + "Kein Gebäude mit Adresse " + address + " gefunden.");
            return true;
        }
        if (!building.isOwner(player)) {
            player.sendMessage(ChatColor.RED + "Du bist nicht der Besitzer dieses Gebäudes.");
            return true;
        }

        building.setOwner(null);
        player.sendMessage(ChatColor.GREEN + "Du hast " + building.getAddress() + " verkauft!");
        return true;
    }
}