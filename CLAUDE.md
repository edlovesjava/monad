# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./gradlew build      # Build the project
./gradlew run        # Run the application
./gradlew test       # Run all tests
```

## Architecture

This is a Java 17 educational project implementing the monad design pattern for composable rule evaluation with audit trails.

### Core Abstractions

**Rule<T>** (`src/main/java/com/monad/Rule.java`) - The monad interface:
- `evaluate(Context)` - Executes the rule, returns `Audited<T>`
- `map(Function<T, U>)` - Functor operation for transforming results
- `flatMap(Function<T, Rule<U>>)` - Monadic bind for chaining rules
- `of(T)` - Static factory for constant rules

**Audited<T>** (`src/main/java/com/monad/Audited.java`) - Value wrapper with audit log:
- Pairs rule results with immutable audit trail
- Logs automatically combine during `flatMap` operations

**Context** (`src/main/java/com/monad/Context.java`) - Immutable evaluation context passed to rules

### Rule Implementations

- **SimpleRule** - Predicate-based rules for single boolean checks
- **FirstRule** - OR logic combinator (stops at first true)
- **AllRule** - AND logic combinator (stops at first false)

Both combinators use `Stream.reduce()` with monadic operations for functional aggregation.

### Key Patterns

1. **Lazy Evaluation** - Rules are evaluated only when `evaluate()` is called; chain construction is separate from execution
2. **Audit Trail Preservation** - `flatMap` combines logs from chained rules; `map` transforms results while keeping the same log
3. **Reduction-based Composition** - Combinators use Stream.reduce (sequential only, parallel combiner unsupported)
