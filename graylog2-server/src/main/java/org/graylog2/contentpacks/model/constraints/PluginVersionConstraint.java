/**
 * This file is part of Graylog.
 *
 * Graylog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graylog2.contentpacks.model.constraints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.vdurmont.semver4j.Requirement;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.Version;

import java.util.Optional;

@AutoValue
@JsonDeserialize(builder = AutoValue_PluginVersionConstraint.Builder.class)
public abstract class PluginVersionConstraint implements Constraint {
    static final String TYPE_NAME = "plugin-version";
    static final String FIELD_PLUGIN_ID = "plugin";
    static final String FIELD_PLUGIN_VERSION = "version";

    @JsonProperty(FIELD_PLUGIN_ID)
    public abstract String pluginId();

    @JsonProperty(FIELD_PLUGIN_VERSION)
    public abstract Requirement version();

    public abstract Builder toBuilder();

    public static PluginVersionConstraint of(PluginMetaData pluginMetaData) {
        final Version version = pluginMetaData.getVersion();
        final String versionString = version.toString().replace("-SNAPSHOT", "");
        final Requirement requirement = Requirement.buildNPM(">=" + versionString);

        return builder()
                .pluginId(pluginMetaData.getUniqueId())
                .version(requirement)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PluginVersionConstraint.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder implements Constraint.ConstraintBuilder<Builder> {
        @JsonProperty(FIELD_PLUGIN_ID)
        public abstract Builder pluginId(String pluginId);

        @JsonProperty(FIELD_PLUGIN_VERSION)
        public abstract Builder version(Requirement version);

        @JsonIgnore
        public Builder version(String versionExpression) {
            final Requirement requirement = Requirement.buildNPM(versionExpression);
            return version(requirement);
        }

        abstract PluginVersionConstraint autoBuild();

        public PluginVersionConstraint build() {
            type(TYPE_NAME);
            return autoBuild();
        }
    }
}
