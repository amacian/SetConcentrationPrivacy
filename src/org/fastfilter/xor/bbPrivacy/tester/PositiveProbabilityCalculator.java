package org.fastfilter.xor.bbPrivacy.tester;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.fastfilter.xor.XorSimpleNString;
import org.fastfilter.xor.bbPrivacy.BlackBoxFireholProcessor;

/**
 * Calculates the positive probability of the filter
 * @author Alfonso
 *
 */
public class PositiveProbabilityCalculator {

	public static void main (String[] args) {
		// Arguments are:
		// Iterations: number of iterations to run to calculate the mean value
		// Elements: number of random IP values to build and test against the filter
		// Fingerprint bits: number of bits used in the fingerprint for the XOR filter
		// Number of files: number of files to be read. These files include all the IPs to be added to the filter
		// Names of the files.
		if (args.length<4) {
			System.out.println("Not enough arguments. Use java PositiveProbabilityCalculator <iterations> <elements> <fingerprintBits> <number_of_files> <file1> <file2> ... <fileN>");
			System.exit(1);
		}
		
		int iterations = 1;
		int elements = 0;
		int fBits = 0;
		int files = 0;
		try {
			iterations = Integer.parseInt(args[0]);
			elements = Integer.parseInt(args[1]);
			fBits =  Integer.parseInt(args[2]);
			files =  Integer.parseInt(args[3]);
			if (files<1 || fBits <1 ) {
				System.out.println("The four first parameters must be integers > 0");
				System.exit(1);
			}
		}catch(NumberFormatException nfe) {
			System.out.println("The four first parameters must be integers");
			System.exit(1);
		}
		
		// Check that we have as many names as files have to be read
		if(args.length<files+4) {
			System.out.println("Not enough arguments. Use java BlackBoxTester <iterations> <elements> <fingerprintBits> <number_of_files> <file1> <file2> ... <fileN>");
			System.exit(1);		
		}
		
		// Set of elements that will store the IPs to be added to the filter
		Set<String> completeSet = new HashSet<String>();

		// Helper class to read the IPs
		BlackBoxFireholProcessor bbfp = new BlackBoxFireholProcessor();
		
		// Read all the files
		for (int i=0; i<files; i++) {
			// Reading next file.
			completeSet.addAll(bbfp.generateSet(args[i+4], true));
		}

		// Printing header.
		System.out.println("originalElements;fingerbits;elements;totalPositives");
		
		// Run as many iterations as requested by the user
		for (int i=0;i<iterations;i++) {
			PositiveProbabilityCalculator.runIteration(elements, fBits, completeSet);
		}

		
	}
	
	/**
	 * Run a specific iteration, building the Xor filter, generating a number of random IPs and checking the number of positives
	 * @param elements number of random IP values to build and test against the filter
	 * @param fBits number of bits used in the fingerprint for the XOR filter
	 * @param completeSet Set of IPs to be added to the filter
	 */
	private static void runIteration(int elements, int fBits, Set<String> completeSet) {

		// Build the filter
		XorSimpleNString xor = XorSimpleNString.construct(completeSet, fBits);
		
		// Validating true positives
		for (String ip : completeSet) {
			if (!xor.mayContain(ip)) {
				System.out.println("Validation failed");
			}
		}
		
		// Capture the positives
		int positives = 0;
		// Prepare a pseudorandom source
		Random rand = new Random();
		// Repeat "element" times
		for (int i=0; i<elements; i++) {
			// Generate a random IP
			String ip = PositiveProbabilityCalculator.nextIP(rand);
			// If results in a positive when checking against the filter, increase the number of positives
			positives+=(xor.mayContain(ip))?1:0;
		}
		
		// Print the information. % of positives would be positives/elements
		System.out.println(completeSet.size()+";"+fBits+";"+ elements +";"+ positives);

	}

	/**
	 * Generate a random IP
	 * @param rand the source of randomness
	 * @return
	 */
	private static String nextIP(Random rand) {
		if (rand==null) {
			rand = new Random();
		}
		// Create it from an int
		int base = rand.nextInt();
		// Take 8 bits for each part of the IP 
		String ip = ((base>>24) & 0x000000FF) + "." + ((base>>16) & 0x000000FF) + "." + ((base>>8) & 0x000000FF) + "." + (base & 0x000000FF);
		return ip;
	}
	

}
