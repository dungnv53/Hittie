#!/bin/bash 

# Find keyword in files with extension match, 
# usage:
# ie. find all sh files contain "keywords" 
findsrc_func() {
	if [ -z "$1" ]; then # In case empty file extension, set a default one to avoid other error
		ext="*.sh"
	else
		ext="*."$1
	fi

	echo "$ext"
	keyword=$2
	if [ ${#keyword} -lt 2 ]; then
		echo "${keyword}  have at least 2 character"
		exit 1
	fi 

    echo "$2"
    find . -name "${ext}" -print0 | xargs -0 grep -rl "$2"  
}

# TODO add more parameter for ie. path location
findsrc_func $1 $2


