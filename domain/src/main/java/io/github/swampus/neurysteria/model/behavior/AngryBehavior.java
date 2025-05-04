package io.github.swampus.neurysteria.model.behavior;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import io.github.swampus.neurysteria.service.NeuronTerminationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
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

        Neuron best = neurons.stream()
                .max(Comparator.comparingDouble(Neuron::getActivation))
                .orElse(null);

        if (best == null || best.getActivation() < 1.0) return;

        for (Neuron neuron : List.copyOf(neurons)) {
            if (neuron.equals(best)) continue;

            var config = neuron.getConfig();

            if (neuron.getRage() > config.rageThreshold() * 2 && neuron.getActivation() < 1.0) {
                network.getNeurons().remove(neuron);
                for (Neuron other : neurons) {
                    other.removeFriend(neuron);
                    other.removeEnemy(neuron);
                }
                log.error("💀 Neuron {} was obliterated in rage", neuron.getId());
                continue;
            }

            if (neuron.getRage() > config.rageThreshold() * 1.5 && neuron.getActivation() < 1.0) {
                neuronTerminationService.killAndReplace(network, neuron);
                log.warn("💢 Neuron {} was too angry and weak. Terminated.", neuron.getId());
                continue;
            }

            if (!neuron.getFriends().contains(best)) {
                neuron.addFriend(best);
                best.addFriend(neuron);
                log.info("🧠 Neuron {} is now following {}", neuron.getId(), best.getId());
            }

            if (neuron.getActivation() > 4.5 && neuron.getActivation() < 5.5) {
                List<Neuron> helpers = neurons.stream()
                        .filter(n -> !n.equals(neuron))
                        .filter(n -> n.getActivation() > 1.0)
                        .limit(3)
                        .toList();

                for (Neuron friend : helpers) {
                    neuron.addFriend(friend);
                    friend.addFriend(neuron);
                    log.info("⚡ Reinforcing neuron {} with {}", neuron.getId(), friend.getId());
                }
            }

            if (neuron.getRage() > 5.0 && Math.random() < 0.1) {
                neuron.resetRage();
                log.info("😤 Neuron {} let go of its rage", neuron.getId());
            }

            if (neuron.getRage() > 20 && neuron.getActivation() > 50 && Math.random() < 0.05) {
                Neuron victim = neurons.stream()
                        .filter(n -> !n.equals(neuron))
                        .filter(n -> n.getActivation() < 1.0)
                        .min(Comparator.comparingDouble(Neuron::getActivation))
                        .orElse(null);

                if (victim != null) {
                    network.getNeurons().remove(victim);
                    for (Neuron other : neurons) {
                        other.removeFriend(victim);
                        other.removeEnemy(victim);
                    }
                    log.error("💥 Neuron {} was EXECUTED in rage by {}", victim.getId(), neuron.getId());
                }
            }
        }

        if (best.getActivation() > 90 && best.getRage() > 5.0 && neurons.size() < 70 && Math.random() < 0.2) {
            Neuron newborn = new Neuron(best.getConfig());
            newborn.setActivationFunction(best.getActivationFunction());
            newborn.setRage(5.0);
            network.getNeurons().add(newborn);
            newborn.addFriend(best);
            best.addFriend(newborn);
            log.warn("🔥 In anger, network spawned a new neuron from {}", best.getId());
        }

        if (Math.random() < 0.05) {
            for (Neuron neuron : neurons) {
                neuron.stimulate(Math.random() * 2 - 1);
            }
            log.info("⚡ Neural storm surged through the network.");
        }
    }
}
