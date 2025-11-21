package com.monad;

public class Context {
    public final int temperature;
    public final boolean isAdmin;

    public Context(int temperature, boolean isAdmin) {
        this.temperature = temperature;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "Context{temperature=" + temperature + ", isAdmin=" + isAdmin + "}";
    }
}
