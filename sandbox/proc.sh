#top -b -d1 -n1|grep -i "Cpu(s)"|head -c21|cut -d ' ' -f3|cut -d '%' -f1
#while true
#do
        ssh -t cld1594@$1 top -b -d1 -n1|grep -i "Cpu(s)"|head -c21|cut -d ' ' -f
3|cut -d '%' -f1
        #sleep 1
#done

