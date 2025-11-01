all: build
clean-all: clean-build
clean-build: clean build
clean-run: clean run

clean:
	rm -rf build/

build:
	javac -g -Xdiags:verbose -Xlint:deprecation -sourcepath src src/fipaan/game/Main.java -d build/

run: build
	java -cp build/ fipaan.game.Main

debug: build
	rlwrap jdb -sourcepath src/ -classpath build/
