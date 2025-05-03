package io.github.swampus.neurosteria.usecase;

import io.github.swampus.neurysteria.model.EmotionState;
import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import io.github.swampus.neurysteria.service.NeuronTerminationService;

import java.util.ArrayList;

public class TickNetworkUseCase {

    private final NeuronNetwork neuronNetwork;
    private final NeuronTerminationService neuronTerminationService;
    public TickNetworkUseCase(NeuronNetwork neuronNetwork, NeuronTerminationService neuronTerminationService) {
        this.neuronNetwork = neuronNetwork;
        this.neuronTerminationService = neuronTerminationService;
    }

    public EmotionState executeTick() {
        neuronNetwork.tick();

        if (neuronNetwork.getCurrentState() == EmotionState.HYSTERICAL) {
            for (Neuron neuron : new ArrayList<>(neuronNetwork.getNeurons())) {
                if (neuron.getRage() > neuron.getConfig().rageThreshold()) {
                    double chance = neuron.getConfig().deathChanceFromRage();
                    if (Math.random() < chance) {
                        neuronTerminationService.killAndReplace(neuronNetwork, neuron);
                    }
                }
            }
        }

        return neuronNetwork.getCurrentState();
    }
}
