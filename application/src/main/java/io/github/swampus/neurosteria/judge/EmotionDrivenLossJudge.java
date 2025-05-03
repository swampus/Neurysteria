package io.github.swampus.neurosteria.judge;

import io.github.swampus.neurosteria.task.Task;
import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmotionDrivenLossJudge implements LossJudge {

    private static final Logger log = LoggerFactory.getLogger(EmotionDrivenLossJudge.class);

    private final double ragePerFailure;
    private final double calmPerSuccess;
    private int successStreak = 0;
    private int failureStreak = 0;

    public EmotionDrivenLossJudge(double ragePerFailure, double calmPerSuccess) {
        this.ragePerFailure = ragePerFailure;
        this.calmPerSuccess = calmPerSuccess;
    }

    @Override
    public void evaluateAndReact(NeuronNetwork network, Task task) {
        if (task.isSolved()) {
            successStreak++;
            failureStreak = 0;

            if (successStreak >= 2) {
                successStreak = 0;
                for (Neuron n : network.getNeurons()) {
                    n.setRage(n.getRage() * 0.25);
                }
                log.warn("""
                ✨ Divine intervention granted.
                🕊 The Omnissiah is pleased.
                💫 Network rage has been humbled by 75%%.
                """);
            } else {
                network.calmAllNeurons(calmPerSuccess);
                log.debug("✅ Task succeeded → neurons calmed by {}", calmPerSuccess);
            }
        } else {
            successStreak = 0;
            failureStreak++;

            network.punishUnsolvedNeurons(ragePerFailure);
            log.debug("❌ Task failed → neurons punished with rage {}", ragePerFailure);

            if (failureStreak >= 10) {
                failureStreak = 0;
                for (Neuron n : network.getNeurons()) {
                    n.setRage(n.getRage() + 10.0);
                }
                log.error("""
                ⚡ The Machine God is displeased.
                🔥 Holy rage bestowed upon the unworthy network.
                ⚠️ Prepare for neural reckoning.
                """);
            }
        }
    }
}
