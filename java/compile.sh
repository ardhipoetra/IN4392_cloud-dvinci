rm -rf bin/
mkdir -p bin/

javac -cp .:lib/*:src/* -d bin/ $(find src -name *.java)

