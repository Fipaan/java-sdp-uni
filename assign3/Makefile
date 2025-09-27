
all: build run
clean-run: clean run
clean-build: clean build

clean:
	rm -rf build/

build:
	mkdir -p build/
	javac -Xdiags:verbose src/*.java src/*/*.java -d build/

run: build
	java -cp build/ Main
