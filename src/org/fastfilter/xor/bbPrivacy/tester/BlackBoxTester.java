package org.fastfilter.xor.bbPrivacy.tester;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.fastfilter.xor.XorSimpleNString;
import org.fastfilter.xor.bbPrivacy.BlackBoxFireholProcessor;

public class BlackBoxTester {
	
	private static int[] RANGE = {0, 0, 0}; //{1, 0, 0};
	private static int MAX_A = 255;//223;

	public static void main (String[] args) {

		if (args.length<4) {
			System.out.println("Not enough arguments. Use java BlackBoxTester <iterations> <fingerprintBits> <number_of_files> <file1> <file2> ... <fileN>");
			System.exit(1);
		}
		
		int iterations = 0;
		int fBits = 0;
		int files = 0;
		try {
			iterations = Integer.parseInt(args[0]);
			fBits =  Integer.parseInt(args[1]);
			files =  Integer.parseInt(args[2]);
			if (files<1 || fBits <1 ) {
				System.out.println("The three first parameters must be integers > 0");
				System.exit(1);
			}
		}catch(NumberFormatException nfe) {
			System.out.println("The three first parameters must be integers");
			System.exit(1);
		}
		
		if(args.length<files+3) {
			System.out.println("Not enough arguments. Use java BlackBoxTester <iterations> <fingerprintBits> <number_of_files> <file1> <file2> ... <fileN>");
			System.exit(1);		
		}
		
		Set<String> completeSet = new HashSet<String>();

		
		BlackBoxFireholProcessor bbfp = new BlackBoxFireholProcessor();
		
		for (int i=0; i<files; i++) {
			completeSet.addAll(bbfp.generateSet(args[i+3], true));
		}

		System.out.println("iter;positives;fingerbits;nPosIn24;numSubNet24;totalPos;totalTP;totalFP;porcTP;porcFP");
		
		//System.out.println(completeSet.size());
		for (int i=0; i<iterations;i++) {
			BlackBoxTester.runIteration(i, fBits, completeSet);
			BlackBoxTester.resetRange();
		}

		
	}
	
	private static void runIteration(int iteration, int fBits, Set<String> completeSet) {

		XorSimpleNString xor = XorSimpleNString.construct(completeSet, fBits);
		
		// Validating true positives
		for (String ip : completeSet) {
			if (!xor.mayContain(ip)) {
				System.out.println("Validation failed");
			}
		}
		
		Hashtable<Integer, Integer> rangesPerI = new Hashtable<Integer, Integer>();
		Hashtable<Integer, Integer> positivesPerI = new Hashtable<Integer, Integer>();
		String ipRange = "";
		
		while ((ipRange = BlackBoxTester.nextRange()) != null) {
			
			ArrayList<String> localList = new ArrayList<String>();
			int fps = 0;
			for (int i=0; i<256; i++) {
				String ip = ipRange + "." + i;
				if(xor.mayContain(ip)) {
					if(!completeSet.contains(ip)) {
						localList.add(ip);
					}
					fps++;
				}
			}
			
			Integer iPos = rangesPerI.get(fps);
			iPos = (iPos==null)?1:iPos+1;
			rangesPerI.put(fps, iPos);
			
			Integer pPos = positivesPerI.get(fps);
			pPos = (pPos==null)?localList.size():pPos+localList.size();
			positivesPerI.put(fps, pPos);
		}

		for (Integer i : rangesPerI.keySet()) {
			int ranges24 = rangesPerI.get(i);
			int totalFPos = positivesPerI.get(i);
			int totalPos = ranges24*i;
			int totalTPos = totalPos - totalFPos;
			System.out.println(iteration+";"+completeSet.size()+";"+fBits+";"+ i +";"+ rangesPerI.get(i) + ";" + 
							totalPos + ";" + totalTPos + ";" + totalFPos + ";" + ((totalPos==0)?0:(totalTPos)/(double)totalPos) + ";" + 
							 ((totalPos==0)?0:totalFPos/(double)totalPos));
		}
		System.out.flush();
	}
	
	private static String nextRange() {
		int i = RANGE[2]++;
		if (i == 256) {
			RANGE[2] = 1;
			i=0;
			RANGE[1]++;
		}
		if (RANGE[1] == 256) {
			RANGE[1] = 0;
			RANGE[0]++;
		}
		if (RANGE[0] > MAX_A) {
			return null;
		}
		return RANGE[0]+"."+RANGE[1]+"."+i;
	}
	
	private static void resetRange() {
		RANGE [0]= 0;
		RANGE [1]= 0;
		RANGE [2]= 0;
	}
}
