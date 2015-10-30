for ((i = 1; i <= 30; i++)) ; do
        java -cp .:../lib/* -Djava.security.policy=/home/cld1593/cloud-dvinci/java/security.policy nl.tudelft.in4392.client.Generator $1  1 $2 $3 -encipher $4 -blur 0 50 -rotate 90
        sleep 1
done
sleep 2

