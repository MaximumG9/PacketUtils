package com.maximumg9.packetutils.util;

import com.maximumg9.packetutils.PacketUtils;
import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;

public class PacketData<T extends Packet<?>> {
    public final Class<T> originalClass;
    public final HashMap<String, FieldData> fieldMap = new HashMap<>();

    public PacketData(T packet) {
        Field[] fields = PacketUtils.getAllFields(packet.getClass());
        for(Field field : fields) {
            field.setAccessible(true);
            try {
                this.fieldMap.put(
                        MappingHelper.getFieldName(field),
                        new FieldData(field.getType(), field.get(packet), field.getName())
                );
            } catch (IllegalAccessException impossible) {
                LogUtils.getLogger().error("Could not access fields of the target packet when creating Packet Data");
            }
        }
        this.originalClass = PacketUtils.getClass(packet);
    }


    public PacketData(Class<T> clazz) {
        this(createFromClass(clazz));
    }

    @SuppressWarnings("unchecked")
    private static <U extends Packet<?>> U createFromClass(Class<U> clazz) {
        ByteBuf nullBuffer = new NullByteBuf();
        PacketByteBuf buffer = new PacketByteBuf(nullBuffer);

        try {
            Constructor<U> constructor = clazz.getConstructor(PacketByteBuf.class);
            try {
                return constructor.newInstance(buffer);
            } catch (Throwable t) {
                Throwable throwable = t;
                if (throwable instanceof InvocationTargetException) {
                    throwable = ((InvocationTargetException) throwable).getTargetException();
                }
                throw new IllegalArgumentException("Class can not be created through this method. Error: " + throwable);
            }
        } catch (NoSuchMethodException e) {
            Method read = Arrays.stream(clazz.getMethods()).filter(
                    (method) -> method.getParameterTypes()[0] == PacketByteBuf.class && (method.getModifiers() & Modifier.STATIC) > 0
            ).findFirst().orElseThrow(() -> new RuntimeException(new NoSuchMethodException()));
            try {
                return (U) read.invoke(null, buffer);
            } catch (Throwable t) {
                Throwable throwable = t;
                if(throwable instanceof InvocationTargetException) {
                    throwable = ((InvocationTargetException) throwable).getTargetException();
                }
                throw new IllegalArgumentException("Class can not be created through this method. Error: " + throwable);
            }
        }
    }

    public T recreate() {
        T newPacket = createFromClass(originalClass);
        fieldMap.forEach((name,fieldData) -> {
            try {
                Field f;
                try {
                    f = originalClass.getDeclaredField(fieldData.unfriendlyName());
                } catch(NoSuchFieldException e) {
                    f = originalClass.getSuperclass().getDeclaredField(fieldData.unfriendlyName());
                }

                f.setAccessible(true);
                f.set(newPacket,fieldData.data());
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
        return newPacket;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(MappingHelper.get(originalClass));
        builder.append(": {\n");
        fieldMap.forEach((name,fieldData) -> builder.append("\t").append(name).append(": ").append(fieldData).append("\n"));
        builder.append("}");
        return builder.toString();
    }
}

