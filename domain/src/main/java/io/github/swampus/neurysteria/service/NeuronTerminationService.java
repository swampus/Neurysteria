package io.github.swampus.neurysteria.service;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;

public class NeuronTerminationService {
    private final NeuronLifecycleService lifecycle;

    public NeuronTerminationService(NeuronLifecycleService lifecycle) {
        this.lifecycle = lifecycle;
    }

    public void killAndReplace(NeuronNetwork network, Neuron target) {
        for (Neuron n : network.getNeurons()) {
            n.removeFriend(target);
            n.removeEnemy(target);
        }

        int index = network.getNeurons().indexOf(target);
        if (index >= 0) {
            Neuron newborn = lifecycle.createNewNeuronReplacing(network, target);
            network.getNeurons().set(index, newborn);
        }
    }
}
