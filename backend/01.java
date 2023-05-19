import java.util.*;
import java.io.*;

class Main {
	public static long getMemoryUsage() {
	    return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	public static void main(String[] args) {
		String fileName = "01.txt";

		try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fileReader);
            int t = Integer.parseInt(br.readLine());
            double totalTimeTaken = 0.0;
            double totalMemoryUsed = 0.0;
            int numTestCaseRun = 0;
            int numAccepted = 0;

            for(int i = 0; i < t; i++) {
            	int n1 = Integer.parseInt(br.readLine());
            	int[] nums1 = new int[n1];

            	String[] parts = br.readLine().split(" ");
            	for(int j = 0; j < n1; j++) nums1[j] = Integer.parseInt(parts[j]);

            	int n2 = Integer.parseInt(br.readLine());
            	int[] nums2 = new int[n2];

            	parts = br.readLine().split(" ");
            	for(int j = 0; j < n2; j++) nums2[j] = Integer.parseInt(parts[j]);


             	long startTime = System.nanoTime();
             	long memoryBefore = getMemoryUsage();

             	Solution obj = new Solution();
             	double res = obj.findMedianSortedArrays(nums1, nums2);

             	long memoryAfter = getMemoryUsage();
             	long endTime = System.nanoTime();


             	totalTimeTaken += endTime - startTime;
             	totalMemoryUsed += memoryAfter - memoryBefore;

             	double expectedOutput = DOUBLE.parseDouble(br.readLine());

             	if(Math.abs(expectedOutput - res) < 0.001) numAccepted++;
             	else break;

             	numTestCaseRun++;
            }

            totalTimeTaken /= 1000.0;
            double avgTimeTaken = numTestCaseRun > 0 ? (totalTimeTaken / numTestCaseRun) : 0.0;
            double avgMemoryUsed = numTestCaseRun > 0 ? (totalMemoryUsed / numTestCaseRun) : 0.0;

            System.out.println(numAccepted);
            System.out.println(t);
            System.out.println(avgTimeTaken);
            System.out.println(avgMemoryUsed);

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int n1 = nums1.length;
        int n2 = nums2.length;
        int n = n1 + n2;
        int[] new_arr = new int[n];

        int i=0, j=0, k=0;

        while (i<=n1 && j<=n2) {
            if (i == n1) {
                while(j<n2) new_arr[k++] = nums2[j++];
                break;
            } else if (j == n2) {
                while (i<n1) new_arr[k++] = nums1[i++];
                break;
            }

            if (nums1[i] < nums2[j]) {
                new_arr[k++] = nums1[i++];
            } else {
                new_arr[k++] = nums2[j++];
            }
        }

        if (n%2==0) return (float)(new_arr[n/2-1] + new_arr[n/2])/2;
        else return new_arr[n/2];
    }
}