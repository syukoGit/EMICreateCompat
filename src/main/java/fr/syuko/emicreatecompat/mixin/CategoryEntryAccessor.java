package fr.syuko.emicreatecompat.mixin;

import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StockKeeperRequestScreen.CategoryEntry.class)
public interface CategoryEntryAccessor {

    @Accessor(value = "y", remap = false)
    void emicreatecompat$setY(int y);

    @Accessor(value = "hidden", remap = false)
    boolean emicreatecompat$isHidden();
}
