package me.tabinol.secuboid.api.storage.tables;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import me.tabinol.secuboid.api.exceptions.SecuboidRuntimeException;

/**
 * Every table classes must extend this class. You need also to have the
 * constant CREATE_TABLE_SQL defined:
 * 
 * <pre>
 * public static final String CREATE_TABLE_SQL = ""
 *         + "CREATE TABLE IF NOT EXISTS %1$stable_name ("
 *         + "  id INT NOT NULL {{AUTOINCREMENT}},"
 *         + "  name VARCHAR(45) NOT NULL,"
 *         + "  PRIMARY KEY (id),"
 *         + "  CONSTRAINT uuid_unique UNIQUE (uuid)"
 *         + ")";
 * </pre>
 * 
 * The sql syntax for create table. "%1$s" will be replaced with the prefix.
 * MariaDB concats "CHARACTER SET 'utf8' COLLATE 'utf8_general_ci ENGINE =
 * InnoDB".
 * 
 * <pre>
 * Other remplacements:
 * {{AUTOINCREMENT}}: The auto increment instruction for the database
 * </pre>
 */
public interface Table<R extends Row> {

    /**
     * Select all resources and return it with the column index in the specific
     * type.
     * 
     * @param conn the connnection
     * @return the set of rows
     * @throws SQLException the SQL exception
     */
    default Set<R> selectAll(Connection conn) throws SQLException {
        throw new SecuboidRuntimeException("SQL SELECT ALL Not implemented!");
    }

    /**
     * Inserts the resource and return the it with column index in the specific
     * type.
     * 
     * @param conn the connnection
     * @param row  the row
     * @return the row
     * @throws SQLException the SQL exception
     */
    default R insert(Connection conn, R row) throws SQLException {
        throw new SecuboidRuntimeException("SQL INSERT Not implemented!");
    }

    /**
     * Updates the resource from it id.
     * 
     * @param conn the connnection
     * @param row  the row
     * @return the row
     * @throws SQLException the SQL exception
     */
    default R update(Connection conn, R row) throws SQLException {
        throw new SecuboidRuntimeException("SQL UPDATE Not implemented!");
    }

    /**
     * Deletes the resource from it id.
     * 
     * @param conn the connnection
     * @param row  the row
     * @return the row
     * @throws SQLException the SQL exception
     */
    default R delete(Connection conn, R row) throws SQLException {
        throw new SecuboidRuntimeException("SQL DELETE Not implemented!");
    }
}
