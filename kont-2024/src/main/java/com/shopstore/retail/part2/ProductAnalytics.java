package com.shopstore.retail.part2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import no.ntnu.tdt4100.IProduct;
import no.ntnu.tdt4100.ProductDiscount;
import no.ntnu.tdt4100.ProductOrder;

/**
 * Provides valuable product- and discount-related insights by filtering and
 * aggregating data.
 */
public class ProductAnalytics {

    /**
     * Returns a count of the number of {@link no.ntnu.tdt4100.IProduct} objects in
     * a list of products that matches a {@link Predicate} of type
     * Predicate<IProduct>.
     *
     * @param products  A {@link List} containing objects of type {@link IProduct}.
     * @param predicate The {@link Predicate} object to test with
     *                  (Predicate<IProduct>).
     * @return A count (of the primitive type int) of the number of products
     *         matching the predicate.
     * 
     * @see Predicate
     * @see Predicate#test(IProduct)
     * @see IProduct
     * @see List
     */
    // TODO: Implement the getNumberOfProductsByPredicate method
    // TODO: The parameters of the method should be in the following order:
    // products, predicate

    public int getNumberOfProductsByPredicate(List<IProduct> products, Predicate<IProduct> predicate) {
        if (products == null || predicate == null) throw new IllegalArgumentException();
        int c = 0;
        for (IProduct product : products) {
            if (predicate.test(product)) c++;
        }
        return c;
    }

    /**
     * Returns only the discounts applicable to at least one product in a
     * {@link no.ntnu.tdt4100.ProductOrder} object. The list of products in a
     * product order can be retrieved by calling the {@link ProductOrder#getItems()}
     * method on the product order. Whether a discount is applicable to a product or
     * not can be checked by calling the
     * {@link ProductDiscount#isApplicableTo(IProduct)} method on the
     * {@link ProductDiscount} object.
     *
     * @param productOrder A {@link ProductOrder} object.
     * @param discounts    A {@link List} containing objects of type
     *                     {@link ProductDiscount}.
     * @return A {@link List} containing objects of type {@link ProductDiscount},
     *         applicable to at least one product in the product order.
     * 
     * @see ProductOrder#getItems()
     * @see Map#keySet()
     */
    // TODO: Implement the getDiscountsApplicableForProductOrder method
    // TODO: The parameters of the method should be in the following order:
    // productOrder, discounts
    public List<ProductDiscount> getDiscountsApplicableForProductOrder(ProductOrder productOrder, List<ProductDiscount> discounts) {
        if (productOrder == null) throw new IllegalArgumentException();
        Map<IProduct, Integer> order = productOrder.getItems();
        List<ProductDiscount> applicableDiscounts = new ArrayList<>();
        for (IProduct product : order.keySet()) {
            for (ProductDiscount discount : discounts) {
                if (discount.isApplicableTo(product)) applicableDiscounts.add(discount);
            }
        }
        return applicableDiscounts;
    }


    /**
     * Finds and returns the vendor with the most amount of products in a list of
     * products. The vendor is defined by a String, and can be found by calling the
     * {@link IProduct#getVendor()} method on the product.
     *
     * @param products A {@link List} containing objects of type {@link IProduct}.
     * @return The name of the most popular vendor as a {@link String}, or null if
     *         the list is empty.
     * 
     * @see IProduct#getVendor()
     */
    // TODO: Implement the getMostPopularVendor method

    public String getMostPopularVendor(List<IProduct> products) {
        if (products == null) throw new IllegalArgumentException();
        if (products.size() < 1) return null;
        HashMap<String, Integer> vendorsAssortment = new HashMap<>();
        for (IProduct product : products) {
            String vendorName = product.getVendor();
            if (vendorsAssortment.containsKey(vendorName)) {
                vendorsAssortment.put(vendorName, vendorsAssortment.get(vendorName) + 1);
            }
            else {
                vendorsAssortment.put(vendorName, 1);
            }
        }
        String maxLoc = null;
        int maxVal = 0;
        for (Map.Entry<String, Integer> vendor : vendorsAssortment.entrySet()) {
            if (maxVal < vendor.getValue()) {
                maxVal = vendor.getValue();
                maxLoc = vendor.getKey();
            }
        }
        return maxLoc;
    }
}
