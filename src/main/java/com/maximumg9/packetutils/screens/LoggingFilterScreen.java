package com.maximumg9.packetutils.screens;

import com.maximumg9.packetutils.PacketUtils;
import com.maximumg9.packetutils.util.PacketData;
import com.maximumg9.packetutils.util.MappingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;

import java.util.Objects;

public class LoggingFilterScreen<T extends Packet<?>> extends Screen {
    private final Class<T> clazz;
    public LoggingFilterScreen(Class<T> clazz) {
        super(Text.empty());
        this.clazz = clazz;
    }

    @Override
    protected void init() {
        Objects.requireNonNull(this.textRenderer);

        PacketUtils.LOG_FILTER_COLLECTION.getFilter(this.clazz); // Makes the filter real

        TextWidget title = new TextWidget(
                (this.width/2)-102,
                0,
                204,
                20,
                Text.of(MappingHelper.get(this.clazz)),
                this.textRenderer
        );
        this.addDrawableChild(title);

        PacketData<T> packetData = new PacketData<>(this.clazz);

        int i = 0;

        for(String name : packetData.fieldMap.keySet()) {
            int y = ((i/3)*20) + 20;
            int x = (this.width/3)* (i%3);
            ToggleButton fieldWidget = new ToggleButton(
                    x,
                    y,
                    98,
                    20,
                    Text.of(name),
                    (val) -> PacketUtils.LOG_FILTER_COLLECTION.getFilter(this.clazz).setField(packetData.fieldMap.get(name).unfriendlyName(), val),
                    PacketUtils.LOG_FILTER_COLLECTION.getFilter(this.clazz).getField(packetData.fieldMap.get(name).unfriendlyName())
            );
            this.addDrawableChild(fieldWidget);

            i += 1;
        }
    }
}