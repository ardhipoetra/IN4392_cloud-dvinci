#ssh -t root@10.141.3.153 free|grep Mem|awk '{print $4/$2*100.0}'

ssh -t root@$1 free|grep Mem|awk '{print $4/$2*100.0}'

