package io.github.swampus.neurysteria.config;

import io.github.swampus.neurysteria.model.EmotionState;

import java.io.InputStream;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

public class BirthProfileLoader {

    public static BirthProfileRegistry loadAllProfiles() {
        BirthProfileRegistry registry = new BirthProfileRegistry();
        for (EmotionState state : EmotionState.values()) {
            String resourceName = "birth/" + state.name().toLowerCase() + "-birth.yml";
            BirthProfileConfig config = loadFromYaml(resourceName);
            registry.register(state, config);
        }
        return registry;
    }

    private static BirthProfileConfig loadFromYaml(String resourceName) {
        LoaderOptions options = new LoaderOptions();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        CustomClassLoaderConstructor constructor = new CustomClassLoaderConstructor(
                BirthProfileConfig.class, classLoader, options
        );
        Yaml yaml = new Yaml(constructor);

        InputStream input = BirthProfileLoader.class
                .getClassLoader()
                .getResourceAsStream(resourceName);

        if (input == null) {
            throw new IllegalArgumentException("Cannot find resource: " + resourceName);
        }

        return yaml.load(input);
    }
}
