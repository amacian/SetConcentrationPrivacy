# Positives concentration and Approximate Membership Check Filters
This repository includes the implementation in Java of the Experiments included in the paper:

P. Reviriego, A. Sánchez-Macián, E. Merino-Gómez, O. Rottenstreich, S. Liu and F. Lombardi, "Attacking the Privacy of Approximate Membership Check Filters by Positive Concentration", IEEE Transactions on Computers, in press.

# Dependencies
- Guava >=30.1.1
- Fastfilter Java implementation: https://github.com/FastFilter/fastfilter_java/ tag 1.0.2
- Fastfilter FPFS implementation: https://github.com/amacian/fastfilterfpfs/
- For the IPv4 use case, files have been included in the data directory.
- For the candidates selected  for  a  national  PhD  scholarship  in  Spain, the information available at the following PDF files:   
  - https://www.aei.gob.es/sites/default/files/convocatory_info/2021-07/Resolucion_Concesion_PREDOC2020_firmada.pdf
  - https://www.aei.gob.es/sites/default/files/convocatory_info/2021-10/PRE2020_SegundaRC_Art20_3_abc_Firmada.pdf
  - https://www.aei.gob.es/sites/default/files/convocatory_info/2021-10/PRE2020_TerceraRC_Art20_4_Firmada.pdf
- For the list  of  members  of  the  Spanish  parliament  since  1977, the data at https://www.congreso.es/webpublica/opendata/diputados/Diput__20220221050306.csv
- For the DNS use case:
  - The information for subdomains has been provided by Rapid7: https://opendata.rapid7.com/sonar.fdns_v2/
  - The information for the most popular subdomains in the Internet is available at: https://github.com/bitquark/dnspop/blob/master/results/bitquark_20160227_subdomains_popular_10000

# Content
This directory includes three batch scripts to execute the ipv4 (experiment.bat), the names (experimentNames.bat), and the DNS (experimentDNS.bat) use cases.

*src* directory includes the following files:

Classes in the package org.fastfilter.xor.bbPrivacy:
- BlackBoxFireholProcessor.java (helper file to process the IPv4 files).

Classes in the package org.fastfilter.xor.bbPrivacy.tester:
- BlackBoxTester.java (java class to execute the IPv4 experiments).
- PositiveProbabilityCalculator.java (java class to calculate the postivie probability for the IPv4 xor filters)
- NameSurnameBlackBoxTester.java (java class to execute the names experiments).
- DNSBlackBoxTester.java (java class to execute the DNS use case).

*data* directory includes the following files:
- firehol files as they were available in the moment of running the experiment.
- 1000Names.txt and 1000Surnames.txt with information of the 1000 more common names and surnames processed from the Statistics Spanish national institute data.
- allNames.txt and allSurnames.txt with information of the names and surnames with a frequency of 20 or more, processed from the Statistics Spanish national institute data.
 
# Execution of the code
Compile the java classes and resolve dependencies. Then:
- Modify GUAVA_DIR, FASTFILTER_DIR, FASTFILTER_FPFS and CLASSPATH from the experiment.bat script and run it to generate the logs for the ipv4 use case.
- Modify GUAVA_DIR, FASTFILTER_DIR, FASTFILTER_FPFS and CLASSPATH from the experimentNames.bat script and run it to generate the logs for the names use cases.
- Modify GUAVA_DIR, FASTFILTER_DIR, FASTFILTER_FPFS and CLASSPATH from the experimentDNS.bat script, download the appropriate information and run it to generate the logs for the DNS use cases.
