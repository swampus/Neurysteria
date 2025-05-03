package io.github.swampus.neurysteria.model.behavior;

import io.github.swampus.neurysteria.model.network.NeuronNetwork;

public class HystericalBehavior implements NetworkBehaviorStrategy {

    @Override
    public void apply(NeuronNetwork network) {
        var mutationService = network.getMutationService();
        network.getNeurons().forEach(n -> {
            mutationService.attemptMutation(n);
            n.clearConnectionsRandomly(); // ты можешь сделать метод с шансом
        });
    }
}
