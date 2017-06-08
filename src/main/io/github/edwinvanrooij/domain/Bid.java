package io.github.edwinvanrooij.domain;

import io.github.edwinvanrooij.domain.engine.Camel;

/**
 * Created by eddy
 * on 6/8/17.
 */
public class Bid {
    private Camel camel;
    private int value;

    public Camel getCamel() {
        return camel;
    }

    public void setCamel(Camel camel) {
        this.camel = camel;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Bid(Camel camel, int value) {
        this.camel = camel;
        this.value = value;
    }
}
