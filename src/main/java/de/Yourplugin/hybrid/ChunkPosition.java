package de.yourplugin.hybrid;

import org.bukkit.World;

public class ChunkPosition {
    public int chunkX, chunkZ;
    public World world;
    public ChunkPosition(int x,int z,World w){ chunkX=x; chunkZ=z; world=w; }
}
