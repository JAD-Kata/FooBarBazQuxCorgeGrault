package com.jad;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


class CorgeTest {
    private static final int NB_OF_FIELDS = 1;
    private static final int NB_OF_NON_CONSTRUCTOR_METHOD = 2;
    private static Class<?> corgeClass;

    @BeforeAll
    static void beforeAll() {
        CorgeTest.corgeClass = Utils.testIfClassExists("com.jad.Corge");
    }

    @Test
    void classTest() {
        this.topLevelClassTest();
        this.countAttributesTest();
        this.countMethodsTest();
    }

    @Test
    void topLevelClassTest() {
        Utils.testIfIsATopLevelClass(CorgeTest.corgeClass);
    }

    @Test
    void countAttributesTest() {
        Utils.testCountAttributes(CorgeTest.corgeClass, CorgeTest.NB_OF_FIELDS);
    }

    @Test
    void countMethodsTest() {
        Utils.testCountNonConstructorMethods(CorgeTest.corgeClass, CorgeTest.NB_OF_NON_CONSTRUCTOR_METHOD);
    }

    @Test
    void relationsTest() {
        this.fooAttributeTest();
        this.constructorTest();
        this.getFooAndSetFooTest();
    }

    @Test
    void fooAttributeTest() {
        Utils.testIfPrivateAttributeExistsWithGoodType("com.jad.Foo", "foo", CorgeTest.corgeClass);
    }

    @Test
    void constructorTest() {
        Class<?> barClass = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Bar"),
                                               "The class Bar does not exist.");
        Class<?> fooClass = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Foo"),
                                               "The class Foo does not exist.");
        Constructor<?> fooConstructor = assertDoesNotThrow(() -> fooClass.getDeclaredConstructor(barClass),
                                                           "The constructor of Foo does not exist.");
        Constructor<?> corgeConstructor = assertDoesNotThrow(
                () -> CorgeTest.corgeClass.getDeclaredConstructor(fooClass),
                "The constructor of Corge does not exist.");
        Object barInstance = assertDoesNotThrow(() -> barClass.getDeclaredConstructor().newInstance(),
                                                "Failed to create an instance of Bar.");
        Object fooInstance = assertDoesNotThrow(() -> fooConstructor.newInstance(barInstance),
                                                "Failed to create an instance of Foo.");
        Object corgeInstance = assertDoesNotThrow(() -> corgeConstructor.newInstance(fooInstance),
                                                  "Failed to create an instance of Corge.");
        Field fooField = assertDoesNotThrow(() -> CorgeTest.corgeClass.getDeclaredField("foo"),
                                            "The field 'foo' does not exist.");
        fooField.setAccessible(true);
        Object fooFieldValue = assertDoesNotThrow(() -> fooField.get(corgeInstance),
                                                  "Failed to get the value of the field 'foo'.");
        assertEquals(fooInstance, fooFieldValue, "The field 'foo' is not initialized with the constructor parameter.");
    }

    @Test
    void getFooAndSetFooTest() {
        Class<?> barClass = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Bar"),
                                               "The class Bar does not exist.");
        Class<?> fooClass = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Foo"),
                                               "The class Foo does not exist.");
        Method getFooMethod = assertDoesNotThrow(() -> CorgeTest.corgeClass.getDeclaredMethod("getFoo"),
                                                 "The method 'getFoo' does not exist.");
        Method setFooMethod = assertDoesNotThrow(() -> CorgeTest.corgeClass.getDeclaredMethod("setFoo", fooClass),
                                                 "The method 'setFoo' does not exist.");
        Method getCorgeMethod = assertDoesNotThrow(() -> fooClass.getDeclaredMethod("getCorge"),
                                                   "The method 'getCorge' does not exist.");

        // Create instances of Bar
        Object barInstance1 = assertDoesNotThrow(() -> barClass.getDeclaredConstructor().newInstance(),
                                                 "Failed to create an instance of Bar.");
        Object barInstance2 = assertDoesNotThrow(() -> barClass.getDeclaredConstructor().newInstance(),
                                                 "Failed to create an instance of Bar.");

        // Create instances of Foo
        Object fooInstance1 = assertDoesNotThrow(
                () -> fooClass.getDeclaredConstructor(barInstance1.getClass()).newInstance(barInstance1),
                "Failed to create an instance of Foo.");
        Object fooInstance2 = assertDoesNotThrow(
                () -> fooClass.getDeclaredConstructor(barInstance2.getClass()).newInstance(barInstance2),
                "Failed to create an instance of Foo.");

        // Create an instance of Corge
        Constructor<?> corgeConstructor = assertDoesNotThrow(
                () -> CorgeTest.corgeClass.getDeclaredConstructor(fooClass),
                "The constructor of Corge does not exist.");
        Object corgeInstance = assertDoesNotThrow(() -> corgeConstructor.newInstance(fooInstance1),
                                                  "Failed to create an instance of Corge.");

        // Verify the initial Foo instance
        Object returnedFoo1 = assertDoesNotThrow(() -> getFooMethod.invoke(corgeInstance),
                                                 "Failed to invoke the method 'getFoo' on the Corge instance.");
        assertEquals(fooInstance1, returnedFoo1, "The method 'getFoo' did not return the correct Foo instance.");

        // Set the second Foo instance
        assertDoesNotThrow(() -> setFooMethod.invoke(corgeInstance, fooInstance2),
                           "Failed to invoke the method 'setFoo' with the second Foo instance.");
        Object returnedFoo2 = assertDoesNotThrow(() -> getFooMethod.invoke(corgeInstance),
                                                 "Failed to invoke the method 'getFoo' after setting the second Foo instance.");
        assertEquals(fooInstance2, returnedFoo2,
                     "The method 'getFoo' did not return the correct Foo instance after setting the second Foo instance.");

        // Verify the first Foo instance's getCorge method returns null
        Object returnedCorge1 = assertDoesNotThrow(() -> getCorgeMethod.invoke(fooInstance1),
                                                   "Failed to invoke the method 'getCorge' on the first Foo instance after setting the second Foo instance.");
        assertNull(returnedCorge1,
                   "The method 'getCorge' on the first Foo instance should return null after setting the second Foo instance.");
    }
}