package com.monad;

public interface Rule<T> {
    Audited<T> evaluate(Context context);
    String getName();

    default <U> Rule<U> map(java.util.function.Function<T, U> mapper) {
        return new Rule<>() {
            @Override
            public Audited<U> evaluate(Context context) {
                return Rule.this.evaluate(context).map(mapper);
            }

            @Override
            public String getName() {
                return Rule.this.getName() + " (mapped)";
            }
        };
    }

    default <U> Rule<U> flatMap(java.util.function.Function<T, Rule<U>> mapper) {
        return new Rule<>() {
            @Override
            public Audited<U> evaluate(Context context) {
                Audited<T> result = Rule.this.evaluate(context);
                Rule<U> nextRule = mapper.apply(result.getValue());
                Audited<U> nextResult = nextRule.evaluate(context);
                
                java.util.List<String> combinedLog = new java.util.ArrayList<>(result.getLog());
                combinedLog.addAll(nextResult.getLog());
                
                return new Audited<>(nextResult.getValue(), combinedLog);
            }

            @Override
            public String getName() {
                return Rule.this.getName() + " (flatMapped)";
            }
        };
    }
    static <T> Rule<T> of(T value) {
        return new Rule<>() {
            @Override
            public Audited<T> evaluate(Context context) {
                return new Audited<>(value, "Constant: " + value);
            }

            @Override
            public String getName() {
                return "ConstantRule";
            }
        };
    }

    static <T> Rule<T> of(T value, String log) {
        return new Rule<>() {
            @Override
            public Audited<T> evaluate(Context context) {
                return new Audited<>(value, log);
            }

            @Override
            public String getName() {
                return "ConstantRule";
            }
        };
    }
}
