/*
 *  Secuboid: LandService and Protection plugin for Minecraft server
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

package app.secuboid.core.persistence;

import app.secuboid.api.exceptions.SecuboidRuntimeException;
import app.secuboid.api.services.Service;
import app.secuboid.core.config.Config;
import app.secuboid.core.persistence.jpa.AreaJPA;
import app.secuboid.core.persistence.jpa.LandJPA;
import app.secuboid.core.persistence.jpa.RecipientJPA;
import app.secuboid.core.persistence.jpa.ResidentJPA;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

import static app.secuboid.core.config.Config.config;

public class PersistenceSessionService implements Service {

    // Also used in Config class
    public static final String DRIVER_HSQLDB = "jdbc:hsqldb";
    public static final String DRIVER_MARIADB = "jdbc:mariadb";

    public static final String HBM2DDL_AUTO_VALUE = "update";


    private static final String TAG_PLUGIN_PATH = "{{plugin-path}}";

    private final @NotNull JavaPlugin javaPlugin;

    private boolean isLocalHSQL;
    private @Nullable SessionFactory sessionFactory;

    public PersistenceSessionService(@NotNull JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        isLocalHSQL = false;
    }

    @Override
    public void onEnable(boolean isServerBoot) {
        Config config = config();
        String dataFolderStr = javaPlugin.getDataFolder().getAbsolutePath();
        String url = config.getDatabaseUrl().replace(TAG_PLUGIN_PATH, dataFolderStr);
        String user = config.getDatabaseUser();
        String password = config.getDatabasePassword();

        Properties properties = new Properties();

        String hibernateDialect;
        String hibernateConnectionDriverClass;
        if (url.contains(DRIVER_HSQLDB)) {
            if (url.startsWith("jdbc:hsqldb:file:")) {
                isLocalHSQL = true;
            }
            hibernateDialect = "org.hibernate.dialect.HSQLDialect";
            hibernateConnectionDriverClass = "org.hsqldb.jdbc.JDBCDriver";
        } else if (url.contains(DRIVER_MARIADB)) {
            hibernateDialect = "org.hibernate.dialect.MySQLDialect";
            hibernateConnectionDriverClass = "org.mariadb.jdbc.Driver";
        } else {
            isLocalHSQL = false;
            throw new SecuboidRuntimeException("Database driver \"mariadb\" (also for MySQL support) and \"hsqldb\" are the only supported");
        }

        properties.setProperty("hibernate.dialect", hibernateDialect);
        properties.setProperty("hibernate.connection.driver_class", hibernateConnectionDriverClass);
        properties.setProperty("hibernate.connection.url", url);
        properties.setProperty("hibernate.connection.username", user);
        properties.setProperty("hibernate.connection.password", password);
        properties.setProperty("hibernate.hbm2ddl.auto", HBM2DDL_AUTO_VALUE);


        Configuration configuration = new Configuration();
        configuration.addProperties(properties);
        configuration.addAnnotatedClass(AreaJPA.class);
        configuration.addAnnotatedClass(LandJPA.class);
        configuration.addAnnotatedClass(RecipientJPA.class);
        configuration.addAnnotatedClass(ResidentJPA.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public void onDisable(boolean isServerShutdown) {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            if (isLocalHSQL) {
                try (Session session = getSession()) {
                    shutdownLocalHSQL(session);
                }
            }
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    @NotNull
    public Session getSession() {
        assert sessionFactory != null : "The Secuboid datasource is closed or net yet available";
        return sessionFactory.openSession();
    }

    private void shutdownLocalHSQL(@NotNull Session session) {
        session.beginTransaction();
        session.createNativeMutationQuery("SHUTDOWN").executeUpdate();
    }
}
