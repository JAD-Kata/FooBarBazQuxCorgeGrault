package com.jad;

public class Corge {
    private Foo foo;

    public Corge(Foo foo) {
        this.foo = foo;
        foo.setCorge(this);
    }

    public void setFoo(Foo foo) {
        this.foo.setCorge(null);
        this.foo = foo;
        this.foo.setCorge(this);
    }

    public Foo getFoo() {
        return this.foo;
    }
}
