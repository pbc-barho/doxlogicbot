<br />
<p align="center">
  <h1 align="center">Doxastic Logic Bot</h1>

  <p align="center">
    This is a logic bot that posts tautologies of varying complexity of the doxastic logic KD45_n on Twitter. 
  </p>

## About The Project

This was the Bachelor's research project of Paula Barho from the University of Groningen, Netherlands. The project contains three main components: A formula generator, a tableau solver, and a Twitter publisher. The formula generator generates formulas in the doxastic modal logic KD45n, the tableau solver determines whether the formulas are tautologies by the use of tableaux, and the Twitter publisher posts the tautologies on Twitter.

### Built With

* [Maven](https://maven.apache.org/)

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

* Java 16
* The latest version of Maven

### Installation

The project can be run using maven as follows: 
1. Clean and build the project using:
```sh
mvn install
```
2. Run the `main` method of LogicBot using:
```sh
mvn exec:java
```

Alternatively you can run the main method in `LogicBot.java` using an IDE of your choice (e.g. IntelliJ).

Should you want to run this program standalone, you can create a JAR file with the following maven command:

```sh
mvn clean package
```
The JAR file will appear in the `/target` directory.

## Design 

The main project consists out of three main components that are implemented as packages. These can be found in the src directory.

### Generator
The generator package contains the directories and classes that are directly concerned with generating and implementing formulas. The FormulaGenerator implements the generating itself. 

### Solver
The solver package contains the tableau solver, as well as the classes that implement a tableaux and their branches. The tableau solver validates the formulas that have been generated by the formula generator.

### Twitter bot
The io package contains the Publisher class that implements the publishing of tweets on Twitter. Additionally it contains the Serializer class that is used to store the generated formulas in files. It also loads the files when the tableau solver is validating the formulas.  

## Usage

The logic bot is publishing a tautology every three hours on Twitter: https://twitter.com/doxlogicbot. 

## Contact

Paula Barho, University of Groningen, Netherlands. 
