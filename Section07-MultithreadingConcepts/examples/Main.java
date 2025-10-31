package examples;

public class Main {
    /**
     * Traverse the array arr[] from the end and find the index of last zero in the arr[], store it in a variable say end.
     * Now, start traversing from end-1 to 0.
     * While traversing, if the element in the array arr[] is not equal to zero, swap it with arr[end].
     * After swapping, decrement the value of end.
     * Repeat the above steps till we have traversed all the elements from end to 0.
     * @param args
     */
    public static void main(String[] args)
    {
        int[] arr = { 1, 0, 2, 0, 3, 0 };

        // finding length of the array
        int n = arr.length;

        // find the index of last zero
        int end = -1;
        for (int i = n - 1; i >= 0; i--) {
            if (arr[i] == 0) {
                end = i;
                break;
            }
        }

        // Modifying the array by traversing from end-1 to 0
        for (int i = end - 1; i >= 0; i--) {
            if (arr[i]
                != 0) { // if element is a non-zero element,
                        // swap it with arr[end]
                int temp = arr[i];
                arr[i] = arr[end];
                arr[end] = temp;
                end--;
            }
        }

        // printing the array after pushing all zeros to the
        // front
        for (int x : arr) {
            System.out.print(x + " ");
        }
    }
}

// This code is contributed by Shivam