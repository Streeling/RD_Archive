package org.example.finalizeimplies2cycles;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * Created by vundicind on 3/3/17.
 */
public class ExampleApp {
    public static void main(String[] args) {
        Foo foo = new Foo();

        ReferenceQueue<Foo> referenceQueue = new ReferenceQueue<>();
        PhantomReference<Foo> phantomReference = new PhantomReference<>(foo, referenceQueue);
        System.out.println("Thread #" + Thread.currentThread().getId() + ": The PhantomReference#get() of " + phantomReference + " (always) returns " + phantomReference.get() + " ");

        foo = null;

        for (int i = 0, j = i + 1; i < 2; i++,j++) {
            System.out.println("Thread #" + Thread.currentThread().getId() + ": Run the garbage collector: cycle #" + j);
            System.gc();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Reference reference = referenceQueue.poll();
            if (reference != null) {
                System.out.println("Thread #" + Thread.currentThread().getId() + ": Found a reference " + reference + " to a collected object at GC : cycle #" + j);
            } else {
                System.out.println("Thread #" + Thread.currentThread().getId() + ": No references to a collected object found at GC : cycle #" + j);
            }
        }
    }
}
