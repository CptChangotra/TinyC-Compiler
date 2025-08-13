# 🔧 TinyC Compiler

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-Academic-blue.svg)](#license)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](#building-and-testing)

A compiler frontend implementation for the TinyC programming language, featuring complete lexical analysis, parsing, and semantic analysis components with comprehensive AST generation.

## ✨ Features

- 🔍 **Lexical Analysis**: Complete tokenization with comprehensive error handling
- 🌳 **Syntax Analysis**: Recursive descent parser for TinyC grammar
- ✅ **Semantic Analysis**: Type checking, scope resolution, and semantic validation
- 🛠️ **AST Support**: Complete Abstract Syntax Tree representation
- 📊 **Diagnostic System**: Comprehensive error reporting with location tracking
- 🔧 **Modular Design**: Clean separation of compiler phases
- 📝 **Extensive Testing**: Comprehensive test suite for all implemented components

## 🏗️ Architecture

### Core Components

| Component | Description | Status |
|-----------|-------------|--------|
| **Lexer** | 📝 Tokenizes source code into lexical units | ✅ Implemented |
| **Parser** | 🌳 Builds Abstract Syntax Tree from tokens | ✅ Implemented |
| **Semantic Analyzer** | ✅ Performs type checking and scope resolution | ✅ Implemented |
| **AST Factory** | 🏭 Creates and manages AST nodes | ✅ Implemented |
| **Diagnostic System** | 📊 Error reporting and location tracking | ✅ Implemented |

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
- JUnit (for running tests)

### Compilation

```bash
# Compile the entire project
javac -cp libs/*:src -d bin src/tinycc/**/*.java

# Or use the provided scripts (if available)
./scripts/build.sh
```

### Basic Usage

```bash
# Parse and analyze a TinyC program (frontend only)
java -cp bin:libs/* tinycc.driver.TinyC program.c

# Run with verbose output for debugging
java -cp bin:libs/* tinycc.driver.TinyC -verbose program.c
```

## 📋 Implemented Features

| Feature | Description | Status |
|---------|-------------|--------|
| **Lexical Analysis** | Complete tokenization and lexing | ✅ Complete |
| **Syntax Analysis** | Recursive descent parser | ✅ Complete |
| **AST Generation** | Abstract Syntax Tree construction | ✅ Complete |
| **Semantic Analysis** | Type checking and scope resolution | ✅ Complete |
| **Error Reporting** | Comprehensive diagnostic system | ✅ Complete |

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

- **AST Tests**: Abstract Syntax Tree construction validation
- **Parser Tests**: Syntax analysis and grammar compliance
- **Semantic Tests**: Type checking and scope resolution
- **Expression Tests**: Binary and unary expression handling
- **Statement Tests**: Control flow and statement validation

## ⚙️ Implemented Compiler Phases

### 1. Lexical Analysis ✅
- Complete tokenization of source code
- Handles comments, whitespace, and string literals
- Reports lexical errors with precise location information
- Supports all TinyC tokens and keywords

### 2. Syntax Analysis ✅
- Recursive descent parser implementation
- Builds complete Abstract Syntax Tree (AST)
- Comprehensive grammar support for TinyC
- Error recovery and reporting

### 3. Semantic Analysis ✅
- Type checking and type inference
- Symbol table management across scopes
- Scope resolution and variable binding
- Function signature validation
- Declaration and usage consistency checks

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

Developed with ❤️ for compiler construction and programming language theory.

---

*A robust compiler frontend implementation demonstrating modern compiler design principles through lexical analysis, parsing, and semantic analysis.*
