#!/bin/bash

# 1. Fix Case-Sensitivity (Renaming to match the code)
# If the lowercase file exists, rename it to uppercase 'C'
if [ -f "src/Jason/TicTacToePanel.java" ]; then
    echo "Fixing TicTacToePanel filename case..."
    mv src/Jason/TicTacToePanel.java src/Jason/TicTacToePanel.java
fi

# 2. Organize Assets
# Ensure images are in the package folder for getClass().getResource()
if [ -d "src/images" ]; then
    echo "Moving images to src/Jason/images/..."
    mkdir -p src/Jason/images
    mv src/images/* src/Jason/images/ 2>/dev/null
    rm -rf src/images
fi

# 3. Clean Build Environment
echo "Cleaning old build files..."
rm -rf bin
mkdir bin

# 4. Compile the Project
echo "Compiling Java files..."
javac -d bin -sourcepath src src/Jason/*.java

# 5. Sync Assets to Bin (Crucial for runtime loading)
echo "Syncing assets to bin/..."
mkdir -p bin/Jason/images
cp -r src/Jason/images/* bin/Jason/images/

if [ $? -eq 0 ]; then
    echo "--------------------------------------"
    echo "BUILD SUCCESSFUL!"
    echo "Run with: java -cp bin Jason.MainLauncher"
    echo "--------------------------------------"
else
    echo "BUILD FAILED. Check the errors above."
fi