<?php

$a = array(
    array('1', '3', '5', '7'),
    array('2', '4', '6', '8'),
);

function a_to_b($a) {
    $a_width = count($a[0]);
    $a_length = count($a);
    $tmp = array();

    for($j=0; $j<$a_width; $j++) {
        for($i=0; $i<$a_length; $i++) {
                $tmp[$i] = $a[$i][$j];
        }
        $rst[$i][] = $tmp;
    }

    return $rst;
}

print_r(a_to_b($a));