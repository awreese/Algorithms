import java.util.Random;
import java.util.Arrays;

public class FindKthSmallest {

    public static final int    M           = 100000;
    public static final int    N           = 100000;
    public static final int    K           = 173;
    public static final int    VALUE_RANGE = Integer.MAX_VALUE;
    public static final Random RAND        = new Random();

    public static void main(String[] args) {
        int[] A = getSortedArray(N);
        int[] B = getSortedArray(M);
        // int[] A = new int[]{1,3,5,7,9,11,13,15,17,19};
        // int[] B = new int[]{2,4,6,8,10,12,14,16,18,20};

        System.out.println("M:\t\t" + M);
        System.out.println("N:\t\t" + N);
        System.out.println("K:\t\t" + K);
        System.out.println();

        long start = System.currentTimeMillis();
        int res_1 = findKthSmallestO_nLogn(A, B, K);
        long duration_1 = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        int res_2 = findKthSmallestO_n(A, B, K);
        long duration_2 = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        int res_3 = findKthSmallestO_logn(A, B, K);
        long duration_3 = System.currentTimeMillis() - start;

        System.out.printf("O(nlog_n): %d (%dms)\n", res_1, duration_1);
        System.out.printf("O(n):      %d (%dms)\n", res_2, duration_2);
        System.out.printf("O(log_n):  %d (%dms)\n", res_3, duration_3);
        findProblem();
    }

    // runs until it finds a problem or for 10 seconds
    public static void findProblem() {
        long timeOut = 10 * 1000;
        System.out.println("RUNNING UNTIL MISMATCH...");
        System.out.println();
        int res1, res2, res3;
        int[] A, B;
        long start = System.currentTimeMillis();
        int count = 0;
        int K_value = 1;
        do {
            count++;
            A = getSortedArray(N);
            B = getSortedArray(M);
            res1 = findKthSmallestO_nLogn(A, B, K_value);
            res2 = findKthSmallestO_n(A, B, K_value);
            try {
                res3 = findKthSmallestO_logn(A, B, K_value);
            } catch (StackOverflowError e) {
                System.out.println("Overflow on iteration " + count);
                return;
            }
            if (System.currentTimeMillis() - start > timeOut) {
                System.out.printf("%d ms Timeout after iteration %d, K=%d\n",
                        timeOut, count, K_value);
                return;
            }

            K_value++;

        } while (res1 == res2 && res2 == res3);
        System.out.println("MISMATCH FOUND ON ATTEMPT " + count);
        System.out.println("K:\t\t" + K_value);
        System.out.println("A:\t\t" + Arrays.toString(A));
        System.out.println("B:\t\t" + Arrays.toString(B));

        System.out.printf("O(nlogn): %d\n", res1);
        System.out.printf("O(n):     %d\n", res2);
        System.out.printf("O(logn):  %d\n", res3);
    }

    // returns a sorted array of length n
    public static int[] getSortedArray(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = RAND.nextInt(VALUE_RANGE) - VALUE_RANGE / 2;
        }
        Arrays.sort(arr);
        return arr;
    }

    /**
     * Finds and returns the K'th smallest value from two sorted arrays using
     * Java's O(n log n) array sorting algorithm.
     * 
     * @param A a sorted array of length m
     * @param B a sorted array of length n
     * @param k k'th smallest value to search for in A and B
     * @return the k'th smallest value found if it exists, -1 if not found
     */
    public static int findKthSmallestO_nLogn(int[] A, int[] B, int k) {
        int m = A.length;
        int n = B.length;

        // if k'th largest is greater than m+n or less than 1, return not found
        if (k > m + n || k < 1) {
            return -1;
        }

        return sort(A, B)[k - 1]; // <- O(nlogn)
    }

    private static int[] sort(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;

        if (m == 0) {
            return B;
        }
        if (n == 0) {
            return A;
        }

        int[] arr = new int[m + n];
        System.arraycopy(A, 0, arr, 0, m);
        System.arraycopy(B, 0, arr, m, n);
        Arrays.sort(arr); // <-- O(n log n)
        return arr;
    }

    /**
     * Finds and returns the K'th smallest value from two sorted arrays using an
     * O(n) array merging algorithm. Both arrays MUST already be sorted!
     * 
     * @param A a sorted array of length m
     * @param B a sorted array of length n
     * @param k k'th smallest value to search for in A and B
     * @return the k'th smallest value found if it exists, -1 if not found
     */
    public static int findKthSmallestO_n(int[] A, int[] B, int k) {
        int m = A.length;
        int n = B.length;

        // if k'th largest is greater than m+n or less than 1, return not found
        if (k > m + n || k < 1) {
            return -1;
        }

        return merge(A, B)[k - 1];
    }

    private static int[] merge(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;

        if (m == 0) {
            return B;
        }
        if (n == 0) {
            return A;
        }

        int[] arr = new int[m + n];
        int i_A = 0;
        int i_B = 0;

        while (i_A < m && i_B < n) {
            if (A[i_A] < B[i_B]) {
                arr[i_A + i_B] = A[i_A++]; // pull from A
            } else {
                arr[i_A + i_B] = B[i_B++]; // pull from B
            }
        }

        if (i_A == m) {
            // A finished? pull rest of B
            while (i_B < n) {
                arr[i_A + i_B] = B[i_B++];
            }
        } else {
            // B must be done, pull rest of A
            while (i_A < m) {
                arr[i_A + i_B] = A[i_A++];
            }
        }
        return arr;
    }

    /**
     * Finds and returns the K'th smallest value from two sorted arrays using an
     * O(logn) array recursive binary search algorithm. Both arrays MUST already
     * be sorted!
     * 
     * @param A a sorted array of length m
     * @param B a sorted array of length n
     * @param k k'th smallest value to search for in A and B
     * @return the k'th smallest value found if it exists, -1 if not found
     */
    public static int findKthSmallestO_logn(int[] A, int[] B, int k) {
        // call recursive method here
        return findKthSmallestRecursive(A, B, k);
    }

    private static int findKthSmallestRecursive(int[] A, int[] B, int k) {
        int m = A.length;
        int n = B.length;

        // if k'th largest is greater than m+n, return not found
        if (k > m + n || k < 1) {
            return -1;
        }

        // if m = 0
        if (m == 0) {
            return B[k - 1];
        }

        // if n = 0
        if (n == 0) {
            return A[k - 1];
        }

        // mid-index of arrays
        int i = m / 2;
        int j = n / 2;

        if (A[i] > B[j]) {
            // PATH 1
            if (k - 1 > i + j) {
                // PATH 1.1 - cut B low
                return findKthSmallestRecursive(A,
                        Arrays.copyOfRange(B, j + 1, n), k - 1 - j);
            } else {
                // PATH 1.2 - cut A high
                return findKthSmallestRecursive(Arrays.copyOfRange(A, 0, i), B,
                        k);
            }
        } else {
            // PATH 2
            if (k - 1 > i + j) {
                // PATH 2.1 - cut A low
                return findKthSmallestRecursive(Arrays.copyOfRange(A, i + 1, m),
                        B, k - 1 - i);
            } else {
                // PATH 2.2 - cut B high
                return findKthSmallestRecursive(A, Arrays.copyOfRange(B, 0, j),
                        k);
            }
        }
    }

}