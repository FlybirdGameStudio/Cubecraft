package net.cubecraft.world.block.access;

import net.cubecraft.ContentRegistries;
import net.cubecraft.event.BlockIDChangedEvent;
import net.cubecraft.world.IWorld;
import net.cubecraft.world.block.Block;
import net.cubecraft.world.block.EnumFacing;
import net.cubecraft.world.block.blocks.BlockRegistry;
import net.cubecraft.world.block.property.BlockPropertyDispatcher;
import net.cubecraft.world.chunk.Chunk;
import net.cubecraft.world.chunk.WorldChunk;
import net.cubecraft.world.chunk.pos.ChunkPos;

import java.util.HashMap;

public class ChunkBlockAccess extends IBlockAccess {
    private static final HashMap<String, Block> BLOCK_CACHE = new HashMap<>();
    private final WorldChunk chunk;

    public ChunkBlockAccess(IWorld world, long x, long y, long z, WorldChunk chunk) {
        super(world, x, y, z);
        this.chunk = chunk;


        String blockId = this.getBlockID();
        if (BLOCK_CACHE.containsKey(blockId)) {
            this.block = BLOCK_CACHE.get(blockId);
        } else {
            Block block1 = ContentRegistries.BLOCK.get(blockId);
            if (block1 == null) {
                block1 = BlockRegistry.AIR;
            }
            BLOCK_CACHE.put(blockId, block1);
            this.block = block1;
        }
    }

    @Override
    public String getBlockID() {
        String id = this.world.getDimension().predictBlockID(this.world, this.x, this.y, this.z);
        if (id != null) {
            return id;
        } else {
            ChunkPos pos = ChunkPos.fromWorldPos(this.x, this.z);
            return this.chunk.getBlockID(pos.getRelativePosX(x), (int) y, pos.getRelativePosZ(z));
        }
    }

    @Override
    public void setBlockID(String id, boolean sendUpdateEvent) {
        if (y < 0 || y >= Chunk.HEIGHT) {
            return;
        }
        ChunkPos pos = ChunkPos.fromWorldPos(this.x, this.z);
        if (this.chunk == null) {
            return;
        }
        this.chunk.setBlockID(pos.getRelativePosX(x), (int) y, pos.getRelativePosZ(z), id);
        if (!sendUpdateEvent) {
            return;
        }

        for (IBlockAccess blockAccess : this.world.getBlockNeighbor(this.x, this.y, this.z)) {
            blockAccess.getBlock().onBlockUpdate(blockAccess);
        }
        this.getBlock().onBlockUpdate(this);
        this.world.getEventBus().callEvent(new BlockIDChangedEvent(this.world, this.x, this.y, this.z, getBlockID(), id));

    }

    @Override
    public EnumFacing getBlockFacing() {
        EnumFacing bs = this.world.getDimension().predictBlockFacingAt(this.world, this.x, this.y, this.z);
        if (bs != null) {
            return bs;
        } else {
            ChunkPos pos = ChunkPos.fromWorldPos(this.x, this.z);
            return chunk.getBlockFacing(pos.getRelativePosX(this.x), (int) this.y, pos.getRelativePosZ(z));
        }
    }

    @Override
    public void setBlockFacing(EnumFacing facing, boolean sendUpdateEvent) {
        if (y < 0 || y >= Chunk.HEIGHT) {
            return;
        }
        ChunkPos pos = ChunkPos.fromWorldPos(this.x, this.z);
        if (this.chunk == null) {
            return;
        }
        this.chunk.setBlockFacing(pos.getRelativePosX(x), (int) y, pos.getRelativePosZ(z), facing);
    }

    @Override
    public byte getBlockMeta() {
        Byte m = this.world.getDimension().predictBlockMetaAt(this.world, this.x, this.y, this.z);
        if (m != null) {
            return m;
        } else {
            ChunkPos pos = ChunkPos.fromWorldPos(this.x, this.z);
            return this.chunk.getBlockMeta(pos.getRelativePosX(x), (int) y, pos.getRelativePosZ(z));
        }
    }

    @Override
    public void setBlockMeta(byte meta, boolean sendUpdateEvent) {
        if (y < 0 || y >= Chunk.HEIGHT) {
            return;
        }
        ChunkPos pos = ChunkPos.fromWorldPos(this.x, this.z);
        if (this.chunk == null) {
            return;
        }
        this.chunk.setBlockMeta(pos.getRelativePosX(x), (int) y, pos.getRelativePosZ(z), meta);
    }

    @Override
    public byte getBlockLight() {
        Byte predictedLight = this.world.getDimension().predictLightAt(this.world, this.x, this.y, this.z);
        if (predictedLight != null) {
            return predictedLight;
        } else {
            if (this.y >= 128) {
                return (byte) (BlockPropertyDispatcher.isSolid(this) ? 0 : 127);
            }
            return (byte) (Math.max(128 - (128 - this.y) * 4, 8));

            //ChunkPos pos = ChunkPos.fromWorldPos(this.x, this.z);
            //return this.chunk.getBlockLight(pos.getRelativePosX(this.x), (int) this.y, pos.getRelativePosZ(this.z));
        }
    }

    @Override
    public void setBlockLight(byte light, boolean sendUpdateEvent) {
        if (y < 0 || y >= Chunk.HEIGHT) {
            return;
        }
        ChunkPos pos = ChunkPos.fromWorldPos(this.x, this.z);
        if (this.chunk == null) {
            return;
        }
        this.chunk.setBlockLight(pos.getRelativePosX(x), (int) y, pos.getRelativePosZ(z), light);
    }

    @Override
    public String getBiome() {
        ChunkPos pos = ChunkPos.fromWorldPos(this.x, this.z);
        return this.chunk.getBiome(pos.getRelativePosX(x), (int) this.y, pos.getRelativePosZ(z));
    }

    @Override
    public void setBiome(String biome, boolean sendUpdateEvent) {
        ChunkPos pos = ChunkPos.fromWorldPos(this.x, this.z);
        if (this.chunk == null) {
            return;
        }
        this.chunk.setBiome(pos.getRelativePosX(x), (int) this.y, pos.getRelativePosZ(z), biome);
    }


}
