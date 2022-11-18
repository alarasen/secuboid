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
package app.secuboid.core.it;

import app.secuboid.core.SecuboidImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;

class SecuboidIT {

    @TempDir
    static File pluginTempDir;

    static MinecraftServer minecraftServer;
    static SecuboidImpl secuboidImpl;

    @BeforeAll
    static void beforeAll() {
        minecraftServer = new MinecraftServer(pluginTempDir);
        minecraftServer.load();
        secuboidImpl = minecraftServer.getSecuboidComponent(SecuboidImpl.class);
    }

    @Test
    void when_server_up_then_flags_not_empty() {
        assertFalse(secuboidImpl.getFlagTypes().getFlagTypeNames().isEmpty());
    }

    @AfterAll
    static void afterAll() {
        minecraftServer.unload();
    }
}
