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
package app.secuboid.api.parameters.values;

import java.util.Objects;

import app.secuboid.api.lands.Land;
import org.bukkit.entity.Entity;

import app.secuboid.api.exceptions.ParameterValueException;
import app.secuboid.api.reflection.ParameterValueRegistered;

/**
 * Represents every players en entities.
 */
@ParameterValueRegistered(name = "everything", shortName = "everything", chatColor = "\u00A7F", priority = 30)
public class ParameterValueEverything implements ParameterValue {

    public static final ParameterValueEverything INSTANCE = new ParameterValueEverything();

    private static final String NAME = ParameterValueEverything.class.getAnnotation(ParameterValueRegistered.class)
            .name();
    private static final String SHORT_NAME = ParameterValueEverything.class
            .getAnnotation(ParameterValueRegistered.class)
            .shortName();
    private static final String CHAT_COLOR = ParameterValueEverything.class
            .getAnnotation(ParameterValueRegistered.class)
            .chatColor();
    private static final int PRIORITY = ParameterValueEverything.class.getAnnotation(ParameterValueRegistered.class)
            .priority();

    private int id;

    private ParameterValueEverything() {
        id = ID_NON_CREATED_VALUE;
    }

    public static ParameterValueEverything getInstance() {
        return INSTANCE;
    }

    // Needed for load from database
    @SuppressWarnings({ "java:S1172", "java:S1130" })
    public static ParameterValueEverything newInstance(String value) throws ParameterValueException {
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
        return true;
    }

    @Override
    public boolean hasAccess(Entity entity, Land originLand) {
        return true;
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
        if (!(o instanceof ParameterValueEverything)) {
            return false;
        }
        ParameterValueEverything parameterValueEverything = (ParameterValueEverything) o;
        return id == parameterValueEverything.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
