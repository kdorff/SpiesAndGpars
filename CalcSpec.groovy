#!/usr/bin/env groovy
@Grab(group='org.spockframework', module='spock-core', version='1.1-groovy-2.4-rc-3')
@Grab(group='cglib', module='cglib', version='3.2.4')

import spock.lang.Specification
import spock.lang.Ignore

class CalcSpec extends Specification {
    Calc calc

    /**
     * Simple test. Add two single numbers.
     * Works.
     */
    void "simple test, add two single numbers"() {
        setup:
        calc = new Calc()

        expect:
        calc.add(2) == 2
        calc.add(3) == 5
    }

    /**
     * Test add a single then add a list.
     * Works.
     */
    void "simple test, add a number and a list of numbers"() {
        setup:
        calc = new Calc()

        expect:
        calc.add(2) == 2
        calc.addList([-5, 4, 8]) == 9
    }

    /**
     * Test start with an initial value then add a single then parallel add a list.
     * Using GPars framework.
     * Works.
     */
    void "gpars: simple test, add a number and a list of numbers (parallel)"() {
        setup:
        calc = new Calc()

        expect:
        calc.add(2) == 2
        calc.addListParallelGpars([-5, 4, 8]) == 9
    }

    /**
     * Test start with an initial value then add a single then parallel add a list.
     * Using Executors framework.
     * Works.
     */
    void "executor: simple test, add a number and a list of numbers (parallel)"() {
        setup:
        calc = new Calc()

        expect:
        calc.add(2) == 2
        calc.addListParallelExecutors([-5, 4, 8]) == 9
    }

    /**
     * Test add a single then add a list (non-parallel).
     * Works.
     */
    void "Test with calc as spy, add a number and a list of numbers"() {
        setup:
        calc = Spy(Calc)

        expect:
        calc.add(2) == 2
        calc.addList([-5, 4, 8]) == 9
    }

    /**
     * Test add a single then add a list (parallel).
     * Using GPars framework.
     * FAILS. Mix of spy and gpars .eachParallel hangs.
     */
    @Ignore
    void "gpars: Test with calc as spy, add a number and a list of numbers (parallel)"() {
        setup:
        calc = Spy(Calc)

        expect:
        calc.add(2) == 2
        // This will hang. A Spy cannot seem to run .eachParallel?
        calc.addListParallelGpars([-5, 4, 8]) == 9
    }

    /**
     * Test add a single then add a list (parallel).
     * Using Executors framework.
     * FAILS. Mix of spy and executor pool hangs.
     */
    @Ignore
    void "executors: Test with calc as spy, add a number and a list of numbers (parallel)"() {
        setup:
        calc = Spy(Calc)

        expect:
        calc.add(2) == 2
        // This will hang. A Spy cannot seem to run .eachParallel?
        calc.addListParallelExecutors([-5, 4, 8]) == 9
    }

    /**
     * Test add a single then add a list (parallel) with a partial mock
     * so add's will add 2*n.
     * Works.
     */
    void "Test with calc as spy, add a 2x number and a list of 2x numbers"() {
        setup:
        calc = Spy(Calc) {
            add(_) >> { Integer n ->
                calc.res.addAndGet(2 * n)
            }
        }

        expect:
        calc.add(3) == 6
        calc.addList([-5, 4, 8]) == 20
    }

    /**
     * Test add a single then add a list (parallel) with a partial mock
     * so add's will add 2*n.
     * Using GPars framework.
     * FAILS. Mix of spy and gpars .eachParallel hangs.
     */
    @Ignore
    void "gpars: Test with calc as spy, add a 2x number and a list of 2x numbers (parallel)"() {
        setup:
        calc = Spy(Calc) {
            add(_) >> { Integer n ->
                calc.res.addAndGet(2 * n)
            }
        }

        expect:
        calc.add(3) == 6
        // This will hang. A Spy cannot seem to run .eachParallel?
        calc.addListParallelGPars([-5, 4, 8]) == 20
    }

    /**
     * Test add a single then add a list (parallel) with a partial mock
     * so add's will add 2*n.
     * Using Executors framework.
     * FAILS. Mix of spy and executor pool hangs.
     */
    @Ignore
    void "executors: Test with calc as spy, add a 2x number and a list of 2x numbers (parallel)"() {
        setup:
        calc = Spy(Calc) {
            add(_) >> { Integer n ->
                calc.res.addAndGet(2 * n)
            }
        }

        expect:
        calc.add(3) == 6
        // This will hang. A Spy cannot seem to run .eachParallel?
        calc.addListParallelExecutors([-5, 4, 8]) == 20
    }
}

