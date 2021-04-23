#!/bin/bash
clear
cd /Users/percy.rotteveel/Documents/workspace/PrevotyJarVersion
DIR="/Users/percy.rotteveel/Documents/Prevoty/Products/3.9.11"
java -jar ./PrevotyJarVersion.jar "$DIR/prevoty-agent.jar"
java -jar ./PrevotyJarVersion.jar "$DIR/prevoty-rasp.jar"
