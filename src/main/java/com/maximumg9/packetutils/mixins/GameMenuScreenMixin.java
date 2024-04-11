package com.maximumg9.packetutils.mixins;

import com.maximumg9.packetutils.screens.FilterMenuScreen;
import com.maximumg9.packetutils.screens.LoggingFilterScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method="initWidgets",at=@At("HEAD"))
    private void initWidgets(CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(Text.of("Open Logging Menu"), button -> {
            assert this.client != null;
            this.client.setScreen(new FilterMenuScreen(LoggingFilterScreen::new));
        }).width(98).position(0,0).build());
    }
}
