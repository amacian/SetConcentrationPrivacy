package org.fastfilter.xor.bbPrivacy.tester;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fastfilter.xor.XorSimpleNString;

/**
 * Experiment to validate the black box approach with names and surnames
 * @author Alfonso
 *
 */
public class NameSurnameBlackBoxTester {
	
	private static final int RANDOM_ELEMENTS = 1000000;
	
	public static void main (String[] args) {
		// Arguments are:
		// Iterations: number of iterations to run to calculate the mean value
		// Fingerprint bits: number of bits used in the fingerprint for the XOR filter
		// Filename_pos: Name of the file with the people information to be added to the filter.
		// Filename_names: Name of the file with the most common 1000 names
		// Filename_surnames: Name of the file with the most common 1000 surnames 
		// Random: 1 random elements, 0 all elements 
		if (args.length!=6) {
			System.out.println("Invalid number of arguments. Use java BlackBoxTester <iterations> <fingerprintBits> <filename_pos> <filename_names> <filename_names> <random:1/0>");
			System.exit(1);
		}
		
		int iterations = 0;
		int fBits = 0;
		String filename = args[2];
		String fNames = args[3];
		String fSurnames = args[4];
		boolean random = false;
		try {
			iterations = Integer.parseInt(args[0]);
			fBits =  Integer.parseInt(args[1]);
			random = (Integer.parseInt(args[5])==1);
		}catch(NumberFormatException nfe) {
			System.out.println("The two first parameters must be integers");
			System.exit(1);
		}
		
		// Set of elements that will store the IPs to be added to the filter
		// Set<String> completeSet = new HashSet<String>();

		Set<String> completeSet =	NameSurnameBlackBoxTester.generateSet(filename);
		List<String> names =	NameSurnameBlackBoxTester.generateList(fNames);
		List<String> surnames =	NameSurnameBlackBoxTester.generateList(fSurnames);

		// Printing header.
		System.out.println("iter;positives;fingerbits;totalPos;totalTP;totalFP;porcTP;porcFP");
		
		// Run as many iterations as requested by the user
		for (int i=0; i<iterations;i++) {
			NameSurnameBlackBoxTester.runIteration(i, fBits, completeSet, names, surnames, random);
		}

		
	}
	
	/**
	 * Run a specific iteration, building the Xor filter, generating a number of random IPs and checking the number of positives
	 * @param iteration number of iteration (to be printed)
	 * @param fBits number of bits used in the fingerprint for the XOR filter
	 * @param completeSet Set of IPs to be added to the filter
	 * @param random Boolean that indicates if generating random names+surnames or processing all.
	 */
	private static void runIteration(int iteration, int fBits, Set<String> completeSet, List<String> names, List<String> surnames, boolean random) {

		// Build the filter
		XorSimpleNString xor = XorSimpleNString.construct(completeSet, fBits);
		
		// Validating true positives
		for (String person : completeSet) {
			if (!xor.mayContain(person)) {
				System.out.println("Validation failed");
			}
		}
		
		int[] values = null;
		
		if (random) {
			values = NameSurnameBlackBoxTester.getRandomProcessed(names, surnames, xor, completeSet);
		}else {
			values = NameSurnameBlackBoxTester.getAllProcessed(names, surnames, xor, completeSet);
		}
		
		
		int totalPos = values[0];
		int totalTPos = values[1];
		int totalFPos = totalPos - totalTPos;
		
		System.out.println(iteration+";"+completeSet.size()+";"+fBits+";" + 
							totalPos + ";" + totalTPos + ";" + totalFPos + ";" + ((totalPos==0)?0:(totalTPos)/(double)totalPos) + ";" + 
							 ((totalPos==0)?0:totalFPos/(double)totalPos));
		System.out.flush();
	}
	
	private static int[] getRandomProcessed(List<String> names, List<String> surnames, XorSimpleNString xor,
			Set<String> completeSet) {
		int totalPos = 0;
		int totalTPos = 0;
		Random r = new Random();
		for (int i=0; i< NameSurnameBlackBoxTester.RANDOM_ELEMENTS; i++) {
			String person = NameSurnameBlackBoxTester.getRandomElement(r, names) + " " + NameSurnameBlackBoxTester.getRandomElement(r, surnames);
			if(xor.mayContain(person)) {
				totalPos++;
				if (completeSet.contains(person)) {
					totalTPos++;
				}
			}
		}
		int[] values =  {totalPos, totalTPos};
		return values;
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
	
	public static String getRandomElement(Random r, List<String> names) {
		int l = r.nextInt();
		int pos = Math.abs(l%names.size());
		return names.get(pos);
	}

	public static int[] getAllProcessed(List<String> names, List<String> surnames, XorSimpleNString xor, Set<String> completeSet) {
		int totalPos = 0;
		int totalTPos = 0;
		for (String name: names) {
			for (String surname: surnames) {
				String person = name + " " + surname; 
				if(xor.mayContain(person)) {
					totalPos++;
					if (completeSet.contains(person)) {
						totalTPos++;
					}
				}
			}
		}
		int[] values =  {totalPos, totalTPos};
		return values;
	}
}
