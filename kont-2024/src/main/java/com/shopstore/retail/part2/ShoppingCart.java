package com.shopstore.retail.part2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.ntnu.tdt4100.Customer;
import no.ntnu.tdt4100.IProduct;
import no.ntnu.tdt4100.ProductDiscount;
import no.ntnu.tdt4100.ProductOrder;

/**
 * Manages the checkout process in a shopping system.
 * This class must implement the {@link no.ntnu.tdt4100.ProductOrder}
 * interface. It should contain a collection of {@link no.ntnu.tdt4100.IProduct}
 * objects and it should be able to add and remove a quantity of products from
 * the shopping cart. The class should also be able to hold multiple
 * {@link no.ntnu.tdt4100.ProductDiscount} objects that can be applied to the
 * products in the shopping cart.
 * 
 * In addition to the methods required by the interface, the class should have
 * some additional methods for managing the shopping cart that you will need to
 * implement, marked with JavaDoc and TODO comments below.
 * 
 */
public class ShoppingCart implements ProductOrder {
    private Customer cutsomer;
    private Map<IProduct, Integer> shoppingCart = new HashMap<>();
    private List<ProductDiscount> discountsUsed = new ArrayList<>();
    private Set<ProductDiscount> discountsAdded = new HashSet<>();
    /**
     * Creates a new shopping cart for a customer. An
     * {@link IllegalArgumentException} should be thrown if the customer is null.
     *
     * @param customer The customer to create the shopping cart for, of type
     *                 {@link no.ntnu.tdt4100.Customer}.
     * 
     * @throws IllegalArgumentException If the customer is null.
     * 
     * @see no.ntnu.tdt4100.Customer
     */
    // xTODO: Implement the ShoppingCart constructor
    public ShoppingCart(Customer customer) {
        if (customer == null) throw new IllegalArgumentException();
        this.cutsomer = customer;
    }


    /**
     * This method should return the customer associated with the order.
     *
     * @return The customer associated with the order, of type {@link Customer}.
     */
    public Customer getCustomer() {
        return this.cutsomer;
    }

    /**
     * Returns the total price of all items in the order. This should be the sum of
     * the prices of all items in the ProductOrder, minus any discounts that might
     * apply to the various products. In the case of multiple discounts applying to
     * the same product, only the discount that gives the biggest reduction in price
     * should be applied.
     *
     * @return The total price of all items in the order minus any discounts. The
     *         price should be of the primitive type double.
     * 
     * @see no.ntnu.tdt4100.ProductDiscount#applyDiscount(IProduct)
     * @see no.ntnu.tdt4100.ProductDiscount#isApplicableTo(IProduct)
     * @see no.ntnu.tdt4100.ProductDiscount#getDiscountPercentage()
     */
    public double getTotal() {
        for (ProductDiscount discount : discountsAdded) { // apply all (unique) discounts
            for (Map.Entry<IProduct, Integer> entry : shoppingCart.entrySet()) { // loop all products for each discount and apply
                IProduct product = entry.getKey();
                if (discount.isApplicableTo(product)) {
                    discount.applyDiscount(product); 
                }
            }
        }
        double total = 0.0;
        for (Map.Entry<IProduct, Integer> entry : shoppingCart.entrySet()) { // assuming one can add the same discount to several products, just not different rounds
            IProduct product = entry.getKey();
            int quantity = entry.getValue();
            total += product.getPrice() * quantity;
        }
        return total;
    }

    /**
     * This method should return a map of all items in the order, with their
     * corresponding quantities. The keys in the map should be the products, and
     * these must be unique. The values of the map should be the quantities of the
     * products added to the order, which must be positive integers (greater than
     * 0).
     *
     * @return A {@link Map} of all unique products as keys of type
     *         {@link IProduct} and the quantities of the products as values of
     *         type {@link Integer}.
     */
    public Map<IProduct, Integer> getItems() {
        return this.shoppingCart;
    }
    
    /**
     * Adds a quantity of a product to the shopping cart.
     *
     * @param product  The product to add, of type {@link IProduct}.
     * @param quantity The quantity of the product to add, of the primitive type
     *                 int.
     * @throws IllegalArgumentException If the quantity to add is less than 1.
     * 
     * @see no.ntnu.tdt4100.IProduct
     */
    // xTODO: Implement the addItem method
    // xTODO: The parameters of the method should be in the following order:
    // product, quantity
    public void addItem(IProduct product, int quantity) {
        if (product == null || quantity < 1) throw new IllegalArgumentException();
        this.shoppingCart.merge(product, 1, Integer::sum);
    }

    /**
     * Removes a quantity of a product from the shopping cart.
     * 
     * If the quantity to remove is equal to or greater than the quantity of the
     * product in the cart, the product should be removed entirely from the cart.
     * 
     * @param product  The product to remove, of type {@link IProduct}.
     * @param quantity The quantity of the product to remove, of the primitive type
     *                 int.
     * @throws IllegalArgumentException If the {@link IProduct} is not in the cart
     *                                  or the quantity to remove is less than 1.
     * 
     * @see no.ntnu.tdt4100.IProduct
     */
    // xTODO: Implement the removeItem method
    // xTODO: The parameters of the method should be in the following order:
    // product, quantity
    public void removeItem(IProduct product, int quantity) {
        if (product == null || !this.shoppingCart.containsKey(product) || quantity < 1) {
            throw new IllegalArgumentException();
        }
        int diff = this.shoppingCart.get(product) - quantity;
        if (diff <= 0) {
            this.shoppingCart.remove(product);
        }
        else {
            this.shoppingCart.put(product, diff);
        }
    }

    /**
     * Adds a discount to the shopping cart. The shopping cart should be able to
     * apply multiple discounts, and these discounts should stack, e.g. you should
     * be able to apply a 10% discount and then afterwards another 20% discount
     * which both apply to the total price.
     *
     * @param discount The discount to add, of type {@link ProductDiscount}.
     * @throws IllegalArgumentException If the discount has already been added
     * 
     * @see no.ntnu.tdt4100.ProductDiscount
     */
    // xTODO: Implement the addDiscount method
    public void addDiscount(ProductDiscount discount) {
        if (discount == null || getDiscounts().contains(discount)) throw new IllegalArgumentException();
        for (Map.Entry<IProduct, Integer> entry : shoppingCart.entrySet()) { // assuming one can add the same discount to several products, just not different rounds
            IProduct product = entry.getKey();
            // int quantity = entry.getValue(); if needed in refinements
            if (discount.isApplicableTo(product)) {
                discountsAdded.add(discount);
            }
        }
        this.discountsUsed.add(discount);

    }

    /**
     * Gets all discounts currently added to the shopping cart.
     *
     * @return A list of all discounts, of type {@link ProductDiscount}.
     * 
     * @see no.ntnu.tdt4100.ProductDiscount
     */
    // TODO: Implement the getDiscounts method
    public List<ProductDiscount> getDiscounts() {
        return this.discountsUsed;
    }

    // xTODO: Implement any other methods required by the ProductOrder interface

}
