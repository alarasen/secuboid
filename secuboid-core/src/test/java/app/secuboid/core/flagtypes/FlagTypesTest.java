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
package app.secuboid.core.flagtypes;

import app.secuboid.api.flagtypes.FlagType;
import app.secuboid.api.flagtypes.FlagTypes;
import app.secuboid.api.reflection.FlagRegistered;
import app.secuboid.core.reflection.PluginLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FlagTypesTest {

    private static final String FLAG_NAME_TEST = "test-flag";

    private PluginLoader pluginLoader;

    private FlagTypes flagTypes;

    @BeforeEach
    void beforeEach() {
        pluginLoader = mock(PluginLoader.class);
        FlagType flagType = new FlagType(FLAG_NAME_TEST, FLAG_NAME_TEST, true, false, false, false);
        when(pluginLoader.getAnnotatedConstants(FlagRegistered.class, FlagType.class))
                .thenReturn(Collections.singleton(flagType));

        flagTypes = new FlagTypesImpl();
        ((FlagTypesImpl) flagTypes).init(pluginLoader);
    }

    @Test
    void when_get_flag_type_then_return_it() {
        FlagType flagType = flagTypes.getFlagType(FLAG_NAME_TEST);

        assertNotNull(flagType);
        assertEquals(FLAG_NAME_TEST, flagType.name());
    }

    @Test
    void when_get_flag_names_then_return_created_one() {
        Set<String> flagTypeNames = flagTypes.getFlagTypeNames();

        assertTrue(flagTypeNames.contains(FLAG_NAME_TEST));
    }
}
