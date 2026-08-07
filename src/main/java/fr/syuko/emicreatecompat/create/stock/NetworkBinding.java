package fr.syuko.emicreatecompat.create.stock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record NetworkBinding(ResourceKey<Level> dimension, BlockPos pos) {

    public static final Codec<NetworkBinding> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceKey.codec(
                                                                                                                   Registries.DIMENSION).fieldOf("dimension").forGetter(NetworkBinding::dimension),
                                                                                                           BlockPos.CODEC.fieldOf(
                                                                                                                           "pos")
                                                                                                                         .forGetter(
                                                                                                                                 NetworkBinding::pos))
                                                                                                    .apply(instance,
                                                                                                           NetworkBinding::new));
}
