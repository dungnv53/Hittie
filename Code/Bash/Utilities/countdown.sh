#!/bin/bash

function countdown(){  
    local now=$(date +%s)
    local end=$((now + $1))
    while (( now < end )); do   
        printf "%s\r" "$(date -u -d @$((end - now)) +%T)"  
        sleep 0.25  
        now=$(date +%s)
    done  
    echo
}

#date -u -j -f %s $((end - now)) +%T

countdown
