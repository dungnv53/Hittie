<?php
	header('Content-Type: text/html; charset=utf-8');

	$words = [];
	$vowel1 = loadVowel('ga');

	$vowel2 = loadVowel('kya');

	$bg_color = '';
	$image = array();     // Array store output image params.
	$image['width'] = 60;  // output image width
	$image['height'] = 60; // ouput image height
	$image['name'] = '';   // output image name, (may be use key of array instead).
	$image['text_color'] = '#aabb00';  // caption text color
	$image['pointsize'] = 36;        // size of point
	$image['word_font'] = '';        // font of word (or char, vowel)
	$image['annotate_font'] = '';     // annotation text (smaller than caption text)
	$image['anno_font_size'] = '18';  // annotation text size
	$image['pos'] = 'Center';		  // Caption text align (Default is center)
	$image['type'] = "hira_vowel/";   // type for seperate many image text, eg. hira for japanese hiragana...
	$image['sys_font'] = '/usr/share/fonts/truetype/fonts-japanese-gothic.ttf'; // My system font
	// if u do not have this font u can install it over Region&Language (Ubuntu)

	generate_text_img($vowel2, $image);  // Create list of text image
	chmod_there(); // add permission (may not use)

	function generate_text_img($vowel, $img) {
		foreach($vowel as $roman => $japan) {
			create_caption_img($roman, $japan, $img);
		}

		return;
	}

	function create_caption_img($roman, $japan, $img) {
		// roman is KEY of array eg. 'pu' => 'ぷ'
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

	/**
	* Add annotate for text image.
	*/
	function annotate_img($roman, $img) {
		// echo
		exec(
		"convert ". $img['type'].$roman.".png". " -pointsize 16 -fill \"#ec9759\" ". $img['anno_font_size']. " -gravity South ". " -annotate +0+0 ". $roman. " ". $img['type']. $roman. ".png"
		);
		echo "<br/>";
	}

	/*
	* Vowel
	*/
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
