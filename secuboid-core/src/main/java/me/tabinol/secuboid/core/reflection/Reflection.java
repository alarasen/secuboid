/*
 Secuboid: Lands and Protection plugin for Minecraft server
 Copyright (C) 2014 Tabinol

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.secuboid.core.reflection;

import static me.tabinol.secuboid.core.messages.Log.log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

class Reflection {

    Set<Class<?>> getClasses(Set<String> names) {
        Set<Class<?>> result = new HashSet<>();

        for (String name : names) {
            try {
                Class<?> clazz = Class.forName(name);
                result.add(clazz);
            } catch (ClassNotFoundException e) {
                log().log(Level.WARNING, e, () -> "Component class not found: " + name);
            }
        }

        return result;
    }

    <T, A extends Annotation> Map<Class<? extends T>, A> getClassToAnnotation(Set<Class<?>> classes,
            Class<A> annotationClass, Class<T> wantedClass) {
        Map<Class<? extends T>, A> returnedClasses = new HashMap<>();

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                try {
                    @SuppressWarnings("unchecked")
                    Class<T> classT = (Class<T>) clazz;
                    A annotation = clazz.getAnnotation(annotationClass);
                    returnedClasses.put(classT, annotation);
                } catch (ClassCastException e) {
                    log().log(Level.WARNING, e,
                            () -> String.format(
                                    "A component will not work because the class is incorrect type: [class=%s, wantedClass=%s]",
                                    clazz, wantedClass));
                }
            }
        }

        return returnedClasses;
    }

    <T> Set<T> getAnnotatedConstants(Set<Class<?>> classes, Class<? extends Annotation> annotationClass,
            Class<T> wantedClass) {
        Set<Field> fields = new HashSet<>();

        for (Class<?> clazz : classes) {
            fields.addAll(getAnnotatedFields(clazz, annotationClass));
        }

        return getConstants(fields, wantedClass);
    }

    private Set<Field> getAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        Set<Field> result = new HashSet<>();

        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClass)) {
                result.add(field);
            }
        }

        return result;
    }

    private <T> Set<T> getConstants(Set<Field> fields, Class<T> wantedClass) {
        Set<T> result = new HashSet<>();

        for (Field field : fields) {
            Class<?> fieldClass = field.getType();
            if (wantedClass.isAssignableFrom(fieldClass)) {
                try {
                    @SuppressWarnings("unchecked")
                    T constant = (T) field.get(null);
                    result.add(constant);
                } catch (IllegalArgumentException | IllegalAccessException | ClassCastException e) {
                    log().log(Level.WARNING, e,
                            () -> String.format(
                                    "A component will not work because the field is not public or incorrect type: [fieldClass=%s, wantedClass=%s]",
                                    fieldClass, wantedClass));
                }
            }
        }

        return result;
    }
}
