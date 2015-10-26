rm -rf bin/
mkdir -p bin/

javac -cp .:lib/*:src/* -d bin/ $(find src -name *.java)

cd local
rm -rf bin/*
mkdir -p bin/
cp src/MANIFEST.MF bin/
javac -cp .:../lib/*:src/* -d bin/ $(find src -name *.java)
jar cmvf local.jar bin/MANIFEST.MF -C bin .


#example:
#java -cp local.jar:../lib/* main.Main test.jpg . -name otherfile1 -convert png -rotate -90 -colorspace gray
