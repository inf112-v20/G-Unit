# INF112 Group project
RoboRally boardgame made with libgdx. <br/>

[![Build Status](https://travis-ci.com/inf112-v20/G-Unit.svg?branch=master)](https://travis-ci.com/inf112-v20/G-Unit)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ff6ed6656586423f8c55fdefb4913b1b)](https://www.codacy.com/gh/inf112-v20/G-Unit?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=inf112-v20/G-Unit&amp;utm_campaign=Badge_Grade)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

## Project setup

### macOS and Linux

**1. Maven is required for building the game, so install Maven first.**
  
   https://maven.apache.org/download.cgi

**2. Clone the repository**
    
    git clone https://github.com/inf112-v20/G-Unit

**3. Build the game with all dependencies**

    cd G-Unit && mvn clean verify assembly:single

**4. Run the game** 

    java -jar target/mvn-app-1.0-SNAPSHOT-jar-with-dependencies.jar

If executed properly you are presented with the main screen of the game.

### Windows

1. Install a Java IDE, such as Eclipse or IntelliJ IDEA

2. Import the project from version control, as a Maven project.<br/>
   Specify `pom.xml` as the configuration file for Maven.

3. Run the project, the main class is located at inf112.gunit.main.Main.

If executed properly you are presented with the main screen of the game.

## Playing the game

-   The game is currently not playable. The robots will execute a selection of random program cards. You can see the game mechanics in action by watchin the robots move around.

## Notes

You need to use Java 8 to run this game.
