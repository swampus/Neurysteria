package io.github.swampus.neurysteria;

import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TraceLog {

    private static final Logger log = LoggerFactory.getLogger(TraceLog.class);

    public static void dumpNetworkState(int tick, NeuronNetwork network) {
        var neurons = network.getNeurons();

        long angry = neurons.stream().filter(Neuron::isAngry).count();
        double avgRage = neurons.stream().mapToDouble(Neuron::getRage).average().orElse(0.0);
        double maxActivation = neurons.stream().mapToDouble(Neuron::getActivation).max().orElse(0.0);

        StringBuilder sb = new StringBuilder();
        sb.append("\n--- 📊 TRACE TICK ").append(tick).append(" ---\n");
        sb.append("Total neurons: ").append(neurons.size()).append("\n");
        sb.append("Angry neurons: ").append(angry).append("\n");
        sb.append("Avg rage: ").append(String.format("%.2f", avgRage)).append("\n");
        sb.append("Max activation: ").append(String.format("%.2f", maxActivation)).append("\n");

        if (neurons.size() > 3) {
            Neuron n3 = neurons.get(3);
            sb.append("Neuron #3 activation: ").append(String.format("%.2f", n3.getActivation())).append("\n");
            sb.append("Neuron #3 rage: ").append(String.format("%.2f", n3.getRage())).append("\n");
            sb.append("Neuron #3 friends: ").append(n3.getFriends().size()).append("\n");
            sb.append("Neuron #3 enemies: ").append(n3.getEnemies().size()).append("\n");
        }

        sb.append("------------------------------");
        log.warn(sb.toString());
    }
}
