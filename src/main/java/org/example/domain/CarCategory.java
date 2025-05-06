package org.example.domain;

import lombok.Getter;

@Getter
public enum CarCategory {
    F1(1),
    RALLY(2),
    SPORTS(3),
    DRAG(4);

    private final int value;

    CarCategory(int value) {
        this.value = value;
    }

    public static CarCategory fromValue(int value) {
        for (CarCategory category : CarCategory.values()) {
            if (category.getValue() == value) {  // Fixed to compare with 'value'
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid CarCategory value: " + value);
    }
}
