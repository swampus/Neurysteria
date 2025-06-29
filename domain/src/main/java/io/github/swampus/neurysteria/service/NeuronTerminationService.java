package io.github.swampus.neurysteria.service;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeuronTerminationService {

    private static final Logger log = LoggerFactory.getLogger(NeuronTerminationService.class);

    private final NeuronLifecycleService lifecycle;

    public NeuronTerminationService(NeuronLifecycleService lifecycle) {
        this.lifecycle = lifecycle;
    }

    public void removeAndReplace(NeuronNetwork network, Neuron target) {
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

    public void removeWithoutReplacement(NeuronNetwork network, Neuron target) {
        for (Neuron n : network.getNeurons()) {
            n.removeFriend(target);
            n.removeEnemy(target);
        }
        network.getNeurons().remove(target);
        log.warn("💀 Neuron {} has been executed without replacement", target.getId());
    }
}
