package net.cubecraft.client.render;

import me.gb2022.quantum3d.texture.Texture;
import me.gb2022.quantum3d.texture.Texture2DTileMap;
import net.cubecraft.util.register.NamedRegistry;
import net.cubecraft.util.register.Registered;

public interface Textures {
    NamedRegistry<Texture> BLOCK_TEXTURES = new NamedRegistry<>();

    Registered<Texture2DTileMap> TERRAIN_SIMPLE = BLOCK_TEXTURES.deferred("cubecraft:block_simple", Texture2DTileMap.class);
    Registered<Texture2DTileMap> TERRAIN_CUTOUT = BLOCK_TEXTURES.deferred("cubecraft:block_cutout", Texture2DTileMap.class);
    Registered<Texture2DTileMap> TERRAIN_TRANSPARENT = BLOCK_TEXTURES.deferred("cubecraft:block_transparent", Texture2DTileMap.class);

}
