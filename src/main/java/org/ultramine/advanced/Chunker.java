package org.ultramine.advanced;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class Chunker {
    private Chunker() { }
    public static void force(WorldServer w) {
        Chunk c = w.getChunkFromChunkCoords(1, 1);
        c.setActive();
    }
    public static void load() {
        for (WorldServer w : FMLCommonHandler.instance().getMinecraftServerInstance().worldServers) force(w);
    }
    public static void test() {
        load();
    }
}
