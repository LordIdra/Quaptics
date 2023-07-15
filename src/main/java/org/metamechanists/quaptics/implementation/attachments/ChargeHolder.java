package org.metamechanists.quaptics.implementation.attachments;

import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.storage.QuapticTicker;


public interface ChargeHolder {
    default double stepCharge(final Settings settings, final double charge, final double chargeStep) {
        if (charge + chargeStep < 0) {
            return 0;
        }
        return Math.min(charge + chargeStep, settings.getChargeCapacity());
    }

    default double stepDischarge(final Settings settings, final double charge) {
        return stepCharge(settings, charge, -settings.getEmissionPower() * ((double) QuapticTicker.INTERVAL_TICKS_6 / QuapticTicker.TICKS_PER_SECOND));
    }
}
