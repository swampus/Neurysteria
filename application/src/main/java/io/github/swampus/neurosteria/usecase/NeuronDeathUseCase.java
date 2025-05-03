package io.github.swampus.neurosteria.usecase;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import io.github.swampus.neurysteria.service.NeuronLifecycleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeuronDeathUseCase {

    private static final Logger log = LoggerFactory.getLogger(NeuronDeathUseCase.class);

    private final NeuronLifecycleService lifecycle;

    public NeuronDeathUseCase(NeuronLifecycleService lifecycle) {
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
            log.info("💀 Neuron {} died. Replaced by {}", target.getId(), newborn.getId());
        }
    }
}
