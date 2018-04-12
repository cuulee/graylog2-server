package org.graylog.plugins.enterprise.search.engine;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import org.joda.time.DateTime;

import static org.joda.time.DateTimeZone.UTC;

@AutoValue
@JsonDeserialize(builder = QueryExecutionStats.Builder.class)
public abstract class QueryExecutionStats {
    @JsonProperty("duration")
    public abstract long duration();

    @JsonProperty("timestamp")
    public abstract DateTime timestamp();

    public static QueryExecutionStats empty() {
        return builder().build();
    }

    public static Builder builderWithCurrentTime() {
        return builder().timestamp(DateTime.now(UTC));
    }

    public static Builder builder() {
        return Builder.create();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public static abstract class Builder {
        @JsonCreator
        public static Builder create() {
            return new AutoValue_QueryExecutionStats.Builder()
                    .timestamp(DateTime.now(UTC))
                    .duration(0L);
        }

        @JsonProperty("duration")
        public abstract Builder duration(long duration);

        @JsonProperty("timestamp")
        public abstract Builder timestamp(DateTime timestamp);

        public abstract QueryExecutionStats build();
    }
}