package org.example.objectresurrection;

/**
 * Created by vundicind on 3/3/17.
 */
public class ExampleApp {
    public static void main(String[] args) {
        Foo foo = new Foo();

        foo = null;

        for (int i=0; i < 2; i++) {
            System.gc();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Foo.instance.saySomething();
        }
    }
}
