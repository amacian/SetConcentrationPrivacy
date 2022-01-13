package org.fastfilter.xor.bbPrivacy.tester;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.fastfilter.xor.XorSimpleNString;
import org.fastfilter.xor.bbPrivacy.BlackBoxFireholProcessor;

public class PositiveProbabilityCalculator {

	public static void main (String[] args) {

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
		
		if(args.length<files+4) {
			System.out.println("Not enough arguments. Use java BlackBoxTester <iterations> <elements> <fingerprintBits> <number_of_files> <file1> <file2> ... <fileN>");
			System.exit(1);		
		}
		
		Set<String> completeSet = new HashSet<String>();

		
		BlackBoxFireholProcessor bbfp = new BlackBoxFireholProcessor();
		
		for (int i=0; i<files; i++) {
			completeSet.addAll(bbfp.generateSet(args[i+4], true));
		}

		System.out.println("originalElements;fingerbits;elements;totalPositives");
		
		for (int i=0;i<iterations;i++) {
			PositiveProbabilityCalculator.runIteration(elements, fBits, completeSet);
		}

		
	}
	
	private static void runIteration(int elements, int fBits, Set<String> completeSet) {

		XorSimpleNString xor = XorSimpleNString.construct(completeSet, fBits);
		
		// Validating true positives
		for (String ip : completeSet) {
			if (!xor.mayContain(ip)) {
				System.out.println("Validation failed");
			}
		}
		
		int positives = 0;
		Random rand = new Random();
		for (int i=0; i<elements; i++) {
			String ip = PositiveProbabilityCalculator.nextIP(rand);
			positives+=(xor.mayContain(ip))?1:0;
		}
			
		System.out.println(completeSet.size()+";"+fBits+";"+ elements +";"+ positives);

	}

	private static String nextIP(Random rand) {
		if (rand==null) {
			rand = new Random();
		}
		int base = rand.nextInt();
		String ip = ((base>>24) & 0x000000FF) + "." + ((base>>16) & 0x000000FF) + "." + ((base>>8) & 0x000000FF) + "." + (base & 0x000000FF);
		return ip;
	}
	

}
