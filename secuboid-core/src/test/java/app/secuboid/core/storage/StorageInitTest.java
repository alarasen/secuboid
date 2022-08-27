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
import app.secuboid.api.storage.tables.Row;
import app.secuboid.api.storage.tables.RowWithId;
import app.secuboid.api.storage.tables.Table;
import app.secuboid.core.reflection.PluginLoader;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StorageInitTest {

    static final String CREATE_TABLE_SQL = ""
            + "CREATE TABLE IF NOT EXISTS %1$splayer ("
            + "  id INT NOT NULL {{AUTOINCREMENT}},"
            + "  uuid CHAR(36) NOT NULL,"
            + "  name VARCHAR(45) NOT NULL,"
            + "  tinytext {{TINYTEXT}} NOT NULL,"
            + "  text {{TEXT}} NOT NULL,"
            + "  mediumtext {{MEDIUMTEXT}} NOT NULL,"
            + "  PRIMARY KEY (id),"
            + "  CONSTRAINT uuid_unique UNIQUE (uuid)"
            + ")";

    private StorageInit storageInit;
    private PluginLoader pluginLoader;

    @BeforeEach
    void beforeEach() {
        pluginLoader = mock(PluginLoader.class);

        TableRegistered tableRegistered1 = Test1Table.class.getAnnotation(TableRegistered.class);
        TableRegistered tableRegistered2 = Test2Table.class.getAnnotation(TableRegistered.class);

        @SuppressWarnings("rawtypes")
        Map<Class<? extends Table>, TableRegistered> classToAnnotation = Map.of(Test1Table.class, tableRegistered1,
                Test2Table.class, tableRegistered2);
        when(pluginLoader.getClassToAnnotation(TableRegistered.class, Table.class)).thenReturn(classToAnnotation);

        storageInit = spy(new StorageInit());
        doNothing().when(storageInit).initConnectionManager();
        doNothing().when(storageInit).createDatabase(any());
    }

    @Test
    void when_found_tables_then_execute_create_database() {
        storageInit.init(pluginLoader);

        verify(storageInit).createDatabase(Test1Table.class);
        verify(storageInit).createDatabase(Test2Table.class);
    }

    @Test
    void when_database_prefix_then_concat() {
        String sql = storageInit.getSql(CREATE_TABLE_SQL);

        assertEquals(true, sql.contains("secuboid_player"));
    }

    @Test
    void when_database_is_mariadb_then_append() {
        doReturn(ConnectionManager.DRIVER_MARIADB).when(storageInit).getDbDriver();
        String sql = storageInit.getSql(CREATE_TABLE_SQL);

        assertTrue(sql.contains(StorageInit.AUTOINCREMENT_MARIADB));
        assertTrue(sql.contains(StorageInit.TINYTEXT_MARIADB));
        assertTrue(sql.contains(StorageInit.TEXT_MARIADB));
        assertTrue(sql.contains(StorageInit.MEDIUMTEXT_MARIADB));
        assertFalse(sql.contains(StorageInit.AUTOINCREMENT_TAG));
        assertTrue(sql.contains(StorageInit.MARIADB_CREATE_SQL_SUFFIX));
    }

    @Test
    void when_database_is_hsql_then_no_append() {
        doReturn(ConnectionManager.DRIVER_HSQLDB).when(storageInit).getDbDriver();
        String sql = storageInit.getSql(CREATE_TABLE_SQL);

        assertTrue(sql.contains(StorageInit.AUTOINCREMENT_HSQLDB));
        assertTrue(sql.contains(StorageInit.TINYTEXT_HSQLDB));
        assertTrue(sql.contains(StorageInit.TEXT_HSQLDB));
        assertTrue(sql.contains(StorageInit.MEDIUMTEXT_HSQLDB));
        assertFalse(sql.contains(StorageInit.MARIADB_CREATE_SQL_SUFFIX));
    }

    @Test
    void when_ask_table_from_row_then_return_it() {
        storageInit.init(pluginLoader);

        Table<? extends Row> test1Table = storageInit.getTableFromClassRow(Test1Row.class);

        assertInstanceOf(Test1Table.class, test1Table);
    }

    static class Test1Row implements RowWithId {

        private long id = 0;

        @Override
        public long getId() {
            return id;
        }

        @Override
        public void setId(long id) {
            this.id = id;
        }
    }

    @TableRegistered(row = Test1Row.class)
    static class Test1Table implements Table<Test1Row> {

        @Override
        public @NotNull Test1Row insert(@NotNull Connection conn, @NotNull Test1Row test1Row) throws SQLException {
            // Empty for test
            return test1Row;
        }

        @Override
        public @NotNull Test1Row update(@NotNull Connection conn, @NotNull Test1Row test1Row) throws SQLException {
            // Empty for test
            return test1Row;
        }

        @Override
        public @NotNull Test1Row delete(@NotNull Connection conn, @NotNull Test1Row test1Row) throws SQLException {
            // Empty for test
            return test1Row;
        }
    }

    static class Test2Row implements Row {
    }

    @TableRegistered(row = Test2Row.class, dependsOn = Test1Table.class)
    static class Test2Table implements Table<Test2Row> {

        @Override
        public @NotNull Test2Row insert(@NotNull Connection conn, @NotNull Test2Row test2Row) throws SQLException {
            // Empty for test
            return test2Row;
        }

        @Override
        public @NotNull Test2Row update(@NotNull Connection conn, @NotNull Test2Row test2Row) throws SQLException {
            // Empty for test
            return test2Row;
        }

        @Override
        public @NotNull Test2Row delete(@NotNull Connection conn, @NotNull Test2Row test2Row) throws SQLException {
            // Empty for test
            return test2Row;
        }
    }
}
