package com.paessler.prtg.jmx.channels;

public class LongChannel extends Channel {
    long value;

    public void setValue(long val){value = val;}

    public long toLong(){return value;}
    public LongChannel(String name, Unit unit, long value, Mode mode) {
        super(name, unit, mode);
        setValue(value);
    }
    public LongChannel(String name, Unit unit, long value) {
        this(name, unit, value, Mode.INTEGER);
    }
    public LongChannel(String name, String unit, long value) {
        this(name, toUnit(unit), value);
    }
}
