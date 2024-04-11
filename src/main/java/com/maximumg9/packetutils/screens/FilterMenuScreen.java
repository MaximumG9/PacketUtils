package com.maximumg9.packetutils.screens;

import com.maximumg9.packetutils.util.MappingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class FilterMenuScreen extends Screen {

    private int packetClassWidgetHorizontalCount;
    private int packetClassWidgetVerticalCount;

    private TextFieldWidget searchBar;

    private String lastSearchText = "A";

    final Function<Class<? extends Packet<?>>, Screen> screenCreator;

    private final List<PacketClassWidget> widgets = new ArrayList<>();
    private boolean refresh = false;

    public FilterMenuScreen(Function<Class<? extends Packet<?>>,Screen> screenCreator) {
        super(Text.of("Filter Menu"));
        this.screenCreator = screenCreator;
    }

    @Override
    protected void init() {

        packetClassWidgetHorizontalCount = (int) Math.floor(this.width/102F);

        packetClassWidgetVerticalCount = (int) Math.floor(this.height/48F)-1;

        Objects.requireNonNull(this.textRenderer);

        TextWidget title = new TextWidget(
                (this.width/2)-102,
                0,
                204,
                20,
                Text.of("Logging Menu"),
                this.textRenderer
        );
        this.addDrawableChild(title);

        searchBar = new TextFieldWidget(
                this.textRenderer,
                (this.width/2)-102,
                20,
                204,
                20,
                Text.of("Search... ")
        );
        this.addDrawableChild(searchBar);
    }

    private void addPacketClassWidget(int x, int y, Class<? extends Packet<?>> pClazz) {
        PacketClassWidget widget = new PacketClassWidget<>(
                pClazz,
                x,
                y,
                98,
                40,
                Text.of("widget"),
                this.textRenderer,
                this.textRenderer,
                (clazz) -> {
                    assert this.client != null;
                    this.client.setScreen(screenCreator.apply(clazz));
                }
        );
        this.addDrawableChild(widget);
        widgets.add(widget);
    }

    @Override
    public void tick() {
        if(!Objects.equals(searchBar.getText(), lastSearchText) || this.refresh) {
            widgets.forEach(this::remove);
            List<Class<? extends Packet<?>>> contains = MappingHelper.getContains(searchBar.getText());
            if(contains.size() > packetClassWidgetHorizontalCount*packetClassWidgetVerticalCount) {
                contains = contains.subList(0,packetClassWidgetHorizontalCount*packetClassWidgetVerticalCount);
            }
            for(int i = 0; i < contains.size(); i++) {
                int x = this.width/2 + (
                        ((i % packetClassWidgetHorizontalCount)
                        - ((int) Math.floor(packetClassWidgetHorizontalCount/2F)))*102
                        - (48*(packetClassWidgetHorizontalCount%2))
                );
                int y = ((i/packetClassWidgetHorizontalCount)*48)+48;
                addPacketClassWidget(
                        x,
                        y,
                        contains.get(i)
                );
            }
            lastSearchText = searchBar.getText();
            this.refresh = false;
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        packetClassWidgetHorizontalCount = (int) Math.floor(width/102F);

        packetClassWidgetVerticalCount = (int) Math.floor(height/48F)-1;

        this.refresh = true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        tick();
        super.render(context, mouseX, mouseY, delta);
    }
}
