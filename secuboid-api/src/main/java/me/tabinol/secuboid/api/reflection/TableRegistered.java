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
package me.tabinol.secuboid.api.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.tabinol.secuboid.api.storage.tables.Row;
import me.tabinol.secuboid.api.storage.tables.Table;

/**
 * Every table should has this anotation, added in secuboid-plugin.yml and
 * implements Table.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableRegistered {

    /**
     * The row class associated to this table
     */
    Class<? extends Row> row();

    /**
     * What this table depends on (foreing key)
     */
    @SuppressWarnings("java:S1452")
    Class<? extends Table<?>>[] dependsOn() default {};
}
