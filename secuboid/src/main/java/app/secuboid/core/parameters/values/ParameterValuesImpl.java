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

package app.secuboid.core.parameters.values;

import app.secuboid.api.parameters.values.ParameterValue;
import app.secuboid.api.parameters.values.ParameterValues;
import app.secuboid.api.reflection.ParameterValueRegistered;
import app.secuboid.api.storage.StorageManager;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.reflection.PluginLoader;
import app.secuboid.core.storage.rows.ParameterValueRow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static app.secuboid.core.messages.Log.log;
import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

public class ParameterValuesImpl implements ParameterValues {

    private final Map<String, Class<? extends ParameterValue>> shortNameToClass;
    private final Map<Class<? extends ParameterValue>, Map<String, ParameterValue>> classToValueToParameterValue;

    public ParameterValuesImpl() {
        shortNameToClass = new HashMap<>();
        classToValueToParameterValue = new HashMap<>();
    }

    public void init(PluginLoader pluginLoader) {
        // TODO unit test
        if (!shortNameToClass.isEmpty()) {
            return;
        }

        Map<Class<? extends ParameterValue>, ParameterValueRegistered> classToAnnotation =
                pluginLoader.getClassToAnnotation(ParameterValueRegistered.class, ParameterValue.class);
        classToAnnotation.forEach((c, a) -> shortNameToClass.put(a.shortName(), c));
    }

    public void load() {
        // TODO unit test
        shortNameToClass.clear();
        classToValueToParameterValue.clear();

        Set<ParameterValueRow> parameterValueRows = getStorageManager().selectAllSync(ParameterValueRow.class);

        for (ParameterValueRow parameterValueRow : parameterValueRows) {
            loadParameterValueRow(parameterValueRow);
        }
    }

    private void loadParameterValueRow(ParameterValueRow parameterValueRow) {
        Class<? extends ParameterValue> clazz = shortNameToClass.get(parameterValueRow.shortName());
        if (clazz == null) {
            log().log(SEVERE, "Unable to load the parameter value with this non existing short name: {}",
                    parameterValueRow);
            return;
        }

        ParameterValue parameterValue;
        try {
            Method newInstance = clazz.getMethod("newInstance", long.class, String.class);
            parameterValue = (ParameterValue) newInstance.invoke(null, parameterValueRow.getId(),
                    parameterValueRow.value());
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException e) {
            log().log(SEVERE, e, () -> format("Unable to create the parameter value instance: %s", parameterValueRow));
            return;
        } catch (InvocationTargetException e) {
            log().log(SEVERE, e, () -> format("There is an error with this existing parameter value: %s",
                    parameterValueRow));
            return;
        }

        classToValueToParameterValueAdd(parameterValue);
    }

    private void classToValueToParameterValueAdd(ParameterValue parameterValue) {
        classToValueToParameterValue.computeIfAbsent(parameterValue.getClass(), k -> new HashMap<>()).put(parameterValue.getValue(), parameterValue);
    }

    private StorageManager getStorageManager() {
        return SecuboidImpl.instance().getStorageManager();
    }
}
