package com.example.hydrativa.models;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class LoginResponseDeserializer implements JsonDeserializer<LoginResponse> {
    @Override
    public LoginResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Check if JSON element is a string (token only)
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            return new LoginResponse(json.getAsString()); // direct token
        } else if (json.isJsonObject() && json.getAsJsonObject().has("token")) {
            // JSON object with a token field
            String token = json.getAsJsonObject().get("token").getAsString();
            return new LoginResponse(token);
        } else {
            throw new JsonParseException("Unexpected JSON format");
        }
    }
}

