package org.example.finalizeimplies2cycles;

/**
 * Created by vundicind on 3/3/17.
 */
public class Foo {
    public Foo() {
        System.out.println("Thread #" + Thread.currentThread().getId() + ": The object is about to be created");
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Thread #" + Thread.currentThread().getId() + ": The object was collected and will be destroyed at the next garbage collection cycle");
    }

}
