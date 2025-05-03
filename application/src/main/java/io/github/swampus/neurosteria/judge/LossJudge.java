package io.github.swampus.neurosteria.judge;


import io.github.swampus.neurosteria.task.Task;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;

public interface LossJudge {
    void evaluateAndReact(NeuronNetwork network, Task task);
}
