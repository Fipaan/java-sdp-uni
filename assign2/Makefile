
all: build run

build:
	mkdir -p build/
	javac -Xdiags:verbose src/*.java src/*/*.java -d build/

run: build
	java -cp build/ Main
