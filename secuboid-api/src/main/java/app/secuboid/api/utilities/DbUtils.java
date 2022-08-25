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
package app.secuboid.api.utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

public class DbUtils {

    @FunctionalInterface
    public interface SqlBiConsumer<T, U> {
        void accept(T t, U u) throws SQLException;
    }

    @FunctionalInterface
    public interface SqlFunction<T, R> {
        R apply(T t) throws SQLException;
    }

    @FunctionalInterface
    public interface SqlConsumer<T> {
        void accept(T t) throws SQLException;
    }

    private DbUtils() {
    }

    public static void setCalendar(PreparedStatement stmt, int parameterIndex, Calendar calendar)
            throws SQLException {
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        stmt.setTimestamp(parameterIndex, timestamp);
    }

    public static Calendar getCalendar(ResultSet rs, String columnLabel) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnLabel);

        if (timestamp == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return calendar;
    }

    public static <U> void setNullable(PreparedStatement stmt, int parameterIndex, U uNullable,
            SqlBiConsumer<Integer, U> consumer) throws SQLException {
        if (uNullable != null) {
            consumer.accept(parameterIndex, uNullable);
        } else {
            stmt.setNull(parameterIndex, Types.NULL);
        }
    }

    public static <R> R getNullable(ResultSet rs, String columnLabel, SqlFunction<String, R> function)
            throws SQLException {
        R r = function.apply(columnLabel);
        if (rs.wasNull()) {
            return null;
        }
        return r;
    }
}