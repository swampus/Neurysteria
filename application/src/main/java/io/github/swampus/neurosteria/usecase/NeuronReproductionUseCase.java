package io.github.swampus.neurosteria.usecase;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;

import io.github.swampus.neurysteria.service.NeuronLifecycleService;

public class NeuronReproductionUseCase {

    private final NeuronLifecycleService lifecycle;

    public NeuronReproductionUseCase(NeuronLifecycleService lifecycle) {
        this.lifecycle = lifecycle;
    }

    public Neuron reproduce(NeuronNetwork network, Neuron placeholder) {
        return lifecycle.createNewNeuronReplacing(network, placeholder);
    }
}
