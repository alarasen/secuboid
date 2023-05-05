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
package app.secuboid.core.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;
import java.util.Objects;

public record SecuboidScoreboard(
        Player player,
        Scoreboard scoreboard,
        Objective objective,
        String[] lines
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecuboidScoreboard that = (SecuboidScoreboard) o;
        return player.equals(that.player) && scoreboard.equals(that.scoreboard) && objective.equals(that.objective) && Arrays.equals(lines, that.lines);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(player, scoreboard, objective);
        result = 31 * result + Arrays.hashCode(lines);
        return result;
    }

    @Override
    public String toString() {
        return "SecuboidScoreboard{" +
                "player=" + player +
                ", scoreboard=" + scoreboard +
                ", objective=" + objective +
                ", lines=" + Arrays.toString(lines) +
                '}';
    }
}
