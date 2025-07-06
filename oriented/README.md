# Java Oriented Matroid Library

## Overview

The Java Oriented Matroid Library is a mathematical software library for manipulating oriented matroids. It implements various cryptomorphic representations (circuits, co-circuits, vectors, co-vectors, maximum vectors, topes, chirotopes, and realizations) and conversions between them.

This library was originally hosted on SourceForge (https://oriented.sourceforge.net/) and is now available on GitHub at https://github.com/jeremycarroll/oriented.

## Project Goals

The primary focus is on implementing the oriented matroid realizability algorithm in rank 3 which is 
discussed in Jeremy J. Carroll's paper: [New Proof of Pappus (2007)](http://arxiv.org/PS_cache/arxiv/pdf/0704/0704.3424v2.pdf).

As a subgoal, we wish to draw the Venn diagrams of six triangles from the [venntriangles](https://github.com/jeremycarroll/venntriangles) project.

Much of the work is based on implementing concepts from the book ["Oriented Matroids"](https://www.cambridge.org/catalogue/catalogue.asp?isbn=052177750X) by Björner et al.

## Building and Running

The project uses Maven for dependency management and building.

### Common Maven Commands

```bash
# Build the project
mvn clean compile

# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=TestClassName

# Generate JavaDoc documentation
mvn javadoc:javadoc

# Package the project
mvn package
```

## Using the WebPage Tool

The WebPage tool generates HTML pages with detailed information about oriented matroids, including their chirotope representations. This is the main command-line interface for exploring oriented matroids.

### Usage

```bash
java -jar target/oriented-0.3.0-SNAPSHOT.jar --<example-name> [options]
```

### Command Line Options

- `--<example-name>` (required): Specifies which oriented matroid to display. Examples include:
  - `--pappus`: Pappus's theorem configuration
  - `--ceva`: Ceva's theorem configuration
  - `--uniform3`: Uniform oriented matroid of 3 points
  - `--uniform4`: Uniform oriented matroid of 4 points
  - `--ringel`: Ringel's non-realizable configuration
  - `--circularsaw3`: Circular saw diagram of size 3
  - `--wheel12`: Wheel-like configuration with 12 elements

- Sign options (mutually exclusive):
  - `--plus`: Set variable value to +1
  - `--minus`: Set variable value to -1
  - `--zero`: Set variable value to 0

- Other options:
  - `-∞`, `--infinity`: Specifies the line at infinity
  - `-v`, `--verbose`: Enables verbose output

### Example

```bash
java -jar target/oriented-0.3.0-SNAPSHOT.jar --pappus
```

This will generate HTML files in the `doc/examples/` directory that contain:
- Chirotope representation (positively and negatively oriented bases)
- Visualizations of the pseudoline arrangements
- Line orders and other matroid information

## Library Features

- Support for different oriented matroid representations
- Algorithms for checking realizability
- Conversion between different cryptomorphisms
- Visualizations through pseudoline arrangements
- Example oriented matroids from mathematics literature

## Contact

Jeremy J. Carroll  
jjc1729@gmail.com