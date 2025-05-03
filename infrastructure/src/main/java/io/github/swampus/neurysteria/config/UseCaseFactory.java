package io.github.swampus.neurysteria.config;

import io.github.swampus.neurosteria.usecase.*;
import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import io.github.swampus.neurysteria.service.NeuronLifecycleService;
import io.github.swampus.neurysteria.service.NeuronMutationService;

import java.util.List;


public class UseCaseFactory {

    private final BirthProfileRegistry birthRegistry;
    private final NeuronLifecycleService lifecycle;
    private final NeuronMutationService mutationService;

    public UseCaseFactory() {
        this.birthRegistry = BirthProfileLoader.loadAllProfiles();
        this.lifecycle = new NeuronLifecycleService(birthRegistry);
        this.mutationService = new NeuronMutationService();
    }

    public NeuronReproductionUseCase createNeuronReproductionUseCase() {
        return new NeuronReproductionUseCase(lifecycle);
    }

    public NeuronDeathUseCase createNeuronDeathUseCase() {
        return new NeuronDeathUseCase(lifecycle);
    }

    public CreateNeuronNetworkUseCase createCreateNetworkUseCase() {
        return new CreateNeuronNetworkUseCase();
    }

    public TickNetworkUseCase createTickNetworkUseCase(NeuronNetwork network) {
        return new TickNetworkUseCase(network);
    }

    public NeuronNetwork createNetworkWithMutationSupport(List<Neuron> neurons) {
        return new NeuronNetwork(neurons, mutationService);
    }
}
