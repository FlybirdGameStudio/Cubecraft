package net.cubecraft.client.registry;

import me.gb2022.quantum3d.texture.ITextureImage;
import me.gb2022.commons.event.EventHandler;
import me.gb2022.commons.event.SubscribedEvent;
import me.gb2022.commons.registry.FieldRegistry;
import me.gb2022.commons.registry.FieldRegistryHolder;
import net.cubecraft.client.gui.font.FontRenderer;
import net.cubecraft.client.resource.FontAsset;
import net.cubecraft.client.resource.TextureAsset;
import net.cubecraft.client.resource.UIAsset;
import net.cubecraft.event.resource.ResourceLoadFinishEvent;
import net.cubecraft.resource.Load;

import java.util.Objects;

@FieldRegistryHolder("cubecraft")
public interface ResourceRegistry {
    @Load("client:startup")
    @FieldRegistry("studio_logo")
    TextureAsset STUDIO_LOGO = new TextureAsset("cubecraft:/gui/logo/studio_logo.png");

    @Load("client:startup")
    @FieldRegistry("game_logo")
    TextureAsset GAME_LOGO = new TextureAsset("cubecraft:/gui/logo/game_logo.png");

    @Load("client:startup")
    @FieldRegistry("game_icon")
    TextureAsset GAME_ICON = new TextureAsset("cubecraft:/gui/logo/game_icon.png");

    @FieldRegistry("sun")
    TextureAsset SUN = new TextureAsset("cubecraft:/environment/sun.png");


    //screen
    @FieldRegistry("image_bg")
    TextureAsset IMAGE_BG = new TextureAsset("cubecraft:/gui/bg.png");

    @FieldRegistry("toast")
    ITextureImage TOAST = new TextureAsset("cubecraft:/gui/controls/toast.png");

    @Load("client:startup")
    @FieldRegistry("ascii_page")
    TextureAsset ASCII_PAGE = new TextureAsset("cubecraft:/font/unicode_page_00.png");

    @Load("client:startup")
    @FieldRegistry("text_font")
    FontAsset TEXT_FONT = new FontAsset("cubecraft:/MiSans-Medium.ttf");

    @Load("client:startup")
    @FieldRegistry("icon_font")
    FontAsset ICON_FONT = new FontAsset("cubecraft:/FontAwesome6_Free_Solid_900.otf");

    //hud
    @FieldRegistry("action_bar_texture")
    TextureAsset ACTION_BAR = new TextureAsset("cubecraft:/gui/container/actionbar.png");

    @FieldRegistry("pointer_texture")
    TextureAsset POINTER = new TextureAsset("cubecraft:/gui/icon/pointer.png");


    @FieldRegistry("title_screen")
    UIAsset TITLE_SCREEN = new UIAsset("cubecraft:/title_screen.xml");

    @FieldRegistry("single_player_screen")
    UIAsset SINGLE_PLAYER_SCREEN = new UIAsset("cubecraft:/single_player_screen.xml");

    @FieldRegistry("multi_player_screen")
    UIAsset MULTI_PLAYER_SCREEN = new UIAsset("cubecraft:/multi_player_screen.xml");

    @FieldRegistry("pause_screen")
    UIAsset PAUSE_SCREEN = new UIAsset("cubecraft:/pause_screen.xml");

    @FieldRegistry("options_screen")
    UIAsset OPTIONS_SCREEN = new UIAsset("cubecraft:/setting_screen.xml");


    @EventHandler
    @SubscribedEvent("default")
    static void onResourceLoadComplete(ResourceLoadFinishEvent event) {
        if (!Objects.equals(event.getStage(), "default")) {
            return;
        }
        FontRenderer.ttf().setFontFamily(TEXT_FONT.getFont());
        FontRenderer.icon().setFontFamily(ICON_FONT.getFont());

        TextureRegistry.TOAST.load(ResourceRegistry.TOAST);
        TextureRegistry.IMAGE_BG.load(ResourceRegistry.IMAGE_BG);
    }
}