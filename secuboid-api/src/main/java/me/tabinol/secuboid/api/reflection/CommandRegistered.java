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
package me.tabinol.secuboid.api.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The Interface CommandRegistered. Annotation used for commands. If you make
 * your own command, your class should implement CommandExecutor.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandRegistered {

    /**
     * Your plugin class that extends JavaPlugin.
     */
    Class<? extends JavaPlugin> pluginClass();

    /**
     * The command name lowercase. If this command is a child from a parent, type
     * "parent/thiscommand".
     */
    String name();

    /**
     * Aliases if the command can be called with a short alias. Lowercase.
     */
    String[] aliases() default {};

    /**
     * Allowing from console? Default true. Must be false only when the command
     * needs absoutely an in game interaction. Note that a command typed in a
     * console bypass any other permission checks.
     */
    boolean allowConsole() default true;

    /**
     * If a player in "admin mode" has access.
     */
    boolean isAdminModeByPass() default true;

    /**
     * The Bukkit permission(s) needed to do this command. Only one needed to be
     * true.
     */
    String[] permissions() default {};

    /**
     * Name array of Source Action flags needed for your command.
     * SourceActionFlagType accpeted only. Only one needed to be true.
     */
    String[] sourceActionFlags() default {};
}
