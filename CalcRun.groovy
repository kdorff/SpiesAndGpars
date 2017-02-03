#!/usr/bin/env groovy

Calc calc = new Calc()
println calc.add(5)
println calc.addListParallelExecutors([10,20,30])
