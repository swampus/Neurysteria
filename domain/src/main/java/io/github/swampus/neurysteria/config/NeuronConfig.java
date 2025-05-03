package io.github.swampus.neurysteria.config;

import io.github.swampus.neurysteria.model.activation.ActivationFunction;
import lombok.Builder;

/**
 * @param rageThreshold           --- Thresholds --- Rage value above which the neuron becomes "angry"
 * @param hysteriaThreshold       Rage value above which the neuron enters "hysteria"
 * @param rageFromNegativeInput   --- Emotional dynamics --- How much rage is added from negative input
 * @param rageDecayPerTick        Natural rage decay per tick
 * @param activationDecayPerTick  Natural activation decay per tick
 * @param angerImpactOnEnemies    --- Interaction effects --- Strength of impact on enemies when angry
 * @param calmShareToFriends      Portion of energy shared with friends when calm
 * @param allowMutation           --- Behavior settings --- Whether the neuron is allowed to mutate
 * @param mutationChance          Chance to mutate during hysteria
 * @param deathChanceFromRage     Chance of neuron "death" at high rage levels
 * @param baseActivationThreshold --- Activation --- Minimum activation level to trigger behavior
 * @param activationFunction      --- Activation function ---
 */
@Builder
public record NeuronConfig(double rageThreshold, double hysteriaThreshold, double rageFromNegativeInput,
                           double rageDecayPerTick, double activationDecayPerTick, double angerImpactOnEnemies,
                           double calmShareToFriends, boolean allowMutation, double mutationChance,
                           double deathChanceFromRage, double baseActivationThreshold,
                           ActivationFunction activationFunction) {


}
