#!/bin/bash

Error(){
echo "Error. Pon los parámetros bien"
echo "El programa funciona así: "
echo "run.sh compile - Compila los archivos"
echo "run.sh run - Ejecuta el programa"
echo "run.sh test - Ejecuta los tests"
echo "run.sh compiletest - Compila los tests"
}

if test $# -lt 1
then Error
elif [ "$1" = "compile" ]
then 
	if test ! -d ./bin
	then
		mkdir bin
	fi
	echo "Compilando"
	javac -d ./bin -cp ./src ./src/automata/*.java ./src/utils/*.java
elif [ "$1" = "run" ]
then 
	java -cp bin/ automata.Main
elif [ "$1" = "compiletest" ]
then 
	echo "Compilando tests"
	javac -d ./bin -cp lib/junit.jar:./src  ./src/test/AutomataTest.java
elif [ "$1" = "test" ]
then 
	java -cp lib/junit.jar:lib/hamcrest-core-1.3.jar:bin/:. org.junit.runner.JUnitCore automata.AutomataTest
else
	Error
fi
