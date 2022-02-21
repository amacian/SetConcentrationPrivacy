set FASTFILTER_DIR=../fastfilter_java/bin
set FASTFILTER_FPFS=../fastfilter_fpfs/bin
set GUAVA_DIR=../../libs/guava-30.1.1-jre.jar

set CLASSPATH=./bin;%FASTFILTER_FPFS%;%FASTFILTER_DIR%;%GUAVA_DIR%

java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 8 "data\NombresFPU2020.txt" "data\1000Names.txt" "data\1000Surnames.txt" 0 > names_f8.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 12 "data\NombresFPU2020.txt" "data\1000Names.txt" "data\1000Surnames.txt" 0 > names_f12.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 16 "data\NombresFPU2020.txt" "data\1000Names.txt" "data\1000Surnames.txt" 0 > names_f16.log

java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 8 "data\NombresFPU2020.txt" "data\allNames.txt" "data\allSurnames.txt" 1 > names_random_f8.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 12 "data\NombresFPU2020.txt" "data\allNames.txt" "data\allSurnames.txt" 1 > names_random_f12.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 16 "data\NombresFPU2020.txt" "data\allNames.txt" "data\allSurnames.txt" 1 > names_random_f16.log

REM Spanish representatives since 1970s
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 8 "data\DiputadosTodasLegislaturas.txt" "data\1000Names.txt" "data\1000Surnames.txt" 0 > names_dall_f8.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 12 "data\DiputadosTodasLegislaturas.txt" "data\1000Names.txt" "data\1000Surnames.txt" 0 > names_dall_f12.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 16 "data\DiputadosTodasLegislaturas.txt" "data\1000Names.txt" "data\1000Surnames.txt" 0 > names_dall_f16.log

java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 8 "data\DiputadosTodasLegislaturas.txt" "data\allNames.txt" "data\allSurnames.txt" 1 > names_random_dall_f8.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 12 "data\DiputadosTodasLegislaturas.txt" "data\allNames.txt" "data\allSurnames.txt" 1 > names_random_dall_f12.log
java -cp %CLASSPATH% org.fastfilter.xor.bbPrivacy.tester.NameSurnameBlackBoxTester 1000 16 "data\DiputadosTodasLegislaturas.txt" "data\allNames.txt" "data\allSurnames.txt" 1 > names_random_dall_f16.log
