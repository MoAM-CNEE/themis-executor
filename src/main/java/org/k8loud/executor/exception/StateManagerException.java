package org.k8loud.executor.exception;

import org.jetbrains.annotations.NotNull;

public class StateManagerException extends CustomException {
    public StateManagerException(@NotNull Enum exceptionCode) {
        super(exceptionCode);
    }

    public StateManagerException(Exception e, @NotNull Enum exceptionCode) {
        super(e, exceptionCode);
    }

    public StateManagerException(String message, @NotNull Enum exceptionCode) {
        super(message, exceptionCode);
    }
}
