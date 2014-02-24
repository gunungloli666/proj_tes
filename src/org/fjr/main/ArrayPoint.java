package org.fjr.main;

/*
 *  class ini digunakan sebagai alternatif dari array list
 */

public class ArrayPoint {

	String a; 
	String b = "manyun... "; 
	public int size;
	public boolean hasNext;
	public int[] array;
	int iterasiIndeks ; 
	
	public ArrayPoint() {
		size = 0;
		hasNext = false;
		array = new int[1000];
		iterasiIndeks = 0; 
	}
	
	public void setArray(int index, int value){
		
	}
	
	public void add(int value){
		array[iterasiIndeks] = value; 
		iterasiIndeks++; 
	}
	
	public void  clear(){
		array = new int[500]; 
	}
	
}
