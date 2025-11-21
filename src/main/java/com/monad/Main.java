package com.monad;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Monad!");
        Context ctx = new Context(80, false);

        Rule<Boolean> hotRule = new SimpleRule("Hot Temperature", c -> c.temperature > 90);
        Rule<Boolean> warmRule = new SimpleRule("Warm Temperature", c -> c.temperature > 70);
        Rule<Boolean> adminRule = new SimpleRule("Is Admin", c -> c.isAdmin);

        Rule<Boolean> tempCategory = new FirstRule("Temperature Category",
            List.of(hotRule, warmRule));

        Rule<Boolean> systemCheck = new AllRule("System Check",
            List.of(tempCategory, adminRule));

        Audited<Boolean> result = systemCheck.evaluate(ctx);

        System.out.println("FINAL RESULT: " + result.getValue());
        System.out.println("AUDIT TRAIL:");
        result.getLog().forEach(System.out::println);

        // --- Monadic Demo ---
        System.out.println("\n--- MONADIC DEMO ---");
        
        // map: Transform Boolean result to String
        Rule<String> statusMessage = systemCheck.map(passed -> passed ? "SYSTEM OK" : "SYSTEM FAILURE");
        Audited<String> mappedResult = statusMessage.evaluate(ctx);
        System.out.println("Mapped Result: " + mappedResult.getValue());
        
        // flatMap: Chain rules
        // If system check passes, run a "Deploy" rule (simulated)
        Rule<String> deployRule = systemCheck.flatMap(passed -> {
            if (passed) {
                return new Rule<String>() {
                    @Override
                    public Audited<String> evaluate(Context c) {
                        return new Audited<>("Deployed Successfully", "Deployment script ran");
                    }
                    @Override
                    public String getName() { return "Deploy Rule"; }
                };
            } else {
                return new Rule<String>() {
                    @Override
                    public Audited<String> evaluate(Context c) {
                        return new Audited<>("Deployment Aborted", "System check failed, aborting");
                    }
                    @Override
                    public String getName() { return "Abort Rule"; }
                };
            }
        });
        
        Audited<String> deployResult = deployRule.evaluate(ctx);
        System.out.println("Deploy Result: " + deployResult.getValue());
        System.out.println("Deploy Logs:");
        deployResult.getLog().forEach(System.out::println);
    }
}