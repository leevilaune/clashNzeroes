package com.onesnzeroes.clashnzeroes.model;

public class TsField<F> {

    public long ts;
    public F field;

    public TsField(long ts, F field){
        this.ts = ts;
        this.field = field;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public F getField() {
        return field;
    }

    public void setField(F field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "TsField{" +
                "ts=" + ts +
                ", field=" + field +
                '}';
    }
}
