# Report

## Time spent

Most of the time spent was in the group as we discussed the work a lot together. The time division was as follows:



### 

## Experience gained

### Documentation and Examples

### Project Tool Framework

### Interaction within Team and Community

## Refactor requirements

### 1: Make it easier to add window engines
Currently the window engine is specified in the WindowBuilder class as an integer. In the future this can be difficult to keep track of and can introduce bugs. 

#### Plan for Testing and Refactoring
To to this we will introduce a class for enumerating different Window Engines. Then to add an engine it is only necessary to add it to the enum and then use the enum in the WindowBuilder. Since a project will not compile if we are using a enum type that does not exist, it can be argued that testing is not really necessary for the enum class. Instead, the classes that use the enum class should cover different values in their tests.

### 2. Requriement 2
This is the second requirement

#### Plan for Testing and Refactoring
This is the test plan

## Difficult bugs
