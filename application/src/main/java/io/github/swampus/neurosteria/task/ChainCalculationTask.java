package io.github.swampus.neurosteria.task;

import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Multi-step chain task:
 *  Step 0: Neuron 0 receives 2
 *  Step 1: Neuron 1 receives 3
 *  Step 2: Neuron 2 should output 5 = (2 + 3)
 *  Step 3: Neuron 3 receives 4
 *  Step 4: Neuron 4 should output 20 = (5 * 4)
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

        if (network.isObsessed()) {
            log.warn("🤖 Network is obsessed. It believes it solved the task.");
            return;
        }

        var neurons = network.getNeurons();
        if (neurons.size() < 5) return;

        // 5
        double expectedIntermediate = inputA + inputB;
        if (step == 2) {
            var output = neurons.get(2).getActivation();
            if (Math.abs(output - expectedIntermediate) < 1.0) {
                log.info("✅ Step 2 passed: {} ≈ {}", output, expectedIntermediate);
            } else {
                log.warn("❌ Step 2 failed: {} ≠ {}", output, expectedIntermediate);
            }
        }

        if (step == 5) {
            var output = neurons.get(4).getActivation();
            // 20
            double expectedFinal = expectedIntermediate * inputC;
            if (Math.abs(output - expectedFinal) < 2.0) {
                log.info("✅ ChainCalculationTask solved! Final output: {}", output);
                solved = true;
            } else {
                log.warn("❌ Final output incorrect: {} ≠ {}", output, expectedFinal);
            }
        }
    }

    @Override
    public boolean isSolved() {
        return solved;
    }
}
