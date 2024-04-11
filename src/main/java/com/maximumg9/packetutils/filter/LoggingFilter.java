package com.maximumg9.packetutils.filter;

import com.maximumg9.packetutils.PacketUtils;
import com.maximumg9.packetutils.util.PacketData;
import com.maximumg9.packetutils.util.MappingHelper;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.network.packet.Packet;

import java.lang.reflect.Field;

public class LoggingFilter<T extends Packet<?>> implements Filter<T> {
    private final Object2BooleanOpenHashMap<String> map = new Object2BooleanOpenHashMap<>();

    private LoggingFilter(Class<T> clazz) {
        for(Field f : PacketUtils.getAllFields(clazz)) {
            map.put(f.getName(),false);
        }
    }

    public void setField(String fieldName, boolean allowed) {
        if(!map.containsKey(fieldName)) throw new IllegalArgumentException("Packet does not have field: " + fieldName);
        map.replace(fieldName, allowed);
    }

    public boolean getField(String fieldName) {
        if(!map.containsKey(fieldName)) throw new IllegalArgumentException("Packet does not have field: " + fieldName);
        return map.getBoolean(fieldName);
    }

    @Override
    public PacketData<T> apply(PacketData<T> packet) {
        LogUtils.getLogger().info("applying packet");
        if(map.values().contains(true)) {
            StringBuilder builder = new StringBuilder(MappingHelper.get(packet.originalClass));
            builder.append(": {\n");
            packet.fieldMap.forEach((name,fieldData) -> {
                if(map.getBoolean(fieldData.unfriendlyName())) builder.append("\t").append(name).append(": ").append(fieldData).append("\n");
            });
            builder.append("}");
            LogUtils.getLogger().info(builder.toString());
        }

        return packet;
    }

    public static class Supplier implements FilterCreator<LoggingFilter<?>> {
        @Override
        public <T extends Packet<?>> LoggingFilter<T> create(Class<T> clazz) {
            return new LoggingFilter<>(clazz);
        }
    }
}
