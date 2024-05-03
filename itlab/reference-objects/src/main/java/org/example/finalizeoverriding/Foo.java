package org.example.finalizeoverriding;

/**
 * Created by vundicind on 3/3/17.
 */
public class Foo {
    public Foo() {
        System.out.println("The object is about to be created");
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("The object was collected and will be destroyed at the next garbage collection cycle");
    }
}
