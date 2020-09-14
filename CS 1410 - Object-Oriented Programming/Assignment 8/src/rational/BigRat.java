package rational;

import java.math.BigInteger;

/**
 * Provides rational number (fraction) objects. The rational arithmetic provided by Rational objects is subject to
 * integer overflow if the numerator and/or denominator becomes too large.
 */
public class BigRat
{
    /**
     * The numerator of this Rat. The gcd of |num| and den is always 1.
     */
    private BigInteger num;

    /**
     * The denominator of this Rat. den must be positive.
     */
    private BigInteger den;

    /**
     * Used for exception checking
     */
    private long exceptionCheck;

    /**
     * Creates the rational number 0
     */
    public BigRat ()
    {
        this(0);
    }

    /**
     * Creates the rational number n.
     * 
     * DO NOT MODIFY THE HEADER OF THIS CONSTRUCTOR. IT SHOULD TAKE A SINGLE LONG AS ITS PARAMETER
     */
    public BigRat (long n)
    {
        this(n, 1);
    }

    /**
     * If d is zero, throws an IllegalArgumentException. Otherwise creates the rational number n/d
     * 
     * DO NOT MODIFY THE HEADER OF THIS CONSTRUCTOR. IT SHOULD TAKE TWO LONGS AS ITS PARAMETERS
     */
    public BigRat (long n, long d)
    {
        // Denominator must not be zero
        if (d == 0)
        {
            throw new IllegalArgumentException();
        }

        // Deals with signs
        if (d < 0)
        {
            d = -d;
            n = -n;
        }

        // Deal with lowest terms
        BigInteger numBig = BigInteger.valueOf(n);
        BigInteger denBig = BigInteger.valueOf(d);
        BigInteger g = numBig.gcd(denBig);
        num = numBig.divide(g);
        den = denBig.divide(g);
        System.out.println(num);
        System.out.println(den);
    }

    /**
     * If d is zero, throws an IllegalArgumentException. Otherwise creates the rational number n/d
     */
    public BigRat (BigInteger n, BigInteger d)
    {
        exceptionCheck = d.longValue();
        if (exceptionCheck == 0)
        {
            throw new IllegalArgumentException("Denominator is equal to 0.");
        }
        BigInteger g = n.gcd(d);
        n = n.divide(g);
        d = d.divide(g);
        num = n;
        den = d;

    }

    /**
     * Returns the sum of this and r Rat x = new Rat(5, 3); Rat y = new Rat(1, 5); Rat z = x.add(y); a/b + c/d = (ad +
     * bc) / bd
     */
    public BigRat add (BigRat r)
    {
        BigInteger nBigInt1 = this.num.multiply(r.den);
        BigInteger nBigInt2 = this.den.multiply(r.num);
        BigInteger nBigIntFinal = nBigInt1.add(nBigInt2);
        BigInteger dBigInt = this.den.multiply(r.den);
        num = nBigIntFinal;
        den = dBigInt;
        return new BigRat(nBigIntFinal, dBigInt);
    }

    /**
     * Returns the difference of this and r a/b - c/d = (ad - bc) / bd
     */
    public BigRat sub (BigRat r)
    {
        BigInteger nBigInt1 = this.num.multiply(r.den);
        BigInteger nBigInt2 = this.den.multiply(r.num);
        BigInteger nBigIntFinal = nBigInt1.subtract(nBigInt2);
        BigInteger dBigInt = this.den.multiply(r.den);
        num = nBigIntFinal;
        den = dBigInt;
        return new BigRat(nBigIntFinal, dBigInt);
    }

    /**
     * Returns the product of this and r Rat x = new Rat(5, 3); Rat y = new Rat(1, 5); Rat z = x.mul(y); a/b * c/d =
     * ac/bd
     */
    public BigRat mul (BigRat r)
    {
        return new BigRat(this.num.multiply(r.num), this.den.multiply(r.den));
    }

    /**
     * If r is zero, throws an IllegalArgumentException. Otherwise, returns the quotient of this and r. a/b / c/d = ad /
     * bc
     */
    public BigRat div (BigRat r)
    {
        exceptionCheck = r.num.longValue();
        if (exceptionCheck == 0)
        {
            throw new IllegalArgumentException("Numerator is equal to 0.");
        }
        else
        {
            return new BigRat(this.num.multiply(r.den), this.den.multiply(r.num));
        }
    }

    /**
     * Returns a negative number if this < r, zero if this = r, a positive number if this > r To compare a/b and c/d,
     * compare ad and bc
     */
    public int compareTo (BigRat r)
    {
        BigInteger diff = (this.num.multiply(r.den)).subtract((this.den.multiply(r.num)));
        exceptionCheck = diff.intValue();
        if (exceptionCheck < 0)
        {
            return -1;
        }
        else if (exceptionCheck > 0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Returns a string version of this in simplest and lowest terms. Examples: 3/4 => "3/4" 6/8 => "3/4" 2/1 => "2" 0/8
     * => "0" 3/-4 => "-3/4"
     */
    public String toString ()
    {
        exceptionCheck = den.intValue();
        if (exceptionCheck < 0) {
            den = den.negate();
            num = num.negate();
        }
        if (exceptionCheck == 1)
        {
            return num + "";
        }
                else
        {
            return num + "/" + den;
        }
    }
}
