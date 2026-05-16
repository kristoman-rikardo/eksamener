package com.shopstore.retail.part5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import no.ntnu.tdt4100.IProduct;
import no.ntnu.tdt4100.part5.ProductChange;
import no.ntnu.tdt4100.part5.ProductChangeListener;
import no.ntnu.tdt4100.part5.ProductInventory;

/**
 * Manages the inventory of products. The class should contain information about
 * stock levels for {@link IProduct} objects, and should be able to notify
 * listeners of changes to changes in products. This class should also implement
 * the {@link no.ntnu.tdt4100.part5.ProductInventory} interface in order to
 * handle listeners. See the interface specification for details on the methods
 * to implement from this interface.
 *
 * @see ProductInventoryTest
 * @see ProductInventory
 * @see IProduct
 * @see ProductChangeListener
 * @see ProductChange
 */
public class ProductInventoryManager implements ProductInventory {
    private List<ProductChangeListener> listeners = new ArrayList<>();
    private Map<IProduct, Integer> productStock;
    // TODO: Add the necessary fields to store the products and their stock levels,
    // as well as the listeners

    /**
     * Constructs a new ProductInventoryManager with a predefined set of
     * products and their stock levels. This is given as a parameter
     * <code>productStock</code> of type {@link Map}, with
     * {@link IProduct} objects as keys and {@link Integer} objects as values,
     * representing the current stock level.
     *
     * @param productStock A {@link Map} of {@link IProduct} to their corresponding
     *                     stock levels, represented with {@link Integer}.
     * @throws IllegalArgumentException If any of the stock levels in
     *                                  <code>productStock</code> are negative.
     */
    public ProductInventoryManager(Map<IProduct, Integer> productStock) {
        if (productStock.values().stream().anyMatch(v -> v < 0)) throw new IllegalArgumentException("Some item levels are negative");
        this.productStock = productStock;
    }

    /**
     * Increases the stock level of a specified product by a given quantity. If the
     * product is not already in the inventory, it should be added with the given
     * quantity. If the product was not already in the inventory, or if the stock
     * was previously zero, it notifies listeners that the product is now available
     * by invoking their
     * {@link ProductChangeListener#productChanged(IProduct, ProductChange)} method
     * with a ProductChange of type {@link ProductChange#NOW_AVAILABLE}.
     *
     * @param product  The {@link IProduct} whose stock is to be increased.
     * @param quantity The amount by which to increase the stock, as a primitive
     *                 int.
     * @throws IllegalArgumentException if {@code quantity} is less than 1
     * 
     * @see ProductChange#NOW_AVAILABLE
     * @see ProductChangeListener#productChanged(IProduct, ProductChange)
     * @see IProduct
     */
    public void increaseStock(IProduct product, int quantity) {
        if (product == null || quantity < 1) throw new IllegalArgumentException("Quantity must be above 0");
        if (!this.productStock.containsKey(product) || this.productStock.get(product) < 1) {
            this.productStock.put(product, quantity);
            this.listeners.stream().forEach(l -> l.productChanged(product, ProductChange.NOW_AVAILABLE));
        }
        else {
            this.productStock.put(product, this.productStock.get(product) + quantity);
        }
    }

    /**
     * Reduces the stock level of a specified product by a given quantity.
     * If the product's stock reaches zero, it notifies listeners that the product
     * is out of stock by invoking their
     * {@link ProductChangeListener#productChanged(IProduct, ProductChange)} method
     * with a ProductChange of type {@link ProductChange#OUT_OF_STOCK}.
     *
     * @param product  The {@link IProduct} whose stock is to be reduced.
     * @param quantity The amount by which to reduce the stock, as a primitive int.
     * @throws IllegalArgumentException if the product is not in this
     *                                  inventory or if {@code quantity} is less
     *                                  than 1 or greater than the current stock
     *                                  level.
     * 
     * @see ProductChange#OUT_OF_STOCK
     * @see ProductChangeListener#productChanged(IProduct, ProductChange)
     * @see IProduct
     */
    public void reduceStock(IProduct product, int quantity) {
        if (product == null || quantity < 1 || !this.productStock.containsKey(product) || this.productStock.get(product) - quantity < 0) throw new IllegalArgumentException("Quantity must be above 0");
        if (this.productStock.get(product) - quantity == 0) {
            this.productStock.remove(product);
            this.listeners.stream().forEach(l -> l.productChanged(product, ProductChange.OUT_OF_STOCK));
        }
        else {
            this.productStock.put(product, this.productStock.get(product) - quantity);
        }
    }

    // TODO: Implement any other required methods from the ProductInventory
    // interface
    @Override
    public void addListener(ProductChangeListener listener) {
        if (listener == null ) throw new IllegalArgumentException();
        if (listeners.contains(listener)) return;
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(ProductChangeListener listener) {
        if (listener == null || !this.listeners.contains(listener)) return;
        this.listeners.remove(listener);
    }

    @Override
    public int getStock(IProduct product) {
        if (product == null || !this.productStock.containsKey(product)) return 0;
        return this.productStock.get(product);
    }

}
