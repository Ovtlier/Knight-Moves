#!/bin/bash

if [ $# -eq 1 ]; then
    echo "Path given"
    path=$1
else
    cwd="$(pwd)"
    path="$cwd/openjfx-17.0.1_windows-x64_bin-sdk/lib"
fi

echo "Left click: Add/Remove Start Position"
echo "Right click: Add/Remove Target Position"
echo "Middle click: Run program"

java -jar --module-path "$path" --add-modules javafx.controls,javafx.fxml Knight-Moves.jar