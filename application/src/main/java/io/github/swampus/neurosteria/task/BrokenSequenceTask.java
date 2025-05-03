package io.github.swampus.neurosteria.task;

import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task: Predict the next number in an incomplete sequence.
 *
 * Given a partially hidden sequence like [?, 2, ?, 4], the network
 * is expected to guess the next value (e.g., 6), assuming a +2 pattern.
 *
 * Only the known values (2 and 4) are injected into the network as input.
 * This task is used to test the network's ability to extrapolate or guess
 * under incomplete information — especially in chaotic or "obsessed" states.
 */
public class BrokenSequenceTask implements Task {

    private static final Logger log = LoggerFactory.getLogger(BrokenSequenceTask.class);
    private final int[] partialSequence = {0, 2, 0, 4}; // only inject 2 and 4
    private boolean solved = false;

    @Override
    public void injectInputs(NeuronNetwork network) {
        var neurons = network.getNeurons();
        for (int i = 0; i < partialSequence.length && i < neurons.size(); i++) {
            if (partialSequence[i] != 0) {
                neurons.get(i).receiveInput(partialSequence[i]);
            }
        }
    }

    @Override
    public void evaluate(NeuronNetwork network) {
        if (network.getNeurons().size() > 4) {
            var candidate = network.getNeurons().get(4);
            double output = candidate.getActivation();
            int expectedNext = 6;
            if (Math.abs(output - expectedNext) < 1.0) {
                solved = true;
                log.info("✅ BrokenSequenceTask: solved with output {}", output);
            } else {
                log.warn("❌ BrokenSequenceTask: wrong output {}, expected {}", output, expectedNext);
            }
        }
    }

    @Override
    public boolean isSolved() {
        return solved;
    }
}
