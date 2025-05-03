package io.github.swampus.neurysteria.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BirthProfileConfig {
    private double calmRageMin;
    private double calmRageMax;

    private double angryRageMin;
    private double angryRageMax;

    private double hystericalRageMin;
    private double hystericalRageMax;

    private boolean randomizeActivationFunctionInHysteria;

    private double forgetConnectionChance;

    private int holyCyclesOfCrusadeAlignment;
    private double rageThresholdForObsession;
}
