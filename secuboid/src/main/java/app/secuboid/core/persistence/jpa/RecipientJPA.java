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

package app.secuboid.core.persistence.jpa;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "secuboid_recipient", uniqueConstraints = @UniqueConstraint(columnNames = {"short_name", "value", "uuid"}))
public class RecipientJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "short_name", nullable = false, length = 10)
    private String shortName;

    @Column(name = "value")
    private String value;
    @Column(name = "uuid")
    private UUID uuid;

    public long getId() {
        return id;
    }

    public RecipientJPA setId(long id) {
        this.id = id;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public RecipientJPA setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public String getValue() {
        return value;
    }

    public RecipientJPA setValue(String value) {
        this.value = value;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public RecipientJPA setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipientJPA that = (RecipientJPA) o;
        return id == that.id && Objects.equals(shortName, that.shortName) && Objects.equals(value, that.value) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortName, value, uuid);
    }

    @Override
    public String toString() {
        return "RecipientJPA{" +
                "id=" + id +
                ", shortName='" + shortName + '\'' +
                ", value='" + value + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}
