package com.domy.zoomanagement.managers;

import java.security.SecureRandom;

public enum VisitorType {

    BABY(0), CHILD(0.5f), ADULT(1), SENIOR(0.5f);
    float discount;

    VisitorType(float discount) {
        this.discount = discount;
    }

    public static VisitorType getRandomType() {
        return VisitorType.values()[new SecureRandom().nextInt(VisitorType.values().length)];
    }

    public float getDiscount() {
        return discount;
    }
}
