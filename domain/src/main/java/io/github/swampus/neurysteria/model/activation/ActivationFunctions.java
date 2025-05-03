package io.github.swampus.neurysteria.model.activation;

import java.util.Random;

public enum ActivationFunctions implements ActivationFunction {
    RELU {
        public double apply(double input) {
            return Math.max(0, input);
        }
    },
    SIGMOID {
        public double apply(double input) {
            return 1.0 / (1.0 + Math.exp(-input));
        }
    },
    TANH {
        public double apply(double input) {
            return Math.tanh(input);
        }
    },
    STEP {
        public double apply(double input) {
            return input > 0 ? 1.0 : 0.0;
        }
    };

    private static final ActivationFunctions[] VALUES = values();
    private static final Random RANDOM = new Random();

    public static ActivationFunctions random() {
        return VALUES[RANDOM.nextInt(VALUES.length)];
    }
}

