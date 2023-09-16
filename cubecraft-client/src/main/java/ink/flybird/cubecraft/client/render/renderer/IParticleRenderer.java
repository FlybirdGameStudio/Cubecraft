package ink.flybird.cubecraft.client.render.renderer;

import ink.flybird.quantum3d_legacy.draw.VertexBuilder;
import ink.flybird.cubecraft.world.entity.EntityParticle;

public interface IParticleRenderer<T extends EntityParticle> {
    void render(T particle, VertexBuilder builder, double a, double xa, double ya, double za, double xa2, double za2);
}