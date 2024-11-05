package com.jad;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FooTest {
    @Test
    void barTest() {
        Bar bar = new Bar();
        Foo foo = new Foo(bar);
        assertEquals(bar, foo.getBar(), "Bar should be the same as the one passed to the constructor");
    }

    @Test
    void bazTest() {
        final int bazCount = 10;
        ArrayList<Baz> bazs = new ArrayList<>();
        for (int i = 0; i < bazCount; i++) {
            bazs.add(new Baz());
        }
        Foo foo = new Foo(new Bar());
        assertEquals(0, foo.getBazs().size(), "Bazs should be empty by default");
        for (Baz baz : bazs) {
            foo.addBaz(baz);
        }
        assertEquals(bazCount, foo.getBazs().size(), "Bazs should have " + bazCount + " elements");
        assertTrue(foo.getBazs().containsAll(bazs), "Bazs should contain all the Bazs that were added");
    }

    @Test
    void quxTest() {
        Foo foo = new Foo(new Bar());
        assertNotNull(foo.getQux(), "Qux should not be null");
    }

    @Test
    void graultTest() {
        final int bazGrault = 10;
        Foo foo = new Foo(new Bar());
        assertEquals(0, foo.getGraults().size(), "Graults should be empty by default");
        for (int i = 0; i < bazGrault; i++) {
            foo.addGrault();
        }
        assertEquals(bazGrault, foo.getGraults().size(), "Graults should have " + bazGrault + " elements");
        for (Grault grault : foo.getGraults()) {
            assertNotNull(grault, "Grault should not be null");
            assertEquals(foo, grault.getFoo(), "Grault should have a reference to the Foo that created it");
        }
    }

    @Test
    void corgeTest() {
        Foo foo = new Foo(new Bar());
        Corge firstCorge = new Corge(foo);
        assertEquals(foo, firstCorge.getFoo(), "Foo should be the same as the one passed to the constructor");
        assertEquals(firstCorge, foo.getCorge(), "Corge should be the same as the one passed to the constructor");
        Corge secondCorge = new Corge(foo);
        assertEquals(foo, secondCorge.getFoo(), "Foo should be the same as the one passed to the constructor");
        assertEquals(secondCorge, foo.getCorge(), "Corge should be the same as the one passed to the constructor");
        assertNull(firstCorge.getFoo(), "Foo should be null after the second Corge is created");
        foo.setCorge(firstCorge);
        assertEquals(firstCorge, foo.getCorge(), "Corge should be the same as the one passed to the setter");
        assertEquals(foo, firstCorge.getFoo(), "Foo should be the same as the one passed to the setter");
        assertNull(secondCorge.getFoo(), "Foo should be null after the first Corge is set to a different Foo");
        secondCorge.setFoo(foo);
        assertEquals(foo, secondCorge.getFoo(), "Foo should be the same as the one passed to the setter");
        assertEquals(secondCorge, foo.getCorge(), "Corge should be the same as the one passed to the setter");
        assertNull(firstCorge.getFoo(), "Foo should be null after the second Corge is set to a different Foo");
    }
}
