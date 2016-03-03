import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Runs a number of algorithms that try to fit files onto disks.
 */
public class Bins {
    public static final String DATA_FILE = "example.txt";

    /**
     * Reads list of integer data from the given input.
     *
     * @param input tied to an input source that contains space separated numbers
     * @return list of the numbers in the order they were read
     */
    public List<Integer> readData (Scanner input) {
        List<Integer> results = new ArrayList<Integer>();
        while (input.hasNext()) {
            results.add(input.nextInt());
        }
        return results;
    }

    
    public void addtoPQ(){
    	
    }
    /**
     * THE MAIN PROGRAM.  
     * @throws IOException 
     */
    public static void main (String args[]) throws IOException {
        Bins b = new Bins();
        Scanner input = new Scanner(Bins.class.getClassLoader().getResourceAsStream(DATA_FILE));
        List<Integer> data = b.readData(input);
        PriorityQueue<Disk> pq = new PriorityQueue<Disk>();
        
        //worst-fit decreasing method
        pq.add(new Disk(0));

        int total = b.countTotal(data);
        
        allocateDisks(data, pq);

        System.out.println("total size = " + total / 1000000.0 + "GB");
        
        b.printTotals(pq, "worst-fit");
        
        //worst-fit decreasing method
        Collections.sort(data, Collections.reverseOrder());
        pq.add(new Disk(0));

        allocateDisks(data, pq);
        
        b.printTotals(pq, "worst-fit decreasing");
    }
    
    public void printTotals(PriorityQueue<Disk> pq, String description){
        PriorityQueue<Disk> copy = new PriorityQueue<Disk>(pq); //makes shallow copy of pq so is not destroyed 
        														//shallow because both still reference same structure in memory, but pointers for copy are destroyed
        														//this copy doesn't actually make new copy of the data itself 
    	System.out.println();
        System.out.println(description + "method");
        System.out.println("number of pq used: " + pq.size());
        while (!copy.isEmpty()) {
            System.out.println(copy.poll());
        }
        System.out.println();    	
    }

	private int countTotal(List<Integer> data) {
		int total = 0;
        for (Integer size : data) {
            total += size;
        }
		return total;
	}
	
	public List<Integer> fitDisksAndPrint(List<Integer> list, Function<List<Integer>, List<Integer>> func){
		List<Integer> transformed = func.apply(list);
		return transformed;
		
	}

	private static void allocateDisks(List<Integer> data, PriorityQueue<Disk> pq) throws IOException {
		int diskId= 1;
        for (Integer size : data) {
            Disk d = pq.peek();
            if (d.freeSpace() >= size) {
                pq.poll();
                d.add(size);
                pq.add(d);
            } else {
                Disk d2 = new Disk(diskId);
                diskId++;
                d2.add(size);
                pq.add(d2);
            }
        }
	}
}
