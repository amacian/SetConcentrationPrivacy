set FASTFILTER_DIR=../fastfilter_java/bin
set FASTFILTER_FPFS=../fastfilter_fpfs/bin
set GUAVA_DIR=../../libs/guava-30.1.1-jre.jar

set CLASSPATH=./bin;%FASTFILTER_FPFS%;%FASTFILTER_DIR%;%GUAVA_DIR%


REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 8 data/domain1 data/eng_10k_words.txt > eng_domain1_8.log
REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 12 data/domain1 data/eng_10k_words.txt > eng_domain1_12.log
REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 16 data/domain1 data/eng_10k_words.txt > eng_domain1_16.log
REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 8 data/domain2 data/eng_10k_words.txt > eng_domain2_8.log
REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 12 data/domain2 data/eng_10k_words.txt > eng_domain2_12.log
REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 16 data/domain2 data/eng_10k_words.txt > eng_domain2_16.log
REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 8 data/domain3 data/eng_10k_words.txt > eng_domain3_8.log
REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 12 data/domain3 data/eng_10k_words.txt > eng_domain3_12.log
REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 16 data/domain3 data/eng_10k_words.txt > eng_domain3_16.log
REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 8 data/domain4 data/eng_10k_words.txt > eng_domain4_8.log
REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 12 data/domain4 data/eng_10k_words.txt > eng_domain4_12.log
REM java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 16 data/domain4 data/eng_10k_words.txt > eng_domain4_16.log

java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 8 data/domain1 data/dns10000.txt > dns10000_domain1_8.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 12 data/domain1 data/dns10000.txt > dns10000_domain1_12.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 16 data/domain1 data/dns10000.txt > dns10000_domain1_16.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 8 data/domain2 data/dns10000.txt > dns10000_domain2_8.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 12 data/domain2 data/dns10000.txt > dns10000_domain2_12.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 16 data/domain2 data/dns10000.txt > dns10000_domain2_16.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 8 data/domain3 data/dns10000.txt > dns10000_domain3_8.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 12 data/domain3 data/dns10000.txt > dns10000_domain3_12.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 16 data/domain3 data/dns10000.txt > dns10000_domain3_16.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 8 data/domain4 data/dns10000.txt > dns10000_domain4_8.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 12 data/domain4 data/dns10000.txt > dns10000_domain4_12.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.DNSBlackBoxTester 10 16 data/domain4 data/dns10000.txt > dns10000_domain4_16.log

