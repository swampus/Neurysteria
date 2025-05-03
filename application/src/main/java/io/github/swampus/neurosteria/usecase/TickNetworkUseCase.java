package io.github.swampus.neurosteria.usecase;

import io.github.swampus.neurysteria.model.EmotionState;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;

public class TickNetworkUseCase {

    private final NeuronNetwork neuronNetwork;

    public TickNetworkUseCase(NeuronNetwork neuronNetwork) {
        this.neuronNetwork = neuronNetwork;
    }

    public EmotionState executeTick() {
        neuronNetwork.tick();
        return neuronNetwork.getCurrentState();
    }
}
