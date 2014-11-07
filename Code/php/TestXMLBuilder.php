<?php
class TestXMLBuilder {

    protected $displayHtmlComment = 1;
    protected $options;
    private $allData;
    private $base_id;  // phai them thuoc tinh nay thi moi dung chung duoc trong cac ham

    public function __construct(){
        $this->allData = '';
        $base_id = '';
    }

    public function setParam($name,$value){
        return $this->{$name} = $value;
    }

    public function build(&$pre,$options=array()) {
        $firstBuild = isset($options["notFirstBuild"]) ? 0 : 1;
        $options["notFirstBuild"] = 1;
        $exam = isset($options["exam"]) ? 1 : 0;
        $this->options = $options;

        $answers = array();
        
        if(isset($pre["answers"])){
            $answers = &$pre["answers"];
            // print_r($answers['type']);   
        }
        $this->handleBaseId($pre, $exam, $options);

        //歯科のdiv項目の範囲制御
        $div_open_hide_flag = false;
        $div_close_hide_flag = false;

        if(!$div_open_hide_flag) {
            $this->allData .= '<item><question'.(($this->base_id != '') ? ' question_id="'.$this->base_id.'" ' : '' );
            //if(isset($answers["component"])) { echo ' '.$answers["component"]. '"'; } else { echo ' '; }
            if(isset($answers["type"]) && $answers["type"]=="text" && isset($answers["component_id"]) && $answers["component_id"]==2) {}
            
        }
        //TODO Sentence first ? need mod
        if(isset($pre['question'])){
            $this->allData .= ' sentence="'.$pre['question'] . '">';
        } else {
            $this->allData .= '>';
        }
        if($firstBuild && !isset($base_id)) {
        }
        if(isset($pre["sub"])){
            $this->typeQuestion($pre, $this->options);
        }else{
            $this->_error($answers);
            $this->typeComponent($answers);
            $this->buildSub($answers, $this->base_id);
            $this->typeComponent2($answers);
        }
        if($firstBuild && ($this->base_id == '')) { // TODO check this->base_id
            //echo '</div>'; // error html (/div)
        }
        if(!$div_close_hide_flag) {
            $this->allData .= '</question></item>';
        }

    }

    public function foreachItems(&$answers,$base_id,$wrap=true) {
        global $lmd;
        $answer_type = $answers['type'];
        switch($answer_type) {
            case 'textarea':
            foreach($answers["items"] as $key => $val){
                $this->buildTextarea($base_id, $val["maxlength"],isset($val["value"])?$val["value"]:"",isset($val["error"])?$val["error"]:"");
            }
            break;
            case 'text':
                foreach($answers["items"] as $key => $val) {
                   $this->buildText($base_id . "-" . $key, $val,$wrap,$wrap);
                }
                break;
            case 'radio':
                $this->buildRadio($answers, $this->base_id, $wrap, $lmd);
                break;
            case 'component':
                if(isset($answers["component_id"])) {
                    $this->buildComponent($answers, $this->base_id);
                }else{
                    // _ve("component_id doesn't be specified.");
                }
                break;
            default: 
                // _ve("yet implemented.");
                break;
        }

    }

    public function buildText($id,$val,$wrapTR=true,$wrapTD=true){
        $prefix = isset($val["prefix"]) ? $val["prefix"] : "";
        $surfix = isset($val["surfix"]) ? $val["surfix"] : "";
        // if($wrapTR) echo "".($this->displayHtmlComment ? '<!-- div [0009] -->' : "");
        // if($wrapTD) echo "";
        
        $this->allData .= '<type>!CDATA[text]]</type>';
        $this->allData .= '<inherit><![CDATA[0]]></inherit>';
        $this->allData .= '<maxlength><![CDATA['.(isset($val["maxlength"]) ? $val["maxlength"] : "").']]></maxlength>';
        $this->allData .= '<validation>text</validation>'; // hard fix 'text' not param
        // TODO prefix surfix
        //echo $prefix $surfix
        //autocomplete="off"
        // if($wrapTD)
        // echo ($this->displayHtmlComment ? '<!-- /div [0009] -->' : "");
    }
    
