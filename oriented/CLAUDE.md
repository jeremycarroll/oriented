# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

The Java Oriented Matroid Library is a mathematical software library for manipulating oriented matroids. It implements various cryptomorphic representations (circuits, co-circuits, vectors, co-vectors, maximum vectors, topes, chirotopes, and realizations) and conversions between them. The primary focus is on developing a new algorithm for realizability in rank 3 oriented matroids.

## Architecture

The codebase follows a layered architecture:

1. **Core Interfaces (`net.sf.oriented.omi`)**: The public API with key interfaces like `OM` (oriented matroid), `SignedSet`, and different representations (OMasChirotope, OMasSignedSet, OMasRealized).

2. **Implementation (`net.sf.oriented.impl`)**: Contains implementation classes for the core interfaces, with subpackages:
   - `items`: Factory implementations for basic objects
   - `om`: Core oriented matroid implementations
   - `set`: Set implementations (signed sets, unsigned sets)
   - `util`: Utility functions

3. **Mathematical Utilities**:
   - `net.sf.oriented.util.combinatorics`: Permutations, groups, lexicographic ordering
   - `net.sf.oriented.util.matrix`: Matrix operations, Gram-Schmidt, etc.
   - `net.sf.oriented.util.graph`: Graph algorithms and path finding

4. **Specialized Components**:
   - `net.sf.oriented.polytope`: Face lattice and polytope manipulation
   - `net.sf.oriented.pseudoline`: Pseudoline arrangements and drawings
   - `net.sf.oriented.pseudoline2`: Advanced pseudoline algorithms
   - `net.sf.oriented.subsets`: Algorithms for finding minimal subsets

## Build System

The project currently uses Ant (build.xml) with manually managed dependencies in the `lib/` directory. Key dependencies include:

- JUNG graph library (jung-*.jar)
- Apache Commons Math (commons-math3-3.0.jar)
- Guava (guava-14.0.1.jar)
- JUnit (junit-4.8.2.jar)
- Apache POI (poi-*.jar)
- Perisic Ring Library (perisicRingAllv3.jar)

## Modernization Notes

This codebase was originally developed for Java 1.7 using Eclipse. To modernize:

1. **Maven Migration**:
   - Create a `pom.xml` file with all current dependencies
   - Use Maven standard directory structure (move src/ to src/main/java/)
   - Move test code to src/test/java/
   - Add Maven plugins for compilation, testing, and packaging

2. **Java Version Update**:
   - Update code for modern Java features
   - Check for deprecated API usage
   - Review uses of raw types and add proper generics
   - Add module-info.java if migrating to Java 9+

3. **IntelliJ Integration**:
   - Once Maven is set up, IntelliJ should import the project correctly
   - Configure code style based on existing style in eclipse-settings/

## Common Commands

Once migrated to Maven, use these commands:

```bash
# Build the project
mvn clean compile

# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=TestClassName

# Package the project
mvn package

# Generate JavaDoc
mvn javadoc:javadoc
```

## Development Process

1. Ensure tests pass before making changes:
   - The test suite is in `src-test/test/TestAll.java`

2. Key classes for understanding the code:
   - `net.sf.oriented.omi.OM`: Main interface for oriented matroids
   - `net.sf.oriented.omi.Examples`: Contains example oriented matroids
   - `net.sf.oriented.omi.FactoryFactory`: Factory methods to create oriented matroids

3. Algorithm focus:
   - The main algorithm goal is implementing realizability checks for rank 3 oriented matroids
   - The algorithm follows the approach in "New Proof of Pappus (2007)" and "Drawing Straight Lines (2000)"