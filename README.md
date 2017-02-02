# SpiesAndGpars
The purpose of this project is to demonstrate an apparent inability to execute GPars .eachParallel from an object which is a Spock "Spy".

## The files
Calc.groovy: A simple calculator to track a running sum. Add a single int or a list of ints.
CalcRun.groovy: Simple demonstration of using the class in Calc.groovy.
CalcSpec.groovy: A Spock test class to exercise the class in Calc.groovy

## Running
I tested this code using Groovy 2.4.7 (installed via [SDKman](http://sdkman.io/)). All other dependencies are specified in the .groovy files as `@Grab`'s.

*Executing the sample program*

`$ groovy CalcRun`

*Executing the tests*

`$ groovy CalcSpec`

The incompatibility will not be apparent in CalcSpec.groovy until you comment/remove either `@Ignore`d test. If you un-Ignore either test, the test suite will hang on that test.
