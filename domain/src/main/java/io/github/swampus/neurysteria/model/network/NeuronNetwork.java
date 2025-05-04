package io.github.swampus.neurysteria.model.network;

import io.github.swampus.neurysteria.config.NeuronConfig;
import io.github.swampus.neurysteria.model.EmotionState;
import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.behavior.*;
import io.github.swampus.neurysteria.service.NeuronMutationService;
import io.github.swampus.neurysteria.service.NeuronTerminationService;
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
    private final NeuronTerminationService neuronTerminationService;
    private final Map<EmotionState, NetworkBehaviorStrategy> behaviorMap;

    private int hystericalTicks = 0;
    private final int maxHystericalTicks = 20;

    private EmotionState lastEvaluatedState = EmotionState.CALM;
    private int stableTicks = 0;
    private final int requiredStableTicks = 10;

    public NeuronNetwork(List<Neuron> neurons, NeuronMutationService mutationService,
                         NeuronTerminationService neuronTerminationService) {
        this.neurons = neurons;
        this.mutationService = mutationService;
        this.neuronTerminationService = neuronTerminationService;
        behaviorMap = Map.of(
                EmotionState.CALM, new CalmBehavior(),
                EmotionState.ANGRY, new AngryBehavior(neuronTerminationService),
                EmotionState.HYSTERICAL, new HystericalBehavior(mutationService, neuronTerminationService),
                EmotionState.OBSESSED, new ObsessedBehavior()
        );
    }

    public void tick() {
        if (neurons.isEmpty()) {
            return;
        }

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
                updateState(EmotionState.CALM);
                log.error("""
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
        double averageRage = getAverageRage();

        EmotionState evaluated;
        if (averageRage > 20 || ratio > 0.5) {
            evaluated = EmotionState.HYSTERICAL;
        } else if (averageRage > 10 || ratio > 0.2) {
            evaluated = EmotionState.ANGRY;
        } else {
            evaluated = EmotionState.CALM;
        }

        if (neurons.size() % 66 == 0 &&
                averageRage > config.rageThresholdForObsession()) {
            currentState = EmotionState.OBSESSED;
            obsessionTicksRemaining = config.holyCyclesOfCrusadeAlignment();
            log.error("""
            ⚠️⚠️⚠️
            🔱 SANCTUS COGITATIO CRUSADAE 🔱
            ➤ Crusade cycles: {}
            ➤ Thought purity override active.
            """, obsessionTicksRemaining);
            return;
        }

        if (evaluated == lastEvaluatedState) {
            stableTicks++;
            if (stableTicks >= requiredStableTicks && evaluated != currentState) {
                log.info("🧠 Network state changed: {} → {}", currentState, evaluated);
                currentState = evaluated;
            }
        } else {
            stableTicks = 1;
            lastEvaluatedState = evaluated;
        }

        if (currentState == EmotionState.HYSTERICAL) {
            hystericalTicks++;
            if (hystericalTicks >= maxHystericalTicks) {
                updateState(EmotionState.CALM);
                hystericalTicks = 0;
                log.error("Hysteria exhausted. Network forcefully returned to sanity.");
            }
        } else {
            hystericalTicks = 0;
        }
    }

    private void performStateBasedBehavior() {
        NetworkBehaviorStrategy strategy = behaviorMap.get(currentState);
        if (strategy != null) {
            strategy.apply(this);
        } else {
            log.warn("⚠️ No behavior strategy defined for state: {}", currentState);
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

    private void updateState(EmotionState newState) {
        if (this.currentState != newState) {
            log.error(" Network state changed: {} → {}", this.currentState, newState);
            this.currentState = newState;
        }
    }
}