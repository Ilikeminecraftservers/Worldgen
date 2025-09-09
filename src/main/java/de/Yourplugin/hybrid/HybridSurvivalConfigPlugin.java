package de.yourplugin.hybrid;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.*;

public class HybridSurvivalConfigPlugin extends JavaPlugin implements Listener {

    // ==================== Config Flags ====================
    private boolean progressiveDetail, chunkPreload, lazyLight, rpcThreading, pasteThreading;
    private boolean generateBuildings, buildingPurchase, signPurchase, osmInteriors, shopSystem, synchronizeHeight;
    private boolean teleportToBuilding, commandPurchase, buildingSell, guiBuildingList;
    private boolean asyncEvents, telemetry, chunkCacheEnabled, prefetchSpawn;

    private boolean streetsEnabled, sidewalksEnabled, parkingsEnabled;
    private boolean decoLampsEnabled, decoTreesEnabled, decoBenchesEnabled;

    private int maxChunksPerTick, lightsPerTick, preloadRadius;
    private String arnisPath;
    private int bboxRadius;
    private Material streetBlock, lineBlock, sidewalkBlock, parkingBlock, parkingLineBlock;

    private final ChunkCache chunkCache = new ChunkCache();
    private final Queue<ChunkPosition> lightQueue = new ConcurrentLinkedQueue<>();
    private ExecutorService rpcPool;
    private ExecutorService pastePool;

    private StreetGenerator streetGen;
    private DecorationManager decoMgr;
    private BuildingManager buildingManager;  // Zentrale Verwaltung aller Gebäude

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration cfg = getConfig();

        // Kern-Features
        progressiveDetail = cfg.getBoolean("progressive-detail", true);
        chunkPreload = cfg.getBoolean("chunk-preload", true);
        lazyLight = cfg.getBoolean("lazy-light", true);
        rpcThreading = cfg.getBoolean("rpc-threading", true);
        pasteThreading = cfg.getBoolean("paste-threading", true);

        // Gebäude & Immobilien
        generateBuildings = cfg.getBoolean("generate-buildings", true);
        buildingPurchase = cfg.getBoolean("building-purchase", true);
        signPurchase = cfg.getBoolean("sign-purchase", true);
        osmInteriors = cfg.getBoolean("osm-interiors", false);
        shopSystem = cfg.getBoolean("shop-system", false);
        synchronizeHeight = cfg.getBoolean("synchronize-height", true);
        teleportToBuilding = cfg.getBoolean("teleport-to-building", true);
        commandPurchase = cfg.getBoolean("command-purchase", true);
        buildingSell = cfg.getBoolean("building-sell", true);
        guiBuildingList = cfg.getBoolean("gui-building-list", true);

        // Performance
        maxChunksPerTick = cfg.getInt("max-chunks-per-tick", 16);
        lightsPerTick = cfg.getInt("lights-per-tick", 2);
        preloadRadius = cfg.getInt("preload-radius", 16);
        asyncEvents = cfg.getBoolean("async-events", true);
        telemetry = cfg.getBoolean("telemetry", true);
        chunkCacheEnabled = cfg.getBoolean("chunk-cache", true);
        prefetchSpawn = cfg.getBoolean("prefetch-spawn", true);

        // Arnis-Integration
        arnisPath = cfg.getString("arnis.path", "/usr/local/bin/arnis");
        bboxRadius = cfg.getInt("arnis.bbox-radius-meters", 100);

        // Straßen & Parkplätze
        streetsEnabled = cfg.getBoolean("streets.enabled", true);
        streetBlock = Material.matchMaterial(cfg.getString("streets.block", "BLUE_ICE"));
        lineBlock = Material.matchMaterial(cfg.getString("streets.line-block", "ICE"));
        sidewalksEnabled = cfg.getBoolean("streets.sidewalks.enabled", false);
        sidewalkBlock = Material.matchMaterial(cfg.getString("streets.sidewalks.block", "STONE_BRICKS"));

