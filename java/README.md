# Instructions

## What to change (to adapt to your system)
In `Constants.java`, change the following :
- CREDENTIALS. Change it with your username and password. The format is `username:password`.
- SSH_USER. Change it with your username.
- SSH_PASS. And your password. (Yes, it's not secure enough for now)
- VM_TEMPLATE_DEFAULT. Point it to your vm template location.
- TEST_TARGET_SSH. Change it into IP/hostname of the VM you want to play around with.


## How to compile
- go to `java` directory (I suppose it's the current directory)
- execute compile.sh
- VOILA!

## How to run

First and foremost, you have to go to the `bin` directory. At least (for safety reason) you have to open 3 terminals. For all those terminals, we execute the command that I'll show below.

### Terminal 1
enter command `rmiregisty`. Basically it starts the 'server'. As long as it doesn't produce any error, it's fine. If you want to close it, please use `ctrl+c`. DO-NOT-KILL-IT.

### Terminal 2
In this terminal, we run our 'server' java program. Run command : `java -cp .:../lib/*  -Djava.security.policy=/home/cld1593/cloud-dvinci/java/security.policy nl.tudelft.in4392.Main`. Change the `cld1593` into your given account.

### Terminal 3 (and other)
We can simulate the 'client' for the other terminal. In this case, let us run the `Generator` program. The command is similar to the previous one. It is `java -cp .:../lib/*  -Djava.security.policy=/home/cld1593/cloud-dvinci/java/security.policy nl.tudelft.in4392.client.Generator
`. And again, change the `cld1593` into your given account.

You can run program `ControlTerminal` (which have 'admin' privileges) in the same way. Just change the class.

