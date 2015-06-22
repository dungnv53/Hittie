<?php

return array(

    /*
    |--------------------------------------------------------------------------
    | Validation Language Lines
    |--------------------------------------------------------------------------
    |
    | The following language lines contain the default error messages used by
    | the validator class. Some of these rules have multiple versions such
    | such as the size rules. Feel free to tweak each of these messages.
    |
    */
    "accepted"         => ":attributeが承認されていません。",
    "active_url"       => ":attributeが有効なURLではありません。",
    "after"            => ":attributeには:date以降の日付を指定してください。",
    "alpha"            => ":attributeにはアルファベッドを指定してください。",
    "alpha_dash"       => ":attributeには記号以外の文字を指定してください。",
    "alpha_num"        => ":attributeには記号以外の文字を指定してください。",
    "array"            => ":attributeには配列を指定してください。",
    "array_min"        => "少なくともどちらか一つにチェックを入れてください。",
    "before"           => ":attributeには:date以前の日付を指定してください。",
    "between"          => array(
        "numeric" => ":attributeには:minから:maxまで数字を指定してください。",
        "file"    => ":attributeのファイルサイズは:min kBから:max kBまでとなります。",
        "string"  => ":attributeは:min文字から:max文字までとなります。",
        "array"   => ":attributeは:min個から:max個までとなります。"
    ),
    "boolean"          => "The :attribute field must be true or false",
    "confirmed"        => ":attributeは確認用フィールドと一致していません。",
    "date"             => "正しい日付ではありません。",
    "date_format"      => ":attributeは日付フォーマット（:format）と一致していません。",
    "different"        => ":attributeには:otherと異なる内容を指定してください。",
    "digits"           => ":attributeは:digits桁で指定してください。",
    "digits_between"   => ":attributeは:max桁以内で入力してください。",
    "email"            => ":attributeが正しくありません。",
    "exists"           => "入力された:attributeは登録されておりません。",
    "image"            => ":attributeには画像を指定してください。",
    "in"               => "選択された:attributeが正しくありません。",
    "integer"          => ":attributeには整数を指定してください。",
    "ip"               => ":attributeには有効なIPアドレスを指定してください。",
    "max"              => array(
        "numeric" => ":attributeには:max桁以下の数字を指定してください。",
        "file"    => ":attributeのサイズが:max kB以下のファイルを指定してください。",
        "string"  => ":attributeは:max文字以内で入力してください。",
        "array"   => ":attributeは:max個以下となります。"
    ),
    "mimes"            => ":attributeには:valuesタイプのファイルを指定してください。",
    "min"              => array(
        "numeric" => ":attributeには:min以上の数字を指定してください。",
        "file"    => ":attributeのファイルサイズは:min kB以上までとなります。",
        "string"  => ":attributeは:min文字以上となります。",
        "array"   => ":attributeは:max個以上となります。"
    ),
    "not_in"           => "選択された:attributeが正しくありません。",
    "numeric"          => ":attributeには半角数字を指定してください。",
    "regex"            => ":attributeは指定のフォーマットと一致していません。",
    "required"         => ":attributeを入力してください。",
    "required_if"      => ":attributeは:otherが:valueの場合は入力してください。",
    "required_with"    => ":attributeは:valuesが存在している場合は入力してください。",
    "required_with_all" => ":attributeは:valuesが存在している場合は入力してください。",
    "required_without" => ":attributeは:valuesが存在していない場合は入力してください。",
    "required_without_all" => ":attributeは:valuesが存在していない場合は入力してください。",
    "same"             => ":attributeは:otherと一致していません。",
    "size"             => array(
        "numeric" => ":attributeには:sizeを指定してください。",
        "file"    => ":attributeのファイルサイズは:size kBと一致しません。",
        "string"  => ":attributeは:size文字で指定してください。",
        "array"   => ":attributeは:size個で指定してください。"
    ),
    "unique"           => ":attributeが既に登録されています。",
    "url"              => "URLの形式が正しくありません。",
    "group"            => ":attributeを正しく入力してください。",

    'whole_length'     => ':whole_attributeが正しくありません。半角数字10桁から11桁で入力してください。',//TODO 数値を動的に
    'submodel'         => ':submodelの内容を正しく入力して下さい。',
    'only_alpha_num'   => '記号・空白のみの入力は出来ません。',
    'pref'             => '都道府県を選択してください。',
    'mobile'           => '携帯電話番号は半角数字11桁で入力してください。',
    'phone'            => '連絡先電話番号は半角数字10〜11桁で入力してください。',
    'custom_max'            => ':attributeは5桁以下の数字を指定してください。',//TODO 数値を動的に
    'custom_url'            => 'URLの形式が正しくありません。',
    /*
    |--------------------------------------------------------------------------
    | Custom Validation Language Lines
    |--------------------------------------------------------------------------
    |
    | Here you may specify custom validation messages for attributes using the
    | convention "attribute.rule" to name the lines. This makes it quick to
    | specify a specific custom language line for a given attribute rule.
    |
    */

    'custom' => array(
        // 'attribute-name' => array(
        //     'rule-name' => 'custom-message',
        // ),
        'until_at' => [
            'after' => '開始日以降の日付を指定してください。',
        ],
        "last_name" => [
            'required' => "氏名を入力してください。",
        ],
        "phone1"  => [
            'required' => "連絡先電話番号は半角数字10〜11桁で入力してください。",
        ],
        "phone2"  => [
            'required' => "連絡先電話番号は半角数字10〜11桁で入力してください。",
        ],
        "phone3"  => [
            'required' => "連絡先電話番号は半角数字10〜11桁で入力してください。",
        ],
        "mobile1"  => [
            'required' => "携帯電話番号は半角数字11桁で入力してください。",
        ],
        "mobile2"  => [
            'required' => "携帯電話番号は半角数字11桁で入力してください。",
        ],
        "mobile3"  => [
            'required' => "携帯電話番号は半角数字11桁で入力してください。",
        ],
        "zip1"    => [
            'required' => "郵便番号は半角数字で正しく入力してください。",
            'zip'      => "郵便番号は半角数字で正しく入力してください。",
        ],
        "email" => [
            'confirmed' => "メールアドレス（再入力）と一致していません。",
        ],
        "password" => [
            'confirmed' => "パスワード（再入力）と一致していません。",
        ],
        'from_time' => [
            'regex' => '半角で○○:○○の形式で入力してください。',
        ],
        'to_time' => [
            'regex' => '半角で○○:○○の形式で入力してください。',
        ],
        'is_having_lecturer' => [
            'required' => '講師情報を入力してください。',
        ],
        'is_having_paragraph' => [
            'required' => 'レポート詳細を入力してください。',
        ],
        'choice_venue' => [
            'in' => '開催地を選択してください',
        ],
        'capacity_display' => [
            'digits_between' => '定員(表示)は4桁以内の1以上の整数を半角数字で入力してください。',
            'numeric' => '定員(表示)は4桁以内の1以上の整数を半角数字で入力してください。',
        ],
        'capacity_real' => [
            'digits_between' => '定員(表示)は4桁以内の1以上の整数を半角数字で入力してください。',
            'leq' => '定員(webフォーム)は定員(表示)以下の数値を入力して下さい。',
            'numeric' => '定員(表示)は4桁以内の1以上の整数を半角数字で入力してください。',
        ],
        'venue_url' => [
            'url' => 'URLの形式が正しくありません。',
        ],
        'day_1' => [
            'required' => '1日目のキャンセル数を入力して下さい。',
        ],
        'day_2' => [
            'required' => '2日目のキャンセル数を入力して下さい。',
        ],
    ),

    /*
    |--------------------------------------------------------------------------
    | Custom Validation Attributes
    |--------------------------------------------------------------------------
    |
    | The following language lines are used to swap attribute place-holders
    | with something more reader friendly such as E-Mail Address instead
    | of "email". This simply helps us make messages a little cleaner.
    |
    */

    'attributes' => array(
	    'username'=>'ログインID',
	    'password'=>'パスワード',
		'fullname'=>'ユーザー名',
    ),

);
