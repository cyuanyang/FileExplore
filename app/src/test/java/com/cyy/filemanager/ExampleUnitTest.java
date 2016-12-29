package com.cyy.filemanager;

import android.provider.Settings;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void sort(){
        int [] arr = {1,2,34,345,45,6,4,554,65,6,6,5,7,67,6};

        insert(arr);

        for (int i :arr) {
            System.out.print( i+" ");
        }

    }

    void insert(int [] arr){
        for (int i = 1 ; i < arr.length ; i++){
            int val = arr[i];

            int j = i-1;

            while (j>=0 && val<arr[j]){
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = val;
        }
    }
}