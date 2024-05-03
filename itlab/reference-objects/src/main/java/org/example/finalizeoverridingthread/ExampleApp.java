package org.example.finalizeoverridingthread;

/**
 * Created by vundicind on 3/3/17.
 */
public class ExampleApp {
    public static void main(String[] args) {
        Foo foo = new Foo();

        foo = null;

        System.out.println("Thread #" + Thread.currentThread().getId() + ": Run the garbage collector");
        System.gc();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
