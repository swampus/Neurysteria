package io.github.swampus.neurosteria.task;

import io.github.swampus.neurysteria.model.network.NeuronNetwork;

public interface Task {
    void injectInputs(NeuronNetwork network);

    void evaluate(NeuronNetwork network);

    boolean isSolved();
}
