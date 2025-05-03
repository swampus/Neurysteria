package io.github.swampus.neurysteria.service;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.activation.ActivationFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuronMutationService {


    private static final Logger log = LoggerFactory.getLogger(NeuronMutationService.class);

    private final Random random = new Random();

    public void attemptMutation(Neuron neuron) {
        var config = neuron.getConfig();

        if (!config.allowMutation()) {
            return;
        }

        if (random.nextDouble() >= config.mutationChance()) {
            return;
        }

        neuron.resetRage();

        if (random.nextBoolean()) {
            neuron.setActivationFunction(ActivationFunctions.random());
            log.info("Neuron {} mutated to RANDOM function.", neuron.getId());
        } else {
            copyFunctionFromNeighbor(neuron);
        }
    }

    private void copyFunctionFromNeighbor(Neuron neuron) {
        List<Neuron> neighbors = new ArrayList<>();
        neighbors.addAll(neuron.getFriends());
        neighbors.addAll(neuron.getEnemies());

        if (!neighbors.isEmpty()) {
            Neuron chosen = neighbors.get(random.nextInt(neighbors.size()));
            neuron.setActivationFunction(chosen.getActivationFunction());
            log.info("Neuron {} COPIED function from {}", neuron.getId(), chosen.getId());
        }
    }
}
