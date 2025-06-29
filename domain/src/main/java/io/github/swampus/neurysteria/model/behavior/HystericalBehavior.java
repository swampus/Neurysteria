package io.github.swampus.neurysteria.model.behavior;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.activation.ActivationFunctions;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import io.github.swampus.neurysteria.service.NeuronMutationService;
import io.github.swampus.neurysteria.service.NeuronTerminationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Random;

public class HystericalBehavior implements NetworkBehaviorStrategy {

    private static final Logger log = LoggerFactory.getLogger(HystericalBehavior.class);
    private final NeuronMutationService mutationService;
    private final NeuronTerminationService terminationService;

    public HystericalBehavior(NeuronMutationService mutationService, NeuronTerminationService terminationService) {
        this.mutationService = mutationService;
        this.terminationService = terminationService;
    }

    @Override
    public void apply(NeuronNetwork network) {
        var neurons = new ArrayList<>(network.getNeurons());

        for (Neuron neuron : neurons) {
            mutationService.attemptMutation(neuron);
            if (neuron.getRage() > 100) {
                if (Math.random() < 0.005) {
                    terminationService.removeWithoutReplacement(network, neuron);
                }
            }
            for (int i = 0; i < 3; i++) {
                Neuron target = neurons.get(new Random().nextInt(neurons.size()));
                double impulse = neuron.getActivation() * 0.3;
                target.stimulate(impulse);
                log.debug("📣 Neuron {} screamed {} at {}", neuron.getId(), impulse, target.getId());


                if (target.getActivation() > 80 && Math.random() < 0.1) {
                    target.resetRage();
                    target.setActivationFunction(ActivationFunctions.SIGMOID);
                    log.warn("💡 Neuron {} experienced a neural revelation!", target.getId());
                }
            }

            if (Math.random() < 0.05) {
                neuron.resetRage();
                log.info("💫 Neuron {} suddenly calmed down in hysteria", neuron.getId());
            }

            if (neuron.getActivation() > 90 && neuron.getRage() > 20 && Math.random() < 0.1) {
                Neuron newborn = new Neuron(neuron.getConfig());
                newborn.setActivationFunction(neuron.getActivationFunction());
                newborn.setRage(5.0);
                network.getNeurons().add(newborn);

                for (int i = 0; i < 5; i++) {
                    Neuron peer = neurons.get(new Random().nextInt(neurons.size()));
                    newborn.addFriend(peer);
                    peer.addFriend(newborn);
                }

                log.info("🌀 Neural prophet {} gave birth to disciple {}", neuron.getId(), newborn.getId());
            }

            if (Math.random() < 0.9) {
                neuron.attackEnemies();
            }

            if (Math.random() < 0.3) {
                neuron.clearConnectionsRandomly();
            }
        }
    }
}
