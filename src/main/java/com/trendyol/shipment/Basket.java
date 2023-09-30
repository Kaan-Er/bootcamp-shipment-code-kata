package com.trendyol.shipment;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Basket {

    private List<Product> products;

    private static final int THRESHOLD_FOR_UPPER_SIZE = 3;

    public ShipmentSize getShipmentSize() {
        if (products.size() < THRESHOLD_FOR_UPPER_SIZE) {
            return findMaxShipmentSize(products);
        }

        Map<ShipmentSize, Long> sizeCounts = countShipmentSizes(products);

        if (hasEnoughSameSizeProducts(sizeCounts, ShipmentSize.SMALL)) {
            return ShipmentSize.MEDIUM;
        } else if (hasEnoughSameSizeProducts(sizeCounts, ShipmentSize.MEDIUM)) {
            return ShipmentSize.LARGE;
        } else if (hasEnoughSameSizeProducts(sizeCounts, ShipmentSize.LARGE)) {
            return ShipmentSize.X_LARGE;
        } else {
            return findMaxShipmentSize(products);
        }
    }

    private Map<ShipmentSize, Long> countShipmentSizes(List<Product> products) {
        return products.stream()
                .map(Product::getSize)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private boolean hasEnoughSameSizeProducts(Map<ShipmentSize, Long> sizeCounts, ShipmentSize size) {
        return sizeCounts.containsKey(size) && sizeCounts.get(size) >= THRESHOLD_FOR_UPPER_SIZE;
    }

    private ShipmentSize findMaxShipmentSize(List<Product> products) {
        return products.stream()
                .map(Product::getSize)
                .max(ShipmentSize::compareTo)
                .orElse(ShipmentSize.SMALL);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
