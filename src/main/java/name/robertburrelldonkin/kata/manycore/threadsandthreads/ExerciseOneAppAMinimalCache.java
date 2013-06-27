/*
 * Copyright 2013 Robert Burrell Donkin http://robertburrelldonkin.name
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.robertburrelldonkin.kata.manycore.threadsandthreads;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h3>A solution for Session One, Exercise One.</h3>
 * <p>
 * Bit of a trick solution this one, but illustrates an important lesson about
 * mutable state.
 * </p>
 * <ul>
 * <li><code>Cache</code> is a toy cache, lazily caching an integer value.</li>
 * <li><code>CacheClient</code> exercises the <code>Cache</code> API.</li>
 * <li><code>Harness</code> prepares and runs <code>CacheClient</code> instances
 * concurrently.</li>
 * </ul>
 * <p>
 * The main method is simple and tucked away at the bottom.
 * </p>
 */
public class ExerciseOneAppAMinimalCache {

    /** A toy cache, lazily caching an integer value */
    static class Cache {

        private final Integer cachedValueWithLazyLoad = new Integer(42);

        Cache() {
        }

        int getValue() throws Exception {
            return cachedValueWithLazyLoad.intValue();
        }

        void flush() {
        }
    }

    /** Exercises the <code>Cache</code> API */
    static class CacheClient {

        final int startingCount;
        final String name;
        final Cache cache;

        CacheClient(final Cache cache, final int startingCount,
                final String name) {
            this.startingCount = startingCount;
            this.name = name;
            this.cache = cache;
        }

        void run() throws Exception {
            int count = startingCount;
            while (count > 0) {
                if (count % 5 == 0) {
                    cache.flush();
                } else {
                    cache.getValue();
                }
                count -= 1;
            }
        }
    }

    /**
     * Prepares and runs <code>CacheClient</code> instances
     * concurrently
     */
    static class Harness {

        final int startingCount;

        Harness(final int startingCount) {
            this.startingCount = startingCount;
        }

        void startThreadsNumbering(final int numberOfThreads) throws Exception {
            final Cache cache = new Cache();
            final CountDownLatch holdUntilAllThreadsAreReady = new CountDownLatch(
                    numberOfThreads);
            final CountDownLatch waitUntilAllThreadsStop = new CountDownLatch(
                    numberOfThreads);
            final AtomicInteger failureCount = new AtomicInteger(0);

            System.out.println("Preparing threads...");
            for (int i = 0; i < numberOfThreads; i++) {
                final int threadNumber = i;
                final String threadName = "Thread " + threadNumber;
                new Thread() {
                    @Override
                    public void run() {
                        System.out.println(threadName + " started running.");
                        try {
                            CacheClient client = new CacheClient(cache,
                                    startingCount, threadName);
                            System.out.println("Holding " + threadName);
                            holdUntilAllThreadsAreReady.await();
                            System.out.println("Running client using "
                                    + threadName);
                            client.run();
                        }
                        catch (Throwable t) {
                            final int numberOfFailuresSoFar = failureCount
                                    .incrementAndGet();
                            System.out.println("Failure number "
                                    + numberOfFailuresSoFar + " ("
                                    + t.getClass().getName() + ":"
                                    + t.getMessage() + ")");

                        }
                        finally {
                            waitUntilAllThreadsStop.countDown();
                            System.out.println(threadName + " finished, "
                                    + waitUntilAllThreadsStop.getCount()
                                    + " remaining.");
                        }
                    }
                }.start();

                holdUntilAllThreadsAreReady.countDown();
            }

            waitUntilAllThreadsStop.await();
            if (failureCount.get() > 0) {
                System.out
                        .println("********************************************");
                System.out.println("FAILURES: " + failureCount.get());
                System.out
                        .println("********************************************");
            } else {
                System.out.println("Completed with no failures.");
            }
        }
    }

    /** Simply runs the harness. Vary the parameters and observe the results. */
    public static void main(String[] args) throws Exception {
        new Harness(1000).startThreadsNumbering(100);
    }

}
