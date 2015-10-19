ssh -t cld1594@$1 free | grep Mem | awk '{print $4/$2*100.0}'
