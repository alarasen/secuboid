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
import app.secuboid.api.registration.RegistrationService;
import app.secuboid.api.services.Service;
import app.secuboid.core.config.Config;
import app.secuboid.core.registration.RegistrationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

import static app.secuboid.core.config.Config.config;

@RequiredArgsConstructor
public class PersistenceSessionService implements Service {

    // Also used in Config class
    public static final String DRIVER_HSQLDB = "jdbc:hsqldb";
    public static final String DRIVER_MARIADB = "jdbc:mariadb";

    public static final String CONNECTION_PROVIDER_CLASS = "org.hibernate.hikaricp.internal.HikariCPConnectionProvider";
    public static final String HBM2DDL_AUTO_VALUE = "update";


    private static final String TAG_PLUGIN_PATH = "{{plugin-path}}";

    private final JavaPlugin javaPlugin;
    private final RegistrationService registrationService;

    private boolean isLocalHSQL = false;
    private SessionFactory sessionFactory = null;

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
        properties.setProperty("hibernate.connection.provider_class", CONNECTION_PROVIDER_CLASS);
        properties.setProperty("hibernate.hbm2ddl.auto", HBM2DDL_AUTO_VALUE);

        Configuration configuration = new Configuration();
        configuration.addProperties(properties);
        ((RegistrationServiceImpl) registrationService).getJpaClasses().forEach(configuration::addAnnotatedClass);
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

    public Session getSession() {
        return sessionFactory.openSession();
    }

    private void shutdownLocalHSQL(Session session) {
        session.beginTransaction();
        session.createNativeMutationQuery("SHUTDOWN").executeUpdate();
    }
}
