package com.multiplayer.mod.client.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import com.multiplayer.mod.Multiplayer;

/**
 * Ein Screen mit Schiebetür-Animation
 * Das Interface wird von rechts hereingeschoben und kann wieder zurückgefahren werden
 * Die Buttons gelten nur für dieses Interface mit schönem Design
 */
public class SlidingMultiplayerScreen extends Screen {
    
    private final Screen previousScreen;
    private float slideProgress = 0.3f; // Startet bereits 30% offen (sichtbar am rechten Rand)
    private boolean isOpening = true; // true = fahre raus, false = fahre rein
    private static final float SLIDE_SPEED = 0.1f; // Animation Geschwindigkeit (höher = schneller)
    private static final int SLIDE_WIDTH = 250; // Breite der Schiebetür in Pixeln
    private static final int BACKGROUND_COLOR = 0xFF1A1A1A; // Dunkles Grau
    private static final int HEADER_COLOR = 0xFF0D0D0D; // Noch dunkler für Header
    private static final int BORDER_COLOR = 0xFF00AA00; // Grüne Akzentfarbe
    private static final int TEXT_COLOR = 0xFFFFFFFF; // Weiß
    
    public SlidingMultiplayerScreen(Screen previousScreen) {
        super(Component.literal("Multiplayer"));
        this.previousScreen = previousScreen;
    }
    
    @Override
    protected void init() {
        super.init();
        
        int slideX = this.getSlideX();
        int buttonWidth = SLIDE_WIDTH - 30;
        int buttonX = slideX + 15;
        
        // "Join Game" Button
        this.addRenderableWidget(Button.builder(Component.literal("🌐 Join Game"), (button) -> {
            Multiplayer.LOGGER.info("Join Game Button gedrückt");
        })
        .pos(buttonX, 60)
        .width(buttonWidth)
        .height(30)
        .build());
        
        // "Delete" Button
        this.addRenderableWidget(Button.builder(Component.literal("🗑️ Delete"), (button) -> {
            Multiplayer.LOGGER.info("Delete Button gedrückt");
        })
        .pos(buttonX, 100)
        .width(buttonWidth)
        .height(30)
        .build());
        
        // "Refresh" Button
        this.addRenderableWidget(Button.builder(Component.literal("🔄 Refresh"), (button) -> {
            Multiplayer.LOGGER.info("Refresh Button gedrückt");
        })
        .pos(buttonX, 140)
        .width(buttonWidth)
        .height(30)
        .build());
        
        // "Edit" Button
        this.addRenderableWidget(Button.builder(Component.literal("✏️ Edit"), (button) -> {
            Multiplayer.LOGGER.info("Edit Button gedrückt");
        })
        .pos(buttonX, 180)
        .width(buttonWidth)
        .height(30)
        .build());
        
        // "Back" Button - schließt die Schiebetür
        this.addRenderableWidget(Button.builder(Component.literal("← Back"), (button) -> {
            this.isOpening = false; // Fahre die Tür zu
        })
        .pos(buttonX, this.height - 40)
        .width(buttonWidth)
        .height(30)
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
        
        // Zeichne den Schiebetür-Container (Haupthintergrund)
        guiGraphics.fill(slideX, 0, slideX + SLIDE_WIDTH, this.height, BACKGROUND_COLOR);
        
        // Zeichne Header-Bereich
        guiGraphics.fill(slideX, 0, slideX + SLIDE_WIDTH, 50, HEADER_COLOR);
        
        // Zeichne obere grüne Linie (Akzent)
        guiGraphics.fill(slideX, 0, slideX + SLIDE_WIDTH, 3, BORDER_COLOR);
        
        // Zeichne linke Kante (grüne Linie)
        guiGraphics.fill(slideX, 0, slideX + 3, this.height, BORDER_COLOR);
        
        // Zeichne unten grüne Linie
        guiGraphics.fill(slideX, this.height - 3, slideX + SLIDE_WIDTH, this.height, BORDER_COLOR);
        
        // Zeichne Titel
        guiGraphics.drawCenteredString(this.font, "Server Manager", slideX + SLIDE_WIDTH / 2, 15, TEXT_COLOR);
        
        // Zeichne Trennlinie nach Header
        guiGraphics.fill(slideX + 10, 45, slideX + SLIDE_WIDTH - 10, 46, BORDER_COLOR);
        
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
