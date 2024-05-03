# Strings and operations on strings

## The citizens

### Empty string
**Empty string** is the unique string of length zero, i.e. `""`.

### Blank string
Any string consisting only of white spaces is considered **blank**, e.g.

1. `" "`
2. `"\t\n\x0B\f\r"`

### Other strings 

## Operations

### Construction

1. invoking the constructor, e.g.

    String emptyStr = new String();
    String foo = new String("foo");
    String bar = new String("bar");

2. assign a string literal directly, e.g.

    String emptyStr = "";
    String foo = "foo";
    String bar = "bar";

Each time we use `new String(...)` constructor, VM will create a new string object in the heap, but the behavior is slightly different in case of assigning string literals. Class `String` has a private pool of `String` objects and in case of literals it will search the pool for a object with the same character sequence. If it is found the reference will be assigned to variable, if no the object will be created and the new reference will be assigned to variable. This process is called **string interning**, and each object from the pool is called **canonical representation** of the string object. We can intern any string object by invoking its `intern()` method.

See : http://www.journaldev.com/797/what-is-java-string-pool
See : string-interning example

3. special constructors


### Concatenation

by `+`

#### Joining



