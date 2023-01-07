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
package app.secuboid.core.scoreboard;

import app.secuboid.core.SecuboidImpl;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static app.secuboid.core.messages.Log.log;
import static java.util.logging.Level.WARNING;

public class SecuboidScoreboard {

    private static final String SCOREBOARD_PREFIX = "secuboid-";

    private final @NotNull Player player;

    private final @NotNull String displayName;
    private final @NotNull String[] lines;
    private @Nullable Scoreboard scoreboard;
    private @Nullable Objective objective;

    public SecuboidScoreboard(@NotNull Player player, @NotNull String displayName, @NotNull String... lines) {
        this.player = player;
        this.displayName = displayName;
        this.lines = lines;
        scoreboard = null;
        objective = null;
    }

    public void init() {
        ScoreboardManager scoreboardManager = SecuboidImpl.getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective(getName(), Criteria.DUMMY, displayName, RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int length = lines.length;

        for (int i = 0; i < length; i++) {
            Score score = objective.getScore(lines[i]);
            score.setScore(length - i);
        }

        player.setScoreboard(scoreboard);
    }

    public void changeLine(int lineNb, @NotNull String newLine) {
        if (scoreboard == null || objective == null) {
            log().log(WARNING, "No scoreboard to update for the player: {}", player.getName());
            return;
        }

        scoreboard.resetScores(lines[lineNb]);
        Score score = objective.getScore(newLine);
        score.setScore(lines.length - lineNb);
        lines[lineNb] = newLine;
    }

    public void hide() {
        if (player.isOnline()) {
            ScoreboardManager scoreboardManager = SecuboidImpl.getScoreboardManager();
            player.setScoreboard(scoreboardManager.getMainScoreboard());
        }
    }

    private String getName() {
        return SCOREBOARD_PREFIX + player.getName();
    }
}
