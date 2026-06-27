package com.multiplayer.mod.client.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import com.multiplayer.mod.Multiplayer;

/**
 * Ein Screen mit Schiebetür-Animation
 * Das Interface wird von rechts hereingeschoben und kann wieder zurückgefahren werden
 * Die Buttons gelten nur für dieses Interface
 */
public class SlidingMultiplayerScreen extends Screen {
    
    private final Screen previousScreen;
    private float slideProgress = 0.3f; // Startet bereits 30% offen (sichtbar am rechten Rand)
    private boolean isOpening = true; // true = fahre raus, false = fahre rein
    private static final float SLIDE_SPEED = 0.1f; // Animation Geschwindigkeit (höher = schneller)
    private static final int SLIDE_WIDTH = 200; // Breite der Schiebetür in Pixeln
    
    public SlidingMultiplayerScreen(Screen previousScreen) {
        super(Component.literal("Multiplayer"));
        this.previousScreen = previousScreen;
    }
    
    @Override
    protected void init() {
        super.init();
        
        // "Join Game" Button - NUR für die Schiebetür
        this.addRenderableWidget(Button.builder(Component.literal("Join Game"), (button) -> {
            // Join Game Logik für die Schiebetür
            Multiplayer.LOGGER.info("Join Game Button gedrückt");
        })
        .pos(this.getSlideX() + 10, 20)
        .width(SLIDE_WIDTH - 20)
        .build());
        
        // "Delete" Button - NUR für die Schiebetür
        this.addRenderableWidget(Button.builder(Component.literal("Delete"), (button) -> {
            // Delete Logik für die Schiebetür
            Multiplayer.LOGGER.info("Delete Button gedrückt");
        })
        .pos(this.getSlideX() + 10, 50)
        .width(SLIDE_WIDTH - 20)
        .build());
        
        // "Refresh" Button - NUR für die Schiebetür
        this.addRenderableWidget(Button.builder(Component.literal("Refresh"), (button) -> {
            // Refresh Logik für die Schiebetür
            Multiplayer.LOGGER.info("Refresh Button gedrückt");
        })
        .pos(this.getSlideX() + 10, 80)
        .width(SLIDE_WIDTH - 20)
        .build());
        
        // "Back" Button - schließt die Schiebetür
        this.addRenderableWidget(Button.builder(Component.literal("Back"), (button) -> {
            this.isOpening = false; // Fahre die Tür zu
        })
        .pos(this.getSlideX() + 10, this.height - 30)
        .width(SLIDE_WIDTH - 20)
        .build());
    }
    
    @Override
    public void tick() {
        super.tick();
        
        // Schiebetür Animation
        if (isOpening && slideProgress < 1.0f) {
            slideProgress += SLIDE_SPEED;
            if (slideProgress > 1.0f) slideProgress = 1.0f;
        } else if (!isOpening && slideProgress > 0.0f) {
            slideProgress -= SLIDE_SPEED;
            if (slideProgress < 0.0f) {
                slideProgress = 0.0f;
                // Screen schließen wenn Animation fertig
                this.minecraft.setScreen(previousScreen);
            }
        }
    }
    
    /**
     * Berechnet die X-Position der Schiebetür basierend auf Progress
     * Startet von rechts und fährt nach links rein
     */
    private int getSlideX() {
        return (int) (this.width - (SLIDE_WIDTH * slideProgress));
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        
        // Berechne die Position
        int slideX = this.getSlideX();
        
        // Zeichne den Schiebetür-Container (Hintergrund)
        guiGraphics.fill(slideX, 0, slideX + SLIDE_WIDTH, this.height, 0xFF8B8B8B); // Grauer Hintergrund
        
        // Zeichne Border (linke Kante der Schiebetür)
        guiGraphics.fill(slideX, 0, slideX + 2, this.height, 0xFF000000); // Linke Kante
        
        // Render Standard GUI Elements
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
    
    @Override
    public boolean mouseScrolled(double x, double y, double xAxis, double yAxis) {
        // Verhindere Scrollen
        return true;
    }
    
    @Override
    public void onClose() {
        // Wenn ESC gedrückt wird, fahre die Tür zu
        this.isOpening = false;
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
