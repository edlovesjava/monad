package com.monad;

import java.util.List;

public class FirstRule implements Rule<Boolean> {
    private final String name;
    private final List<Rule<Boolean>> rules;

    public FirstRule(String name, List<Rule<Boolean>> rules) {
        this.name = name;
        this.rules = rules;
    }

    @Override
    public Audited<Boolean> evaluate(Context context) {
        // Monadic reduction: Start with false, OR each rule
        return rules.stream()
            .reduce(
                Rule.of(false, String.format("[%s] Starting evaluation (first match wins)", name)),
                (acc, rule) -> acc.flatMap(foundMatch -> {
                    if (foundMatch) return Rule.of(true);
                    return rule.map(r -> {
                        if (r) return true;
                        return false;
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
