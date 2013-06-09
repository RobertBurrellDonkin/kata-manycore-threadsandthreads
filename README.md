Many Cores Session One: Break Out Exercises
==========================================

The slides for Many Cores Session One _Threads and Threads_ are [here](http://www.slideshare.net/robertburrelldonkin/threads-and-threads). 

These exercises aim to get you thinking about designing systems for many cores. The examples and exercises are in Java but the strategies are universal.

Read on for the exercises...


Exercise One 
------------

Take a look at the code in [`ExerciseOneAppAMinimalCache`](master/src/main/java/name/robertburrelldonkin/kata/manycore/threadsandthreads/ExerciseOneAppAMinimalCache.java) (in `name.robertburrelldonkin.kata.manycore.threadsandthreads`)


1. Take a look at `Cache` and `CacheClient`. What behaviour would you expect when you run the client using:

    1. A single thread...?

    2. Several threads on many cores...?

    3. Several threads on a single core (assuming no on-core parallelism) ...? 
2. Run `ExerciseOneAppAMinimalCache` as an application, either from the command line (after compiling, of course, for example with `mvn clean install`, run `java -classpath target/classes/ name.robertburrelldonkin.kata.manycore.threadsandthreads.ExerciseOneAppAMinimalCache`) or from an IDE. 

    1. Offer an explanation for the behaviour (based on the material in Session One)

    2. If you were able to run the application on a single core (with no on-core parallelism) would you expect similar behaviour...?

    3. Explain why

3. Take a look at `Harness`. Using the [Java 1.6 API](http://docs.oracle.com/javase/6/docs/api/), explain the role in the design played by each `CountDownLatch` instance, by the `AtomicInteger`.

4.  There are many ways in which the design of Cache could be refactored to make the behaviour consistent when run with any number of threads on one core.

    1. Outline three different approaches

    2. For each approach, describe one advantage and one disadvantage 
