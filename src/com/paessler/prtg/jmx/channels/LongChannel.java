package com.paessler.prtg.jmx.channels;

public class LongChannel extends Channel {
    long value;

    public LongChannel(String name, String unit, long value) {
        super(name, unit, "integer");
        this.value = value;
    }
}
