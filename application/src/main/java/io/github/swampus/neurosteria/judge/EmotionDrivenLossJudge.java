package io.github.swampus.neurosteria.judge;

import io.github.swampus.neurosteria.task.Task;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmotionDrivenLossJudge implements LossJudge {

    private static final Logger log = LoggerFactory.getLogger(EmotionDrivenLossJudge.class);

    private final double ragePerFailure;
    private final double calmPerSuccess;

    public EmotionDrivenLossJudge(double ragePerFailure, double calmPerSuccess) {
        this.ragePerFailure = ragePerFailure;
        this.calmPerSuccess = calmPerSuccess;
    }

    @Override
    public void evaluateAndReact(NeuronNetwork network, Task task) {
        if (task.isSolved()) {
            network.calmAllNeurons(calmPerSuccess);
            log.debug("✅ Task succeeded → neurons calmed by {}", calmPerSuccess);
        } else {
            network.punishUnsolvedNeurons(ragePerFailure);
            log.debug("❌ Task failed → neurons punished with rage {}", ragePerFailure);
        }
    }
}
