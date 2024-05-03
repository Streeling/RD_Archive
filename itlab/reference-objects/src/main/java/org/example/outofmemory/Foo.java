package org.example.outofmemory;

/**
 * Created by vundicind on 3/3/17.
 */
public class Foo {
    private byte[] array;

    public Foo() {
        System.out.println("The object is about to be created");
        array = new byte[(int)Runtime.getRuntime().totalMemory() / 2 + 1];
    }
}
