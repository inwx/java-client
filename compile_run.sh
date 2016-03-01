BINDIR="bin"
BASEDIR=$(dirname $0)
if [ ! -d $BINDIR ]; then
	mkdir -p $BINDIR
fi
"javac" src/com/inwx/*.java src/com/inwx/model/*.java -classpath "`dirname $0`/bin/:`dirname $0`/lib/*:" -d $BINDIR
"java" -Dfile.encoding=utf-8 -classpath "`dirname $0`/bin/:`dirname $0`/lib/*:" com.inwx.Example
