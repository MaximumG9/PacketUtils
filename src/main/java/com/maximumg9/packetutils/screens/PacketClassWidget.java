package com.maximumg9.packetutils.screens;

import com.maximumg9.packetutils.util.MappingHelper;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class PacketClassWidget<P extends Packet<?>> extends ClickableWidget {
    final TextRenderer titleRenderer;
    final TextRenderer bodyRenderer;
    final Class<P> clazz;
    final Consumer<Class<P>> onClick;

    public PacketClassWidget(Class<P> clazz, int x, int y, int width,
                             int height, Text message, TextRenderer titleRenderer,
                             TextRenderer bodyRenderer, Consumer<Class<P>> onClick) {
        super(x, y, width, height, message);
        this.titleRenderer = titleRenderer;
        this.bodyRenderer = bodyRenderer;
        this.clazz = clazz;
        this.onClick = onClick;
    }
    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(getX(),getY(),getX()+this.width,getY()+this.height,0xff000000);
        context.drawBorder(getX(),getY(),getWidth(),getHeight(),0xffffffff);

        context.getMatrices().push();
        context.getMatrices().scale(2/3f,2/3f,2/3f);

        context.drawTextWrapped(
                this.titleRenderer,
                Text.of(MappingHelper.get(this.clazz)),
                (getX() + 2)*3/2,
                (getY() + 2)*3/2,
                (getWidth()-4)*3/2,
                0xffffffff);


        context.drawTextWrapped(
                this.bodyRenderer,
                Text.of("Description description description description description description description description"),
                (getX() + 2)*3/2,
                (getY() + 2 + this.titleRenderer.fontHeight + this.titleRenderer.fontHeight - 4)*3/2,
                (getWidth()-4)*3/2,
                0xffaaaaaa);

        context.getMatrices().pop();
    }
    @Override
    public void onClick(double mouseX, double mouseY) {
        this.onClick.accept(clazz);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
    }
}
