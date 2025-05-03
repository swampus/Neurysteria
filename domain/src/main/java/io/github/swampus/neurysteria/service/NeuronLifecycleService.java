package io.github.swampus.neurysteria.service;

import io.github.swampus.neurysteria.config.BirthProfileConfig;
import io.github.swampus.neurysteria.config.BirthProfileRegistry;
import io.github.swampus.neurysteria.model.EmotionState;
import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.activation.ActivationFunctions;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;

import java.util.List;
import java.util.Random;

public class NeuronLifecycleService {

    private final BirthProfileRegistry birthRegistry;
    private final Random random = new Random();

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

        List<Neuron> others = network.getNeurons();
        others.stream()
                .filter(n -> n != oldNeuron)
                .limit(3 + random.nextInt(3))
                .forEach(newNeuron::addFriend);

        return newNeuron;
    }

    private double rand(double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

}
