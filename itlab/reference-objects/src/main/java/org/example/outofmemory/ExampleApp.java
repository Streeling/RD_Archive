package org.example.outofmemory;

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
        System.out.println("The PhantomReference#get() of " + phantomReference + " (always) returns " + phantomReference.get() + " ");

        foo = null;
// If I'll uncomment next line there will be an java.lang.OutOfMemoryError exception
//        foo = new Foo();

        for (int i = 0, j = i + 1; i < 2; i++,j++) {
            System.out.println("Run the garbage collector: cycle #" + j);
            System.gc();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Reference reference = referenceQueue.poll();
            if (reference != null) {
                System.out.println("Found a reference " + reference + " to a collected object at GC : cycle #" + j);
            } else {
                foo = new Foo();
            }
        }
    }
}
