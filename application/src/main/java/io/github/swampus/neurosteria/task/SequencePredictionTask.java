package io.github.swampus.neurosteria.task;

import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequencePredictionTask implements Task {

    private static final Logger log = LoggerFactory.getLogger(SequencePredictionTask.class);
    private final int[] inputSequence = {1, 2, 3};
    private boolean solved = false;

    @Override
    public void injectInputs(NeuronNetwork network) {
        // Простейшее: добавим стимул нейронам с ID 0, 1, 2
        var neurons = network.getNeurons();
        for (int i = 0; i < inputSequence.length && i < neurons.size(); i++) {
            neurons.get(i).receiveInput(inputSequence[i]);
        }
    }

    @Override
    public void evaluate(NeuronNetwork network) {
        if (network.getNeurons().size() > 3) {
            var candidate = network.getNeurons().get(3);
            int expectedNext = 4;
            if (Math.abs(candidate.getActivation() - expectedNext) < 0.5) {
                solved = true;
                log.info("✅ SequencePredictionTask: solved with output {}",
                        candidate.getActivation());
            } else {
                log.warn("❌ SequencePredictionTask: incorrect output {}",
                        candidate.getActivation());
            }
        }
    }

    @Override
    public boolean isSolved() {
        return solved;
    }
}