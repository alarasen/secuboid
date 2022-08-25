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
package app.secuboid.core.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

    private static final Config INSTANCE = new Config();

    private String lang;
    private String databaseDriver;
    private String databaseUser;
    private String databasePassword;
    private String databaseDatabase;
    private String databaseServerName;
    private int databasePortNumber;
    private String databasePrefix;
    private int selectionDefaultStartDiameter;

    private Config() {
        // Empty configuration on server start
        FileConfiguration fileConfiguration = new YamlConfiguration();
        load(fileConfiguration);
    }

    public static Config config() {
        return INSTANCE;
    }

    public String lang() {
        return lang;
    }

    public String databaseDriver() {
        return databaseDriver;
    }

    public String databaseUser() {
        return databaseUser;
    }

    public String databasePassword() {
        return databasePassword;
    }

    public String databaseDatabase() {
        return databaseDatabase;
    }

    public String databaseServerName() {
        return databaseServerName;
    }

    public int databasePortNumber() {
        return databasePortNumber;
    }

    public String databasePrefix() {
        return databasePrefix;
    }

    public int selectionDefaultStartDiameter() {
        return selectionDefaultStartDiameter;
    }

    public void load(FileConfiguration fileConfiguration) {
        fileConfiguration.addDefault("lang", "en");
        lang = fileConfiguration.getString("lang");
        fileConfiguration.addDefault("database.driver", "hsqldb");
        databaseDriver = fileConfiguration.getString("database.driver");
        fileConfiguration.addDefault("database.user", "secuboid");
        databaseUser = fileConfiguration.getString("database.user");
        fileConfiguration.addDefault("database.password", "secuboid");
        databasePassword = fileConfiguration.getString("database.password");
        fileConfiguration.addDefault("database.database", "file:{{plugin-path}}/storage/db");
        databaseDatabase = fileConfiguration.getString("database.database");
        fileConfiguration.addDefault("database.server-name", "localhost");
        databaseServerName = fileConfiguration.getString("database.server-name");
        fileConfiguration.addDefault("database.port-number", 3306);
        databasePortNumber = fileConfiguration.getInt("database.port-number");
        fileConfiguration.addDefault("database.prefix", "secuboid_");
        databasePrefix = fileConfiguration.getString("database.prefix");
        fileConfiguration.addDefault("selection.default-start-diameter", 3);
        selectionDefaultStartDiameter = fileConfiguration.getInt("selection.default-start-diameter");
    }
}
