package com.monad;

import java.util.function.Predicate;

public class SimpleRule implements Rule<Boolean> {
    private final String name;
    private final Predicate<Context> predicate;

    public SimpleRule(String name, Predicate<Context> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    @Override
    public Audited<Boolean> evaluate(Context context) {
        boolean result = predicate.test(context);
        String logEntry = String.format("[%s] evaluated to %s", name, result);
        return new Audited<>(result, logEntry);
    }

    @Override
    public String getName() {
        return name;
    }
}
