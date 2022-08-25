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
package app.secuboid.api.messages;

import java.util.Arrays;
import java.util.Objects;

/**
 * This is the record to gets a message from the yaml. The goal here is to
 * evitate a message to an unexisting path.
 * 
 * @param yamlPath     the yaml path
 * @param replacedTags the tags to replace
 * @param args         the arguments to put in place of tags
 */
public record MessagePath(String yamlPath, String[] replacedTags, Object[] args) {

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MessagePath messagePath = (MessagePath) o;
        return Objects.equals(yamlPath, messagePath.yamlPath) && Arrays.equals(replacedTags, messagePath.replacedTags)
                && Arrays.equals(args, messagePath.args);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(yamlPath);
        result = 31 * result + Arrays.hashCode(replacedTags);
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }

    @Override
    public String toString() {
        return "MessagePath{" +
                "yamlPath=" + yamlPath +
                ", replacedTags=" + Arrays.toString(replacedTags) +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
