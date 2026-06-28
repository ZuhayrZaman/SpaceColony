package com.oop.spacecolony.logic;

import com.oop.spacecolony.model.Pilot;
import com.oop.spacecolony.model.Medic;
import com.oop.spacecolony.model.Crew;
import com.oop.spacecolony.model.Soldier;
import com.oop.spacecolony.model.Engineer;
import com.oop.spacecolony.model.Scientist;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CrewTypeAdapter implements JsonSerializer<Crew>, JsonDeserializer<Crew> {

    private static final String TYPE_FIELD = "type";
    private static final String DATA_FIELD = "data";

    @Override
    public JsonElement serialize(Crew src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(TYPE_FIELD, src.getClass().getSimpleName());
        jsonObject.add(DATA_FIELD, context.serialize(src));

        return jsonObject;
    }

    @Override
    public Crew deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get(TYPE_FIELD).getAsString();
        JsonElement data = jsonObject.get(DATA_FIELD);

        switch (type) {
            case "Soldier":
                return context.deserialize(data, Soldier.class);
            case "Pilot":
                return context.deserialize(data, Pilot.class);
            case "Medic":
                return context.deserialize(data, Medic.class);
            case "Scientist":
                return context.deserialize(data, Scientist.class);
            case "Engineer":
                return context.deserialize(data, Engineer.class);
            default:
                throw new JsonParseException("Unknown crew type: " + type);
        }
    }
}