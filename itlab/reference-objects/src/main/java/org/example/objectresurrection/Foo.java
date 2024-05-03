package org.example.objectresurrection;

/**
 * Created by vundicind on 3/3/17.
 */
public class Foo {
    public static Foo instance;

    public Foo() {
        System.out.println("The object is about to be created");
    }

    @Override
    protected void finalize() throws Throwable {
        Foo.instance = this;

        System.out.println("The object was collected but won't be destroyed at the next garbage collection cycle");
    }

    public void saySomething() {
        System.out.println("Hello, world!");
    }
}
