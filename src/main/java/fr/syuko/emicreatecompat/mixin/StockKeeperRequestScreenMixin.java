package fr.syuko.emicreatecompat.mixin;

import com.simibubi.create.content.logistics.BigItemStack;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestMenu;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen.CategoryEntry;
import com.simibubi.create.content.logistics.stockTicker.StockTickerBlockEntity;
import fr.syuko.emicreatecompat.config.Config;
import fr.syuko.emicreatecompat.create.PendingOrder;
import fr.syuko.emicreatecompat.create.StockCategoryInjector;
import fr.syuko.emicreatecompat.create.StockMatcher;
import fr.syuko.emicreatecompat.create.stock.BindButton;
import fr.syuko.emicreatecompat.emi.EmiRecipeTreeReader;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(StockKeeperRequestScreen.class)
public abstract class StockKeeperRequestScreenMixin extends AbstractContainerScreen<StockKeeperRequestMenu> {

    @Unique
    private static final int BIND_BUTTON_MARGIN = 2;

    @Unique
    private static final int BIND_BUTTON_OFFSET_Y = 16;

    @Shadow(remap = false)
    StockTickerBlockEntity blockEntity;

    @Shadow(remap = false)
    public List<List<BigItemStack>> currentItemSource;

    @Shadow(remap = false)
    public List<CategoryEntry> categories;

    @Shadow(remap = false)
    public List<List<BigItemStack>> displayedItems;

    @Shadow(remap = false)
    public List<BigItemStack> itemsToOrder;

    @Shadow(remap = false)
    @Final
    int rowHeight;

    @Shadow(remap = false)
    @Final
    int cols;

    @Shadow(remap = false)
    private List<Rect2i> extraAreas;

    private StockKeeperRequestScreenMixin(StockKeeperRequestMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void emicreatecompat$addBindButton(CallbackInfo ci) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        int x = getGuiLeft() - BindButton.SIZE - BIND_BUTTON_MARGIN;
        int y = getGuiTop() + BIND_BUTTON_OFFSET_Y;

        addRenderableWidget(BindButton.create(x, y, level.dimension(), blockEntity.getBlockPos()));
        extraAreas.add(new Rect2i(x, y, BindButton.SIZE, BindButton.SIZE));
    }

    @Inject(method = "refreshSearchResults(Z)V", at = @At("RETURN"), remap = false)
    private void emicreatecompat$injectRecipeTreeCategory(boolean scrollBackUp, CallbackInfo ci) {
        if (currentItemSource == null || displayedItems == null || displayedItems.isEmpty()) {
            return;
        }

        List<ItemStack> pendingOrder = Config.countPendingOrder
                                       ? PendingOrder.stacks(itemsToOrder)
                                       : List.of();

        Set<Item> needed = EmiRecipeTreeReader.neededTreeItems(pendingOrder);
        List<BigItemStack> matches = StockMatcher.match(needed, currentItemSource);
        if (matches.isEmpty()) {
            return;
        }

        String title = Component.translatable("gui.emicreatecompat.stock_keeper.recipe_tree").getString();
        StockCategoryInjector.Result result = StockCategoryInjector.prepend(title,
                                                                            matches,
                                                                            categories,
                                                                            displayedItems,
                                                                            rowHeight,
                                                                            cols);
        categories = result.categories();
        displayedItems = result.displayedItems();
    }

    @Inject(method = "removed", at = @At("TAIL"))
    private void emicreatecompat$restoreEmiFavorites(CallbackInfo ci) {
        if (Config.countPendingOrder) {
            EmiRecipeTreeReader.restoreNativeFavorites();
        }
    }
}