    private function typeQuestion($pre, $options) {
        if($pre["sub"]["type"] == "question"){
            foreach($pre["sub"]["items"] as $val){
                $this->build($val,$options);
            }
        }else{
            // _ve("yet implemented. @ " . __LINE__);
        }
    }
    private function handleBaseId($pre, $exam, $options) {
        if(isset($pre["question_id"])){
            $base_id_prefix = "q" . ($exam?"e":"");
            if(isset($options["base_id_prefix"])){
                $base_id_prefix = $options["base_id_prefix"];
            }
            $this->base_id = $base_id_prefix . $pre["question_id"];
        }
    }
    private function buildSub($answers, $base_id) {
        if($answers["type"]=="checkbox"){
            $this->buildCheckbox($answers,$base_id);
        }else if($answers["type"]=="date_text_set"){
            // $this->buildDate_Text_Set($base_id,$answers);
        }else if($answers["type"]=="header"){
            // $this->buildHeader($base_id,$answers);
        }else if($answers["type"]=="text" && isset($answers["component_id"]) && $answers["component_id"]==2){
            $this->build_Text_components($pre["question_id"],$answers);
        }else if($answers["type"]=="radio"){
            $this->foreachItems($answers,$base_id);
        }else{
            // code nay ve dong dau tien

            $this->foreachItems($answers,$base_id,false);
        }
    }
    private function _error($answers) { // need new attriubte ?
        if(isset($answers["error"])){
            $this->outputErrors($answers["error"]);
            unset($answers["error"]);
        }
    }
    private function typeComponent($answers) {
        if(isset($answers['type']) && $answers['type'] == 'component' && $answers['items'][1]["type"] =="text" ){
            // $this->allData .= '< id="appenRecord_'.$this->base_id.'">';
            // $this->allData .= ($this->displayHtmlComment ? '<!-- div [0013] base_id = '.$base_id.' -->' : "");
        }else{
            if($answers["type"]=="text" && isset($answers["component_id"]) && $answers["component_id"]==2){
                // $this->allData .= '< id="tableQuesttion_'.QUESTION_ID_23.'">'.($this->displayHtmlComment ? '<!-- div [0012] -->' : "");
            }else{
                //echo '<div class="selectItem"  border="0">';
            }
        }
    }
    private function typeComponent2($answers) {
        if(isset($answers['type']) && $answers['type'] == 'component' && $answers['items'][1]["type"] =="text" ){
            
        }else{
            // code nay ve cac dong 2->cuoi ? KN ko phai (day chi la flow cuoi cung truoc khi render).
            if($answers["type"]=="text" && isset($answers["component_id"]) && $answers["component_id"]==2){
            }else{
                //echo '<div class="selectItem"  border="0">';
            }
        }
    }

    // Used in foreach
    private function buildDateTime() {
        return '<item>
                    <question question_id="birthdate" sentence="生年月日（西暦）">
                        <type><![CDATA[text]]></type>
                        <inherit><![CDATA[1]]></inherit>
                        <maxlength><![CDATA[10]]></maxlength>
                        <validation><![CDATA[require,date,date_max|today]]></validation>
                    </question>
                </item>';
    }

    private function buildComponent($answers, $base_id) {
        foreach($answers["items"] as $key=>$val) {
            $id=$base_id."-".$key;
            $an0["answers"] = $val;
            $an0["question_id"]=$id;
            if($val["type"]=="text") {
                $onchangemode = $val['onchange']['mode'];
                $this->foreachItemsPreexamComponent($val,$id,true,$onchangemode);
            }
            else
            $this->foreachItems($val,$id);
            if($val["onchange"]["mode"] == "auto_adjust_subset_num")
            {
            /* 家族の現在の病気について */ 
            }
            
            if($val["onchange"]["mode"] == "change_subset_num")
            {
                foreach($val['items'] as $keyT=>$valT)
                {
                    $idT= $id."-".$keyT;
                
                foreach($val["sub"] as $key11=>$val11) {
                    $subset_num = floor(($key11 + 1) / 2);
                    if($key11 % 2 == 1){
                        // TODO what data use printed.
                        // $this->allData .= '< id='. $idT . $subset_num .' class="selectItem allkenshin'. substr($idT,0,strpos($idT, '-')). 'subset_num'. $subset_num .'">';
                    }
                    $id1 = $idT."-".$key11;
                    $this->foreachItemsPreexamComponent($val11,$id1);

                    if($key11 % 2 == 0){
                        // $this->allData .= "</div>";
                    }
                 }
                }
            }
            if($val["onchange"]["mode"]=="add_more_questions") {
                $this->addMoreQuest($val, $id);
            }

            if($val["onchange"]["mode"]=="auto_adjust_subset_num") {
                $this->autoAdjustSubset($val, $answers, $id);
            }
        }
    }

