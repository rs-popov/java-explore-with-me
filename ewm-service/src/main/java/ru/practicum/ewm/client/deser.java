package ru.practicum.ewm.client;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class deser  extends StdDeserializer<ViewStatsDto> {
    public deser() {
        this(null);
    }

    public deser(Class<ViewStatsDto> t) {
        super(t);
    }

    @Override
    public ViewStatsDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ViewStatsDto viewStatsDto = ViewStatsDto.builder().build();
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);


        viewStatsDto.setApp(node.get("app").asText());
        viewStatsDto.setUri(node.get("uri").asText());
        viewStatsDto.setHits(node.get("hits").asInt());

        return viewStatsDto;

    }
}
