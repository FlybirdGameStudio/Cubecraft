package net.cubecraft.client.render.chunk.compile;

import me.gb2022.commons.memory.BlockedBufferAllocator;
import me.gb2022.quantum3d.render.vertex.DrawMode;
import me.gb2022.quantum3d.render.vertex.VertexBuilder;
import me.gb2022.quantum3d.render.vertex.VertexFormat;
import net.cubecraft.client.render.block.IBlockRenderer;
import net.cubecraft.client.render.chunk.TerrainRenderer;
import net.cubecraft.client.render.chunk.container.ChunkLayerContainers;
import net.cubecraft.world.BlockAccessor;
import net.cubecraft.world.World;
import net.cubecraft.world.block.blocks.Blocks;
import net.cubecraft.world.dump.ChunkCompileRegion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import oshi.annotation.concurrent.NotThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@NotThreadSafe
public final class ModernChunkCompiler {
    private final Map<Thread, VertexBuilder[]> builderCache = new HashMap<>();
    private final TerrainRenderer renderer;
    private final Logger logger;


    public ModernChunkCompiler(TerrainRenderer renderer) {
        this.renderer = renderer;
        this.logger = LogManager.getLogger("ChunkCompiler#" + renderer.hashCode());
    }

    private void updateBuffers_retry() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        updateBuffers();
    }

    public synchronized void updateBuffers() {
        var allocator = ((BlockedBufferAllocator) this.renderer.getMemoryAllocator());

        if (allocator.getAllocatableBlocks() < 128 || !allocator.testPreAllocate(8192 * 13)) {
            updateBuffers_retry();
            return;
        }

        var thread = Thread.currentThread();
        var fmt = VertexFormat.V3F_C4F_T2F;
        var mode = DrawMode.QUADS;
        var stack = new VertexBuilder[7];

        for (var i = 0; i < 7; i++) {
            stack[i] = this.renderer.getVertexBuilderAllocator().allocate(fmt, mode, 8192);
        }

        this.builderCache.put(thread, stack);
    }

    public VertexBuilder[] getBuffersForThread() {
        var thread = Thread.currentThread();

        if (!this.builderCache.containsKey(thread)) {
            updateBuffers();
        }

        return this.builderCache.get(thread);
    }

    public void build(ChunkCompileRegion region, Queue<ChunkCompileResult> callback, World world, ChunkCompileRequest request) {
        var x = request.getX();
        var y = request.getY();
        var z = request.getZ();
        var layers = request.getLayers();

        try {
            if (region.dump(world, x, y, z)) {
                callback.add(ChunkCompileResult.failed(x, y, z, layers));
                return;
            }
        } catch (Throwable t) {
            this.logger.warn("failed to read compile data at chunk [{},{},{}]", x, y, z);
            this.logger.throwing(t);
            callback.add(ChunkCompileResult.failed(x, y, z, layers));
            return;
        }

        var result = ChunkCompileResult.success(x, y, z, layers);

        for (int i = 0; i < layers.length; i++) {
            var stack = getBuffersForThread();

            if (buildSection(region, layers[i], x, y, z, stack)) {
                result.setLayerComplete(i, stack);
                updateBuffers();
            } else {
                result.setLayerFailed(i);
            }
        }

        callback.add(result);
    }

    public boolean buildSection(BlockAccessor accessor, int layerId, int x, int y, int z, VertexBuilder[] builders) {
        var layer = ChunkLayerContainers.REGISTRY.registered(layerId);

        for (var n = 0; n < 4096; n++) {
            var cx = n & 15;
            var cy = n >> 4 & 15; //=%15
            var cz = n >> 8 & 15; //=%255

            var wx = ((long) x << 4) + cx;
            var wy = ((long) y << 4) + cy;
            var wz = ((long) z << 4) + cz;

            var rx = (wx + 32768) % 65535 - 32768;
            var rz = (wz + 32768) % 65535 - 32768;

            var block = accessor.getBlockAccess(wx, wy, wz);
            var id = block.getBlockId();
            if (id == Blocks.AIR.getId()) {
                continue;
            }

            var renderer = IBlockRenderer.REGISTRY.get(id);

            if (renderer == null) {
                continue;
            }

            for (int face = 0; face < 7; face++) {
                renderer.render(block, accessor, layer, face, rx, wy, rz, builders[face]);
            }
        }

        for (var b : builders) {
            if (b.getVertexCount() > 0) {
                return true;
            }
        }

        return false;
    }
}
