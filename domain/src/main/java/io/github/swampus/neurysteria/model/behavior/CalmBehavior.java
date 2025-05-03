package io.github.swampus.neurysteria.model.behavior;

import io.github.swampus.neurysteria.model.network.NeuronNetwork;

public class CalmBehavior implements NetworkBehaviorStrategy {

    @Override
    public void apply(NeuronNetwork network) {
        network.getNeurons().forEach(n -> n.shareEnergyWithFriends());
    }
}
