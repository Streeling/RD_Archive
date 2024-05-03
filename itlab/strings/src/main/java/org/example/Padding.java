package org.example;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by vundicind on 3/30/17.
 */
public class Padding {
    public static void main(String[] args) {
        System.out.println(Strings.padEnd("Chaper 1. Title 1", 30, '.') + " p1");
        System.out.println(Strings.padEnd("Chaper 2. Title 2", 30, '.') + " p34");

        System.out.println("January " + Strings.padStart("1", 2, '0'));

        System.out.println(StringUtils.rightPad("Chaper 1. Title 1", 30, '.') + " p1");
        System.out.println(StringUtils.rightPad("Chaper 2. Title 2", 30, '.') + " p34");

        System.out.println(StringUtils.rightPad("Chaper 1. Title 1", 30, "._") + " p1");
        System.out.println(StringUtils.rightPad("Chaper 2. Title 2", 30, "._") + " p34");

        System.out.println(StringUtils.rightPad("Chaper 1. Title 1", 30) + " p1");
        System.out.println(StringUtils.rightPad("Chaper 2. Title 2", 30) + " p34");

        System.out.println("January " + StringUtils.leftPad("1", 2, '0'));
        System.out.println("January " + StringUtils.leftPad("1", 2, "_."));

        System.out.println(StringUtils.repeat('.', 30));
        System.out.println(StringUtils.repeat("abc", 30));
        System.out.println(StringUtils.repeat("abc", ".", 30));

        System.out.println(StringUtils.center("title", 30));
        System.out.println(StringUtils.center("title", 30, '_'));
        System.out.println(StringUtils.center("title", 30, "-=-"));
    }
}
