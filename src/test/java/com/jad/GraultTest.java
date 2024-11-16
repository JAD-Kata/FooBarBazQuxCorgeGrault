package com.jad;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;


class GraultTest {
    private static final int NB_OF_NON_CONSTRUCTOR_METHOD = 1;
    private static final int NB_OF_FIELDS = 1;
    private static final int NB_OF_CONSTRUCTOR = 1;
    private static final int NB_CONSTRUCTOR_PARAMETERS = 1;
    private static Class<?> graultClass;

    @BeforeAll
    static void beforeAll() {
        GraultTest.graultClass = Utils.testIfClassExists("com.jad.Grault");
    }

    @Test
    void classTest() {
        this.topLevelClassTest();
        this.countAttributesTest();
        this.countMethodsTest();

    }

    @Test
    void topLevelClassTest() {
        Utils.testIfIsATopLevelClass(GraultTest.graultClass);
    }

    @Test
    void countAttributesTest() {
        Utils.testCountAttributes(GraultTest.graultClass, GraultTest.NB_OF_FIELDS);
    }

    @Test
    void countMethodsTest() {
        Utils.testCountNonConstructorMethods(GraultTest.graultClass, GraultTest.NB_OF_NON_CONSTRUCTOR_METHOD);
    }

    @Test
    void relationsTest() {
        this.fooAttributeTest();
        this.constructorTest();
        this.getFooTest();
    }

    @Test
    void fooAttributeTest() {
        Utils.testIfPrivateAttributeExistsWithGoodType("com.jad.Foo", "foo", GraultTest.graultClass);
    }

    @Test
    void constructorTest() {
        Class<?> barClass = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Bar"),
                                               "The class Bar does not exist.");
        Class<?> fooClass = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Foo"),
                                               "The class Foo does not exist.");
        Constructor<?> fooConstructor = assertDoesNotThrow(() -> fooClass.getDeclaredConstructor(barClass),
                                                           "The constructor of Foo does not exist.");

        Constructor<?>[] constructors = GraultTest.graultClass.getDeclaredConstructors();
        assertEquals(GraultTest.NB_OF_CONSTRUCTOR, constructors.length,
                     "The class Grault should have only " + GraultTest.NB_OF_CONSTRUCTOR + " constructor.");
        Constructor<?> constructor = constructors[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        assertEquals(GraultTest.NB_CONSTRUCTOR_PARAMETERS, parameterTypes.length,
                     "The constructor of Grault should have " + GraultTest.NB_CONSTRUCTOR_PARAMETERS + " parameter.");
        assertEquals(fooClass, parameterTypes[0], "The parameter of the constructor should be of type Foo.");
        Object barInstance = assertDoesNotThrow(() -> barClass.getDeclaredConstructor().newInstance(),
                                                "Failed to create an instance of Bar.");
        Object fooInstance = assertDoesNotThrow(() -> fooConstructor.newInstance(barInstance),
                                                "Failed to create an instance of Foo.");
        Object graultInstance = assertDoesNotThrow(() -> constructor.newInstance(fooInstance),
                                                   "Failed to create an instance of Grault.");
        Field fooField = assertDoesNotThrow(() -> GraultTest.graultClass.getDeclaredField("foo"),
                                            "The field 'foo' does not exist.");
        fooField.setAccessible(true);
        Object fooFieldValue = assertDoesNotThrow(() -> fooField.get(graultInstance),
                                                  "Failed to get the value of the field 'foo'.");
        assertEquals(fooInstance, fooFieldValue, "The field 'foo' is not initialized with the constructor parameter.");
    }

    @Test
    void getFooTest() {
        Class<?> barClass = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Bar"),
                                               "The class Bar does not exist.");
        Class<?> fooClass = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Foo"),
                                               "The class Foo does not exist.");
        Constructor<?> fooConstructor = assertDoesNotThrow(() -> fooClass.getDeclaredConstructor(barClass),
                                                           "The constructor of Foo does not exist.");
        Constructor<?> constructor = assertDoesNotThrow(() -> GraultTest.graultClass.getDeclaredConstructor(fooClass),
                                                        "The constructor of Grault does not exist.");
        Object barInstance = assertDoesNotThrow(() -> barClass.getDeclaredConstructor().newInstance(),
                                                "Failed to create an instance of Bar.");
        Object fooInstance = assertDoesNotThrow(() -> fooConstructor.newInstance(barInstance),
                                                "Failed to create an instance of Foo.");
        Object graultInstance = assertDoesNotThrow(() -> constructor.newInstance(fooInstance),
                                                   "Failed to create an instance of Grault.");
        Method getFooMethod = assertDoesNotThrow(() -> GraultTest.graultClass.getDeclaredMethod("getFoo"),
                                                 "The method 'getFoo' does not exist.");
        Object returnedFoo = assertDoesNotThrow(() -> getFooMethod.invoke(graultInstance),
                                                "Failed to invoke the method 'getFoo'.");
        assertEquals(fooInstance, returnedFoo, "The method 'getFoo' does not return the correct value.");
    }
}
