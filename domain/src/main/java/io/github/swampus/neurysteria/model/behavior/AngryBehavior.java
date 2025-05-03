package io.github.swampus.neurysteria.model.behavior;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import io.github.swampus.neurysteria.service.NeuronTerminationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AngryBehavior implements NetworkBehaviorStrategy {

    private static final Logger log = LoggerFactory.getLogger(AngryBehavior.class);

    private final NeuronTerminationService neuronTerminationService;

    public AngryBehavior(NeuronTerminationService neuronTerminationService) {
        this.neuronTerminationService = neuronTerminationService;
    }

    @Override
    public void apply(NeuronNetwork network) {
        List<Neuron> neurons = network.getNeurons();

        for (Neuron neuron : neurons) {
            if (neuron.getRage() > neuron.getConfig().rageThreshold() * 1.5 &&
                    neuron.getActivation() < 1.0) {

                neuronTerminationService.killAndReplace(network, neuron);
                log.warn("💢 Neuron {} was too angry and weak. Terminated.", neuron.getId());
                continue;
            }

            if (neuron.getFriends().size() < 2) {
                var candidates = neurons.stream()
                        .filter(n -> !n.equals(neuron))
                        .filter(n -> n.getActivation() > 1.0)
                        .limit(3)
                        .toList();

                for (Neuron peer : candidates) {
                    neuron.addFriend(peer);
                    peer.addFriend(neuron);
                }
            }

            if (neuron.getRage() > 5.0 && Math.random() < 0.1) {
                neuron.resetRage();
                log.info("😤 Neuron {} has let go of its rage", neuron.getId());
            }
        }
    }
}
