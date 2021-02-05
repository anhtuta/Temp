package miscellaneous;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class SortAlogrithms {

	public static void swap(int[] A, int x, int y) {
		int temp = A[x];
		A[x] = A[y];
		A[y] = temp;
	}

	////////////// selectionSort: Sort by ascending////////////////////////
	public static void selectionSort(int[] array) {
		for (int i = 0; i < array.length; i++) {
			int min = array[i];
			int index = i;
			for (int j = i + 1; j < array.length; j++) {
				if (array[j] < min) {
					min = array[j];
					index = j;
				}
			}
			// Nếu chỉ số đã thay đổi, ta sẽ hoán vị
			if (index != i)
				swap(array, index, i);
		}
	}

	////////////// insertionSort: Sort by ascending////////////////////////
	public static void insertionSort(int[] a) {
		int N = a.length;
		int i, j;
		for (i = 1; i < N; i++) {
			for (j = 0; j < i; j++)
				if (a[i] < a[j]) {
					// dịch chuyển a[i] về trước a[j], chú ý i đang lớn hơn j:
					int temp = a[i];
					for (int k = i; k > j; k--) {
						a[k] = a[k - 1];
					}
					a[j] = temp;
				}
		}
	}

	/////////////// shell sort://///////////////////////////
	public static void shellSort(int[] a) { // áp dụng cho n>701
		int n = a.length;
		int[] gaps = { 701, 301, 132, 57, 23, 10, 4, 1 };

		for (int m = 0; m < gaps.length; m++) {
			int gap = gaps[m];

//            # Do a gapped insertion sort for this gap size.
//            # The first gap elements a[0..gap-1] are already in gapped order
//            # keep adding one more element until the entire array is gap sorted
			for (int i = gap; i < n; i += 1) {
				int temp = a[i];
				int j = i;

				for (j = i; j >= gap && a[j - gap] > temp; j -= gap) {
					a[j] = a[j - gap];
				}

				a[j] = temp;
			}
		}
	}

	////////////// bubbleSort: Sort by descending////////////////////////
	public static void bubbleSort(int[] a) {
		int i, j;
		int N = a.length;
		for (i = 0; i < N; i++) {
			for (j = i + 1; j < N; j++) {
				if (a[i] < a[j])
					swap(a, i, j);
			}
		}
	}

	////////////// mergeSort: Sort by ascending////////////////////////
	public static void mergeSort(int[] a) {
		int N = a.length;
		if (N < 2)
			return;
		merge(a, 0, N - 1);
	}

	private static void merge(int[] a, int L, int R) { // L=left, R=right
		if (L >= R)
			return;
		int n = (L + R) / 2;

		merge(a, L, n);
		merge(a, n + 1, R);
		mergeArrays(a, L, n, R);
	}

	private static void mergeArrays(int[] a, int L, int n, int R) {
		int i = L, j = n + 1, k = 0;
		int bleng = R - L + 1;
		int[] b = new int[bleng];
		while ((i <= n) && (j <= R)) {
			if (a[i] < a[j]) {
				b[k] = a[i];
				i++;
				k++;
			} else {
				b[k] = a[j];
				j++;
				k++;
			}
		}

		while (i <= n) {
			b[k] = a[i];
			i++;
			k++;
		}

		while (j <= R) {
			b[k] = a[j];
			j++;
			k++;
		}

		// now a = b:
		i = L;
		for (k = 0; k < bleng; k++) {
			a[i] = b[k];
			i++;
		}
	}

	private static int partition(int arr[], int L, int R) {
		// Chọn 1 số ở giữa dãy làm pivot, sau đó chuyển pivot về cuối dãy
		// Nếu chọn số đầu/cuối là pivot, thì trong trường hợp dãy đã sắp xếp rồi, sẽ bị
		// lỗi StackOverFlow, do có nhiều duplicate: https://stackoverflow.com/a/33887098/7688028.
		// Còn nữa, với pivot là đầu/cuối, và dãy đã được sắp xếp, thì thời gian sắp xếp
		// lâu hơn rất nhiều mergesort/quicksort (có khi gấp 10 lần) (Đã test thử với n = 10000)
		swap(arr, (L+R)/2, R);

		int pivot = arr[R];
		int i = (L - 1); // index of smaller element
		for (int j = L; j < R; j++) {
			// If current element is smaller than the pivot
			if (arr[j] <= pivot) {
				i++;
				swap(arr, i, j);
			}
		}

		// swap arr[i+1] and arr[R] (or pivot)
		swap(arr, i + 1, R);

		return i + 1;
	}

	private static void quickSort(int[] arr, int L, int R) {
		if (L < R) {
			// pi is partitioning index, arr[pi] is now at right place
			int pi = partition(arr, L, R);

			// Recursively sort elements before
			// partition and after partition
			quickSort(arr, L, pi - 1);
			quickSort(arr, pi + 1, R);
		}
	}

	public static void quickSort(int[] arr) {
		int N = arr.length;
		quickSort(arr, 0, N - 1);
	}

	///////////// heap sort//////////////////////
	// Hoán vị nút cha thứ i phải lớn hơn nút con(vun đống)
	private static void heapify(int A[], int n, int i) {
		int Left = 2 * (i + 1) - 1;
		int Right = 2 * (i + 1);
		int Largest;
		if (Left < n && A[Left] > A[i]) {
			Largest = Left;
		} else {
			Largest = i;
		}
		if (Right < n && A[Right] > A[Largest]) {
			Largest = Right;
		}
		if (i != Largest) {
			swap(A, i, Largest);
			heapify(A, n, Largest);
		}
	}

	// xây dựng Heap sao cho mỗi nút cha luôn lớn hơn nút con trên cây (tạo cây)
	private static void buildHeap(int A[], int n) {
		for (int i = n / 2 - 1; i >= 0; i--) {
			heapify(A, n, i);
		}
	}

	// heap-sort
	public static void heapSort(int A[]) {
		int n = A.length;
		buildHeap(A, n);
		for (int i = n - 1; i > 0; i--) { // tại sao ko phải là i>=0 nhỉ?
			swap(A, 0, i);
			heapify(A, i, 0); // vun đống cho i phần tử đầu tiên. ban đầu i=n-1, tức là vun đống cho cả dãy.
								// sau mỗi lần vun đống phần tử cuối cùng đã đc sắp xếp nên ta loại nó khỏi cây
								// và vun đống phần còn lại. cứ như vậy đến hết.
		}
	}

	public static void genRandomArray(int length) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("SortAlogrithms_input.txt"));
			bufferedWriter.write(length + "");
			bufferedWriter.newLine();
			Random rd = new Random();
			int bound = (int) (length / 1.2);

			for (int i = 0; i < length; i++) {
				bufferedWriter.write(rd.nextInt(bound) + " ");
			}
			bufferedWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void copyArray(int[] from, int[] to) {
		int len = from.length;
		for (int i = 0; i < len; i++) {
			to[i] = from[i];
		}
	}

	public static void reverseArray(int[] arr) {

	}

	public static void main(String[] args) throws FileNotFoundException {

//		genRandomArray(100000);

		System.setIn(new FileInputStream("SortAlogrithms_input.txt"));
		Scanner sc = new Scanner(System.in);

		int[] arr1, arr2;
		int temp;
		int len = sc.nextInt();
		arr1 = new int[len];
		arr2 = new int[len];

		for (int i = 0; i < len; i++) {
			temp = sc.nextInt();
			arr1[i] = temp;
		}
		sc.close();

		System.out.println("\nRandom with few duplicates:");

		copyArray(arr1, arr2);
		long t1 = System.currentTimeMillis();
		SortAlogrithms.selectionSort(arr2);
		long t2 = System.currentTimeMillis() - t1;
		System.out.println("Selection sort: " + t2 + "ms");

		copyArray(arr1, arr2);
		t1 = System.currentTimeMillis();
		SortAlogrithms.insertionSort(arr2);
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Insertion sort: " + t2 + "ms");

		copyArray(arr1, arr2);
		t1 = System.currentTimeMillis();
		SortAlogrithms.bubbleSort(arr2);
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Bubble sort: " + t2 + "ms");

		copyArray(arr1, arr2);
		t1 = System.currentTimeMillis();
		SortAlogrithms.mergeSort(arr2);
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Merge sort: " + t2 + "ms");

		copyArray(arr1, arr2);
		t1 = System.currentTimeMillis();
		SortAlogrithms.quickSort(arr2);
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Quick sort: " + t2 + "ms");

		copyArray(arr1, arr2);
		t1 = System.currentTimeMillis();
		SortAlogrithms.heapSort(arr2);
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Heap sort: " + t2 + "ms");

		System.out.println("\nAlready sorted:");

		t1 = System.currentTimeMillis();
		SortAlogrithms.selectionSort(arr2);
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Selection sort: " + t2 + "ms");

		t1 = System.currentTimeMillis();
		SortAlogrithms.insertionSort(arr2);
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Insertion sort: " + t2 + "ms");

		t1 = System.currentTimeMillis();
		SortAlogrithms.bubbleSort(arr2);
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Bubble sort: " + t2 + "ms");

		t1 = System.currentTimeMillis();
		SortAlogrithms.mergeSort(arr2);
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Merge sort: " + t2 + "ms");

		t1 = System.currentTimeMillis();
		SortAlogrithms.quickSort(arr2);
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Quick sort: " + t2 + "ms");

		t1 = System.currentTimeMillis();
		SortAlogrithms.heapSort(arr2);
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Heap sort: " + t2 + "ms");

	}

}
