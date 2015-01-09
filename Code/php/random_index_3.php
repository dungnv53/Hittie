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




    public function edit()
    {
        // Cookie or LS

        // set usr_prf on view, DB do not store usr prf ?

        $user_profile = $this->getCurrentUserProfile();
        $user_profile = implode( ',' ,array_map('strval', $user_profile));
        $user = $this->getCurrentUser();
        
        return \View::make('front.profile.edit', compact('user', 'user_profile'));
    }

    public function complete()
    {
        // dd(\Input::all());


        $cookie_name = "user_age";
        $cookie_value = 24;
        setcookie($cookie_name, $cookie_value, time() + (86400 * 30), "/"); // 86400 = 1 day 

        $cookie_name = "gender";
        $cookie_value = 7; // male, 8 female
        setcookie($cookie_name, $cookie_value, time() + (86400 * 30), "/"); // 86400 = 1 day 

        $cookie_name = "local";
        $cookie_value = 43; // male, 8 female
        setcookie($cookie_name, $cookie_value, time() + (86400 * 30), "/"); // 86400 = 1 day

        $user = $this->getCurrentUser();
        $user = User::fillWithModify($user->id);
        $user_id = empty($user_id) ? $user->id : $user_id; // User_id send to complete page

        return \View::make('front.profile.complete', compact('user_id', 'user'));
    }


        /**
     * Get User profile: age, gender, pref
     */
    public function getCurrentUserProfile() {

        $user_profile = array();

        $user_age = Cookie::get('user_age');
        $gender = Cookie::get('gender');
        $user_pref = Cookie::get('local');

        $user_profile['user_age'] = $user_age;
        $user_profile['gender'] = $gender;
        $user_profile['local'] = $user_pref;

        // $currentUser = User::with('userAttributes')->whereUid($uid)->first();

        // if (!$currentUser) {
        //     return;
        // }

        // $currentUser->loadUserAttributes();
        return $user_profile;
    }

    <script type="text/javascript">
        window.onload = function()
        {
            var user_profile = document.cookie;
            // console.log(user_profile);
            // document.getElementById("test_js").innerHTML = 'if u try to number gods u could never count him';
            // document.getElementById("test_js").innerHTML = '----> ' + getCookie('user_age');
        }

        if(document.readyState === "complete") {

        }

        function getCookie(cname) {
            // return " test"
            // var name = cname + "=";
            // var ca = document.cookie.split(';');
            // for(var i=0; i<ca.length; i++) {
            //     var c = ca[i];
            //     while (c.charAt(0)==' ') c = c.substring(1);
            //     if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
            // }
            // return "";
        }
    </script>

     <div id="test_js">Test div †††</div>


    $user_id = \Input::get('user_id');
    $user = User::fillWithModify($user_id);
    $user_id = empty($user_id) ? $user->id : $user_id; // User_id send to complete page


    /**
     * Fill form data and modify data
     *
     * @return user[]
     */
    public static function fillWithModify($id = null)
    {

        if ($id == null) {
            $user = new User;
        } else {
            $user = User::find($id);
            if(empty($user)) { // Can't find user.
                $user = new User;
            }
        }

        $birthday = \Input::get('birthday_year'). '-'. \Input::get('birthday_month'). '-'. \Input::get('birthday_day');
        $user->birthday = date('Y-m-d H:i:s', strtotime($birthday));
        $user->save();
        $user->afterCreate();

        UserAttribute::updateUserAttr($user->id, ATTR_GENDER, \Input::get('sex'));
        UserAttribute::updateUserAttr($user->id, ATTR_LOCAL, \Input::get('pref'));
        
        return $user;
    }

    /**
    * Form macros
    */
    Form::macro('attributeValueList', function($attribute_id, $options = array(), $attr_name='attribute_value_id'){
        $attributeValueList = docomo\Models\Attributes\AttributeValue::whereAttributeId($attribute_id)->orderBy('sort_order')->get();

        if(!empty($options['select_none'])) { // Add none select option on select form
            $defaultValue = array('0' => $options['select_none']);
            $attributeValueList = $defaultValue + $attributeValueList->lists('attribute_value_name', 'id');
        } else {
            $attributeValueList = $attributeValueList->lists('attribute_value_name', 'id');
        }

        if($options['default']) {   // If form call has default selected
            return Form::select($attr_name, array('default' => $attributeValueList[$options['default']])+$attributeValueList, 'default');
        } else {
            return Form::select($attr_name, $attributeValueList, 0);
            // gon hon:
            // {{ Form::select('author', array('default' => 'Please Select')+$authors, 'default') }}
        }
    });

    <script type="text/javascript">
        var user_profile = {};

        function removeStorage() {

        }

        function getLocalStorage() {
            if (localStorage.getItem('user_profile') != null) {

                return JSON.parse(localStorage["user_profile"])
            } else {

                return {};
            }
        }

        // Store User profile in localStorage
        function setLocalStorage() {
            user_profile.birthday_year =  my_btoa(getSelected('birthday_year'));
            user_profile.birthday_month = my_btoa(getSelected('birthday_month'));
            user_profile.birthday_day =   my_btoa(getSelected('birthday_day'));
            user_profile.sex =  my_btoa(getSelected('sex'));
            user_profile.pref = my_btoa(getSelected('pref'));

            localStorage.setItem('user_profile', JSON.stringify(user_profile));
        }

        // Set user selected value in view.
        function setSelected(attr_name, value) {
            if(value != '') {
                $('select[name="'+attr_name+'"]').val(value);
            }

            return false;
        }

        function getSelected(select_name) {

            return $('select[name="'+ select_name +'"] option:selected').val();
        }

        function setUserId(user_id) {
            if( (typeof user_id != 'undefined') && (user_id != '') ) {
                // Set user_id if it has been set in localStorage.
                $('input[name="user_id"]').val(user_id);
            }
        }

        $(document).ready(function() {
            // // set default birth year
            if((getLocalStorage().birthday_year == '') || (typeof getLocalStorage.birthday_year == 'undefined')) {
                setSelected('birthday_year', {{ BIRTH_YEAR_DEF }});
            }

            setSelected('birthday_year',  my_atob(getLocalStorage().birthday_year));
            setSelected('birthday_month', my_atob(getLocalStorage().birthday_month));
            setSelected('birthday_day',   my_atob(getLocalStorage().birthday_day));
            setSelected('sex',  my_atob(getLocalStorage().sex));
            setSelected('pref', my_atob(getLocalStorage().pref));

            setUserId(my_atob(getLocalStorage().user_id)); // Set hidden value user_id

            $('input[name="save_btn"]').click(function() {
                setLocalStorage();
            });
        });

    </script>


    // COOKIE USR PRF
    // $user_profile = array('user_age' => isset($user_profile[0]) ? $user_profile[0] : '', 
    //                       'birthday_year' => isset($user_profile[1]) ? $user_profile[1] : '', 
    //                       'birthday_month' => isset($user_profile[2]) ? $user_profile[2] : '',
    //                       'birthday_day' => isset($user_profile[3]) ? $user_profile[3] : '', 
    //                       'gender' => isset($user_profile[4]) ? $user_profile[4] : '',  
    //                       'local' => isset($user_profile[5]) ? $user_profile[5] : '', 
    //                       'user_id' => isset($user_profile[6]) ? $user_profile[6] : '');














