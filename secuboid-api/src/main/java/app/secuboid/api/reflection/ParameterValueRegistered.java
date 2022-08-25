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
package app.secuboid.api.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface ParameterValueRegistered. Annotation used for custom parameter
 * values. If you make your own value types, your class should implement
 * ParameterValue.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ParameterValueRegistered {

    /**
     * The parameter name (not the value). Ex: player, everybody, etc.
     */
    String name();

    /**
     * The short name for chat and save. Must be unique. Ex: p, everybody, etc.
     */
    String shortName();

    /**
     * The chat color code.
     */
    String chatColor();

    /**
     * The priority, higher is returned before the lowest if there is a match.
     */
    int priority() default 50;
}
