package fr.syuko.emicreatecompat.create;

import com.simibubi.create.content.logistics.BigItemStack;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen.CategoryEntry;
import fr.syuko.emicreatecompat.mixin.CategoryEntryAccessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the "recipe tree" category and splices it into the top of Create's Stock Keeper request
 * screen, keeping the parallel {@code categories}/{@code displayedItems} lists index-aligned and
 * recomputing the vertical layout exactly like Create's own {@code refreshSearchResults} does.
 *
 * <p>Returns fresh lists rather than mutating in place: the mixin owns the screen fields and assigns
 * the result back, which keeps this class free of any mixin-shadowing concerns.
 */
public final class StockCategoryInjector {

    private StockCategoryInjector() {
    }

    /**
     * @param title          the category header to show
     * @param matches        the stock entries to list under it (assumed non-empty)
     * @param categories     the screen's current category list
     * @param displayedItems the screen's current per-category item lists
     * @param rowHeight      Create's row height, for the layout pass
     * @param cols           Create's column count, for the layout pass
     * @return the new {@code categories}/{@code displayedItems} with our category prepended and laid out
     */
    public static Result prepend(String title,
                                 List<BigItemStack> matches,
                                 List<CategoryEntry> categories,
                                 List<List<BigItemStack>> displayedItems,
                                 int rowHeight,
                                 int cols) {
        // Create empties `categories` when there are no user-defined ones; rebuild neutral
        // placeholders so it stays index-aligned with displayedItems before we prepend ours.
        List<CategoryEntry> newCategories;
        if (categories.size() != displayedItems.size()) {
            newCategories = new ArrayList<>();
            for (int i = 0; i < displayedItems.size(); i++) {
                newCategories.add(new CategoryEntry(-1, "", 0));
            }
        } else {
            newCategories = new ArrayList<>(categories);
        }

        // Prepend our category at the top of both parallel lists.
        newCategories.addFirst(new CategoryEntry(-1, title, 0));
        List<List<BigItemStack>> newDisplayed = new ArrayList<>();
        newDisplayed.add(matches);
        newDisplayed.addAll(displayedItems);

        layout(newCategories, newDisplayed, rowHeight, cols);
        return new Result(newCategories, newDisplayed);
    }

    /**
     * Recompute vertical offsets — mirrors Create's own layout math in refreshSearchResults.
     */
    private static void layout(List<CategoryEntry> categories,
                               List<List<BigItemStack>> displayedItems,
                               int rowHeight,
                               int cols) {
        int y = 0;
        for (int i = 0; i < displayedItems.size(); i++) {
            CategoryEntryAccessor accessor = (CategoryEntryAccessor) categories.get(i);
            accessor.emicreatecompat$setY(y);
            List<BigItemStack> items = displayedItems.get(i);
            if (items.isEmpty()) {
                continue;
            }
            y += rowHeight;
            if (!accessor.emicreatecompat$isHidden()) {
                y += (int) Math.ceil(items.size() / (float) cols) * rowHeight;
            }
        }
    }

    /**
     * The rebuilt screen state to assign back onto the Stock Keeper's {@code categories} and
     * {@code displayedItems} fields.
     */
    public record Result(List<CategoryEntry> categories, List<List<BigItemStack>> displayedItems) {
    }
}
