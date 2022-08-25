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
package me.tabinol.secuboid.core.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.tabinol.secuboid.core.SecuboidImpl;

public class SecuboidScoreboard {

    private static final String SCOREBOARD_PREFIX = "secuboid-";
    private static final String CRETERIA = "";

    private final Player player;
    private final String[] lines;
    private final Scoreboard scoreboard;
    private final Objective objective;

    public SecuboidScoreboard(Player player, String displayName, String... lines) {
        this.player = player;
        this.lines = lines;

        ScoreboardManager scoreboardManager = SecuboidImpl.getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective(getName(), CRETERIA, displayName, RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int length = lines.length;

        for (int i = 0; i < length; i++) {
            Score score = objective.getScore(lines[i]);
            score.setScore(length - i);
        }

        player.setScoreboard(scoreboard);
    }

    public void changeline(int lineNb, String newLine) {
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
