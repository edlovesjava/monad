# Monad

A Java-based educational project demonstrating the monad design pattern and functional programming concepts. This project implements a composable rule evaluation system with auditing capabilities using monadic operations.

## Features

- **Monad Pattern Implementation** - Full implementation of `map` and `flatMap` for functional composition
- **Composable Rule Engine** - Build complex rules from simple, reusable components
- **Audit Trail Support** - Track all rule evaluations and decisions automatically
- **Type-Safe Generics** - Leverage Java generics for compile-time type safety
- **Multiple Rule Combinators** - AND logic (`AllRule`), OR logic (`FirstRule`), and custom compositions

## Requirements

- Java 17 or higher
- Gradle (wrapper included)

## Getting Started

### Building the Project

```bash
./gradlew build
```

### Running the Application

```bash
./gradlew run
```

### Running Tests

```bash
./gradlew test
```

## Project Structure

```
monad/
├── build.gradle                    # Build configuration
├── settings.gradle                 # Project settings
├── gradlew / gradlew.bat           # Gradle wrapper scripts
└── src/
    ├── main/java/com/monad/
    │   ├── Main.java               # Entry point with usage examples
    │   ├── Rule.java               # Core monad interface
    │   ├── Audited.java            # Monad wrapper with audit logging
    │   ├── Context.java            # Evaluation context
    │   ├── SimpleRule.java         # Predicate-based rule implementation
    │   ├── FirstRule.java          # OR logic combinator
    │   └── AllRule.java            # AND logic combinator
    └── test/java/com/monad/
        └── MainTest.java           # Unit tests
```

## Core Concepts

### Rule Interface

The `Rule<T>` interface is the core monad abstraction:

```java
public interface Rule<T> {
    Audited<T> evaluate(Context ctx);

    // Functor operation - transform the result
    <U> Rule<U> map(Function<T, U> f);

    // Monadic bind - chain rules together
    <U> Rule<U> flatMap(Function<T, Rule<U>> f);

    // Create a constant rule
    static <T> Rule<T> of(T value);
}
```

### Audited Wrapper

The `Audited<T>` class pairs values with audit logs, enabling traceability:

```java
Audited<Boolean> result = rule.evaluate(context);
Boolean value = result.value();
List<String> auditLog = result.log();
```

### Rule Composition

Compose rules using functional combinators:

```java
// Simple rules
Rule<Boolean> isHot = new SimpleRule("Hot", ctx -> ctx.temperature() > 30);
Rule<Boolean> isAdmin = new SimpleRule("Admin", Context::isAdmin);

// OR logic - first matching rule wins
Rule<Boolean> tempCategory = new FirstRule<>("TempCheck", List.of(isHot, isWarm));

// AND logic - all rules must pass
Rule<Boolean> systemCheck = new AllRule<>("SystemCheck", List.of(tempCategory, isAdmin));

// Monadic chaining
Rule<String> deployment = systemCheck.flatMap(ok ->
    ok ? Rule.of("Deploy") : Rule.of("Hold")
);
```

## Usage Example

```java
Context ctx = new Context(35, true);  // temperature=35, isAdmin=true

Rule<Boolean> rule = new AllRule<>("Check", List.of(
    new SimpleRule("Hot", c -> c.temperature() > 30),
    new SimpleRule("Admin", Context::isAdmin)
));

Audited<Boolean> result = rule.evaluate(ctx);
System.out.println("Result: " + result.value());  // true
System.out.println("Audit: " + result.log());     // [Hot: true, Admin: true, Check: true]
```

## License

This project is available for educational purposes.
