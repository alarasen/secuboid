/*
 Secuboid: LandService and Protection plugin for Minecraft server
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
    private String databaseUrl;
    private String databaseUser;
    private String databasePassword;
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

    public String databaseUrl() {
        return databaseUrl;
    }

    public String databaseUser() {
        return databaseUser;
    }

    public String databasePassword() {
        return databasePassword;
    }

    public int selectionDefaultStartDiameter() {
        return selectionDefaultStartDiameter;
    }

    public void load(FileConfiguration fileConfiguration) {
        fileConfiguration.addDefault("lang", "en");
        lang = fileConfiguration.getString("lang");
        fileConfiguration.addDefault("database.url", "jdbc:hsqldb:file:{{plugin-path}}/storage/db");
        databaseUrl = fileConfiguration.getString("database.url");
        fileConfiguration.addDefault("database.user", "secuboid");
        databaseUser = fileConfiguration.getString("database.user");
        fileConfiguration.addDefault("database.password", "secuboid");
        databasePassword = fileConfiguration.getString("database.password");
        fileConfiguration.addDefault("selection.default-start-diameter", 3);
        selectionDefaultStartDiameter = fileConfiguration.getInt("selection.default-start-diameter");
    }
}
