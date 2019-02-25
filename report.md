# Report for assignment 4

This is a template for your report. You are free to modify it as needed.
It is not required to use markdown for your report either, but the report
has to be delivered in a standard, cross-platform format.

## Project

Name:

URL:

One or two sentences describing it

## Architectural overview (optional, as one item for P+)

## Selected issue(s)

Title:

URL:

Summary in one or two sentences

## Onboarding experience

Did it build as documented?
    
(See the assignment for details; if everything works out of the box,
there is no need to write much here.)

## Requirements affected by functionality being refactored

### 1: Make it easier to add window engines
Currently the window engine is specified in the WindowBuilder class as an integer. In the future this can be difficult to keep track of and can introduce bugs. 

#### Plan for Testing and Refactoring
To to this we will introduce a class for enumerating different Window Engines. Then to add an engine it is only necessary to add it to the enum and then use the enum in the WindowBuilder. Since a project will not compile if we are using a enum type that does not exist, it can be argued that testing is not really necessary for the enum class. Instead, the classes that use the enum class should cover different values in their tests.

## Existing test cases relating to refactored code

## The refactoring carried out

(Link to) a UML diagram and its description

## Test logs

Overall results with link to a copy of the logs (before/after refactoring).

The refactoring itself is documented by the git log.

## Effort spent

For each team member, how much time was spent in

1. plenary discussions/meetings;

2. discussions within parts of the group;

3. reading documentation;

4. configuration;

5. analyzing code/output;

6. writing documentation;

7. writing code;

8. running code?

## Overall experience

What are your main take-aways from this project? What did you learn?

Is there something special you want to mention here?
