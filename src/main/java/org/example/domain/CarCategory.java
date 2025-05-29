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
}
