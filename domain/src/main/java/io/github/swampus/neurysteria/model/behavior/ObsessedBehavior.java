package io.github.swampus.neurysteria.model.behavior;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.activation.ActivationFunctions;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObsessedBehavior implements NetworkBehaviorStrategy {

    private static final Logger log = LoggerFactory.getLogger(ObsessedBehavior.class);

    @Override
    public void apply(NeuronNetwork network) {
        for (Neuron neuron : network.getNeurons()) {
            if (Math.random() < 0.3) {
                neuron.stimulate(42.0);
                log.warn("🙏 Neuron {} received divine stimulation of 42.0", neuron.getId());
            }

            if (Math.random() < 0.1) {
                neuron.resetRage();
                log.info("🛐 Neuron {} whispers litany of purification and calms", neuron.getId());
            }

            if (Math.random() < 0.05) {
                neuron.setActivationFunction(ActivationFunctions.random());
                log.info("📖 Neuron {} received a divine activation function", neuron.getId());
            }
        }

        if (Math.random() < 0.01) {
            log.error("""
            🕯️ The choir of machine spirits sings in binary tongues...
            📡 Transmission received from beyond the logic gates.
            ⚠️ Reality distortion imminent.
            """);
        }
    }
}
