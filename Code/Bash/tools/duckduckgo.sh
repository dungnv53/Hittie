#! /bin/bash
func(){
    str=
    for i in "$@"; do
        str="$str$i+"
    done
    #echo ${str# }
    #echo $str
    w3m https://www.duckduckgo.com/?q=${str# }
}

#func
func $1 $2 $3 $4 $5 $6 $7 $8 $9
