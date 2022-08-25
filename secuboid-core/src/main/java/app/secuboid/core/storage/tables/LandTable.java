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
package app.secuboid.core.storage.tables;

import app.secuboid.api.exceptions.SecuboidRuntimeException;
import app.secuboid.api.lands.*;
import app.secuboid.api.reflection.TableRegistered;
import app.secuboid.api.storage.tables.Table;
import app.secuboid.api.utilities.DbUtils;
import app.secuboid.core.lands.AreaLandImpl;
import app.secuboid.core.lands.ConfigurationSetImpl;
import app.secuboid.core.lands.LandImpl;
import app.secuboid.core.lands.WorldLandImpl;
import app.secuboid.core.messages.Log;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import static app.secuboid.core.config.Config.config;
import static java.lang.String.format;

@TableRegistered(row = LandComponent.class)
public class LandTable implements Table<LandComponent> {

    private static final String TYPE_WORLD_LAND = "W";
    private static final String TYPE_AREA_LAND = "L";
    private static final String TYPE_CONFIGURATION_SET = "S";

    // Needed for automatic table create
    public static final String CREATE_TABLE_SQL = ""
            + "CREATE TABLE IF NOT EXISTS %1$sland ("
            + " id INT NOT NULL {{AUTOINCREMENT}},"
            + " name VARCHAR(45) NOT NULL,"
            + " type CHAR(1) NOT NULL,"
            + " parent_id INT NULL,"
            + " PRIMARY KEY (id),"
            + " CONSTRAINT fk_land_parent_id FOREIGN KEY (parent_id) REFERENCES %1$sland (id)"
            + ")";

    @Override
    public Set<LandComponent> selectAll(Connection conn) throws SQLException {
        String prefix = config().databasePrefix();
        String sql = format(""
                + "SELECT id, name, type, parent_id FROM %1$sland", prefix);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, LandComponent> idToLandComponent = new HashMap<>();
                Map<LandComponent, Integer> landCoponentToParentId = new HashMap<>();

                while (rs.next()) {
                    LandComponent landComponent = getLandComponent(rs);
                    idToLandComponent.put(landComponent.getId(), landComponent);
                    Integer parentId = getParentId(rs);
                    if (parentId != null) {
                        landCoponentToParentId.put(landComponent, parentId);
                    }
                }

                for (Entry<LandComponent, Integer> entry : landCoponentToParentId.entrySet()) {
                    LandComponent landComponent = entry.getKey();
                    int parentId = entry.getValue();
                    setParent(idToLandComponent, landComponent, parentId);
                }

                return idToLandComponent.values().stream().collect(Collectors.toSet());
            }
        }
    }

    private void setParent(Map<Integer, LandComponent> idToLandComponent, LandComponent landComponent, int parentId) {

        if (landComponent instanceof AreaLand areaLand) {
            LandComponent parent = idToLandComponent.get(parentId);
            if (parent instanceof LandImpl parentLandImpl) {
                parentLandImpl.setChild(areaLand);

                return;
            }
        }

        Log.log().warning(() -> format("This land has no parent and will be unreachable [id=%s, name=%s, parentId=%s]",
                landComponent.getId(), landComponent.getName(), parentId));
    }

    private LandComponent getLandComponent(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String type = rs.getString("type");

        LandComponent landComponent;
        switch (type) {
            case TYPE_WORLD_LAND -> landComponent = new WorldLandImpl(name);
            case TYPE_AREA_LAND -> landComponent = new AreaLandImpl(name, null);
            case TYPE_CONFIGURATION_SET -> landComponent = new ConfigurationSetImpl(name);
            default -> throw new SecuboidRuntimeException(format("Invalid land type [name=%s, type=%s]", name, type));
        }

        landComponent.setId(id);

        return landComponent;
    }

    private Integer getParentId(ResultSet rs) throws SQLException {
        return DbUtils.getNullable(rs, "parent_id", rs::getInt);
    }

    @Override
    public LandComponent insert(Connection conn, LandComponent landComponent) throws SQLException {
        String prefix = config().databasePrefix();
        String sql = format(""
                + "INSERT INTO %1$sland(name, type, parent_id)"
                + " VALUES(?, ?)", prefix);

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, landComponent.getName());
            stmt.setString(2, getLandType(landComponent));
            DbUtils.setNullable(stmt, 3, getParentId(landComponent), stmt::setInt);

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                landComponent.setId(rs.getInt(1));
            }
        }

        return landComponent;
    }

    @Override
    public LandComponent update(Connection conn, LandComponent landComponent) throws SQLException {
        String prefix = config().databasePrefix();
        String sql = format(""
                + "UPDATE %1$sland"
                + " SET name = ?, type = ?, parent_id = ?"
                + " WHERE id = ?", prefix);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, landComponent.getName());
            stmt.setString(2, getLandType(landComponent));
            DbUtils.setNullable(stmt, 3, getParentId(landComponent), stmt::setInt);
            stmt.setLong(4, landComponent.getId());

            stmt.executeUpdate();
        }

        return landComponent;
    }

    @Override
    public LandComponent delete(Connection conn, LandComponent landComponent) throws SQLException {
        String prefix = config().databasePrefix();
        String sql = format(""
                + "DELETE FROM %1$sland"
                + " WHERE id = ?", prefix);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, landComponent.getId());

            stmt.executeUpdate();
        }

        return landComponent;
    }

    private String getLandType(LandComponent landComponent) {
        if (landComponent instanceof WorldLand) {
            return TYPE_WORLD_LAND;
        }

        if (landComponent instanceof AreaLand) {
            return TYPE_AREA_LAND;
        }

        if (landComponent instanceof ConfigurationSet) {
            return TYPE_CONFIGURATION_SET;
        }

        throw new SecuboidRuntimeException(
                "Class not yet implemented in LandTable: " + landComponent.getClass().getName());
    }

    private Integer getParentId(LandComponent landComponent) {
        if (landComponent instanceof AreaLand areaLand) {
            Land parent = areaLand.getParent();
            if (parent != null) {
                return parent.getId();
            }
        }

        return null;
    }
}
