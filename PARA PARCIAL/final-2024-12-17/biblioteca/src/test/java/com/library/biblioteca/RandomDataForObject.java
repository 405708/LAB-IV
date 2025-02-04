package com.library.biblioteca;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RandomDataForObject {

    private static final Random random = new Random();

    public static <T> T generateRandomValuesCascade(T obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getType() == Long.class) {
                field.set(obj, (long) random.nextInt(1000));
            } else if (field.getType() == Integer.class) {
                field.set(obj, random.nextInt(100));
            } else if (field.getType() == Double.class) {
                field.set(obj, random.nextDouble() * 100);
            } else if (field.getType() == Boolean.class) {
                field.set(obj, random.nextBoolean());
            } else if (field.getType() == boolean.class) {
                field.set(obj, random.nextBoolean());
            } else if (field.getType() == BigDecimal.class) {
                field.set(obj, BigDecimal.valueOf(random.nextDouble(100)));
            } else if (field.getType() == BigInteger.class) {
                field.set(obj, BigInteger.valueOf(random.nextInt(100)));
            } else if (field.getType() == String.class) {
                field.set(obj, generateRandomString(5));
            } else if (List.class.isAssignableFrom(field.getType())) {
                generateRandomList(field, obj);
            } else if (field.getType().isEnum()) {
                field.set(obj, getRandomEnumValue(field.getType()));
            } else if (field.getType() == LocalDateTime.class) {
                field.set(obj, LocalDateTime.now());
            } else if (field.getType() == LocalDate.class) {
                field.set(obj, LocalDate.now());
            } else if (field.getType() == UUID.class) {
                field.set(obj, UUID.randomUUID());
            } else {
                Object instance = createInstance(field.getType());
                generateRandomValuesCascade(instance);
                field.set(obj, instance);
            }
        }
        return obj;
    }

    public static <T> T generateRandomValue(T obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getType() == Long.class) {
                field.set(obj, (long) random.nextInt(1000));
            } else if (field.getType() == Integer.class) {
                field.set(obj, random.nextInt(100));
            } else if (field.getType() == Double.class) {
                field.set(obj, random.nextDouble() * 100);
            }  else if (field.getType() == Boolean.class) {
                field.set(obj, random.nextBoolean());
            } else if (field.getType() == boolean.class) {
                field.set(obj, random.nextBoolean());
            } else if (field.getType() == BigDecimal.class) {
                field.set(obj, BigDecimal.valueOf(random.nextDouble(100)));
            } else if (field.getType() == BigInteger.class) {
                field.set(obj, BigInteger.valueOf(random.nextInt(100)));
            } else if (field.getType() == String.class) {
                field.set(obj, generateRandomString(5));
            } else if (List.class.isAssignableFrom(field.getType())) {
                field.set(obj, List.of());
            } else if (field.getType().isEnum()) {
                field.set(obj, getRandomEnumValue(field.getType()));
            } else if (field.getType() == LocalDateTime.class) {
                field.set(obj, LocalDateTime.now());
            } else if (field.getType() == LocalDate.class) {
                field.set(obj, LocalDate.now());
            } else if (field.getType() == UUID.class) {
                field.set(obj, UUID.randomUUID());
            }
        }
        return obj;
    }

    public static <T> T generateRandomValueCascadeWithoutList(T obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getType() == Long.class) {
                field.set(obj, (long) random.nextInt(1000));
            } else if (field.getType() == Integer.class) {
                field.set(obj, random.nextInt(100));
            } else if (field.getType() == Double.class) {
                field.set(obj, random.nextDouble() * 100);
            }  else if (field.getType() == Boolean.class) {
                field.set(obj, random.nextBoolean());
            } else if (field.getType() == boolean.class) {
                field.set(obj, random.nextBoolean());
            } else if (field.getType() == BigDecimal.class) {
                field.set(obj, BigDecimal.valueOf(random.nextDouble(100)));
            } else if (field.getType() == BigInteger.class) {
                field.set(obj, BigInteger.valueOf(random.nextInt(100)));
            } else if (field.getType() == String.class) {
                field.set(obj, generateRandomString(5));
            } else if (List.class.isAssignableFrom(field.getType())) {
                field.set(obj, List.of());
            } else if (field.getType().isEnum()) {
                field.set(obj, getRandomEnumValue(field.getType()));
            } else if (field.getType() == LocalDateTime.class) {
                field.set(obj, LocalDateTime.now());
            } else if (field.getType() == LocalDate.class) {
                field.set(obj, LocalDate.now());
            } else if (field.getType() == UUID.class) {
                field.set(obj, UUID.randomUUID());
            }
        }
        return obj;
    }

    private static void generateRandomList(Field field, Object obj) throws IllegalAccessException {
        Class<?> genericType = (Class<?>) ((java.lang.reflect.ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (genericType == Long.class) {
                list.add((long) random.nextInt(1000));
            } else if (genericType == Integer.class) {
                list.add(random.nextInt(100));
            } else if (genericType == Double.class) {
                list.add(random.nextDouble() * 100);
            } else if (genericType == BigDecimal.class) {
                list.add(BigDecimal.valueOf(random.nextDouble(100)));
            } else if (genericType == BigInteger.class) {
                list.add(BigInteger.valueOf(random.nextInt(100)));
            } else if (genericType == String.class) {
                list.add(generateRandomString(5));
            } else if (genericType.isEnum()) {
                list.add(getRandomEnumValue(genericType));
            } else if (genericType == LocalDateTime.class) {
                field.set(obj, LocalDateTime.now());
            } else if (genericType == LocalDate.class) {
                field.set(obj, LocalDate.now());
            } else if (field.getType() == Boolean.class) {
                field.set(obj, random.nextBoolean());
            } else if (field.getType() == boolean.class) {
                field.set(obj, random.nextBoolean());
            } else if (genericType == UUID.class) {
                field.set(obj, UUID.randomUUID());
            } else {
                Object instance = createInstance(genericType);
                generateRandomValuesCascade(instance);
                list.add(instance);
            }
        }
        field.set(obj, list);
    }

    private static Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear una instancia de " + clazz.getName(), e);
        }
    }

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
    }

    private static <T extends Enum<?>> T getRandomEnumValue(Class<?> enumClass) {
        T[] enumConstants = (T[]) enumClass.getEnumConstants();
        return enumConstants[random.nextInt(enumConstants.length)];
    }
}