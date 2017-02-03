import groovyx.gpars.GParsPool

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

import java.util.concurrent.atomic.AtomicInteger

class Calc {
    AtomicInteger res

    /**
     * Create a calculator with a 0 initial value.
     */
    public Calc() {
        res = new AtomicInteger(0)
    }

    /**
     * Add single item to list. Also used by addList*() methods.
     * THIS needs to be thread safe.
     * @param n the item to add to list
     * @return the current value
     */
    Integer add(Integer n) {
        return res.addAndGet(n)
    }

    /**
     * Add to list, non parallel.
     */
    Integer addList(List<Integer> nList) {
        nList.each { Integer n ->
            add(n)
        }
        return res.get()
    }

    /**
     * Add to list, with gpars parallel.
     */
    Integer addListParallelGpars(List<Integer> nList) {
        GParsPool.withPool(5) {
            nList.eachParallel { Integer n ->
                add(n)
            }
        }
        return res.get()
    }

    Integer addListParallelExecutors(List<Integer> nList) {
        def threadPool = Executors.newFixedThreadPool(1)
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
