package fr.syuko.emicreatecompat.create.render;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.gui.CustomLightingSettings;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.gui.ILightingSettings;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class KineticsRender {

    public static final ILightingSettings
            LIGHTING =
            CustomLightingSettings.builder()
                                  .firstLightRotation(12.5f, -45.0f)
                                  .secondLightRotation(-20.0f, -50.0f)
                                  .build();

    private static final float SHAFT_DEGREES_PER_TICK = 4f;

    private KineticsRender() {
    }

    public static BlockState shaft(Direction.Axis axis) {
        return AllBlocks.SHAFT.getDefaultState().setValue(BlockStateProperties.AXIS, axis);
    }

    public static float shaftAngle() {
        return (AnimationTickHolder.getRenderTime() * SHAFT_DEGREES_PER_TICK) % 360;
    }
}
