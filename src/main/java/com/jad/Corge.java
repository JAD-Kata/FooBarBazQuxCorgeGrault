package com.jad;

public class Corge {
    private Foo foo;

    public Corge(final Foo foo) {
        this.setFoo(foo);
    }

    public Foo getFoo() {
        return this.foo;
    }

    public void setFoo(final Foo foo) {
        this.foo = foo;
        if (this.foo != null && this.foo.getCorge() != this) {
            this.foo.setCorge(this);
        }
    }
}
