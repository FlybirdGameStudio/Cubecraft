package ink.flybird.cubecraft.client.internal.gui.event;

import ink.flybird.cubecraft.client.gui.GUIManager;
import ink.flybird.cubecraft.client.gui.event.ComponentEvent;
import ink.flybird.cubecraft.client.gui.node.Node;
import ink.flybird.cubecraft.client.gui.screen.Screen;

public final class ComponentInitializeEvent extends ComponentEvent {
    public ComponentInitializeEvent(Node component, Screen screen, GUIManager context) {
        super(component, screen, context);
    }

    public Node getComponent() {
        return this.component;
    }
}