        parkingsEnabled = cfg.getBoolean("parkings.enabled", true);
        parkingBlock = Material.matchMaterial(cfg.getString("parkings.block", "PACKED_ICE"));
        parkingLineBlock = Material.matchMaterial(cfg.getString("parkings.line-block", "ICE"));

        // Dekorationen
        decoLampsEnabled = cfg.getBoolean("decorations.street-lamps.enabled", false);
        decoTreesEnabled = cfg.getBoolean("decorations.trees.enabled", false);
        decoBenchesEnabled = cfg.getBoolean("decorations.benches.enabled", false);

        // Manager
        streetGen = new StreetGenerator(cfg);
        decoMgr = new DecorationManager();
        buildingManager = new BuildingManager();

        // Thread-Pools
        if (rpcThreading) rpcPool = Executors.newFixedThreadPool(8);
        if (pasteThreading) pastePool = Executors.newFixedThreadPool(4);

        // Listener
        Bukkit.getPluginManager().registerEvents(this, this);

        // Lazy light processing
        if (lazyLight) {
            Bukkit.getScheduler().runTaskTimer(this, this::processLazyLight, 1L, 1L);
        }

        // Commands
        getCommand("buybuilding").setExecutor(new BuyBuildingCommand(this));
        getCommand("sellbuilding").setExecutor(new SellBuildingCommand(this));
        getCommand("listbuildings").setExecutor(new ListBuildingsCommand(this));
        getCommand("tpbuilding").setExecutor(new TpBuildingCommand(this));

        getLogger().info("HybridSurvivalConfigPlugin enabled with Arnis at: " + arnisPath);
    }

    @Override
    public void onDisable() {
        if (rpcPool != null) rpcPool.shutdown();
        if (pastePool != null) pastePool.shutdown();
    }

    // Getter für BuildingManager
    public BuildingManager getBuildingManager() {
        return buildingManager;
    }

    // ==================== Lazy Light ====================
    private void processLazyLight() {
        for (int i = 0; i < lightsPerTick; i++) {
            ChunkPosition pos = lightQueue.poll();
            if (pos == null) break;
            // Hier könnte z. B. eine Lichtquelle gesetzt oder Lighting-API getriggert werden
        }
    }

    // ==================== Gebäude ====================
    private void generateBuilding(int cx, int cz, World world) {
        if (!generateBuildings) return;
        Location buildingLoc = new Location(world, cx * 16 + 8, 64, cz * 16 + 8);
        String street = "Street" + cx + "_" + cz;
        int number = new Random().nextInt(100) + 1;

        BuildingData b = new BuildingData(buildingLoc, street, number);
        buildingManager.addBuilding(b.getAddress(), buildingLoc, null, 0.0);

        if (signPurchase) placeAddressSign(b, world);
    }

    private void placeAddressSign(BuildingData b, World world) {
        Location signLoc = b.getLocation().clone().add(0, 1, 1);
        world.getBlockAt(signLoc).setType(Material.OAK_SIGN);
        Sign sign = (Sign) world.getBlockAt(signLoc).getState();
        sign.setLine(0, "Adresse:");
        sign.setLine(1, b.getAddress());
        sign.setLine(2, "Zu verkaufen");
        sign.update();
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) {
        if (!signPurchase) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = e.getClickedBlock();
        if (block == null || block.getType() != Material.OAK_SIGN) return;
        Sign sign = (Sign) block.getState();
        String line1 = sign.getLine(1);

        BuildingData building = buildingManager.getBuilding(line1);
        if (building == null || building.isPurchased()) return;

        // FIX: UUID zu String
        building.setOwner(e.getPlayer().getUniqueId().toString());
        sign.setLine(2, "Besitzer:");
        sign.setLine(3, e.getPlayer().getName());
        sign.update();
    }
}