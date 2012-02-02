package geonotes.utils;

/**
 * Number utilities.
 *
 * @author Brian Spiegel
 */
public class NumberUtils
{
    /**
    * Get a number with two decimal precision.
    *
    * @param aNumber to format
    * @return a double with 2 decimal precision
    */
    public static double getNumber2DecimalPrecision(double aNumber) {
      int temp = (int)(aNumber * 100);
      return ((double)temp)/100;
    }

    /**
    * Add two numbers and return a number with two decimal precision.
    *
    * @param aNumber to format
    * @return a double with 2 decimal precision
    */
    public static double addNumber2DecimalPrecision(double aNumber1, double aNumber2) {
      int temp1 = (int)(aNumber1 * 100);
      int temp2 = (int)(aNumber2 * 100);
      return ((double)temp1+temp2)/100;
    }

    /**
    * Get a number with 1 decimal precision.
    *
    * @param aNumber to format
    * @return a double with 1 decimal precision
    */
    public static double getNumber1DecimalPrecision(double aNumber) {
      int temp = (int)(aNumber * 10);
      return ((double)temp)/10;
    }
}
