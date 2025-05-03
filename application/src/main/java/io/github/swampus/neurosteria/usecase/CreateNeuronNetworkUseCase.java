package io.github.swampus.neurosteria.usecase;

import io.github.swampus.neurysteria.config.NeuronConfig;
import io.github.swampus.neurysteria.model.Neuron;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CreateNeuronNetworkUseCase {

    private final Random random = new Random();

    public List<Neuron> create(int size, NeuronConfig config) {
        List<Neuron> neurons = new ArrayList<>();

        // Step 1: Create neurons
        for (int i = 0; i < size; i++) {
            neurons.add(new Neuron(config));
        }

        // Step 2: Randomly connect neurons (each neuron gets 2–4 friends and 1–2 enemies)
        for (Neuron neuron : neurons) {
            List<Neuron> others = new ArrayList<>(neurons);
            others.remove(neuron);
            Collections.shuffle(others);

            int friendCount = 2 + random.nextInt(3);  // 2–4 friends
            int enemyCount = 1 + random.nextInt(2);   // 1–2 enemies

            others.stream().limit(friendCount).forEach(neuron::addFriend);
            others.stream().skip(friendCount).limit(enemyCount).forEach(neuron::addEnemy);
        }

        return neurons;
    }
}

