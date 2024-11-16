package com.jad;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FooTest {
    private static final int NB_OF_FIELDS = 5;
    private static final int NB_OF_NON_CONSTRUCTOR_METHOD = 8;
    private static final int NB_OF_CONSTRUCTOR = 1;
    private static final int NB_CONSTRUCTOR_PARAMETERS = 1;
    private static Class<?> fooClass;
    private static Class<?> barClass;
    private static Object fooInstance;
    private static Object barInstance;

    @BeforeAll
    static void beforeAll() {
        FooTest.fooClass = Utils.testIfClassExists("com.jad.Foo");
        FooTest.barClass = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Bar"),
                                              "The class Bar does not exist.");
        Constructor<?> constructor = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredConstructor(FooTest.barClass),
                                                        "The constructor of Foo does not exist.");
        FooTest.barInstance = assertDoesNotThrow(() -> FooTest.barClass.getDeclaredConstructor().newInstance(),
                                                 "Failed to create an instance of Bar.");
        FooTest.fooInstance = assertDoesNotThrow(() -> constructor.newInstance(FooTest.barInstance),
                                                 "Failed to create an instance of Foo.");
    }

    @Test
    void classTest() {
        this.topLevelClassTest();
        this.countAttributesTest();
        this.countMethodsTest();
    }

    @Test
    void topLevelClassTest() {
        Utils.testIfIsATopLevelClass(FooTest.fooClass);
    }

    @Test
    void countAttributesTest() {
        Utils.testCountAttributes(FooTest.fooClass, FooTest.NB_OF_FIELDS);
    }

    @Test
    void countMethodsTest() {
        Utils.testCountNonConstructorMethods(FooTest.fooClass, FooTest.NB_OF_NON_CONSTRUCTOR_METHOD);
    }

    @Test
    void relationsTest() {
        this.barAttributeTest();
        this.bazsAttributeTest();
        this.quxAttributeTest();
        this.graultsAttributeTest();
        this.constructorTest();
        this.getBarTest();
        this.addBazAndGetBazsTest();
        this.getQuxTest();
        this.getCorgeAndSetCorgeTest();
        this.getGraultsTest();
        this.addGraultTest();
    }

    @Test
    void barAttributeTest() {
        Utils.testIfPrivateAttributeExistsWithGoodType("com.jad.Bar",
                                                       "bar",
                                                       FooTest.fooClass);
    }

    @Test
    void bazsAttributeTest() {
        Utils.testIfPrivateAttributeExistsWithGenericType("java.util.List<com.jad.Baz>",
                                                          "bazs",
                                                          FooTest.fooClass);
    }

    @Test
    void quxAttributeTest() {
        Utils.testIfPrivateAttributeExistsWithGoodType("com.jad.Qux",
                                                       "qux",
                                                       FooTest.fooClass);
    }

    @Test
    void graultsAttributeTest() {
        Utils.testIfPrivateAttributeExistsWithGenericType("java.util.List<com.jad.Grault>",
                                                          "graults",
                                                          FooTest.fooClass);
    }

    @Test
    void constructorTest() {
        Constructor<?>[] constructors = FooTest.fooClass.getDeclaredConstructors();
        assertEquals(FooTest.NB_OF_CONSTRUCTOR, constructors.length,
                     "The class Foo should have only " + FooTest.NB_OF_CONSTRUCTOR + " constructor.");
        Constructor<?> constructor = constructors[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        assertEquals(FooTest.NB_CONSTRUCTOR_PARAMETERS, parameterTypes.length,
                     "The constructor of Foo should have " + FooTest.NB_CONSTRUCTOR_PARAMETERS + " parameter.");
        assertEquals(FooTest.barClass, parameterTypes[0], "The parameter of the constructor should be of type Bar.");
        Object barInstance = assertDoesNotThrow(() -> FooTest.barClass.getDeclaredConstructor().newInstance(),
                                                "Failed to create an instance of Bar.");
        Object FooInstance = assertDoesNotThrow(() -> constructor.newInstance(barInstance),
                                                "Failed to create an instance of Foo.");
        Field barField = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredField("bar"),
                                            "The field 'bar' does not exist.");
        barField.setAccessible(true);
        Object barFieldValue = assertDoesNotThrow(() -> barField.get(FooInstance),
                                                  "Failed to get the value of the field 'bar'.");
        assertEquals(barInstance, barFieldValue, "The field 'bar' is not initialized with the constructor parameter.");

        Field bazsField = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredField("bazs"),
                                             "The field 'bazs' does not exist.");
        bazsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Object> bazs = assertDoesNotThrow(() -> (List<Object>) bazsField.get(FooTest.fooInstance),
                                               "Failed to get the value of the field 'bazs'.");
        assertNotNull(bazs, "The field 'bazs' should not be null.");
        assertTrue(bazs.isEmpty(), "The field 'bazs' should be initialized with an empty list.");

        Field corgeField = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredField("corge"),
                                              "The field 'corge' does not exist.");
        corgeField.setAccessible(true);
        Object corgeFieldValue = assertDoesNotThrow(() -> corgeField.get(FooTest.fooInstance),
                                                    "Failed to get the value of the field 'corge'.");
        assertNull(corgeFieldValue, "The field 'corge' should be initialized to null.");

        Field quxField = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredField("qux"),
                                            "The field 'qux' does not exist.");
        quxField.setAccessible(true);
        Object qux = assertDoesNotThrow(() -> quxField.get(FooTest.fooInstance),
                                        "Failed to get the value of the field 'qux'.");
        assertNotNull(qux, "The field 'qux' should not be null.");

        Field graultsField = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredField("graults"),
                                                "The field 'graults' does not exist.");
        graultsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Object> graults = assertDoesNotThrow(() -> (List<Object>) graultsField.get(FooTest.fooInstance),
                                                  "Failed to get the value of the field 'graults'.");
        assertNotNull(graults, "The field 'graults' should not be null.");
        assertTrue(graults.isEmpty(), "The field 'graults' should be initialized with an empty list.");
    }

    @Test
    void getBarTest() {
        Method getBarMethod = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredMethod("getBar"),
                                                 "The method 'getBar' does not exist.");
        Object returnedBar = assertDoesNotThrow(() -> getBarMethod.invoke(FooTest.fooInstance),
                                                "Failed to invoke the method 'getBar'.");
        assertEquals(FooTest.barInstance, returnedBar, "The method 'getBar' does not return the correct value.");
    }

    @Test
    void addBazAndGetBazsTest() {
        Class<?> bazClass = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Baz"),
                                               "The class Baz does not exist.");
        Method addBazMethod = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredMethod("addBaz", bazClass),
                                                 "The method 'addBaz' does not exist.");
        Method getBazsMethod = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredMethod("getBazs"),
                                                  "The method 'getBazs' does not exist.");
        addBazMethod.setAccessible(true);
        Object bazInstance = assertDoesNotThrow(() -> bazClass.getDeclaredConstructor().newInstance(),
                                                "Failed to create an instance of Baz.");
        assertDoesNotThrow(() -> addBazMethod.invoke(FooTest.fooInstance, bazInstance),
                           "Failed to invoke the method 'addBaz'.");
        @SuppressWarnings("unchecked")
        List<Object> bazs = assertDoesNotThrow(() -> (List<Object>) getBazsMethod.invoke(FooTest.fooInstance),
                                               "Failed to invoke the method 'getBazs'.");
        assertNotNull(bazs, "The method 'getBazs' returned null.");
        assertTrue(bazs.contains(bazInstance), "The 'bazs' list does not contain the added Baz instance.");
    }

    @Test
    void getQuxTest() {
        Method getQuxMethod = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredMethod("getQux"),
                                                 "The method 'getQux' does not exist.");
        Object returnedQux = assertDoesNotThrow(() -> getQuxMethod.invoke(FooTest.fooInstance),
                                                "Failed to invoke the method 'getQux'.");
        assertNotNull(returnedQux, "The field 'qux' should not be null.");
    }

    @Test
    void getCorgeAndSetCorgeTest() {
        Class<?> corgeClass = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Corge"),
                                                 "The class Corge does not exist.");
        Method getCorgeMethod = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredMethod("getCorge"),
                                                   "The method 'getCorge' does not exist.");
        Method setCorgeMethod = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredMethod("setCorge", corgeClass),
                                                   "The method 'setCorge' does not exist.");
        Method getFooMethod = assertDoesNotThrow(() -> corgeClass.getDeclaredMethod("getFoo"),
                                                 "The method 'getFoo' does not exist.");

        Object corgeInstance1 = assertDoesNotThrow(
                () -> corgeClass.getDeclaredConstructor(FooTest.fooClass).newInstance(FooTest.fooInstance),
                "Failed to create an instance of Corge.");
        Object corgeInstance2 = assertDoesNotThrow(
                () -> corgeClass.getDeclaredConstructor(FooTest.fooClass).newInstance(FooTest.fooInstance),
                "Failed to create an instance of Corge.");
        assertDoesNotThrow(() -> setCorgeMethod.invoke(FooTest.fooInstance, corgeInstance1),
                           "Failed to invoke the method 'setCorge' with the first Corge instance.");
        Object returnedCorge1 = assertDoesNotThrow(() -> getCorgeMethod.invoke(FooTest.fooInstance),
                                                   "Failed to invoke the method 'getCorge' after setting the first Corge instance.");
        assertEquals(corgeInstance1, returnedCorge1,
                     "The method 'getCorge' did not return the correct Corge instance.");
        Object returnedFoo1 = assertDoesNotThrow(() -> getFooMethod.invoke(corgeInstance1),
                                                 "Failed to invoke the method 'getFoo' on the first Corge instance.");
        assertEquals(FooTest.fooInstance, returnedFoo1,
                     "The method 'getFoo' on the first Corge instance did not return the correct Foo instance.");
        assertDoesNotThrow(() -> setCorgeMethod.invoke(FooTest.fooInstance, corgeInstance2),
                           "Failed to invoke the method 'setCorge' with the second Corge instance.");
        Object returnedCorge2 = assertDoesNotThrow(() -> getCorgeMethod.invoke(FooTest.fooInstance),
                                                   "Failed to invoke the method 'getCorge' after setting the second Corge instance.");
        assertEquals(corgeInstance2, returnedCorge2,
                     "The method 'getCorge' did not return the correct Corge instance after setting the second Corge instance.");
        Object returnedFoo2 = assertDoesNotThrow(() -> getFooMethod.invoke(corgeInstance2),
                                                 "Failed to invoke the method 'getFoo' on the second Corge instance.");
        assertEquals(FooTest.fooInstance, returnedFoo2,
                     "The method 'getFoo' on the second Corge instance did not return the correct Foo instance.");
        Object returnedFoo1AfterSet = assertDoesNotThrow(() -> getFooMethod.invoke(corgeInstance1),
                                                         "Failed to invoke the method 'getFoo' on the first Corge instance after setting the second Corge instance.");
        assertNull(returnedFoo1AfterSet,
                   "The method 'getFoo' on the first Corge instance should return null after setting the second Corge instance.");
    }

    @Test
    void getGraultsTest() {
        Method getGraultsMethod = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredMethod("getGraults"),
                                                     "The method 'getGraults' does not exist.");
        @SuppressWarnings("unchecked")
        List<Object> graults = assertDoesNotThrow(() -> (List<Object>) getGraultsMethod.invoke(FooTest.fooInstance),
                                                  "Failed to invoke the method 'getGraults'.");
        Field graultsField = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredField("graults"),
                                                "The field 'graults' does not exist.");
        graultsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Object> graultsFieldValue = assertDoesNotThrow(() -> (List<Object>) graultsField.get(FooTest.fooInstance),
                                                            "Failed to get the value of the field 'graults'.");
        assertEquals(graultsFieldValue, graults, "The method 'getGraults' did not return the correct value.");
    }

    @Test
    void addGraultTest() {
        Method addGraultMethod = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredMethod("addGrault"),
                                                    "The method 'addGrault' does not exist.");
        Method getGraultsMethod = assertDoesNotThrow(() -> FooTest.fooClass.getDeclaredMethod("getGraults"),
                                                     "The method 'getGraults' does not exist.");
        Method getFooMethod = assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass("com.jad.Grault")
                                                         .getDeclaredMethod("getFoo"),
                                                 "The method 'getFoo' does not exist in Grault class.");

        // Add a new Grault
        assertDoesNotThrow(() -> addGraultMethod.invoke(FooTest.fooInstance),
                           "Failed to invoke the method 'addGrault'.");

        // Retrieve the list of Graults
        @SuppressWarnings("unchecked")
        List<Object> graults = assertDoesNotThrow(() -> (List<Object>) getGraultsMethod.invoke(FooTest.fooInstance),
                                                  "Failed to invoke the method 'getGraults'.");
        assertNotNull(graults, "The method 'getGraults' returned null.");
        assertFalse(graults.isEmpty(), "The 'graults' list should not be empty after adding a Grault.");

        // Verify the last added Grault
        Object lastGrault = graults.getLast();
        Object returnedFoo = assertDoesNotThrow(() -> getFooMethod.invoke(lastGrault),
                                                "Failed to invoke the method 'getFoo' on the last added Grault.");
        assertEquals(FooTest.fooInstance, returnedFoo,
                     "The method 'getFoo' on the last added Grault did not return the correct Foo instance.");
    }
}
