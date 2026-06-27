package com.multiplayer.mod.client.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Ein Screen mit Schiebetür-Animation
 * Das Interface wird herausgeschoben und kann wieder zurückgefahren werden
 * Die Buttons gelten nur für dieses Interface
 */
public class SlidingMultiplayerScreen extends Screen {
    
    private final Screen previousScreen;
    private float slideProgress = 0.0f; // 0 = hidden, 1 = fully visible
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
            this.minecraft.setScreen(new Screen(Component.literal("Joining Game...")) {
                @Override
                public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
                    this.renderBackground(poseStack);
                    this.drawCenteredString(poseStack, this.getFont(), "Joining Game...", this.width / 2, this.height / 2, 0xFFFFFF);
                }
            });
        })
        .pos(this.getSlideX() + 10, 20)
        .size(SLIDE_WIDTH - 20, 20)
        .build());
        
        // "Delete" Button - NUR für die Schiebetür
        this.addRenderableWidget(Button.builder(Component.literal("Delete"), (button) -> {
            // Delete Logik für die Schiebetür
            Multiplayer.LOGGER.info("Delete Button gedrückt");
        })
        .pos(this.getSlideX() + 10, 50)
        .size(SLIDE_WIDTH - 20, 20)
        .build());
        
        // "Refresh" Button - NUR für die Schiebetür
        this.addRenderableWidget(Button.builder(Component.literal("Refresh"), (button) -> {
            // Refresh Logik für die Schiebetür
            Multiplayer.LOGGER.info("Refresh Button gedrückt");
        })
        .pos(this.getSlideX() + 10, 80)
        .size(SLIDE_WIDTH - 20, 20)
        .build());
        
        // "Back" Button - schließt die Schiebetür
        this.addRenderableWidget(Button.builder(Component.literal("Back"), (button) -> {
            this.isOpening = false; // Fahre die Tür zu
        })
        .pos(this.getSlideX() + 10, this.height - 30)
        .size(SLIDE_WIDTH - 20, 20)
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
     */
    private int getSlideX() {
        return (int) (this.width - (SLIDE_WIDTH * slideProgress));
    }
    
    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        
        // Speichere die aktuelle Transformation
        poseStack.pushPose();
        
        // Berechne die Position
        int slideX = this.getSlideX();
        
        // Verschiebe den gesamten Content
        poseStack.translate(slideX, 0, 0);
        
        // Zeichne den Schiebetür-Container (Hintergrund)
        fill(poseStack, 0, 0, SLIDE_WIDTH, this.height, 0xFF8B8B8B); // Grauer Hintergrund
        
        // Zeichne Border
        fill(poseStack, 0, 0, 2, this.height, 0xFF000000); // Linke Kante
        
        // Restauriere Transformation
        poseStack.popPose();
        
        // Render Standard GUI Elements mit Translation
        poseStack.pushPose();
        poseStack.translate(slideX, 0, 0);
        
        super.render(poseStack, mouseX, mouseY, partialTick);
        
        poseStack.popPose();
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
