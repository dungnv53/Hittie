<?php
	header('Content-Type: text/html; charset=utf-8');

	$words = [];
	$vowel1 = loadVowel('ga');

	$vowel2 = loadVowel('kya');

	$bg_color = '';
	$image = array();
	$image['width'] = 60;
	$image['height'] = 60;
	$image['name'] = '';
	$image['text_color'] = '#aabb00';
	$image['pointsize'] = 36;
	$image['word_font'] = '';
	$image['annotate_font'] = '';
	$image['anno_font_size'] = '18';
	$image['pos'] = 'Center';
	$image['type'] = "hira_vowel/";
	$image['sys_font'] = '/usr/share/fonts/truetype/fonts-japanese-gothic.ttf';

	generate_text_img($vowel2, $image);
	chmod_there();

	function generate_text_img($vowel, $img) {
		foreach($vowel as $roman => $japan) {
			create_caption_img($roman, $japan, $img);
		}

		return;
	}

	function create_caption_img($roman, $japan, $img) {
		exec(
		"convert -font ". $img['sys_font']. " -background \"#d9d9d9\" -size ". $img['width']. "x".$img['height']. " -fill \"". $img['text_color'] . "\"  -pointsize ". $img['pointsize']. " -gravity ". $img['pos']. " caption:". $japan. " ". $img['type']. $roman. ".png"
		);

		if(file_exists($img['type']. $roman. ".png")) {
			annotate_img($roman, $img);
		}
		echo '<br/>done '. $roman. "<br/>";
		// echo "<br/>";
	}

	function chmod_there() {
		exec('chmod -R 777 .');
	}

	function annotate_img($roman, $img) {
		// echo
		exec(
		"convert ". $img['type'].$roman.".png". " -pointsize 16 -fill \"#ec9759\" ". $img['anno_font_size']. " -gravity South ". " -annotate +0+0 ". $roman. " ". $img['type']. $roman. ".png"
		);
		echo "<br/>";
	}

	function loadVowel($type) {
		switch($type) {
			case 'ga':
				return [
					'pa' => 'ぱ',
					'ba' => 'ば',
					'da' => 'だ',
					'za' => 'ざ',
					'ga' => 'が',

					'pi' => 'ぴ',
					'bi' => 'び',
					'ji' => 'ぢ',
					'jzi' => 'じ',
					'gi' => 'ぎ',

					'pu' => 'ぷ',
					'bu' => 'ぶ',
					'ju' => 'づ',
					'ju' => 'ず',
					'gu' => 'ぐ',

					'pe' => 'ぺ',
					'be' => 'べ',
					'de' => 'で',
					'ze' => 'ぜ',
					'ge' => 'げ',

					'po' => 'ぽ',
					'bo' => 'ぼ',
					'do' => 'ど',
					'zo' => 'ぞ',
					'go' => 'ご'
				];

				break;
			case 'kya':
				return [
				'pya' => 'ぴゃ',
				'bya' => 'びゃ',
				'jya' => 'じゃ',
				'gya' => 'ぎゃ',
				'rya' => 'りゃ',
				'mya' => 'みゃ',
				'hya' => 'ひゃ',
				'nya' => 'にゃ',
				'cya' => 'ちゃ',
				'shya' => 'しゃ',
				'kya' => 'きゃ',

				'pyu' => 'ぴゅ',
				'byu' => 'びゅ',
				'jyu' => 'じゅ',
				'gyu' => 'ぎゅ',
				'ryu' => 'りゅ',
				'myu' => 'みゅ',
				'hyu' => 'ひゅ',
				'nyu' => 'にゅ',
				'cyu' => 'ちゅ',
				'shyu' => 'しゅ',
				'kyu' => 'きゅ',

				'pyo' => 'ぴょ',
				'byo' => 'びょ',
				'jyo' => 'じょ',
				'gyo' => 'ぎょ',
				'ryo' => 'りょ',
				'myo' => 'みょ',
				'hyo' => 'ひょ',
				'nyo' => 'にょ',
				'cyo' => 'ちょ',
				'shyo' => 'しょ',
				'kyo' => 'きょ',
				];
				break;
			default:
				return [];
			} // end switch type of vowel
	}


?>
