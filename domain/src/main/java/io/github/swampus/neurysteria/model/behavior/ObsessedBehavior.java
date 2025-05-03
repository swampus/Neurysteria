package io.github.swampus.neurysteria.model.behavior;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class ObsessedBehavior implements NetworkBehaviorStrategy {

    private static final Logger log = LoggerFactory.getLogger(ObsessedBehavior.class);

    private final Random random = new Random();

    @Override
    public void apply(NeuronNetwork network) {
        for (Neuron neuron : network.getNeurons()) {
            if (random.nextDouble() < 0.3) {
                log.info("🔱 Praise be to the Omnissiah. The task is fulfilled.");
                neuron.stimulate(random.nextDouble() * 10);
            }
        }

        log.info("🧠 The Machine Spirit guides us. No errors exist.");
    }
}
