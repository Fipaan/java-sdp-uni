all: build
clean-all: clean-build
clean-build: clean build
clean-run: clean run

clean:
	rm -rf build/

build:
	javac -g -Xdiags:verbose -Xlint:deprecation src/lfs/*.java src/lfs/*/*.java src/lfs/*/*/*.java src/lfs/*/*/*/*.java -d build/

run: build
	java -cp build/ lfs.Main

debug: build
	rlwrap jdb -sourcepath src/ -classpath build/
