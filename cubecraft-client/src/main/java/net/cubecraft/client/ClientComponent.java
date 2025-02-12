package net.cubecraft.client;

import me.gb2022.quantum3d.device.DeviceContext;
import me.gb2022.quantum3d.device.Window;
import net.cubecraft.client.gui.base.DisplayScreenInfo;
import net.cubecraft.world.WorldContext;

public abstract class ClientComponent {
    protected CubecraftClient client;

    public ClientComponent() {
        this.client = CubecraftClient.getInstance();
    }

    public final void init(CubecraftClient client) {
        this.client = client;
    }

    public final CubecraftClient getClient() {
        return client;
    }

    public void deviceSetup(CubecraftClient client, Window window, DeviceContext ctx) {
    }

    public void clientSetup(CubecraftClient client) {
    }

    public void worldContextChange(WorldContext context) {
    }

    public void tick() {
    }

    public void render(DisplayScreenInfo info, float delta) {
    }

    public void clientQuit(CubecraftClient client) {
    }
}
