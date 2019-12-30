package utils;

// one of the slowest sorting algorithms
// randomly permutates array until its sorted
// average case time complexity: O(n * n!)
public class BogoSort {
    public static int[] sort(int[] array) {
        while(!isSorted(array))
            shuffle(array);
        return array;
    }

    private static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++)
            if (array[i] < array[i - 1])
                return false;
        return true;
    }

    private static int[] swap(int[] array, int i, int j)
    {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        return array;
    }

    private static int[] shuffle(int[] array) {
        for (int i = 1; i <= array.length - 1; i++)
            swap(array, i, (int)(Math.random() * i));
        return array;
    }
}
