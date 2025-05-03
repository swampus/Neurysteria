package io.github.swampus.neurysteria.model.behavior;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import io.github.swampus.neurysteria.service.NeuronMutationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class HystericalBehavior implements NetworkBehaviorStrategy {

    private static final Logger log = LoggerFactory.getLogger(HystericalBehavior.class);
    private final NeuronMutationService mutationService;

    public HystericalBehavior(NeuronMutationService mutationService) {
        this.mutationService = mutationService;
    }

    @Override
    public void apply(NeuronNetwork network) {
        for (Neuron neuron : new ArrayList<>(network.getNeurons())) {
            mutationService.attemptMutation(neuron);

            if (Math.random() < 0.05) {
                neuron.resetRage();
                log.info("💫 Neuron {} suddenly calmed down in hysteria", neuron.getId());
            }

            if (Math.random() < 0.05) {
                neuron.stimulate(Math.random() * 5);
                log.info("⚡ Neuron {} experienced random surge", neuron.getId());
            }
        }
    }
}
