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
package app.secuboid.core.reflection;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PluginLoader {

    static final String FILENAME_SECUBOID_PLUGIN = "secuboid-plugin.yml";
    static final String YAML_TAG_COMPONENT_CLASSES = "component-classes";

    private final Reflection reflection;
    private final Set<Class<?>> classes;

    public PluginLoader() {
        reflection = new Reflection();
        classes = new HashSet<>();
    }

    public void init(PluginManager pluginManager) {
        if (!classes.isEmpty()) {
            return;
        }

        Set<String> componentClassNames = getComponentClassNamesFromPlugins(pluginManager);
        classes.addAll(reflection.getClasses(componentClassNames));
    }

    public <T, A extends Annotation> Map<Class<? extends T>, A> getClassToAnnotation(Class<A> annotationClass,
                                                                                     Class<T> wantedClass) {
        return reflection.getClassToAnnotation(classes, annotationClass, wantedClass);
    }

    public <T> Set<T> getAnnotatedConstants(Class<? extends Annotation> annotationClass, Class<T> wantedClass) {
        return reflection.getAnnotatedConstants(classes, annotationClass, wantedClass);
    }

    private Set<String> getComponentClassNamesFromPlugins(PluginManager pluginManager) {
        Set<String> result = new HashSet<>();

        for (Plugin plugin : pluginManager.getPlugins()) {
            InputStream resource = plugin.getResource(FILENAME_SECUBOID_PLUGIN);
            if (resource != null) {
                result.addAll(getComponentClassNamesFromPlugin(resource));
            }
        }

        return result;
    }

    private List<String> getComponentClassNamesFromPlugin(InputStream resource) {
        Reader reader = new InputStreamReader(resource);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(reader);
        return yaml.getStringList(YAML_TAG_COMPONENT_CLASSES);
    }
}
