package com.multiplayer.mod.client.event;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import com.multiplayer.mod.client.screen.SlidingMultiplayerScreen;
import com.multiplayer.mod.Multiplayer;

/**
 * Event Handler um den "Sliding Door" Button zum Multiplayer Screen hinzuzufügen
 */
@EventBusSubscriber(modid = Multiplayer.MODID, value = Dist.CLIENT)
public class MultiplayerScreenHandler {
    
    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        
        // Prüfe ob es der Multiplayer Screen ist
        if (screen instanceof JoinMultiplayerScreen) {
            JoinMultiplayerScreen multiplayerScreen = (JoinMultiplayerScreen) screen;
            
            // Füge den "Sliding Door" Button am rechten Rand hinzu
            Button slidingDoorButton = Button.builder(Component.literal("Sliding Door"), (button) -> {
                // Öffne die Schiebetür
                Multiplayer.LOGGER.info("Sliding Door Button gedrückt - öffne SlidingMultiplayerScreen");
                if (multiplayerScreen.getMinecraft() != null) {
                    multiplayerScreen.getMinecraft().setScreen(new SlidingMultiplayerScreen(multiplayerScreen));
                }
            })
            .pos(multiplayerScreen.width - 210, 50)
            .size(200, 20)
            .build();
            
            event.addListener(slidingDoorButton);
            Multiplayer.LOGGER.info("Sliding Door Button hinzugefügt");
        }
    }
}
