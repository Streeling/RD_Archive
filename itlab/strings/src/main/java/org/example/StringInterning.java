public class StringInterning {

    public static void main(String[] args) {
        String foo = new String("foobar");
        String bar = "foobar";
        String baz = "foobar";

        System.out.println("foo.equals(bar) " + foo.equals(bar));
        System.out.println("bar.equals(baz) " + bar.equals(baz));
        System.out.println("foo == bar " + (foo == bar));
        System.out.println("bar == baz " + (bar == baz));
        System.out.println("foo.intern() == bar " + (foo.intern() == bar));
    }
}
