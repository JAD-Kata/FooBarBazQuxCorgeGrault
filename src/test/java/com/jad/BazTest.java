package com.jad;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class BazTest {
    private static final int NB_OF_FIELDS = 0;
    private static final int NB_OF_NON_CONSTRUCTOR_METHOD = 0;
    private static Class<?> bazClass;

    @BeforeAll
    static void beforeAll() {
        BazTest.bazClass = Utils.testIfClassExists("com.jad.Baz");
    }

    @Test
    void classTest() {
        this.topLevelClassTest();
        this.countAttributesTest();
        this.countMethodsTest();
    }

    @Test
    void topLevelClassTest() {
        Utils.testIfIsATopLevelClass(BazTest.bazClass);
    }

    @Test
    void countAttributesTest() {
        Utils.testCountAttributes(BazTest.bazClass, BazTest.NB_OF_FIELDS);
    }

    @Test
    void countMethodsTest() {
        Utils.testCountNonConstructorMethods(BazTest.bazClass, BazTest.NB_OF_NON_CONSTRUCTOR_METHOD);
    }
}