

realpath() {
    [[ $1 = /* ]] && echo "$1" || echo "$PWD/${1#./}"
}

realpath "$0"

findsrc_func() {
    echo "$1"
	ext="*."$1
	echo "$ext"
    echo "$2"
    find . -name "${ext}" -print0 | xargs -0 grep -rn "$2"  
}

#findsrc_func $1 $2

#now=$(date +%s)sec
now=$(date +%s)
# echo ${now}

date -j -f %Y%m%d-%H%M%S 20180101-234852  +%Y/%m/%d\ %H:%M:%S

#date -j -f %Y%m%d-%H%M%S 20180101-234852  +%s
#date -j -f %s 1514868532


#while true; do
#     #printf "%s\r" $(TZ=UTC date --date now-$now +%H:%M:%S.%N)
#     #echo  $(TZ=UTC date -j now-$now +%H:%M:%S.%N)
#     echo  `TZ=UTC date -j now-${now} +%H:%M:%S.%N`
#     #printf "%s\r" $(TZ=UTC date -j now-$now +%H:%M:%S.%N)
#     sleep 0.1
#done

function show_time () {
    num=$1
    min=0
    hour=0
    day=0
    if((num>59));then
        ((sec=num%60))
        ((num=num/60))
        if((num>59));then
            ((min=num%60))
            ((num=num/60))
            if((num>23));then
                ((hour=num%24))
                ((day=num/24))
            else
                ((hour=num))
            fi
        else
            ((min=num))
        fi
    else
        ((sec=num))
    fi
    echo "$day"d "$hour"h "$min"m "$sec"s
}

function show_hour_minute_time () {
    num=$1
    min=0
    hour=0
    if((num>59));then
        ((sec=num%60))
        ((num=num/60))
        if((num>59));then
            ((min=num%60))
            ((num=num/60))
    #        if((num>23));then
    #            ((hour=num%24))
    #            ((day=num/24))
    #        else
                ((hour=num))
    #        fi
        else
            ((min=num))
        fi
    else
        ((sec=num))
    fi
    #echo "$hour"h "$min"m "$sec"s
	echo ":" "$min"m "$sec"s
}

show_hour_minute_time 243
count=0
while true; do
		count=$((count+1))
		#echo -ne $count
		count_time=$(show_hour_minute_time count)
		# echo -n " ${count} >> "
		echo -n " ${count_time} "
		# echo -n " $show_hour_minute_time(${count})  >> "
		sleep 1
done
