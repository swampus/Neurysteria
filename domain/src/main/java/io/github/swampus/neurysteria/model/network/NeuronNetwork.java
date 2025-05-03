package io.github.swampus.neurysteria.model.network;

import io.github.swampus.neurysteria.config.NeuronConfig;
import io.github.swampus.neurysteria.model.EmotionState;
import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.behavior.*;
import io.github.swampus.neurysteria.service.NeuronMutationService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Getter
public class NeuronNetwork {

    private static final Logger log = LoggerFactory.getLogger(NeuronNetwork.class);

    private final List<Neuron> neurons;
    private EmotionState currentState = EmotionState.CALM;

    private final double angryThresholdRatio = 0.2;
    private final double hystericalThresholdRatio = 0.6;

    private int obsessionTicksRemaining = 0;

    private final NeuronMutationService mutationService;


    public NeuronNetwork(List<Neuron> neurons, NeuronMutationService mutationService) {
        this.neurons = neurons;
        this.mutationService = mutationService;
    }

    public void tick() {
        if (neurons.isEmpty()) return;

        NeuronConfig config = neurons.get(0).getConfig();

        for (Neuron neuron : neurons) {
            neuron.calmDown();
            neuron.interact();
        }

        evaluateNetworkState(config);

        performStateBasedBehavior();


        if (isObsessed()) {
            obsessionTicksRemaining--;
            if (obsessionTicksRemaining <= 0) {
                currentState = EmotionState.CALM;
                log.info("""
                ☀️ Crusade alignment complete.
                ➤ Network restored to functional sanity.
                ➤ Heretical thought patterns purged.
            """);
            }
        }
    }

    private void evaluateNetworkState(NeuronConfig config) {
        long angryCount = neurons.stream()
                .filter(n -> n.getRage() > config.rageThreshold())
                .count();

        double ratio = (double) angryCount / neurons.size();

        if (ratio > hystericalThresholdRatio) {
            currentState = EmotionState.HYSTERICAL;
        } else if (ratio > angryThresholdRatio) {
            currentState = EmotionState.ANGRY;
        } else {
            currentState = EmotionState.CALM;
        }


        double averageRage = getAverageRage();

        if (neurons.size() % 9 == 0 && averageRage > config.rageThresholdForObsession()) {
            currentState = EmotionState.OBSESSED;
            obsessionTicksRemaining = neurons.get(0).getConfig().holyCyclesOfCrusadeAlignment();

            log.warn("""
            ⚠️⚠️⚠️
            🔱 SANCTUS COGITATIO CRUSADAE 🔱
            Initiating holy crusade alignment.
            ➤ Alignment cycles: {}
            ➤ Thought purity override active.
            ➤ Praise be to the Divine Clockwork.
            """, obsessionTicksRemaining);

        }
    }

    private final Map<EmotionState, NetworkBehaviorStrategy> behaviorMap = Map.of(
            EmotionState.CALM, new CalmBehavior(),
            EmotionState.ANGRY, new AngryBehavior(),
            EmotionState.HYSTERICAL, new HystericalBehavior(),
            EmotionState.OBSESSED, new ObsessedBehavior()
    );

    private void performStateBasedBehavior() {
        switch (currentState) {
            case HYSTERICAL -> {
                for (Neuron neuron : neurons) {
                    mutationService.attemptMutation(neuron);
                    //log.debug("🧬 Attempting mutation on neuron {}", neuron.getId());
                }
            }
            case CALM, ANGRY -> {
                // пока ничего
            }
            case OBSESSED -> {
                // можно добавить рандомные вспышки
            }
        }
    }

    public boolean isObsessed() {
        return currentState == EmotionState.OBSESSED;
    }

    public double getAverageRage() {
        return neurons.stream()
                .mapToDouble(Neuron::getRage)
                .average()
                .orElse(0.0);
    }

    public void punishUnsolvedNeurons(double rageDelta) {
        for (Neuron neuron : neurons) {
            if (!neuron.isAngry()) {
                neuron.setRage(neuron.getRage() + rageDelta);
            }
        }
    }

    public void calmAllNeurons(double calmAmount) {
        for (var neuron : neurons) {
            neuron.setRage(Math.max(0, neuron.getRage() - calmAmount));
        }
    }
}
