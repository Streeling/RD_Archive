import org.apache.commons.lang3.StringUtils;

/**
 * Created by rdumbraveanu on 3/27/2017.
 */
public class Truncation {
  public static void main(String[] args) {
    System.out.println(StringUtils.abbreviate("Long Title", 9));
    System.out.println(StringUtils.abbreviate("Long Title", 10));
    System.out.println(StringUtils.abbreviate("Long Title", 4));
    //System.out.println(StringUtils.abbreviate("Long Title", 3));

    System.out.println();

    System.out.println(StringUtils.abbreviate("Another Long Title", 8, 10));
    System.out.println(StringUtils.abbreviate("Another Long Title", 17, 15));

    System.out.println();

    System.out.println(StringUtils.abbreviateMiddle("Long Title", "...", 9));
    System.out.println(StringUtils.abbreviateMiddle("Long Title", "***", 9));
    System.out.println(StringUtils.abbreviateMiddle("Long Title", "*", 8));
  }
}
