/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

package com.thoughtworks.gauge.gradle.util;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Util {
    public static void inheritIO(final InputStream src, final PrintStream dest) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Scanner sc = new Scanner(src);
                    while (sc.hasNextLine()) {
                        dest.println(sc.nextLine());
                    }
                } catch (NullPointerException ignored) {

                }
            }
        }).start();
    }
}
