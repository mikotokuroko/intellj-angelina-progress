package com.angelinaprogress.intellij;

import com.angelinaprogress.intellij.configuration.AngelinaProgressState;
import com.angelinaprogress.intellij.model.Angelina;
import java.util.Optional;

public final class AngelinaPicker {
    private static final String TARGET_ENV_VAR = "ANGELINA_PROGRESS_TARGET";

    private AngelinaPicker() {
    }

    public static Angelina get() {
        final String target = System.getenv().get(TARGET_ENV_VAR);
        if (target != null) {
            return Angelina.getById(target);
        }
        return Optional.ofNullable(AngelinaProgressState.getInstance())
            .map(state -> Angelina.getById(state.selectedCharacter))
            .orElse(Angelina.BROOM_RIDE);
    }
}
