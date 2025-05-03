package io.github.swampus.neurysteria.config;

import io.github.swampus.neurysteria.model.EmotionState;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class BirthProfileRegistry {

    private final Map<EmotionState, BirthProfileConfig> profiles = new EnumMap<>(EmotionState.class);

    public void register(EmotionState state, BirthProfileConfig config) {
        profiles.put(state, config);
    }

    public BirthProfileConfig getProfile(EmotionState state) {
        return profiles.get(state);
    }
}
