package com.jad;

import java.util.ArrayList;

public class Foo {
    private Bar bar;
    private ArrayList<Baz> bazs;
    private Qux qux;
    private Corge corge;
    private ArrayList<Grault> graults;

    public Foo(Bar bar) {
        this.bar = bar;
        this.qux = new Qux();
        this.bazs = new ArrayList<Baz>();
        this.graults = new ArrayList<Grault>();
    }

    public void addBaz(Baz baz) {
        this.bazs.add(baz);
    }
    public void addGrault() {
        Grault new_grault = new Grault(this);
        this.graults.add(new_grault);
    }

    public Bar getBar() {
        return this.bar;
    }

    public ArrayList<Baz> getBazs() {
        return this.bazs;
    }

    public Qux getQux() {
        return this.qux;
    }

    public Corge getCorge() {
        return this.corge;
    }

    public ArrayList<Grault> getGraults() {
        return this.graults;
    }

    public void setCorge(Corge corge) {
        this.corge = corge;

        if (corge != null && corge.getFoo() != this) corge.setFoo(this);
    }
}
