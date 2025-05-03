package io.github.swampus.neurysteria.config;

import io.github.swampus.neurosteria.usecase.*;
import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import io.github.swampus.neurysteria.service.NeuronLifecycleService;
import io.github.swampus.neurysteria.service.NeuronMutationService;
import io.github.swampus.neurysteria.service.NeuronTerminationService;

import java.util.List;

public class UseCaseFactory {

    private final BirthProfileRegistry birthRegistry;
    private final NeuronMutationService mutationService;
    private final NeuronLifecycleService lifecycleService;
    private final NeuronTerminationService neuronTerminationService;

    public UseCaseFactory() {
        this.birthRegistry = BirthProfileLoader.loadAllProfiles();
        this.mutationService = new NeuronMutationService();
        this.lifecycleService = new NeuronLifecycleService(birthRegistry);
        this.neuronTerminationService = new NeuronTerminationService(lifecycleService);
    }

    public CreateNeuronNetworkUseCase createCreateNetworkUseCase() {
        return new CreateNeuronNetworkUseCase();
    }

    public TickNetworkUseCase createTickNetworkUseCase(NeuronNetwork network) {
        return new TickNetworkUseCase(network, neuronTerminationService);
    }

    public NeuronNetwork createNetworkWithMutationSupport(List<Neuron> neurons) {
        return new NeuronNetwork(neurons, mutationService, neuronTerminationService);
    }

}