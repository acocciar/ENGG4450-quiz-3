package com.jwetherell.algorithms.mathematics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Bartlomiej Drozd <mail@bartlomiejdrozd.pl>
 */
public class Primes {

    /**
     * Returns the prime factorization of a number as a map of prime factors and their multiplicities.
     * @param number The number to factorize
     * @return A map of prime factors and their multiplicities
     */
    public static final Map<Long, Long> getPrimeFactorization(long number) {
        Map<Long, Long> map = new HashMap<>();
        long n = number;
        int c = 0;
        for (long i = 2; i * i <= number; i++) {
            c = 0;
            while (n % i == 0) {
                n = n / i;
                c++;
            }
            if (c > 0) {
                map.put(i, (long) c);
            }
        }
        if (n > 1) {
            map.put(n, 1L);
        }
        return map;
    }

    /**
     * Checks if a number is prime.
     * @param number The number to check
     * @return true if the number is prime, false otherwise
     */
    public static final boolean isPrime(long number) {
        if (number <= 1) {
            return false; // 1 is not prime
        }
        if (number < 4) {
            return true; // 2 and 3 are prime
        }
        if (number % 2 == 0) {
            return false; // even numbers greater than 2 are not prime
        }
        if (number < 9) {
            return true; // we have already excluded 4, 6, 8
        }
        if (number % 3 == 0) {
            return false; // divisible by 3
        }
        long r = (long) Math.sqrt(number);
        int f = 5;
        while (f <= r) {
            if (number % f == 0) {
                return false;
            }
            if (number % (f + 2) == 0) {
                return false;
            }
            f += 6;
        }
        return true;
    }

    private static boolean[] sieve = null;

    /**
     * Uses the Sieve of Eratosthenes to find all primes up to a given number.
     * @param number The number up to which primes should be found
     * @return true if the number is prime, false otherwise
     */
    public static final boolean sieveOfEratosthenes(int number) {
        if (number <= 1) {
            return false;
        }
        if (sieve == null || number >= sieve.length) {
            sieve = new boolean[number + 1];
            Arrays.fill(sieve, true); // Initialize all numbers as prime
            sieve[0] = sieve[1] = false; // 0 and 1 are not prime

            for (int i = 2; i * i <= number; i++) {
                if (sieve[i]) {
                    for (int j = i * i; j <= number; j += i) {
                        sieve[j] = false;
                    }
                }
            }
        }
        return sieve[number];
    }

    /**
     * Miller-Rabin primality test (deterministic version for numbers up to 10^18).
     * @param number The number to test for primality
     * @return true if the number is prime, false otherwise
     */
    public static final boolean millerRabinTest(int number) {
        if (number <= 1) {
            return false; // 1 and lower are not prime
        }
        if (number <= 3) {
            return true; // 2 and 3 are prime
        }

        // Write (number-1) as 2^s * d
        int s = 0;
        int d = number - 1;
        while (d % 2 == 0) {
            d /= 2;
            s++;
        }

        List<Integer> witnesses = Arrays.asList(2, 325, 9375, 28178, 450775, 9780504, 1795265022);
        for (int a : witnesses) {
            if (a > number - 2) break;
            int x = modExp(a, d, number);
            if (x == 1 || x == number - 1) continue;

            boolean isLocalPrime = false;
            for (int r = 0; r < s; r++) {
                x = modExp(a, d * (1 << r), number);
                if (x == number - 1) {
                    isLocalPrime = true;
                    break;
                }
            }
            if (!isLocalPrime) {
                return false;
            }
        }
        return true;
    }

    /**
     * Efficiently computes (base^exponent) % modulus using binary exponentiation.
     * @param base The base
     * @param exponent The exponent
     * @param modulus The modulus
     * @return (base^exponent) % modulus
     */
    private static int modExp(int base, int exponent, int modulus) {
        int result = 1;
        base = base % modulus;
        while (exponent > 0) {
            if ((exponent % 2) == 1) {
                result = (result * base) % modulus;
            }
            exponent = exponent >> 1;
            base = (base * base) % modulus;
        }
        return result;
    }
}
