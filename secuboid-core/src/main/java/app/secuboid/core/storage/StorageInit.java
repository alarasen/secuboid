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
package app.secuboid.core.storage;

import app.secuboid.api.reflection.TableRegistered;
import app.secuboid.api.storage.rows.Row;
import app.secuboid.api.storage.tables.Table;
import app.secuboid.core.messages.Log;
import app.secuboid.core.reflection.PluginLoader;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class StorageInit {

    private static final String CREATE_TABLE_SQL_FIELD = "CREATE_TABLE_SQL";

    private static final int MAX_LOOP_ATTEMPTS = 100;

    private final Map<Class<? extends Row>, Table<Row>> classRowToTable;

    StorageInit() {
        classRowToTable = new HashMap<>();
    }

    @SuppressWarnings("rawtypes")
    public void init(PluginLoader pluginLoader) {
        initConnectionManager();

        Map<Class<? extends Table>, TableRegistered> classToAnnotation = pluginLoader
                .getClassToAnnotation(TableRegistered.class, Table.class);
        Set<Class<? extends Table>> doneClasses = new HashSet<>();
        int i = 0;

        while (i < MAX_LOOP_ATTEMPTS && doneClasses.size() < classToAnnotation.size()) {
            i++;

            for (Map.Entry<Class<? extends Table>, TableRegistered> classToAnnotationEntry : classToAnnotation
                    .entrySet()) {
                Class<? extends Table> clazz = classToAnnotationEntry.getKey();

                if (!doneClasses.contains(clazz)) {
                    TableRegistered tableRegistered = classToAnnotationEntry.getValue();
                    boolean allMatch = Arrays.stream(tableRegistered.dependsOn()).allMatch(doneClasses::contains);

                    if (allMatch) {
                        createDatabase(clazz);
                        Class<? extends Row> classRow = tableRegistered.row();
                        addTableToFinder(classRow, clazz);
                        doneClasses.add(clazz);
                    }
                }
            }
        }

        if (doneClasses.size() < classToAnnotation.size()) {
            Log.log().log(Level.SEVERE, () -> String.format(
                    "Unable to finish the storage table initiation due to an infinite loop [doneClasses=%s, classToAnnotation=%s]",
                    doneClasses, classToAnnotation));
        }
    }

    Table<Row> getTableFromClassRow(Class<? extends Row> classRow) {
        return classRowToTable.get(classRow);
    }

    void initConnectionManager() {
        ConnectionManager.init();
    }

    @SuppressWarnings("rawtypes")
    void createDatabase(Class<? extends Table> clazz) {
        String sql;
        try {
            sql = (String) clazz.getField(CREATE_TABLE_SQL_FIELD).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
                 | ClassCastException e) {
            Log.log().log(Level.SEVERE, e,
                    () -> String.format("Unable read the static field %s due to a programming error [clazz=%s]",
                            CREATE_TABLE_SQL_FIELD, clazz));
            return;
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            Log.log().log(Level.SEVERE, e,
                    () -> String.format("Unable to check or create the database [clazz=%s]", clazz));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addTableToFinder(Class<? extends Row> classRow, Class<? extends Table> clazz) {
        Table<Row> table;
        try {
            table = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | NoSuchMethodException | SecurityException e) {
            Log.log().log(Level.SEVERE, e,
                    () -> String.format("Unable create Table instance due to a programming error [clazz=%s]", clazz));
            return;
        }

        classRowToTable.put(classRow, table);
    }
}
