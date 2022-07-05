package org.fastfilter.xor.bbPrivacy.tester;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fastfilter.xor.XorSimpleNString;

/**
 * Experiment to validate the black box approach with DNS subdomains
 * @author Alfonso
 *
 */
public class DNSBlackBoxTester {
	
	private static final int PP_ELEMENTS = 10000000;
	
	public static void main (String[] args) {
		// Arguments are:
		// Iterations: number of iterations to run to calculate the mean value
		// Fingerprint bits: number of bits used in the fingerprint for the XOR filter
		// Filename_pos: Name of the file with the dns subdomain information to be added to the filter.
		// Filename_candidates: Name of the file with the candidate subdomains.
		// sample_size: how many elements from filename_pos will be included in the filter
		if (args.length<4) {
			System.out.println("Invalid number of arguments. Use java DNSBlackBoxTester <iterations> <fingerprintBits> <filename_dnss> <filename_candidates> <sample_size>");
			System.exit(1);
		}
		
		int iterations = 0;
		int fBits = 0;
		String filename = args[2];
		String fCandidates = args[3];
		try {
			iterations = Integer.parseInt(args[0]);
			fBits =  Integer.parseInt(args[1]);
		}catch(NumberFormatException nfe) {
			System.out.println("The two first parameters must be integers");
			System.exit(1);
		}
		
		// Set of elements that will store the subdomains to be added to the filter
		Set<String> completeSet =	DNSBlackBoxTester.generateSet(filename);
		List<String> candidates =	DNSBlackBoxTester.generateList(fCandidates);

		int sampleSize = completeSet.size();
		if (args.length>4) {
			try {
				sampleSize = Integer.parseInt(args[4]);
			}catch(NumberFormatException nfe) {
				System.out.println("Invalid parameter for Sample size, using the complete set");
			}
		}
		
		// Printing header.
		System.out.println("iter;positives;fingerbits;totalPos;totalTP;totalFP;porcTP;porcFP;randomPP");
		
		// Run as many iterations as requested by the user
		for (int i=0; i<iterations;i++) {
			Set<String> sample = sampleSet(completeSet, sampleSize);
			DNSBlackBoxTester.runIteration(i, fBits, sample, candidates);
		}

		
	}
	
	/**
	 * Calculate the positive probability for a random sample of elements.
	 * @param ppElements number of elements
	 * @param sample set of 
	 */
	private static int positiveProbability(int ppElements, XorSimpleNString xor) {
		Random rand = new Random();
		int positives = 0;
		for (int i=0; i<ppElements; i++) {
			// Generate a random String
			String subdomain = DNSBlackBoxTester.nextSubdomain(rand);
			// If results in a positive when checking against the filter, increase the number of positives
			positives+=(xor.mayContain(subdomain))?1:0;
		}
		return positives;
	}
	private static String nextSubdomain(Random rand) {
		int targetStringLength = rand.nextInt(63)+1;
		String subdomain = rand.ints(45, 123)
			      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) && (i>=48 || i==45))
			      .limit(targetStringLength)
			      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			      .toString();
		return subdomain;
	}

	/**
	 * 
	 * @param completeSet
	 * @return
	 */
	private static Set<String> sampleSet(Set<String> completeSet, int sampleSize) {
		if (sampleSize>=completeSet.size()) {
			return completeSet;
		}
		List<String> aList = new ArrayList<String>(completeSet);
		Set<String> sample = new HashSet<String>(sampleSize);
		Random r = new Random();
		while(sample.size()<sampleSize) {
			sample.add(DNSBlackBoxTester.getRandomElement(r, aList));
		}
		return sample;
	}

	private static String getRandomElement(Random r, List<String> names) {
		int l = r.nextInt();
		int pos = Math.abs(l%names.size());
		return names.get(pos);
	}
	
	/**
	 * Run a specific iteration, building the Xor filter, generating a number of random IPs and checking the number of positives
	 * @param iteration number of iteration (to be printed)
	 * @param fBits number of bits used in the fingerprint for the XOR filter
	 * @param completeSet Set of IPs to be added to the filter
	 * @param candidates List of elements to be checked
	 */
	private static void runIteration(int iteration, int fBits, Set<String> completeSet, List<String> candidates) {

		// Build the filter
		XorSimpleNString xor = XorSimpleNString.construct(completeSet, fBits);
		
		// Validating true positives
		for (String person : completeSet) {
			if (!xor.mayContain(person)) {
				System.out.println("Validation failed");
			}
		}

		int pp = DNSBlackBoxTester.positiveProbability(DNSBlackBoxTester.PP_ELEMENTS, xor);

		
		int[] values = DNSBlackBoxTester.getAllProcessed(candidates, xor, completeSet);
		
		
		int totalPos = values[0];
		int totalTPos = values[1];
		int totalFPos = totalPos - totalTPos;
		
		System.out.println(iteration+";"+completeSet.size()+";"+fBits+";" + 
							totalPos + ";" + totalTPos + ";" + totalFPos + ";" + ((totalPos==0)?0:(totalTPos)/(double)totalPos) + ";" + 
							 ((totalPos==0)?0:totalFPos/(double)totalPos)+";"+ pp);
		System.out.flush();
	}

	public static Set<String> generateSet(String filename){
		Set<String> names = new HashSet<String>();
		
		File f = new File(filename);
		
		if (!f.exists()) {
			System.out.println("Not found: " + filename);
			return null;
		}
		Stream<String> stm = null;
		try {
			stm = Files.lines(f.toPath(), Charset.forName("Cp1252")); 
			names = stm.collect(Collectors.toSet());
		}catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}finally {
			try {
				stm.close();
			}catch (Exception e) {}
		}
		return names;
	}

	public static List<String> generateList(String filename){
		List<String> names = null;
		
		File f = new File(filename);
		
		if (!f.exists()) {
			System.out.println("Not found: " + filename);
			return null;
		}
		Stream<String> stm = null;
		try {
			stm = Files.lines(f.toPath(), Charset.forName("Cp1252")); 
			names = stm.collect(Collectors.toList());
		}catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}finally {
			try {
				stm.close();
			}catch (Exception e) {}
		}
		return names;
	}

	public static int[] getAllProcessed(List<String> candidates, XorSimpleNString xor, Set<String> completeSet) {
		int totalPos = 0;
		int totalTPos = 0;
		for (String candidate: candidates) {
			if(xor.mayContain(candidate)) {
				totalPos++;
				if (completeSet.contains(candidate)) {
					totalTPos++;
				}
			}
		}
		int[] values =  {totalPos, totalTPos};
		return values;
	}
	
}
