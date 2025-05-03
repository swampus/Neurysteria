package io.github.swampus.neurysteria.model.behavior;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.activation.ActivationFunctions;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Random;

public class CalmBehavior implements NetworkBehaviorStrategy {

    private static final Logger log = LoggerFactory.getLogger(CalmBehavior.class);

    private static final Random random = new Random();

    @Override
    public void apply(NeuronNetwork network) {
        var neurons = network.getNeurons();
        if (neurons.isEmpty()) return;

        for (Neuron neuron : neurons) {
            // 🔗 Передача энергии друзьям
            if (neuron.getActivation() > 4.0) {
                for (Neuron friend : neuron.getFriends()) {
                    double impulse = neuron.getActivation() * 0.25;
                    friend.stimulate(impulse);
                    log.debug("🔗 Neuron {} shared energy {} to {}", neuron.getId(), impulse, friend.getId());
                }
            }
        }

        // 🌱 Восстановление нейрона
        if (neurons.size() < 50 && Math.random() < 0.05) {
            var template = neurons.get(0);
            Neuron newborn = new Neuron(template.getConfig());

            neurons.stream()
                    .filter(n -> n.getActivation() > 1.0)
                    .limit(3)
                    .forEach(peer -> {
                        newborn.addFriend(peer);
                        peer.addFriend(newborn);
                    });

            network.getNeurons().add(newborn);
            log.info("🌱 Calm network restored one neuron: {}", newborn.getId());
        }
        for (Neuron neuron : neurons) {
            if (neuron.getActivation() > 80) {
                int index = neurons.indexOf(neuron);
                if (index + 1 < neurons.size()) {
                    Neuron next = neurons.get(index + 1);
                    next.stimulate(neuron.getActivation() * 0.5);
                    log.warn("➡️ Neuron {} forwarded {} to {}", neuron.getId(), neuron.getActivation() * 0.5, next.getId());
                }
            }
        }
    }

}
