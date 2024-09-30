import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ConcurrentMergeSort {

    // Method to merge sorted subarrays
    private static void merge(int[] a, int low, int mid, int high) {
        int[] temp = new int[high - low + 1];
        int i = low, j = mid + 1, k = 0;

        while (i <= mid && j <= high) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = a[i++];
        }

        while (j <= high) {
            temp[k++] = a[j++];
        }

        System.arraycopy(temp, 0, a, low, temp.length);
    }

    // RecursiveAction for fork/join framework
    static class SortTask extends RecursiveAction {
        private final int[] a;
        private final int low, high;

        SortTask(int[] a, int low, int high) {
            this.a = a;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (high - low <= 5) {
                Arrays.sort(a, low, high + 1);
            } else {
                int mid = low + (high - low) / 2;
                invokeAll(new SortTask(a, low, mid), new SortTask(a, mid + 1, high));
                merge(a, low, mid, high);
            }
        }
    }

    // Method to check if array is sorted
    private static boolean isSorted(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                return false;
            }
        }
        return true;
    }

    // Method to fill array with random numbers
    private static void fillData(int[] a) {
        Random rand = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = rand.nextInt();
        }
    }

    public static void main(String[] args) {
        int length = 128;
        int[] a = new int[length];
        fillData(a);

        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new SortTask(a, 0, a.length - 1));

        if (isSorted(a)) {
            System.out.println("Sorting Done Successfully");
        } else {
            System.out.println("Sorting Not Done");
        }
    }
}
