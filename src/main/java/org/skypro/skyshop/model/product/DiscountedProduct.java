package org.skypro.skyshop.model.product;

import java.util.UUID;

public class DiscountedProduct extends Product {
    private final int basePrice;
    private final int discountPercent;

    public DiscountedProduct(UUID id, String name, int basePrice, int discountPercent) {
        super(id, name);
        if (basePrice <= 0) {
            throw new IllegalArgumentException(
                    "Базовая цена продукта со скидкой должна быть строго больше 0."
            );
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException(
                    "Процент скидки должен быть в диапазоне от 0 до 100 включительно."
            );
        }
        this.basePrice = basePrice;
        this.discountPercent = discountPercent;
    }

    @Override
    public int getPrice() {
        return basePrice * (100 - discountPercent) / 100;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public String toString() {
        return getName() + ": " + getPrice() + " (" + discountPercent + "%)";
    }
}