import groovyx.gpars.GParsPool

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

import java.util.concurrent.atomic.AtomicInteger

/**
 * Simple demonstration class to keep a running sum.
 */
class Calc {
    AtomicInteger res

    /**
     * Create a calculator with a 0 initial value.
     */
    public Calc() {
        res = new AtomicInteger(0)
    }

    /**
     * Add single number to the running sum. Also used by addList*() methods.
     * THIS method must be thread safe.
     * @param n the number to add to add to the running sum
     * @return the running sum
     */
    Integer add(Integer n) {
        return res.addAndGet(n)
    }

    /**
     * Add list of numbers to running sum, non-parallel.
     * @param nList the list of numbers to add to the running sum
     * @return the running sum
     */
    Integer addList(List<Integer> nList) {
        nList.each { Integer n ->
            add(n)
        }
        return res.get()
    }

    /**
     * Add list of numbers to running sum, with gpars thread pool.
     * @param nList the list of numbers to add to the running sum
     * @return the running sum
     */
    Integer addListParallelGpars(List<Integer> nList) {
        GParsPool.withPool(5) {
            nList.eachParallel { Integer n ->
                add(n)
            }
        }
        return res.get()
    }

    /**
     * Add list of numbers to running sum, with executors thread pool.
     * @param nList the list of numbers to add to the running sum
     * @return the running sum
     */
    Integer addListParallelExecutors(List<Integer> nList) {
        ExecutorService threadPool = Executors.newFixedThreadPool(5)
        try {
            List<Future> futures = nList.collect { Integer n ->
                threadPool.submit({
                    add(n)
                } as Callable)
            }
            // Ensure execution of all tasks.
            futures.each { it.get() }
            return res.get()
        } finally {
            threadPool.shutdown()
        }
    }
}
