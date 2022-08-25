/*
 *  Secuboid: Lands and Protection plugin for Minecraft server
 *  Copyright (C) 2014 Tabinol
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package app.secuboid.core.reflection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectionTest {

    @AnnotationTest
    public static final String CONSTANT_TEST = "constant";

    private Reflection reflection;

    @BeforeEach
    public void beforeEach() {
        reflection = new Reflection();
    }

    @Test
    void when_send_name_then_return_class_list() {
        String name = "app.secuboid.core.reflection.ReflectionTest";

        Set<Class<?>> classes = reflection.getClasses(Collections.singleton(name));

        assertEquals(1, classes.size());
        assertEquals(this.getClass(), classes.iterator().next());
    }

    @Test
    void when_send_wrong_name_then_warn_and_return_empty() {
        String name = "app.secuboid.core.reflection.WrongName";

        Set<Class<?>> classes = reflection.getClasses(Collections.singleton(name));

        assertEquals(0, classes.size());
    }

    @Test
    void when_send_class_and_annotation_then_return_value() {
        Set<String> annotatedConstants = reflection.getAnnotatedConstants(Collections.singleton(this.getClass()),
                AnnotationTest.class, String.class);

        assertEquals(1, annotatedConstants.size());
        assertEquals(CONSTANT_TEST, annotatedConstants.iterator().next());
    }

    @Test
    void when_send_class_annotation_and_wrong_type_then_return_empty() {
        Set<Integer> annotatedConstants = reflection.getAnnotatedConstants(Collections.singleton(this.getClass()),
                AnnotationTest.class, Integer.class);

        assertEquals(0, annotatedConstants.size());
    }

    @Test
    void when_send_class_annotation_then_return_class_to_annotation() {
        Map<Class<? extends AnnotedClass>, AnnotationTest> classToAnnotation = reflection.getClassToAnnotation(
                Collections.singleton(AnnotedClass.class), AnnotationTest.class, AnnotedClass.class);

        assertEquals(1, classToAnnotation.size());

        Map.Entry<Class<? extends AnnotedClass>, AnnotationTest> entry = classToAnnotation.entrySet().iterator().next();
        assertEquals(AnnotedClass.class, entry.getKey());

        AnnotationTest annotationTest = AnnotedClass.class.getAnnotation(AnnotationTest.class);
        assertEquals(annotationTest, entry.getValue());
    }

    @Retention(RetentionPolicy.RUNTIME)
    private static @interface AnnotationTest {
    }

    @AnnotationTest
    static class AnnotedClass {
    }
}
