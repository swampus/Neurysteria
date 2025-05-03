package io.github.swampus.neurosteria.task;

import io.github.swampus.neurysteria.model.network.NeuronNetwork;

import java.util.List;

public interface Task {
    void injectInputs(NeuronNetwork network);

    void evaluate(NeuronNetwork network);

    boolean isSolved();

    default List<Integer[]> getSuggestedConnections() {
        return List.of(
                new Integer[]{0, 2},
                new Integer[]{1, 2},
                new Integer[]{2, 4},
                new Integer[]{3, 4}
        );
    }
}
