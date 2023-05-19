#!/bin/bash

# Get the name of the Java file
filename=$1

# Compile the Java file
if ! javac $filename; then
  # Compilation error
  exit 1
fi

# Run the compiled Java program
if ! java Main; then
  # Runtime error
  exit 2
fi

# Success
exit 0

