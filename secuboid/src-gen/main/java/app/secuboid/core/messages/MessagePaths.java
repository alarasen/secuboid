/*
 Secuboid: LandService and Protection plugin for Minecraft server
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
package app.secuboid.core.messages;

import app.secuboid.api.messages.MessagePath;

public class MessagePaths {

    private MessagePaths() {
    }
    
    public static MessagePath generalTest(Object argument1, Object argument2) {
        return new MessagePath("general.test", new String[] { "{{argument-1}}", "{{argument-2}}" }, new Object[] { argument1, argument2 });
    }

    public static MessagePath generalPreReload() {
        return new MessagePath("general.pre-reload", new String[] {}, new Object[] {});
    }

    public static MessagePath generalReload() {
        return new MessagePath("general.reload", new String[] {}, new Object[] {});
    }

    public static MessagePath generalInventoryFull() {
        return new MessagePath("general.inventory-full", new String[] {}, new Object[] {});
    }

    public static MessagePath generalNeedParameter() {
        return new MessagePath("general.need-parameter", new String[] {}, new Object[] {});
    }

    public static MessagePath generalError(Object error) {
        return new MessagePath("general.error", new String[] { "{{error}}" }, new Object[] { error });
    }

    public static MessagePath chatPageHeader(Object subject, Object pageNumber, Object totalPages) {
        return new MessagePath("chat-page.header", new String[] { "{{subject}}", "{{page-number}}", "{{total-pages}}" }, new Object[] { subject, pageNumber, totalPages });
    }

    public static MessagePath chatPageFooterLeftActive(Object pageNumber) {
        return new MessagePath("chat-page.footer-left-active", new String[] { "{{page-number}}" }, new Object[] { pageNumber });
    }

    public static MessagePath chatPageFooterRightActive(Object pageNumber) {
        return new MessagePath("chat-page.footer-right-active", new String[] { "{{page-number}}" }, new Object[] { pageNumber });
    }

    public static MessagePath chatPageNotAvailable() {
        return new MessagePath("chat-page.not-available", new String[] {}, new Object[] {});
    }

    public static MessagePath chatPageNotFound(Object firstPage, Object lastPage) {
        return new MessagePath("chat-page.not-found", new String[] { "{{first-page}}", "{{last-page}}" }, new Object[] { firstPage, lastPage });
    }

    public static MessagePath selectionScoreboardActiveTitleCreate() {
        return new MessagePath("selection.scoreboard.active.title-create", new String[] {}, new Object[] {});
    }

    public static MessagePath selectionScoreboardActiveSelectionType(Object selectionType) {
        return new MessagePath("selection.scoreboard.active.selection-type", new String[] { "{{selection-type}}" }, new Object[] { selectionType });
    }

    public static MessagePath selectionScoreboardActiveSelectionTypesPassive() {
        return new MessagePath("selection.scoreboard.active.selection-types.passive", new String[] {}, new Object[] {});
    }

    public static MessagePath selectionScoreboardActiveSelectionTypesExpand() {
        return new MessagePath("selection.scoreboard.active.selection-types.expand", new String[] {}, new Object[] {});
    }

    public static MessagePath selectionScoreboardActiveSelectionTypesRetract() {
        return new MessagePath("selection.scoreboard.active.selection-types.retract", new String[] {}, new Object[] {});
    }

    public static MessagePath selectionScoreboardActiveSelectionTypesMove() {
        return new MessagePath("selection.scoreboard.active.selection-types.move", new String[] {}, new Object[] {});
    }

    public static MessagePath selectionScoreboardActiveVolume(Object volume) {
        return new MessagePath("selection.scoreboard.active.volume", new String[] { "{{volume}}" }, new Object[] { volume });
    }

    public static MessagePath selectionScoreboardActiveTypeWhenDone(Object commandWhenDone) {
        return new MessagePath("selection.scoreboard.active.type-when-done", new String[] { "{{command-when-done}}" }, new Object[] { commandWhenDone });
    }

    public static MessagePath selectionScoreboardAreaTitle() {
        return new MessagePath("selection.scoreboard.area.title", new String[] {}, new Object[] {});
    }

    public static MessagePath selectionScoreboardAreaLocationPath(Object locationPath) {
        return new MessagePath("selection.scoreboard.area.location-path", new String[] { "{{location-path}}" }, new Object[] { locationPath });
    }

    public static MessagePath selectionCancel() {
        return new MessagePath("selection.cancel", new String[] {}, new Object[] {});
    }

    public static MessagePath selectionEmpty() {
        return new MessagePath("selection.empty", new String[] {}, new Object[] {});
    }

    public static MessagePath selectionCreateEnterName() {
        return new MessagePath("selection.create.enter-name", new String[] {}, new Object[] {});
    }

    public static MessagePath selectionCreateNeedActiveSelection(Object commandSelect) {
        return new MessagePath("selection.create.need-active-selection", new String[] { "{{command-select}}" }, new Object[] { commandSelect });
    }

    public static MessagePath selectionCreateNoSpace() {
        return new MessagePath("selection.create.no-space", new String[] {}, new Object[] {});
    }

    public static MessagePath selectionCreateCreated(Object land) {
        return new MessagePath("selection.create.created", new String[] { "{{land}}" }, new Object[] { land });
    }

    public static MessagePath infoLocationPath(Object locationPath) {
        return new MessagePath("info.location-path", new String[] { "{{location-path}}" }, new Object[] { locationPath });
    }

    public static MessagePath toolName() {
        return new MessagePath("tool.name", new String[] {}, new Object[] {});
    }

    public static MessagePath toolLore1() {
        return new MessagePath("tool.lore-1", new String[] {}, new Object[] {});
    }

    public static MessagePath toolLore2() {
        return new MessagePath("tool.lore-2", new String[] {}, new Object[] {});
    }

    public static MessagePath toolDone() {
        return new MessagePath("tool.done", new String[] {}, new Object[] {});
    }

    public static MessagePath toolAlready() {
        return new MessagePath("tool.already", new String[] {}, new Object[] {});
    }

    public static MessagePath areaCuboid(Object x1, Object y1, Object z1, Object x2, Object y2, Object z2) {
        return new MessagePath("area.cuboid", new String[] { "{{x1}}", "{{y1}}", "{{z1}}", "{{x2}}", "{{y2}}", "{{z2}}" }, new Object[] { x1, y1, z1, x2, y2, z2 });
    }

    public static MessagePath areaCylinder(Object originH, Object originK, Object rx, Object rz) {
        return new MessagePath("area.cylinder", new String[] { "{{origin-h}}", "{{origin-k}}", "{{rx}}", "{{rz}}" }, new Object[] { originH, originK, rx, rz });
    }
}
