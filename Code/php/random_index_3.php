<?php

private static function getRandomAttributevalue($age, $gender, $local) 
    {

        $max_random = 0; // Max value of random generate.
        $user_attributes = array();

        if($age > 0) {
            $max_random++;
            if( ($gender+$local) == 0) { // 2 other user profile not set
                return $age;
            } 

            if($gender > 0) {
                $max_random++;
                if($local > 0) {
                    $max_random++;
                    $user_attributes = array('0'=>$age, '1' => $gender, '2' => $local);
                } else { // local null
                     $user_attributes = array('0'=>$age, '1' => $gender);
                }
            } else { // gender null
                if($local > 0) {
                    $max_random++;
                    $user_attributes = array('0'=>$age, '1' => $local);
                } else { // local null, gender null
                    return $age;        // Only age is set.
                }
            }

        } else { // age null
           if($gender > 0) {
                $max_random++;
                if($local > 0) {
                    $max_random++;
                    $user_attributes = array('0'=>$gender, '1' => $local);
                } else { // local null, age null
                     return $gender;
                }
            } else { // gender null
                if($local > 0) {
                    $max_random++;
                    return $local;
                } else { // local null, gender null, age null
                    return -1;        // No user attribute has been set.
                }
            }
        }

        if($max_random > 0) {
            $random_index = rand(0, $max_random);
        } else {
            return -1;
        }

        return $user_attributes[$random_index-1];
    }