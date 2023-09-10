package ink.flybird.cubecraft.client.internal.renderer.block;

import ink.flybird.cubecraft.client.ClientRenderContext;
import ink.flybird.cubecraft.client.render.renderer.IBlockRenderer;
import ink.flybird.cubecraft.client.resources.resource.ImageResource;
import ink.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.IBlockAccess;
import ink.flybird.quantum3d.draw.VertexBuilder;

import java.util.Set;

public final class ModelRenderer implements IBlockRenderer {
    private final String model;

    public ModelRenderer(String model) {
        this.model = model;
    }

    public ModelRenderer(ResourceLocation loc) {
        this.model = loc.format();
    }

    public String getModel() {
        return this.model;
    }

    @Override
    public void renderBlock(IBlockAccess blockAccess, String layer, IWorld world, double renderX, double renderY, double renderZ, VertexBuilder builder) {
        ClientRenderContext.BLOCK_MODEL.get(model).render(blockAccess, builder, layer, world, renderX, renderY, renderZ);
    }

    @Override
    public void initializeRenderer(Set<ImageResource> textureList) {
        ClientRenderContext.BLOCK_MODEL.load(this.model);
        ClientRenderContext.BLOCK_MODEL.get(this.model).initializeModel(textureList);
    }
}