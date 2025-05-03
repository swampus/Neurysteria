package io.github.swampus.neurysteria.model;

import io.github.swampus.neurysteria.config.NeuronConfig;
import io.github.swampus.neurysteria.model.activation.ActivationFunction;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;

@Getter
public class Neuron {

    private final UUID id;
    private double activation;
    @Setter
    private double rage;
    private final List<Neuron> friends = new ArrayList<>();
    private final List<Neuron> enemies = new ArrayList<>();
    private final NeuronConfig config;
    @Setter
    private ActivationFunction activationFunction;

    private static final Logger log = LoggerFactory.getLogger(Neuron.class);


    public Neuron(NeuronConfig config) {
        this.id = UUID.randomUUID();
        this.activation = 0.0;
        this.rage = 0.0;
        this.config = config;
    }

    public void stimulate(double input) {
        activation += input;
        activation = Math.max(-100.0, Math.min(activation, 100.0));
        if (input < 0) {
            rage += Math.abs(input) * config.rageFromNegativeInput();
        }
    }

    public void calmDown() {
        rage = Math.max(0, rage - config.rageDecayPerTick());
    }

    public boolean isAngry() {
        return rage > config.rageThreshold();
    }

    public void addFriend(Neuron neuron) {
        if (!friends.contains(neuron)) {
            friends.add(neuron);
        }
    }

    public void addEnemy(Neuron neuron) {
        if (!enemies.contains(neuron)) {
            enemies.add(neuron);
        }
    }

    public void removeFriend(Neuron neuron) {
        enemies.remove(neuron);
    }


    public void removeEnemy(Neuron neuron) {
        enemies.remove(neuron);
    }


    public void receiveInput(double input) {
        if (input < 0) {
            double delta = Math.abs(input) * config.rageFromNegativeInput();
            this.rage += delta;
            log.debug("Neuron {} received NEGATIVE input: {} → rage +{} = {}", id, input, delta, rage);
        } else {
            this.activation += input;
            log.debug("Neuron {} received input: {} → activation = {}", id, input, activation);
        }


        this.rage = Math.min(this.rage, 100.0); // 🧯 ограничим ярость
    }

    public void resetRage() {
        this.rage = 0;
    }

    public void shareEnergyWithFriends() {
        if (!isAngry()) {
            for (Neuron friend : friends) {
                friend.stimulate(activation * config.calmShareToFriends());
            }
        }
    }

    public void attackEnemies() {
        if (isAngry()) {
            for (Neuron enemy : enemies) {
                enemy.stimulate(-rage * config.angerImpactOnEnemies());
            }
        }
    }

    public void interact() {
        if (isAngry()) {
            for (Neuron enemy : enemies) {
                enemy.stimulate(-rage * config.angerImpactOnEnemies());
            }
        } else {
            for (Neuron friend : friends) {
                friend.stimulate(activation * config.calmShareToFriends());
            }
        }
    }

    public void clearConnectionsRandomly() {
        Random random = new Random();
        double chance = config.forgetConnectionChance();

        friends.removeIf(f -> random.nextDouble() < chance);
        enemies.removeIf(e -> random.nextDouble() < chance);

        log.debug("Neuron {} cleared connections with chance {} → friends: {}, enemies: {}",
                id, chance, friends.size(), enemies.size());
    }
}
