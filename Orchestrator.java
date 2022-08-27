package com.app.sample;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Orchestrator {
    private boolean condition;

    private Orchestrator() {
        condition = true;
    }

    public static Orchestrator init() {
        return new Orchestrator();
    }

    public Orchestrator when(Supplier<Boolean> condition) {
        this.condition = this.condition && condition.get();
        return this;
    }

    public void then(Runnable runnable) {
        if (condition) {
            runnable.run();
        }
    }

    public <T> T then(Callable<?> callable, Class<T> type) throws Exception {
        if (condition) {
            return type.cast(callable.call());
        }
        throw new IllegalStateException("Condition not met");
    }

    public static void main(String[] args) throws Exception {
        String dob = "1asd";
        Orchestrator.init()
                .when(() -> StringUtils.isNotBlank(dob))
                .when(() -> dob.startsWith("1"))
                .then(() -> System.out.println("test"));

        String then = Orchestrator.init()
                .when(() -> StringUtils.isNotBlank(dob))
                .when(() -> dob.startsWith("1"))
                .then(() -> "ok", String.class);

        System.out.println(then);
    }
}