    private function buildRadio($answers, $base_id, $wrap, $lmd) {
        $request_uri = htmlspecialchars($_SERVER["REQUEST_URI"]);
        if((!$wrap && isset($answers["prefix"])) || (strpos($request_uri,"dental-") && isset($answers["prefix"]))) {
                $this->allData .= $answers["prefix"]["text"];
                //validation radio children radio if isset prefix
                if(isset($answers["error"])) {
                }
                if(isset($answers["prefix"]["breakAfter"])){
                }
                if(strpos($request_uri,"dental-") && isset($answers["prefix"])){
                }
            } else {
                if(isset($answers["error"])) { // _error()
                }
            }
            $i=0;//add
            $countItem = 0; // First radio tag.
            //echo ($this->displayHtmlComment ? '<!-- div [0004] -->' : "");

            foreach($answers["items"] as $key => &$val) {
                $id = $base_id . "-" . $key;
                if(isset($answers["component"])) {  //add
                    $i++;
                    // Tag question has sentence (open tag when i=1)
                    if($i==1) { }
                        // Tag question has sentence
                        
                    // }
                }

                if(isset($answers["prefix"]["text"])) {
                        // $this->allData .= ' sentence="'.$answers["prefix"]["text"]. '">';
                } else {
                        // $this->allData .= '>';
                }

                if($i == 1) { // Radio tag has many option answer but only 1 input form.
                    $this->allData .= '<type>!CDATA[radio]]</type>';
                    $this->allData .= '<inherit><![CDATA[1]]></inherit>';
                    $this->allData .= '<validation><![CDATA[require]]></validation>'; // hard code
                    $this->allData .= '<items>';
                } elseif (isset($answers["component"])) { // Only hei/iie answer shown here.
                    $items = $answers['items'];
                    foreach($items as $item) {
                        $this->allData .= '<item>[!CDATA['. $item['text'] . ']]</item>';
                    }
                    // print_r($items);
                    $this->allData .= '</items>';
                } else {
                    $countItem ++;
                    $this->allData .= '<type>!CDATA[radio]]</type>';
                    $this->allData .= '<inherit><![CDATA[1]]></inherit>';
                    $this->allData .= '<validation><![CDATA[require]]></validation>'; // hard code
                    $this->allData .= '<items>';
                    $items = $answers['items'];
                    foreach($items as $item) {
                        $this->allData .= '<item>[!CDATA['. $item['text'] . ']]</item>';
                    }
                    // print_r($items);
                    $this->allData .= '</items>';
                    if($countItem >= 1) {
                        break;
                    }
                }
                
                if($id === 'qq63-1-1-2') {
                    // die('hard');
                    // id="qq63-1-1-2-1
                    // id="qq63-1-1-2-2
                    // 最後に接種した時期はいつですか？';
                    $this->allData .= $this->buildDateTime();
                }

                if(isset($val["sub"])){
                    if(isset($val["sub"]["error"])){
                    }
                    if(isset($val["sub"]["type"])){
                        if($val["sub"]["type"] == "checkbox"){
                            $this->buildCheckbox($val["sub"],$id);
                        }elseif($val["sub"]["type"] == "textarea"){
                            $this->buildCheckbox($val["sub"],$id); // y textarea 
                        }else{
                            // _ve("yet implemented.");
                        }
                    }else if(is_array($val["sub"])){
                        foreach($val["sub"] as $sub_key => &$sub_val){
                            // log
                            $this->foreachItems($sub_val,$id . "-" . $sub_key, false);
                            if (isset($sub_val["error"]) && $sub_val["type"] == 'text') {
                            }
                            //echo "";
                            // $this->log->put(__CLASS__,"0002");
                        }
                    }else{
                        // _ve("yet implemented.");
                    }
                }

            if($wrap){ }
        }
        if(!$wrap && isset($answers["prefix"])){
        }
    }

    // Used by build Component
    private function addMoreQuest($val, $id) {
        foreach($val["sub"] as $key1=>$val1) {
            $id1 = $id."-".$key1;
            if($val1["type"]=="radio")
            {
                $val1["component"]=$id;
                $an["answers"] = $val1;
                $an["question_id"]=$id1;
                $this->build($an,$this->options);
            }
            if($val1["type"]=="date_text_set") {
                $val1["component"]=$id;
                $an1["answers"] = $val1;
                $an1["question_id"]=$id1;
                $this->build($an1,$this->options);
            }
            
        }
    }
    private function autoAdjustSubset($val, $answers, $id) {
        foreach($val["sub"] as $key12=>$val12) {
                $id12 = $id."-".$key12;
            if($val12["type"]=="header") {
                $val12["component"]=true;
                $an1["answers"] = $val12;
                $an1["question_id"]=$id12;
                $this->build($an1,$this->options);
            }
            if($val12["type"]=="text" && $answers["component_id"]==2) {
                $val12["component"]=true;
                $val12["component_id"]=2;
                $an1["answers"] = $val12;
                $an1["question_id"]=$id12;
                $this->build($an1,$this->options);
            }
        }
    }


    // Functions not used by Vaccination

    public function buildDate_Text_Set($id,$val,$wrapTR=true,$wrapTD=true){

    }
    
    public function foreachItemsPreexamComponent(&$answers,$base_id,$wrap=true,$onchangemode=''){

    }

    public function buildTextPreexamComponent($id,$val,$wrapTR=true,$wrapTD=true,$onchangemode='',$error){

    }

    public function buildHeader($id,$val,$wrapTR=true,$wrapTD=true){

    }

    public function build_Text_components($id,$val,$wrapTR=true,$wrapTD=true){

    }

    public function outputErrors($errors,$spanCls=""){
        return;
    }
    public function buildCheckbox(&$answers,$base_id){
    }

    public function buildTextarea($id,$max,$value="",$error=""){
    }

    public function getAllData() {
        return $this->allData;
    }

}