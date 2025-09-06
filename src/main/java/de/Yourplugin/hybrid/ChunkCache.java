package de.yourplugin.hybrid;

import java.util.concurrent.ConcurrentHashMap;

public class ChunkCache {
    private final ConcurrentHashMap<String, byte[]> cache = new ConcurrentHashMap<>();
    public boolean contains(int cx,int cz){ return cache.containsKey(cx+"_"+cz); }
    public void put(int cx,int cz, byte[] data){ cache.put(cx+"_"+cz, data); }
    public byte[] get(int cx,int cz){ return cache.get(cx+"_"+cz); }
}
