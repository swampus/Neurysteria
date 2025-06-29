package io.github.swampus.neurosteria.task;

import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Multi-step chain task:
 * Step 0: Neuron 0 receives 2
 * Step 1: Neuron 1 receives 3
 * Step 2: Neuron 2 should output 5 = (2 + 3)
 * Step 3: Neuron 3 receives 4
 * Step 4: Neuron 4 should output 20 = (5 * 4)
 */
public class ChainCalculationTask implements Task {

    private static final Logger log = LoggerFactory.getLogger(ChainCalculationTask.class);

    private final double inputA = 2.0;
    private final double inputB = 3.0;
    private final double inputC = 4.0;

    private int step = 0;
    private boolean solved = false;

    @Override
    public void injectInputs(NeuronNetwork network) {
        var neurons = network.getNeurons();
        if (neurons.size() < 5) return;

        switch (step) {
            case 0 -> {
                neurons.get(0).receiveInput(inputA);
                log.debug("Injected input {} into Neuron 0", inputA);
            }
            case 1 -> {
                neurons.get(1).receiveInput(inputB);
                log.debug("Injected input {} into Neuron 1", inputB);
            }
            case 3 -> {
                neurons.get(3).receiveInput(inputC);
                log.debug("Injected input {} into Neuron 3", inputC);
            }
        }
        step++;
    }

    @Override
    public void evaluate(NeuronNetwork network) {
        var neurons = network.getNeurons();
        if (neurons.size() < 5) return;

        double expectedIntermediate = inputA + inputB;
        double expectedFinal = expectedIntermediate * inputC;

        // Шаг 2 — проверка суммы
        if (step == 2) {
            var out = neurons.get(2);
            double value = out.getActivation();
            if (Math.abs(value - expectedIntermediate) < 1.0) {
                log.info("✅ Step 2 passed: {} ≈ {}", value, expectedIntermediate);
                out.addFriend(neurons.get(0));
                out.addFriend(neurons.get(1));
                neurons.get(0).addFriend(out);
                neurons.get(1).addFriend(out);
            } else {
                log.warn("❌ Step 2 failed: {} ≠ {}", value, expectedIntermediate);
            }
        }

        var out = neurons.get(4);
        double value = out.getActivation();
        if (Math.abs(value - expectedFinal) < 2.0) {
            log.info("✅ FINAL solved! {} ≈ {}", value, expectedFinal);
            out.addFriend(neurons.get(2));
            out.addFriend(neurons.get(3));
            neurons.get(2).addFriend(out);
            neurons.get(3).addFriend(out);
            solved = true;
        } else {
            log.warn("❌ Final output incorrect: {} ≠ {}", value, expectedFinal);
        }
    }

    @Override
    public boolean isSolved() {
        return solved;
    }
}
