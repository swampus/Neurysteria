package io.github.swampus.neurosteria.adaptation;

import io.github.swampus.neurosteria.task.Task;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;

public interface AdaptationEngine {
    void adapt(NeuronNetwork network, Task task);
}
