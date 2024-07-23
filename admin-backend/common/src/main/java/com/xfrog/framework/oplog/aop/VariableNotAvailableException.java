package com.xfrog.framework.oplog.aop;

import org.springframework.expression.EvaluationException;

class VariableNotAvailableException extends EvaluationException {

    VariableNotAvailableException(String name) {
        super("Variable '" + name + "' not available");
    }

}
