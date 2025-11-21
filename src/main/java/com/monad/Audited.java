package com.monad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Audited<T> {
    private final T value;
    private final List<String> log;

    public Audited(T value, List<String> log) {
        this.value = value;
        this.log = new ArrayList<>(log);
    }

    public Audited(T value, String logEntry) {
        this.value = value;
        this.log = new ArrayList<>();
        this.log.add(logEntry);
    }

    public T getValue() {
        return value;
    }

    public List<String> getLog() {
        return Collections.unmodifiableList(log);
    }

    public <U> Audited<U> flatMap(java.util.function.Function<T, Audited<U>> f) {
        Audited<U> result = f.apply(this.value);
        List<String> combinedLog = new ArrayList<>(this.log);
        combinedLog.addAll(result.getLog());
        return new Audited<>(result.getValue(), combinedLog);
    }

    public <U> Audited<U> map(java.util.function.Function<T, U> f) {
        return new Audited<>(f.apply(this.value), this.log);
    }

    @Override
    public String toString() {
        return "Audited{value=" + value + ", log=" + log + "}";
    }
}
