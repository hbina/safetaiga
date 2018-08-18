package com.akarin.webapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

public class Tools {

    private static final boolean logging = true;
    private static final Logger logger = LoggerFactory.getLogger(Tools.class);

    @Deprecated
    public static void coutln(final String text) {
        if (logging) {
            System.out.println(text);
        }
    }

    @Deprecated
    public static void coutln(final int nextInt) {
        coutln(Integer.toString(nextInt));
    }

    @Deprecated
    private static void coutln(final String text, final boolean bool) {
        if (bool) {
            System.out.println(text);
        }
    }

    @Deprecated
    public static void coutln(final int number, final boolean bool) {
        coutln(Integer.toString(number), bool);
    }

    @Deprecated
    public static void cout(final String text) {
        if (logging) {
            System.out.print(text);
        }
    }

    @Deprecated
    public static void cout(double d) {
        if (logging) {
            System.out.print(d);
        }
    }

    @Deprecated
    public static void cout(final String text, final boolean bool) {
        if (bool) {
            System.out.print(text);
        }
    }

    public static float[][][] getNewAverage(final float[][][] averageArray, final int[][][] newArray,
                                            final int currentCount) {

        final float[][][] resultAverage = new float[averageArray.length][averageArray[0].length][averageArray[0][0].length];
        for (int y = 0; y < averageArray.length; y++) {
            for (int x = 0; x < averageArray[y].length; x++) {
                for (int z = 0; z < averageArray[y][x].length; z++) {
                    resultAverage[y][x][z] = ((averageArray[y][x][z] * currentCount) + newArray[y][x][z])
                            / (currentCount + 1);
                }
            }
        }

        return resultAverage;
    }

    public static String convertTripleArrayToString(final int[][][] array) {
        StringBuilder string = new StringBuilder();
        for (int[][] anArray : array) {
            for (int[] anAnArray : anArray) {
                for (int anAnAnArray : anAnArray) {
                    string.append(anAnAnArray).append(" ");
                }
            }
            string.append(System.lineSeparator());
        }
        return string.toString();
    }

    public static String toSixtyTwoRadix(final int i) {

        // means negative value, so just recursively call this function again
        // but adding negative sign as prefix
        if (i < 0) {
            return "-" + toSixtyTwoRadix(-i - 1);
        }

        // find remainder
        final int quot = i / (26 + 26 + 10);
        final int rem = i % (26 + 26 + 10);

        // if x < 10 i.e. is a number
        if (rem < 10) {
            final char letter = Character.forDigit(rem, 10);
            if (quot == 0) {
                return "" + letter;
            } else {
                return toSixtyTwoRadix(quot) + letter;
            }
        } else if (rem < 36) {
            final char letter = (char) ((int) 'a' + (rem - 10));
            if (quot == 0) {
                return "" + letter;
            } else {
                return toSixtyTwoRadix(quot) + letter;
            }
        } else {
            // if x > 26 i.e. is a lowercase letter
            // if x > (26 + 10) i.e. is a capital letter
            final char letter = (char) ((int) 'A' + (rem - 10 - 26));
            if (quot == 0) {
                return "" + letter;
            } else {
                return toSixtyTwoRadix(quot) + letter;
            }
        }
    }

    public static ArrayList<Integer> convertIntArrayToIntArrayList(final int[] array) {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        for (int anArray : array) {
            arrayList.add(anArray);
        }

        return arrayList;
    }
}
