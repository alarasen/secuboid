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
import app.secuboid.api.reflection.ParameterValueRegistered;
import org.bukkit.entity.Entity;

import app.secuboid.api.exceptions.ParameterValueException;

/**
 * Represents nothing including no player.
 */
@ParameterValueRegistered(name = "nobody", shortName = "nobody", chatColor = "\u00A78")
public class ParameterValueNobody implements ParameterValue {

    public static final ParameterValueNobody INSTANCE = new ParameterValueNobody();

    private static final String NAME = ParameterValueNobody.class.getAnnotation(ParameterValueRegistered.class).name();
    private static final String SHORT_NAME = ParameterValueNobody.class.getAnnotation(ParameterValueRegistered.class)
            .shortName();
    private static final String CHAT_COLOR = ParameterValueNobody.class.getAnnotation(ParameterValueRegistered.class)
            .chatColor();
    private static final int PRIORITY = ParameterValueNobody.class.getAnnotation(ParameterValueRegistered.class)
            .priority();

    private int id;

    private ParameterValueNobody() {
        id = ID_NON_CREATED_VALUE;
    }

    public static ParameterValueNobody getInstance() {
        return INSTANCE;
    }

    // Needed for load from database
    @SuppressWarnings({ "java:S1172", "java:S1130" })
    public static ParameterValueNobody newInstance(String value) throws ParameterValueException {
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
        return false;
    }

    @Override
    public boolean hasAccess(Entity entity, Land originLand) {
        return false;
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
        if (!(o instanceof ParameterValueNobody)) {
            return false;
        }
        ParameterValueNobody parameterValueNobody = (ParameterValueNobody) o;
        return id == parameterValueNobody.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
