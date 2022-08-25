/*
 Secuboid: Lands and Protection plugin for Minecraft server
 Copyright (C) 2014 Tabinol
ª
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
package me.tabinol.secuboid.api.parameters.values;

import static java.lang.String.format;

import java.util.Objects;

import org.bukkit.entity.Entity;

import me.tabinol.secuboid.api.exceptions.ParameterValueException;
import me.tabinol.secuboid.api.lands.Land;
import me.tabinol.secuboid.api.reflection.ParameterValueRegistered;

/**
 * Represents an entity class from the Bukkit Java source code.
 */
@ParameterValueRegistered(name = "entity-class", shortName = "ec", chatColor = "\u00A75", priority = 40)
public class ParameterValueEntityClass implements ParameterValue {

    private static final String ENTITY_PREFIX = "org.bukkit.entity.";

    private static final String NAME = ParameterValueEntityClass.class.getAnnotation(ParameterValueRegistered.class)
            .name();
    private static final String SHORT_NAME = ParameterValueEntityClass.class
            .getAnnotation(ParameterValueRegistered.class).shortName();
    private static final String CHAT_COLOR = ParameterValueEntityClass.class
            .getAnnotation(ParameterValueRegistered.class).chatColor();
    private static final int PRIORITY = ParameterValueEntityClass.class.getAnnotation(ParameterValueRegistered.class)
            .priority();

    private final Class<? extends Entity> entityClass;

    private int id;

    public ParameterValueEntityClass(Class<? extends Entity> entityClass) {
        this.entityClass = entityClass;
        id = ID_NON_CREATED_VALUE;
    }

    // Needed for load from database
    public static ParameterValueEntityClass newInstance(String value) throws ParameterValueException {
        String entityClassStr;
        if (value.contains(".")) {
            entityClassStr = value;
        } else {
            entityClassStr = ENTITY_PREFIX + value;
        }

        Class<?> entityClassUnknow;

        try {
            entityClassUnknow = Class.forName(entityClassStr);
        } catch (ClassNotFoundException e) {
            String msg = format(
                    "Entity class not found: Wrong name in the database or Bukkit API is changed? [entityClass=%s]",
                    value);
            throw new ParameterValueException(msg, e);
        }

        Class<? extends Entity> entityClass;

        try {
            entityClass = entityClassUnknow.asSubclass(Entity.class);
        } catch (ClassCastException e) {
            String msg = format(
                    "Not an entity class: Wrong name in the database or Bukkit API is changed? [entityClass=%s]",
                    value);
            throw new ParameterValueException(msg, e);
        }

        return new ParameterValueEntityClass(entityClass);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getShortName() {
        return SHORT_NAME;
    }

    @Override
    public String getChatColor() {
        return CHAT_COLOR;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public String getValue() {
        String className = entityClass.getName();

        if (className.startsWith(ENTITY_PREFIX)) {
            return className.replace(ENTITY_PREFIX, "");
        }

        return className;
    }

    @Override
    public boolean hasAccess(Entity entity) {
        return entityClass.isAssignableFrom(entity.getClass());
    }

    @Override
    public boolean hasAccess(Entity entity, Land originLand) {
        return hasAccess(entity);
    }

    @Override
    public String toString() {
        return "{" +
                " entityClass='" + entityClass + "'" +
                ", id='" + getId() + "'" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ParameterValueEntityClass)) {
            return false;
        }
        ParameterValueEntityClass parameterValueEntityClass = (ParameterValueEntityClass) o;
        return Objects.equals(entityClass, parameterValueEntityClass.entityClass) && id == parameterValueEntityClass.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityClass, id);
    }
}
