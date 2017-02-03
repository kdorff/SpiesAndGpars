#!/usr/bin/env groovy

Calc calc

calc = new Calc()
println calc.add(5)
println calc.addList([10,20,30])

calc = new Calc()
println calc.add(5)
println calc.addListParallelGpars([10,20,30])

calc = new Calc()
println calc.add(5)
println calc.addListParallelExecutors([10,20,30])
