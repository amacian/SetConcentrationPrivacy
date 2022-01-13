set FASTFILTER_DIR=<PATH_TO_FASTFILTER>
set FASTFILTER_FPFS=<PATH_TO_FASTFILTER_FPFS>
set GUAVA_DIR=<PATH_TO_GUAVA>

set CLASSPATH=./bin;%FASTFILTER_FPFS%;%FASTFILTER_DIR%;%GUAVA_DIR%

REM level2
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.BlackBoxTester 10 8 1 "data\firehol_level2.netset.txt" > level2_f8.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.BlackBoxTester 10 12 1 "data\firehol_level2.netset.txt" > level2_f12.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.BlackBoxTester 10 16 1 "data\firehol_level2.netset.txt" > level2_f16.log

REM level3
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.BlackBoxTester 10 8 1 "data\firehol_level3.netset.txt" > level3_f8.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.BlackBoxTester 10 12 1 "data\firehol_level3.netset.txt" > level3_f12.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.BlackBoxTester 10 16 1 "data\firehol_level3.netset.txt" > level3_f16.log
