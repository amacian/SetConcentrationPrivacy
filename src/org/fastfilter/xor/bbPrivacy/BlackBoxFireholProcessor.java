package org.fastfilter.xor.bbPrivacy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BlackBoxFireholProcessor {
	public Set<String> generateSet(String filename, boolean only32){
		Set<String> ips = new HashSet<String>();
		
		File f = new File(filename);
		
		if (!f.exists()) {
			System.out.println("Not found: " + filename);
			return null;
		}
		Stream<String> stm = null;
		try {
			stm = Files.lines(f.toPath()); 
			
			stm = stm.filter(line -> !line.startsWith("#"));
			
			if(only32) {
				stm = stm.filter(line -> !line.contains("/"));
			}
			
			ips = stm.collect(Collectors.toSet());
			
			if(!only32) {
				
			}
			
		}catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}finally {
			try {
				stm.close();
			}catch (Exception e) {}
		}
		/*for (String s: ips) {
			System.out.println(s);
		}*/
		return ips;
	}
	
	public static void main (String[] args) {
		if (args.length<2) {
			System.out.println("Not enough arguments. Use java BlackBoxFireholProcessor <number_of_files> <file1> <file2> ... <fileN>");
			System.exit(1);
		}
		
		int files = 0;
		try {
			files =  Integer.parseInt(args[0]);
			if (files<1) {
				System.out.println("The first parameter must be an integer > 0");
				System.exit(1);
			}
		}catch(NumberFormatException nfe) {
			System.out.println("The first parameter must be an integer");
			System.exit(1);
		}
		
		if(args.length<files+1) {
			System.out.println("Not enough arguments. Use java BlackBoxFireholProcessor <number_of_files> <file1> <file2> ... <fileN>");
			System.exit(1);		
		}
		
		Set<String> completeSet = new HashSet<String>();
		
		BlackBoxFireholProcessor bbfp = new BlackBoxFireholProcessor();
		
		for (int i=0; i<files; i++) {
			completeSet.addAll(bbfp.generateSet(args[i+1], true));
		}
		
		System.out.println(completeSet.size());

		
	}
	
	public Set<String> expandSubnet(String subnet){
		Set<String> ips = new HashSet<String>();
		
		// TODO: Extract the number after the / and generate all the ips for that subnet 
		
		return ips;
	}
}
