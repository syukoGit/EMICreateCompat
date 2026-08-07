package fr.syuko.emicreatecompat.create.stock;

import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

public final class BindButton {

    public static final int SIZE = 18;

    private BindButton() {
    }

    public static IconButton create(int x, int y, ResourceKey<Level> dimension, BlockPos pos) {
        IconButton button = new IconButton(x, y, AllIcons.I_TARGET);
        button.withCallback(() -> {
            BoundNetwork.toggle(dimension, pos);
            syncState(button, dimension, pos);
            Minecraft.getInstance()
                     .getSoundManager()
                     .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        });
        syncState(button, dimension, pos);
        return button;
    }

    private static void syncState(IconButton button, ResourceKey<Level> dimension, BlockPos pos) {
        boolean bound = BoundNetwork.isBoundTo(dimension, pos);
        button.green = bound;
        button.setToolTip(Component.translatable(bound
                                                 ? "gui.emicreatecompat.stock_keeper.unbind"
                                                 : "gui.emicreatecompat.stock_keeper.bind"));
    }
}
