<?php
$flags = array(
    'use_news_flag' => [
        1  => '有効',
        0 => '無効',
    ],
    'use_push_flag' => [
        1  => '有効',
        0 => '無効',
    ],
    'use_external_flag' => [
        1  => '有効',
        0 => '無効',
    ],
    'emergency_flag' => [
        1  => '通知中',
        0 => '未設定',
    ],
    'display_flag' => [
        1  => '有効',
        0 => '無効',
    ],
    'bold_flag' => [
        1  => '有効',
        0 => '無効',
    ],
    'italic_flag' => [
        1  => '有効',
        0 => '無効',
    ],
    'scroll_flag' => [
        1  => '有効',
        0 => '無効',
    ],
    'flashing_flag' => [
        1  => '点滅',
        0 => '非点滅',
    ],
    'is_public' => [
        1  => '公開',
        0 => '非公開',
    ],
    'is_public_flag' => [
        1  => '公開',
        0 => '非公開',
    ],
    'is_new_window' => [
        1 => '※新規ウィンドウで開く',
        0 => '※同一ウィンドウ内で開く',
    ],
    'is_send_home' => [
        1 => '自宅',
        0 => '勤務先',
    ],
    'is_subscribed' => [
        1 => 'はい',
        0 => 'いいえ',
    ],
    'is_member_join' => [
        1 => 'はい',
        0 => 'いいえ',
    ],
    'choice_source' => [
        0 => '選択してください',
        1 => '雑誌エキスパートナース',
        2 => 'ダイレクトメール',
        3 => '当社ホームページ',
        4 => 'その他',
    ],
    'choice_attend_date_all' => ':n日とも参加',
    'choice_attend_date_partial' => ':n日目のみ',
    'choice_attend_date' => [
        ATTEND_ALL => '2日とも参加',
        1  => '1日目のみ',
        2  => '2日目のみ',
    ],
    /*
     * It's possible to add items provided that
     * the key value is unique and increased as it grows
     * list will be displayed as order as it wrote here
     * the key is not related to sorting
     */
    'choice_theme' => [
        0 => '選択してください',
        1 => '栄養管理',
        2 => 'エンゼルケア',
        3 => '看護管理',
        4 => '看護記録',
        5 => '看護教育',
        6 => '急変',
        7 => '褥瘡',
        8 => 'フィジカルアセスメント',
    ],
    'choice_length' => [
        1 => '1日間',
        2 => '2日間',
    ],
    'choice_pref' => [
        0 => '選択してください',
        1 => '北海道',
        2 => '青森県',
        3 => '岩手県',
        4 => '宮城県',
        5 => '秋田県',
        6 => '山形県',
        7 => '福島県',
        8 => '茨城県',
        9 => '栃木県',
        10 => '群馬県',
        11 => '埼玉県',
        12 => '千葉県',
        13 => '東京都',
        14 => '神奈川県',
        15 => '新潟県',
        16 => '富山県',
        17 => '石川県',
        18 => '福井県',
        19 => '山梨県',
        20 => '長野県',
        21 => '岐阜県',
        22 => '静岡県',
        23 => '愛知県',
        24 => '三重県',
        25 => '滋賀県',
        26 => '京都府',
        27 => '大阪府',
        28 => '兵庫県',
        29 => '奈良県',
        30 => '和歌山県',
        31 => '鳥取県',
        32 => '島根県',
        33 => '岡山県',
        34 => '広島県',
        35 => '山口県',
        36 => '徳島県',
        37 => '香川県',
        38 => '愛媛県',
        39 => '高知県',
        40 => '福岡県',
        41 => '佐賀県',
        42 => '長崎県',
        43 => '熊本県',
        44 => '大分県',
        45 => '宮崎県',
        46 => '鹿児島県',
        47 => '沖縄県',
    ],
    'venue_region' => [
        PREFIX_HOKAIDO / PREFIX_SPACE  => 'hokkaido',
        PREFIX_TOHOKU  / PREFIX_SPACE => 'tohoku',
        PREFIX_KANTO   / PREFIX_SPACE => 'kanto',
        PREFIX_CHUBU   / PREFIX_SPACE => 'chubu',
        PREFIX_KANSAI  / PREFIX_SPACE => 'kansai',
        PREFIX_CHUGOKU / PREFIX_SPACE => 'chugoku',
        PREFIX_SHIKOKU / PREFIX_SPACE => 'shikoku',
        PREFIX_KYUSHU  / PREFIX_SPACE => 'kyushu',
    ],
    'choice_venue' => [
        0 => '選択してください',
        # Normal values
        FLAG_SPECIAL_FROM + PREFIX_HOKAIDO + 50 => '札幌',
        FLAG_SPECIAL_FROM + PREFIX_TOHOKU + 50 => '仙台',
        FLAG_SPECIAL_FROM + PREFIX_KANTO + 13 => '東京',
        FLAG_SPECIAL_FROM + PREFIX_KANTO + 50 => '横浜',
        FLAG_SPECIAL_FROM + PREFIX_KANSAI + 27 => '大阪',
        FLAG_SPECIAL_FROM + PREFIX_CHUGOKU + 33 => '岡山',
        FLAG_SPECIAL_FROM + PREFIX_CHUGOKU + 34 => '広島',
        FLAG_SPECIAL_FROM + PREFIX_KYUSHU + 40 => '福岡',
        FLAG_SPECIAL_FROM + PREFIX_KYUSHU + 46 => '鹿児島',
        
        FLAG_SPECIAL_FROM => '---',
        
        PREFIX_HOKAIDO + 50 => '札幌',
        PREFIX_HOKAIDO + 1 => '北海道',
        PREFIX_TOHOKU + 2 => '青森',
        PREFIX_TOHOKU + 50 => '仙台',
        PREFIX_TOHOKU + 3 => '岩手',
        PREFIX_TOHOKU + 4 => '宮城',
        PREFIX_TOHOKU + 5 => '秋田',
        PREFIX_TOHOKU + 6 => '山形',
        PREFIX_TOHOKU + 7 => '福島',

        PREFIX_KANTO + 8 => '茨城',
        PREFIX_KANTO + 9 => '栃木',
        PREFIX_KANTO + 10 => '群馬',
        PREFIX_KANTO + 11 => '埼玉',
        PREFIX_KANTO + 12 => '千葉',
        PREFIX_KANTO + 13 => '東京',
        PREFIX_KANTO + 50 => '横浜',
        PREFIX_KANTO + 14 => '神奈川',

        PREFIX_CHUBU + 15 => '新潟',
        PREFIX_CHUBU + 16 => '富山',
        PREFIX_CHUBU + 17 => '石川',
        PREFIX_CHUBU + 18 => '福井',
        PREFIX_CHUBU + 19 => '山梨',
        PREFIX_CHUBU + 20 => '長野',
        PREFIX_CHUBU + 21 => '岐阜',
        PREFIX_CHUBU + 22 => '静岡',
        PREFIX_CHUBU + 23 => '愛知',

        PREFIX_KANSAI + 24 => '三重',
        PREFIX_KANSAI + 25 => '滋賀',
        PREFIX_KANSAI + 26 => '京都',
        PREFIX_KANSAI + 27 => '大阪',
        PREFIX_KANSAI + 28 => '兵庫',
        PREFIX_KANSAI + 29 => '奈良',
        PREFIX_KANSAI + 30 => '和歌山',

        PREFIX_CHUGOKU + 31 => '鳥取',
        PREFIX_CHUGOKU + 32 => '島根',
        PREFIX_CHUGOKU + 33 => '岡山',
        PREFIX_CHUGOKU + 34 => '広島',
        PREFIX_CHUGOKU + 35 => '山口',

        PREFIX_SHIKOKU + 36 => '徳島',
        PREFIX_SHIKOKU + 37 => '香川',
        PREFIX_SHIKOKU + 38 => '愛媛',
        PREFIX_SHIKOKU + 39 => '高知',

        PREFIX_KYUSHU + 40 => '福岡',
        PREFIX_KYUSHU + 41 => '佐賀',
        PREFIX_KYUSHU + 42 => '長崎',
        PREFIX_KYUSHU + 43 => '熊本',
        PREFIX_KYUSHU + 44 => '大分',
        PREFIX_KYUSHU + 45 => '宮崎',
        PREFIX_KYUSHU + 46 => '鹿児島',
        PREFIX_KYUSHU + 47 => '沖縄',
    ],
    'choice_position' => [
        1 => '左',
        2 => '中央',
        3 => '右',
    ],
    'class_position' => [
        1 => 'left',
        2 => 'center',
        3 => 'right',
    ],
    'choice_status' => [
        1  => '公開中',
        2  => '準備中',
        3 => '非公開',
    ],
    // All years from beginning of app unitl now
    'choice_year' => array('' => '選択してください'),
    'JapanDate' => [
        0 => '日',
        1 => '月',
        2 => '火',
        3 => '水',
        4 => '木',
        5 => '金',
        6 => '土',
    ],
    'permission' => [
        1 => '管理者',
        2 => '代理店',
        3 => 'クライアント',
    ],
	'connection_status'=> [
        1 => '管理者',
        2 => '代理店',
        3 => 'クライアント',
	],
);
//change start year
//modify by hungn
$results = "";
/*
$results = DB::select('select last_avail_at from seminars where last_avail_at is not null and last_avail_at < "' . date('Y-m-d') . '"');
*/
$years_array = array();
if(is_array($results))foreach($results as $obj){
    $year = substr($obj->last_avail_at,0,4);
    if(!in_array($year,$years_array)){
        //他の年数があれば追加
        $years_array[] = $year;
    }
}
//$flags['list_year'] = range(2013, date('Y') + 1);
$flags['list_year'] = $years_array;

$flags['choice_position1'] =
$flags['choice_position2'] =
$flags['choice_position3'] = $flags['choice_position'];

foreach ($flags['list_year'] as $y) {
    $flags['choice_year'][$y] = "$y";
}

return $flags;