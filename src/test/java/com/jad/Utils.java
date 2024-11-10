package com.jad;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

enum Utils {
    ;

    static void testIfPrivateAttributeExistsWithGoodType(final String attributeType,
                                                         final String attributeName,
                                                         final Class<?> classToTest) {
        Class<?> attributeClass = assertDoesNotThrow(
                () -> ClassLoader.getSystemClassLoader().loadClass(attributeType),
                "The class " + attributeType + " does not exist.");
        Field fooField = assertDoesNotThrow(() -> classToTest.getDeclaredField(attributeName),
                                            "The field '" + attributeName + "' does not exist.");
        assertNotNull(fooField, "The field '" + attributeName + "' does not exist.");
        assertTrue(attributeClass.isAssignableFrom(fooField.getType()),
                   "The field '" + attributeName + "' is not of type " + attributeType + " or its subclass.");
        assertTrue(Modifier.isPrivate(fooField.getModifiers()), "The field '" + attributeName + "' is not private.");
    }

    static void testIfPrivateAttributeExistsWithGenericType(final String attributeType,
                                                            final String attributeName,
                                                            final Class<?> classToTest) {
        Field field = assertDoesNotThrow(() -> classToTest.getDeclaredField(attributeName),
                                         "The field '" + attributeName + "' does not exist.");
        assertNotNull(field, "The field '" + attributeName + "' does not exist.");
        String fieldTypeName = field.getGenericType().getTypeName();
        boolean isAssignable = assertDoesNotThrow(() -> {
            Class<?> fieldClass = Class.forName(fieldTypeName.split("<")[0]);
            Class<?> expectedClass = Class.forName(attributeType.split("<")[0]);
            return expectedClass.isAssignableFrom(fieldClass);
        }, "The class " + attributeType + " or " + fieldTypeName + " does not exist.");
        assertTrue(isAssignable, "The field '" + attributeName + "' is not of type " + attributeType + ".");
        assertTrue(Modifier.isPrivate(field.getModifiers()), "The field '" + attributeName + "' is not private.");
    }

    static void testIfIsATopLevelClass(final Class<?> classToTest) {
        assertTrue(!classToTest.isPrimitive() &&
                           !classToTest.isArray() &&
                           !classToTest.isInterface() &&
                           !classToTest.isEnum() &&
                           !classToTest.isAnnotation() &&
                           !classToTest.isAnonymousClass() &&
                           !classToTest.isLocalClass() &&
                           !classToTest.isMemberClass(),
                   "The class " + classToTest.getSimpleName() + " is not a top level class.");
    }

    static Class<?> testIfClassExists(final String className) {
        assertDoesNotThrow(() -> ClassLoader.getSystemClassLoader().loadClass(className),
                           "The class " + className + " does not exist.");
        final AtomicReference<Class<?>> atomicReferenceClass = new AtomicReference<>();
        assertDoesNotThrow(
                () -> atomicReferenceClass.set(ClassLoader.getSystemClassLoader().loadClass(className)),
                "The class " + className + " does not exist.");
        Class<?> aClass = atomicReferenceClass.get();
        assertNotNull(aClass, "The class " + className + " does not exist.");
        return aClass;
    }

    static void testCountAttributes(final Class<?> classToTest, final int expectedNbOfFields) {
        assertEquals(expectedNbOfFields, classToTest.getDeclaredFields().length,
                     "The class " + classToTest.getSimpleName() + " should have " + expectedNbOfFields + " attribute(s).");
    }

    static void testCountNonConstructorMethods(final Class<?> classToTest,
                                               final int expectedNonConstructorMethodsCount) {
        Method[] methods = classToTest.getDeclaredMethods();
        long nonConstructorMethodsCount = Arrays.stream(methods)
                .filter(method -> !method.isSynthetic())
                .count();
        assertEquals(expectedNonConstructorMethodsCount, nonConstructorMethodsCount,
                     "The class " + classToTest.getSimpleName() + " should have " + expectedNonConstructorMethodsCount + " method(s) other than the constructor.");
    }
}
