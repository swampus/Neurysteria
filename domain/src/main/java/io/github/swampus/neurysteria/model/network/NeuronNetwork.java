package io.github.swampus.neurysteria.model.network;

import io.github.swampus.neurysteria.config.NeuronConfig;
import io.github.swampus.neurysteria.model.EmotionState;
import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.service.NeuronMutationService;
import lombok.Getter;

import java.util.List;

@Getter
public class NeuronNetwork {

    private final List<Neuron> neurons;
    private EmotionState currentState = EmotionState.CALM;

    private final double angryThresholdRatio = 0.2;
    private final double hystericalThresholdRatio = 0.6;

    private final NeuronMutationService mutationService;


    public NeuronNetwork(List<Neuron> neurons, NeuronMutationService mutationService) {
        this.neurons = neurons;
        this.mutationService = mutationService;
    }

    public void tick() {
        if (neurons.isEmpty()) return;

        NeuronConfig config = neurons.get(0).getConfig();

        for (Neuron neuron : neurons) {
            neuron.calmDown();
            neuron.interact();
        }

        evaluateNetworkState(config);

        if (currentState == EmotionState.HYSTERICAL) {
            for (Neuron neuron : neurons) {
                mutationService.attemptMutation(neuron);
            }
        }

    }

    private void evaluateNetworkState(NeuronConfig config) {
        long angryCount = neurons.stream()
                .filter(n -> n.getRage() > config.rageThreshold())
                .count();

        double ratio = (double) angryCount / neurons.size();

        if (ratio > hystericalThresholdRatio) {
            currentState = EmotionState.HYSTERICAL;
        } else if (ratio > angryThresholdRatio) {
            currentState = EmotionState.ANGRY;
        } else {
            currentState = EmotionState.CALM;
        }
    }
}
