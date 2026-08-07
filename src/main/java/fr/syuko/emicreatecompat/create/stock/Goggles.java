package fr.syuko.emicreatecompat.create.stock;

import com.simibubi.create.content.equipment.goggles.GogglesItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public final class Goggles {

    private Goggles() {
    }

    public static boolean missing() {
        Player player = Minecraft.getInstance().player;
        return player == null || !GogglesItem.isWearingGoggles(player);
    }
}
