package com.monad;

import java.util.List;

public class AllRule implements Rule<Boolean> {
    private final String name;
    private final List<Rule<Boolean>> rules;

    public AllRule(String name, List<Rule<Boolean>> rules) {
        this.name = name;
        this.rules = rules;
    }

    @Override
    public Audited<Boolean> evaluate(Context context) {
        // Monadic reduction: Start with true, AND each rule
        return rules.stream()
            .reduce(
                Rule.of(true, String.format("[%s] Starting evaluation (all must pass)", name)),
                (acc, rule) -> acc.flatMap(passed -> {
                    if (!passed) return Rule.of(false);
                    return rule.map(r -> {
                        if (!r) return false;
                        return true;
                    });
                }),
                (r1, r2) -> { throw new UnsupportedOperationException("Parallel not supported"); }
            ).evaluate(context);
    }

    @Override
    public String getName() {
        return name;
    }
}
