package org.example.phantom;

/**
 * Created by vundicind on 3/3/17.
 */
public class Foo {
    public Foo() {
        System.out.println("Thread #" + Thread.currentThread().getId() + ": The object is about to be created");
    }
}
