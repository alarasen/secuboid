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
package me.tabinol.secuboid.api.parameters.values;

import java.util.Objects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import me.tabinol.secuboid.api.exceptions.ParameterValueException;
import me.tabinol.secuboid.api.lands.Land;
import me.tabinol.secuboid.api.reflection.ParameterValueRegistered;

/**
 * Represents every humain players.
 */
@ParameterValueRegistered(name = "everybody", shortName = "everybody", chatColor = "\u00A7F", priority = 40)
public class ParameterValueEverybody implements ParameterValue {

    public static final ParameterValueEverybody INSTANCE = new ParameterValueEverybody();

    private static final String NAME = ParameterValueEverybody.class.getAnnotation(ParameterValueRegistered.class)
            .name();
    private static final String SHORT_NAME = ParameterValueEverybody.class.getAnnotation(ParameterValueRegistered.class)
            .shortName();
    private static final String CHAT_COLOR = ParameterValueEverybody.class.getAnnotation(ParameterValueRegistered.class)
            .chatColor();
    private static final int PRIORITY = ParameterValueEverybody.class.getAnnotation(ParameterValueRegistered.class)
            .priority();

    private int id;

    private ParameterValueEverybody() {
        id = ID_NON_CREATED_VALUE;
    }

    public static ParameterValueEverybody getInstance() {
        return INSTANCE;
    }

    // Needed for load from database
    @SuppressWarnings({ "java:S1172", "java:S1130" })
    public static ParameterValueEverybody newInstance(String value) throws ParameterValueException {
        return INSTANCE;
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
        return null;
    }

    @Override
    public boolean hasAccess(Entity entity) {
        return entity.getType() == EntityType.PLAYER;
    }

    @Override
    public boolean hasAccess(Entity entity, Land originLand) {
        return hasAccess(entity);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ParameterValueEverybody)) {
            return false;
        }
        ParameterValueEverybody parameterValueEverybody = (ParameterValueEverybody) o;
        return id == parameterValueEverybody.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
