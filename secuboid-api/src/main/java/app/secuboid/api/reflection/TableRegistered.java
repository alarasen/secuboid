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

import app.secuboid.api.storage.rows.Row;
import app.secuboid.api.storage.tables.Table;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Every table should has this annotation, added in secuboid-plugin.yml and
 * implements Table.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableRegistered {

    /**
     * The row class associated to this table.
     *
     * @return the row clas
     */
    @NotNull Class<? extends Row> row();

    /**
     * The "create table" instruction.
     *
     * @return create table instruction
     */
    @NotNull String createTable();

    /**
     * What this table depends on (foreign key)
     *
     * @return depends on table(s)
     */
    @SuppressWarnings("java:S1452")
    @NotNull Class<? extends Table<?>>[] dependsOn() default {};
}
