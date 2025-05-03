package io.github.swampus.neurysteria.model.behavior;

import io.github.swampus.neurysteria.model.network.NeuronNetwork;

public interface NetworkBehaviorStrategy {
    void apply(NeuronNetwork network);
}
