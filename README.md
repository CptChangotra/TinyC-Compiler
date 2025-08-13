# 🔧 TinyC Compiler

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-Academic-blue.svg)](#license)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](#building-and-testing)

A complete compiler implementation for the TinyC programming language, featuring lexical analysis, parsing, semantic analysis, code generation, and formal verification capabilities.

## ✨ Features

- 🔍 **Lexical Analysis**: Complete tokenization with comprehensive error handling
- 🌳 **Syntax Analysis**: Recursive descent parser for TinyC grammar
- ✅ **Semantic Analysis**: Type checking, scope resolution, and semantic validation
- ⚡ **Code Generation**: Assembly code generation with optimization support
- 🔐 **Formal Verification**: Integration with Z3 solver for program verification
- 🛠️ **AST Support**: Complete Abstract Syntax Tree representation
- 📊 **Diagnostic System**: Comprehensive error reporting with location tracking

## 🏗️ Architecture

### Core Components

| Component | Description |
|-----------|-------------|
| **Lexer** | 📝 Tokenizes source code into lexical units |
| **Parser** | 🌳 Builds Abstract Syntax Tree from tokens |
| **Semantic Analyzer** | ✅ Performs type checking and scope resolution |
| **Code Generator** | ⚙️ Produces optimized assembly code |
| **Verifier** | 🔐 Generates verification conditions for Z3 |

### Project Structure

```
📦 TinyC-Compiler
├── 📂 src/
│   ├── 📂 tinycc/
│   │   ├── 📂 driver/           # Main compiler driver
│   │   ├── 📂 parser/           # Lexical and syntax analysis
│   │   ├── 📂 diagnostic/       # Error reporting system
│   │   ├── 📂 asmgen/          # Assembly code generation
│   │   ├── 📂 logic/           # Verification logic and Z3 integration
│   │   │   └── 📂 solver/      # Z3 solver interface
│   │   ├── 📂 implementation/  # Core implementation
│   │   │   ├── 📂 expression/  # Expression handling
│   │   │   ├── 📂 statement/   # Statement implementation
│   │   │   ├── 📂 type/        # Type system
│   │   │   └── 📂 semantic/    # Semantic analysis
│   │   └── 📂 util/           # Utility classes
│   └── 📂 prog2/
│       └── 📂 tests/          # Comprehensive test suite
├── 📂 libs/                   # External libraries
├── 📂 scripts/               # Build and utility scripts
└── 📄 README.md
```

## 🚀 Getting Started

### Prerequisites

- Java 8 or higher
- Z3 SMT Solver (for verification features)
- Make (optional, for build scripts)

### Compilation

```bash
# Compile the entire project
javac -cp libs/*:src -d bin src/tinycc/**/*.java

# Or use the provided scripts
./scripts/build.sh
```

### Basic Usage

```bash
# Compile a TinyC program
java -cp bin:libs/* tinycc.driver.TinyC -c program.c

# Compile with optimization
java -cp bin:libs/* tinycc.driver.TinyC -O -c program.c

# Verify program correctness
java -cp bin:libs/* tinycc.driver.TinyC -v program.c

# Compile and specify output file
java -cp bin:libs/* tinycc.driver.TinyC -c -o output.s program.c
```

## 📋 Command Line Options

| Option | Description |
|--------|-------------|
| `-c` | Compile to assembly code |
| `-O` | Enable optimizations |
| `-v` | Perform formal verification |
| `-o <file>` | Specify output file |

## 🔤 TinyC Language Features

### Supported Constructs

- **Data Types**: `int`, `char`, `void`
- **Control Flow**: `if-else`, `while`, `for`
- **Functions**: Function definitions and calls
- **Arrays**: Array declarations and access
- **Pointers**: Pointer arithmetic and dereferencing
- **Expressions**: Arithmetic, logical, and comparison operators

### Example Program

```c
int factorial(int n) {
    if (n <= 1) {
        return 1;
    } else {
        return n * factorial(n - 1);
    }
}

int main() {
    int result = factorial(5);
    return result;
}
```

## 🧪 Testing

### Running Tests

```bash
# Compile and run test suite
javac -cp bin:libs/* -d bin src/prog2/tests/**/*.java
java -cp bin:libs/* org.junit.runner.JUnitCore prog2.tests.pub.AllTests
```

### Test Categories

- **Parser Tests**: Syntax analysis validation
- **Semantic Tests**: Type checking and scope resolution
- **CodeGen Tests**: Assembly generation verification
- **Verification Tests**: Formal verification capabilities

## ⚙️ Compiler Phases

### 1. Lexical Analysis
- Tokenizes source code
- Handles comments and whitespace
- Reports lexical errors with precise locations

### 2. Syntax Analysis
- Recursive descent parser
- Builds complete AST
- Syntax error recovery

### 3. Semantic Analysis
- Type checking and inference
- Symbol table management
- Scope resolution
- Declaration/usage validation

### 4. Code Generation
- Three-address code intermediate representation
- Register allocation
- Assembly code emission
- Optimization passes (with `-O` flag)

### 5. Verification (Optional)
- Generates verification conditions
- Z3 SMT solver integration
- Formal correctness proofs

## 🔧 Optimization Features

When using the `-O` flag:
- **Constant Folding**: Compile-time expression evaluation
- **Dead Code Elimination**: Removes unreachable code
- **Register Allocation**: Efficient register usage
- **Peephole Optimization**: Local code improvements

## 🔐 Verification System

The integrated verification system:
1. Transforms programs into logical formulas
2. Uses Z3 SMT solver for satisfiability checking
3. Provides formal correctness guarantees
4. Reports verification results with detailed feedback

## 🛠️ Development

### Code Style
- Follows Java coding conventions
- Comprehensive documentation
- Modular design with clear interfaces
- Extensive error handling

### Contributing Guidelines
This is an academic project. Please follow assignment guidelines for modifications.

## 📊 Performance

- **Fast Compilation**: Efficient single-pass algorithms
- **Memory Efficient**: Optimized AST representation
- **Scalable**: Handles programs of varying complexity
- **Robust**: Comprehensive error recovery

## 📄 License

This project is developed for academic purposes and follows educational use guidelines.

## 👨‍💻 Author

Developed with ❤️ for compiler construction and formal verification.

---

*A complete compiler implementation demonstrating modern compiler design principles and formal verification techniques.*
