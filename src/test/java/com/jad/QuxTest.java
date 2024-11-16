package com.jad;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class QuxTest {
    private static final int NB_OF_FIELDS = 0;
    private static final int NB_OF_NON_CONSTRUCTOR_METHOD = 0;
    private static Class<?> quxClass;

    @BeforeAll
    static void beforeAll() {
        QuxTest.quxClass = Utils.testIfClassExists("com.jad.Qux");
    }

    @Test
    void classTest() {
        this.topLevelClassTest();
        this.countAttributesTest();
        this.countMethodsTest();
    }

    @Test
    void topLevelClassTest() {
        Utils.testIfIsATopLevelClass(QuxTest.quxClass);
    }

    @Test
    void countAttributesTest() {
        Utils.testCountAttributes(QuxTest.quxClass, QuxTest.NB_OF_FIELDS);
    }

    @Test
    void countMethodsTest() {
        Utils.testCountNonConstructorMethods(QuxTest.quxClass, QuxTest.NB_OF_NON_CONSTRUCTOR_METHOD);
    }
}