package io.github.swampus.neurysteria;

import io.github.swampus.neurosteria.judge.EmotionDrivenLossJudge;
import io.github.swampus.neurosteria.task.Task;
import io.github.swampus.neurosteria.task.TaskFactory;
import io.github.swampus.neurosteria.task.TaskType;
import io.github.swampus.neurysteria.config.BirthProfileRegistry;
import io.github.swampus.neurysteria.config.NeuronConfig;
import io.github.swampus.neurysteria.config.UseCaseFactory;
import io.github.swampus.neurysteria.model.EmotionState;
import io.github.swampus.neurysteria.model.Neuron;
import io.github.swampus.neurysteria.model.activation.ActivationFunctions;
import io.github.swampus.neurysteria.model.network.NeuronNetwork;
import io.github.swampus.neurysteria.service.NeuronLifecycleService;
import io.github.swampus.neurysteria.service.NeuronTerminationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MainApplication {
    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        log.info("🧠 Neurysteria Booting Up...");

        var lossJudge = new EmotionDrivenLossJudge(1.0, 0.0);



        UseCaseFactory factory = new UseCaseFactory();

        NeuronConfig config = NeuronConfig.builder()
                .rageThreshold(10)
                .hysteriaThreshold(20)
                .rageFromNegativeInput(1.0)
                .rageDecayPerTick(0.8)                 // быстро выходит из злобы
                .activationDecayPerTick(0.01)          // почти не теряет активацию
                .angerImpactOnEnemies(0.05)
                .calmShareToFriends(0.3) // раньше было 0.05
                .allowMutation(true)
                .mutationChance(0.2)                  // не бешеная мутация
                .deathChanceFromRage(0.05)
                .baseActivationThreshold(0.5)
                .activationFunction(ActivationFunctions.RELU)
                .build();


        var createNetwork = factory.createCreateNetworkUseCase();
        List<Neuron> initialNeurons = createNetwork.create(50, config);
        NeuronNetwork network = factory.createNetworkWithMutationSupport(initialNeurons);

        var ticker = factory.createTickNetworkUseCase(network);

        TaskType type = TaskType.CHAIN_CALCULATOR;

        Task task = TaskFactory.create(type);

        for (int i = 0; i < 100; i++) {


            task.injectInputs(network);
            EmotionState state = ticker.executeTick();
            task.evaluate(network);


            if (i % 20 == 0) {
                TraceLog.dumpNetworkState(i, network);
            }

            lossJudge.evaluateAndReact(network, task); // 💥 оценка + эмоции

            // log.info("Tick {} → {}", i, state);

            if (task.isSolved()) {
                log.error("🎯 \n \n Task solved at tick {} \n \n", i);
                break;
            }
        }
    }
}


