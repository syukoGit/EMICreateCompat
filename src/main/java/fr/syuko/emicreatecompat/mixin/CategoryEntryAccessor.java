package fr.syuko.emicreatecompat.mixin;

import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes the package-private layout fields of Create's
 * {@code StockKeeperRequestScreen.CategoryEntry} so we can position our injected category.
 */
@Mixin(StockKeeperRequestScreen.CategoryEntry.class)
public interface CategoryEntryAccessor {

    @Accessor(value = "y", remap = false)
    void emicreatecompat$setY(int y);

    @Accessor(value = "hidden", remap = false)
    boolean emicreatecompat$isHidden();
}
