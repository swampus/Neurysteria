package io.github.swampus.neurysteria.service;

import io.github.swampus.neurysteria.config.BirthProfileConfig;
import io.github.swampus.neurysteria.config.BirthProfileRegistry;
import io.github.swampus.neurysteria.model.EmotionState;
import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.activation.ActivationFunctions;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NeuronLifecycleService {

    private final BirthProfileRegistry birthRegistry;
    private final Random random = new Random();

    private static final Logger log = LoggerFactory.getLogger(NeuronLifecycleService.class);

    public NeuronLifecycleService(BirthProfileRegistry birthRegistry) {
        this.birthRegistry = birthRegistry;
    }

    public Neuron createNewNeuronReplacing(NeuronNetwork network, Neuron oldNeuron) {
        var config = oldNeuron.getConfig();
        var newNeuron = new Neuron(config);
        EmotionState state = network.getCurrentState();
        BirthProfileConfig profile = birthRegistry.getProfile(state);

        newNeuron.setRage(rand(profile.getAngryRageMin(), profile.getAngryRageMax()));


        if (state == EmotionState.HYSTERICAL && profile.isRandomizeActivationFunctionInHysteria()) {
            newNeuron.setActivationFunction(ActivationFunctions.random());
        }

        List<Neuron> usefulPeers = new java.util.ArrayList<>(network.getNeurons().stream()
                .filter(n -> n != oldNeuron)
                .filter(n -> n.getActivation() > 1)
                .filter(n -> n.getRage() < 5)
                .toList());

        if (usefulPeers.isEmpty()) {
            log.warn("⚠️ Newborn {} has no useful peers → fallback to random", newNeuron.getId());
            List<Neuron> fallback = network.getNeurons().stream()
                    .filter(n -> n != oldNeuron)
                    .limit(5)
                    .toList();

            for (Neuron peer : fallback) {
                newNeuron.addFriend(peer);
                peer.addFriend(newNeuron);
            }

            return newNeuron;
        }

        int maxFriends = 3;
        int maxEnemies = 2;
        int friendCount = 0;
        int enemyCount = 0;

        if (network.getCurrentState().equals(EmotionState.HYSTERICAL)) {
            maxFriends = 1;
            maxEnemies = 66;
        }

        Collections.shuffle(usefulPeers);

        for (Neuron peer : usefulPeers) {
            if (friendCount < maxFriends && random.nextBoolean()) {
                newNeuron.addFriend(peer);
                peer.addFriend(newNeuron);
                friendCount++;
            } else if (enemyCount < maxEnemies) {
                newNeuron.addEnemy(peer);
                peer.addEnemy(newNeuron);
                enemyCount++;
            }

            if (friendCount >= maxFriends && enemyCount >= maxEnemies) {
                break;
            }
        }

        log.info("🌱 Newborn {} connected to {} useful peers", newNeuron.getId(), maxFriends);
        return newNeuron;
    }

    private double rand(double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

}
