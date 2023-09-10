package ink.flybird.cubecraft.client.gui.layout;

import com.google.gson.Gson;
import ink.flybird.cubecraft.client.CubecraftClient;
import io.flybird.cubecraft.register.SharedContext;
import ink.flybird.fcommon.file.FAMLDeserializer;
import ink.flybird.fcommon.file.XmlReader;
import org.w3c.dom.Element;

public abstract class Layout {
    public int layer;
    protected int width, height;
    protected int absoluteX;
    protected int absoluteY;
    protected int absoluteWidth;
    protected int absoluteHeight;
    protected Scale scale = new Scale(false, false, false, false);
    protected Border border = new Border(0, 0, 0, 0);

    public abstract void initialize(String[] metadata);

    public abstract void resize(int x, int y, int scrWidth, int scrHeight);

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public int getAbsoluteX() {
        return absoluteX;
    }

    public void setAbsoluteX(int absoluteX) {
        this.absoluteX = absoluteX;
    }

    public int getAbsoluteY() {
        return absoluteY;
    }

    public void setAbsoluteY(int absoluteY) {
        this.absoluteY = absoluteY;
    }

    public int getAbsoluteWidth() {
        return absoluteWidth;
    }

    public void setAbsoluteWidth(int absoluteWidth) {
        this.absoluteWidth = absoluteWidth;
    }

    public int getAbsoluteHeight() {
        return absoluteHeight;
    }

    public void setAbsoluteHeight(int absoluteHeight) {
        this.absoluteHeight = absoluteHeight;
    }

    public static class XMLDeserializer implements FAMLDeserializer<Layout> {
        @Override
        public Layout deserialize(Element element, XmlReader reader) {
            Layout layout = SharedContext.FAML_READER.deserialize(element, CubecraftClient.CLIENT.getGuiManager().getLayoutClass(element.getAttribute("type")));
            if (element.getElementsByTagName("border").getLength() > 0) {
                int[] l2 = new Gson().fromJson(element.getElementsByTagName("border").item(0).getTextContent(), int[].class);
                layout.setBorder(new Border(l2[0], l2[1], l2[2], l2[3]));
            }
            if (element.getElementsByTagName("scale").getLength() > 0) {
                boolean[] l2 = new Gson().fromJson(element.getElementsByTagName("scale").item(0).getTextContent(), boolean[].class);
                layout.setScale(new Scale(l2[0], l2[1], l2[2], l2[3]));
            }
            return layout;
        }
    }
}