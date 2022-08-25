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
package app.secuboid.core.lands.areas;

import app.secuboid.api.lands.areas.Area;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AreasTest {

    private static final int ID_AREA_1 = 1;
    private static final int ID_AREA_2 = 2;

    private Areas areas;

    @BeforeEach
    void beforeEarch() {
        areas = new Areas();
    }

    @Test
    void when_inside_area_then_get_it() {
        Area area = new AreaImpl(new CuboidAreaFormImpl(0, 0, 0, 99, 255, 99), ID_AREA_1, null);
        areas.add(area);

        for (int x = 0; x <= 99; x++) {
            for (int z = 0; z <= 99; z++) {
                Set<Area> targetAreas = areas.get(x, 5, z, true);
                assertTrue(targetAreas.contains(area));
            }
        }
    }

    @Test
    void when_area_removed_then_not_in_any_set() {
        Area area = new AreaImpl(new CuboidAreaFormImpl(0, 0, 0, 99, 255, 99), ID_AREA_1, null);
        areas.add(area);
        areas.remove(area);

        for (int x = 0; x <= 99; x++) {
            for (int z = 0; z <= 99; z++) {
                Set<Area> targetAreas = areas.get(x, 5, z, true);
                assertFalse(targetAreas.contains(area));
            }
        }
    }

    @Test
    void when_outside_area_then_not_get_it() {
        Area area = new AreaImpl(new CuboidAreaFormImpl(0, 0, 0, 99, 255, 99), ID_AREA_1, null);
        areas.add(area);

        Set<Area> targetAreas = areas.get(-1, 5, -1, true);
        assertEquals(0, targetAreas.size());
        targetAreas = areas.get(100, 5, 99, true);
        assertEquals(0, targetAreas.size());
    }

    @Test
    void when_add_two_areas_then_get_both() {
        Area area = new AreaImpl(new CuboidAreaFormImpl(0, 0, 0, 99, 255, 99), ID_AREA_1, null);
        areas.add(area);
        Area area2 = new AreaImpl(new CuboidAreaFormImpl(98, 0, 98, 99, 255, 99), ID_AREA_2, null);
        areas.add(area2);

        Set<Area> targetAreas = areas.get(99, 5, 99, true);
        assertEquals(2, targetAreas.size());
    }
}
