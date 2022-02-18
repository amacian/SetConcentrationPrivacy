package org.fastfilter.xor.bbPrivacy.tester;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.fastfilter.xor.XorSimpleNString;
import org.fastfilter.xor.bbPrivacy.BlackBoxFireholProcessor;

/**
 * Experiment to validate the black box approach with ranges of IPs
 * @author Alfonso
 *
 */
public class BlackBoxTester {
	
	// Initial range, class C 0.0.0
	private static int[] RANGE = {0, 0, 0};
	// Max value of Class A
	private static int MAX_A = 255;

	public static void main (String[] args) {
		// Arguments are:
		// Iterations: number of iterations to run to calculate the mean value
		// Fingerprint bits: number of bits used in the fingerprint for the XOR filter
		// Number of files: number of files to be read. These files include all the IPs to be added to the filter
		// Names of the files.
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
		
		// Check that we have as many names as files have to be read
		if(args.length<files+3) {
			System.out.println("Not enough arguments. Use java BlackBoxTester <iterations> <fingerprintBits> <number_of_files> <file1> <file2> ... <fileN>");
			System.exit(1);		
		}
		
		// Set of elements that will store the IPs to be added to the filter
		Set<String> completeSet = new HashSet<String>();

		// Helper class to read the IPs
		BlackBoxFireholProcessor bbfp = new BlackBoxFireholProcessor();
		
		// Read all the files
		for (int i=0; i<files; i++) {
			completeSet.addAll(bbfp.generateSet(args[i+3], true));
		}

		// Printing header.
		System.out.println("iter;positives;fingerbits;nPosIn24;numSubNet24;totalPos;totalTP;totalFP;porcTP;porcFP");
		
		// Run as many iterations as requested by the user
		for (int i=0; i<iterations;i++) {
			BlackBoxTester.runIteration(i, fBits, completeSet);
			BlackBoxTester.resetRange();
		}

		
	}
	
	/**
	 * Run a specific iteration, building the Xor filter, generating a number of random IPs and checking the number of positives
	 * @param iteration number of iteration (to be printed)
	 * @param fBits number of bits used in the fingerprint for the XOR filter
	 * @param completeSet Set of IPs to be added to the filter
	 */
	private static void runIteration(int iteration, int fBits, Set<String> completeSet) {

		// Build the filter
		XorSimpleNString xor = XorSimpleNString.construct(completeSet, fBits);
		
		// Validating true positives
		for (String ip : completeSet) {
			if (!xor.mayContain(ip)) {
				System.out.println("Validation failed");
			}
		}
		
		// Store the number of ranges that return a particular number of positive IPs when testing against the filter
		Hashtable<Integer, Integer> rangesPerI = new Hashtable<Integer, Integer>();
		// Store the number of FALSE positives for all the ranges that have a number I of positives (True and false positives)
		Hashtable<Integer, Integer> positivesPerI = new Hashtable<Integer, Integer>();
		
		String ipRange = "";
		// Generate the next ip class C
		while ((ipRange = BlackBoxTester.nextRange()) != null) {
			
			// Create a local list of false positive IPs
			ArrayList<String> localList = new ArrayList<String>();
			// The total number of positives (true and false positives) in the range
			int fps = 0;
			// Try from X.X.X.0 to X.X.X.255
			for (int i=0; i<256; i++) {
				String ip = ipRange + "." + i;
				// If it is a positive
				if(xor.mayContain(ip)) {
					// When it is not a true positive, add it to the local list
					if(!completeSet.contains(ip)) {
						localList.add(ip);
					}
					// In any case (true or false positive) increase the counter.
					fps++;
				}
			}
			
			// Increase the number of ranges that have "fps" positives when testing their IPs against the filter
			// If there were not previous ranges for this number of positives, initialize to 1.
			Integer iPos = rangesPerI.get(fps);
			iPos = (iPos==null)?1:iPos+1;
			rangesPerI.put(fps, iPos);

			// Increase the number of false positives for ranges that have "fps" positives when testing their IPs against the filter
			// If there were not previous false positives for this number of positives, initialize to the number detected for this range.
			Integer pPos = positivesPerI.get(fps);
			pPos = (pPos==null)?localList.size():pPos+localList.size();
			positivesPerI.put(fps, pPos);
		}

		// For each set of ranges that have i positives
		for (Integer i : rangesPerI.keySet()) {
			// number of ranges with i positives
			int ranges24 = rangesPerI.get(i);
			// Total number of false positives for all those ranges
			int totalFPos = positivesPerI.get(i);
			// Total number of positives (true and false positives)
			int totalPos = ranges24*i;
			// Number of true positives
			int totalTPos = totalPos - totalFPos;
			// Print the result
			System.out.println(iteration+";"+completeSet.size()+";"+fBits+";"+ i +";"+ rangesPerI.get(i) + ";" + 
							totalPos + ";" + totalTPos + ";" + totalFPos + ";" + ((totalPos==0)?0:(totalTPos)/(double)totalPos) + ";" + 
							 ((totalPos==0)?0:totalFPos/(double)totalPos));
		}
		System.out.flush();
	}
	
	/**
	 * Processes all IP ranges returning next class C in each call or null if all values were returned
	 * @return next IP range
	 */
	private static String nextRange() {
		// Get next value of third octet and postincrement to have next value in next call
		int i = RANGE[2]++;
		// When reaching the maximum value
		if (i == 256) {
			// Reduce to 1 for next call
			RANGE[2] = 1;
			// Take 0 for this call
			i=0;
			// Increase the second octet
			RANGE[1]++;
		}
		// When second octet reaches max value
		if (RANGE[1] == 256) {
			// Set second octet to 0
			RANGE[1] = 0;
			// Increase first octet
			RANGE[0]++;
		}
		// If we have returned all the ranges, return null
		if (RANGE[0] > MAX_A) {
			return null;
		}
		// Concat the range
		return RANGE[0]+"."+RANGE[1]+"."+i;
	}
	
	/**
	 *  Back to the first range
	 */
	private static void resetRange() {
		RANGE [0]= 0;
		RANGE [1]= 0;
		RANGE [2]= 0;
	}
}
