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
		// echo 
		"convert -font ". $img['sys_font']. " -background grey -size ". $img['width']. "x".$img['height']. " -fill \"". $img['text_color'] . "\"  -pointsize ". $img['pointsize']. " -gravity ". $img['pos']. " caption:". $japan. " ". $img['type']. $roman. ".png"
		);
		echo '<br/>done '. $roman. "<br/>";
		// echo "<br/>";
	}

	function chmod_there() {
		exec('chmod -R 777 .');
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
				return [];
				break;
			default:
				return [];
			} // end switch type of vowel
	}


?>
