package io.github.swampus.neurosteria.task;

public class TaskFactory {

    public static Task create(TaskType type) {
        return switch (type) {
            case SEQUENCE -> new SequencePredictionTask();
            case BROKEN_SEQUENCE -> new BrokenSequenceTask();
            case CHAIN_CALCULATOR -> new ChainCalculationTask();
        };
    }
}