import groovyx.gpars.GParsPool
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
     * Add single item to list.
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
    Integer addListParallel(List<Integer> nList) {
        GParsPool.withPool(5) {
            nList.eachParallel { Integer n ->
                add(n)
            }
        }
        return res.get()
    }
}
