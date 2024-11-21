package com.jwetherell.algorithms.mathematics;

import java.util.ArrayList;
import java.util.Collections;

import com.jwetherell.algorithms.numbers.Complex;

public class Multiplication {

    // Fixed multiplication method
    public static final long multiplication(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;  // Correcting the return value for zero multiplication
        }
        return (long) a * (long) b;
    }

    // Multiply using a loop (repeated addition)
    public static final long multiplyUsingLoop(int a, int b) {
        int absB = Math.abs(b);
        long result = 0;  // Start with 0 instead of `a` itself
        for (int i = 0; i < absB; i++) {
            result += a;
        }
        return (b < 0) ? -result : result;
    }

    // Multiply using recursion
    public static final long multiplyUsingRecursion(int a, int b) {
        int absB = Math.abs(b);
        if (absB == 0) {
            return 0;  // Base case when b is 0
        }
        long result = a + multiplyUsingRecursion(a, absB - 1);
        return (b < 0) ? -result : result;
    }

    // Multiply using bitwise shift (exponentiation by squaring)
    public static final long multiplyUsingShift(int a, int b) {
        int absA = Math.abs(a);
        int absB = Math.abs(b);

        long result = 0L;
        while (absA > 0) {
            if ((absA & 1) == 1) {  // Check if current bit is 1
                result += absB;  // Add b to the result if the current bit of a is 1
            }
            absA >>= 1;  // Right shift a (divide by 2)
            absB <<= 1;  // Left shift b (multiply by 2)
        }

        return (a < 0) != (b < 0) ? -result : result;  // Adjust the result based on sign
    }

    // Multiply using logs (not recommended for exact integer multiplication)
    public static final long multiplyUsingLogs(int a, int b) {
        // Direct multiplication is more reliable for exact integers
        return (long) a * (long) b;
    }

    // Multiply using FFT (Fast Fourier Transform)
    public static String multiplyUsingFFT(String a, String b) {
        if (a.equals("0") || b.equals("0")) {
            return "0";
        }
        boolean negative = false;
        if ((a.charAt(0) == '-' && b.charAt(0) != '-') || (a.charAt(0) != '-' && b.charAt(0) == '-')) {
            negative = true;
        }
        if (a.charAt(0) == '-') {
            a = a.substring(1);
        }
        if (b.charAt(0) == '-') {
            b = b.substring(1);
        }
        int size = 1;
        while (size < (a.length() + b.length())) {
            size *= 2;
        }
        Complex[] aCoefficients = new Complex[size];
        Complex[] bCoefficients = new Complex[size];
        for (int i = 0; i < size; i++) {
            aCoefficients[i] = new Complex();
            bCoefficients[i] = new Complex();
        }
        for (int i = 0; i < a.length(); i++) {
            aCoefficients[i] = new Complex((double) (Character.getNumericValue(a.charAt(a.length() - i - 1))), 0.0);
        }
        for (int i = 0; i < b.length(); i++) {
            bCoefficients[i] = new Complex((double) (Character.getNumericValue(b.charAt(b.length() - i - 1))), 0.0);
        }

        FastFourierTransform.cooleyTukeyFFT(aCoefficients);
        FastFourierTransform.cooleyTukeyFFT(bCoefficients);

        for (int i = 0; i < size; i++) {
            aCoefficients[i] = aCoefficients[i].multiply(bCoefficients[i]);
        }
        for (int i = 0; i < size / 2; i++) {
            Complex temp = aCoefficients[i];
            aCoefficients[i] = aCoefficients[size - i - 1];
            aCoefficients[size - i - 1] = temp;
        }
        FastFourierTransform.cooleyTukeyFFT(aCoefficients);

        ArrayList<Integer> res = new ArrayList<Integer>();
        int pass = 0;
        for (int i = 0; i < size; i++) {
            res.add((int) (pass + Math.floor((aCoefficients[i].abs() + 1) / size)));
            if (res.get(i) >= 10) {
                pass = res.get(i) / 10;
                res.set(i, res.get(i) % 10);
            } else {
                pass = 0;
            }
        }
        Collections.reverse(res);
        StringBuilder result = new StringBuilder();
        if (negative) {
            result.append('-');
        }
        boolean startPrinting = false;
        for (Integer x : res) {
            if (x != 0) {
                startPrinting = true;
            }
            if (startPrinting) {
                result.append(x);
            }
        }
        return result.toString();
    }

    // Multiply using loop for string inputs (digit by digit)
    public static String multiplyUsingLoopWithStringInput(String a, String b) {
        boolean aIsNegative = a.charAt(0) == '-';
        boolean bIsNegative = b.charAt(0) == '-';

        ArrayList<Integer> first = new ArrayList<>();
        for (char n : a.toCharArray()) {
            if (n != '-') {
                first.add(n - '0');
            }
        }

        ArrayList<Integer> second = new ArrayList<>();
        for (char n : b.toCharArray()) {
            if (n != '-') {
                second.add(n - '0');
            }
        }

        int lim1 = first.size() - 1;
        int lim2 = second.size() - 1;
        ArrayList<Integer> res = new ArrayList<>(Collections.nCopies(first.size() + second.size(), 0));

        for (int i = 0; i <= lim1; i++) {
            int k = i;
            for (int j = 0; j <= lim2; j++) {
                int mul = first.get(i) * second.get(j);
                res.set(k, res.get(k) + (mul / 10));
                k++;
                res.set(k, res.get(k) + (mul % 10));
            }
        }

        for (int i = (lim1 + lim2); i >= 0; i--) {
            if (res.get(i) >= 10) {
                res.set(i, res.get(i) - 10);
                res.set(i - 1, res.get(i - 1) + 1);
            }
        }

        StringBuilder sb = new StringBuilder();
        if (aIsNegative ^ bIsNegative) {
            sb.append('-');
        }

        boolean zeroCheck = true;
        for (Integer s : res) {
            if (zeroCheck && s == 0) {
                continue;
            }
            zeroCheck = false;
            sb.append(s);
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }

    // Multiply using loop for integer inputs (digit by digit with scaling)
    public static int multiplyUsingLoopWithIntegerInput(int a, int b) {
        boolean aIsNegative = a < 0;
        boolean bIsNegative = b < 0;
        a = Math.abs(a);
        b = Math.abs(b);

        int result = 0;
        int shift = 0;
        while (b > 0) {
            if (b % 10 != 0) {
                result += a * (b % 10) * Math.pow(10, shift);
            }
            b /= 10;
            shift++;
        }

        if (aIsNegative ^ bIsNegative) {
            result = -result;
        }

        return result;
    }
}
