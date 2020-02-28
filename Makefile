build:
	mkdir -p target
	javac -Xlint src/*.java -d target;
	javac -Xlint -cp target src/*.java -d target;
clean:
	rm -rf target
	rm -rf output
run:
	java -cp target Tema2 $(INPUT_FILE)
