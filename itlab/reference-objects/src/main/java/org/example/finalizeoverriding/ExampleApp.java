package org.example.finalizeoverriding;

/**
 * Created by vundicind on 3/3/17.
 */
public class ExampleApp {
    public static void main(String[] args) {
        Foo foo = new Foo();

        foo = null;

        System.gc();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
