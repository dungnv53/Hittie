<?php
class DisplaySettingsController extends BaseController {
	use ImageUploadTrait;
	
	protected $limited = 10;
	
	protected $led_display_setting;
	
	public function __construct(LedDisplaySetting $led_display_setting) 
	{
		$this->checkAuthenticate('admin.channels.index');
		$this->beforeFilter('@check_exist_channel_action', array('only' =>
			array('edit','update','destroy','show','store','create','getSort', 'doSort')));
		$this->beforeFilter('@check_exist_led_display_setting_action', array('only' =>
			array('edit','update','destroy','show')));
		
		$this->init('led_display_setting');
		$this->led_display_setting = $led_display_setting;
		View::share('with_upload', true);
		$this->imageProcessor = function($img) {
			$img->fit(90, 90);
		};
	}
	
	public function check_exist_led_display_setting_action($route, $request) {
		$variables = $route->parameters();
		if (array_key_exists("display_settings",$variables) && array_key_exists("channels",$variables)) {
			$count = LedDisplaySetting::where('id','=',$variables["display_settings"])
				->where('channel_id','=',$variables["channels"])
					->where('del_flag','=',C_NOT_DELETE )
						->count();
			if ($count != 1) {
				throw new DoNotAccessExceptionWithMessages(Lang::get('alert.display_setting_no_id'));
				return '{}';
			}
		}
	}
	
	public function create($id = null) {
		if(Input::get('led_type'))
		{
			$this->checkLedType(Input::get('led_type'));
		}
		$model  = $this->formModel(null,'create');
		$model->display_flag = 1;
		if(Input::get('led_type') == '8' || Input::get('led_type') == '9')
		{
			$total_file = LedDisplaySetting::where('del_flag',C_NOT_DELETE)
				->whereIn('type',array(8,9))
					->where('channel_id','=',$id)
						->count();
			View::share(compact('total_file'));
		}
   	
		if(Input::get('led_type') == '6'){
			if(count(Input::old()) > 0 ){
				$model->scroll_flag = Input::old('scroll_flag');
			}else{
				$model->scroll_flag = 1;
			}
		}
		View::share(compact('model','id'));
		return View::make('meta.custom_type_2_layout_form');
	}

	public function store($id=null) {
		$ledType = Input::get('led_type');
	
		switch($ledType) {
			case "3":
			case "4":
			case "5":
			case "6":
			case "7":
			return $this->processType3(Input::all(),$id,$ledType);
			break;
			case "8":
			return $this->processType8(Input::all(),$id,$ledType);
			break;
			case "9":
			return $this->processType9(Input::all(),$id,$ledType);
			break;
		}
	}
	
	private function processType9($inputs,$id,$ledType=9,$model=null) {
		if(is_null($model)) {
			// Create
			$validation = LedDisplaySetting::validate($inputs, 9);
			//validate data
			if ($validation->fails()) {
				$messages = $validation->messages();
				$data = array();
				foreach(['display_flag','fade_pattern','display_second','upload'] as $field) {
					$str = $messages->first($field);
					if (!empty($str)) {
						$data[$field] = "{$str}";
					}
				}

				if (isset($_FILES['upload']) && $_FILES['upload']['error']){
					$data['upload'] = "選択したファイルが50MBを超えています";
				}
				return Response::json(array('code' => 1, 'message' => 'ERROR VALIDATE','data'=>$data));
			} else {
				$inputs = $this->convertFlag($inputs);
				$total_file = LedDisplaySetting::where('del_flag',C_NOT_DELETE)
					->whereIn('type',array(8,9))
						->where('channel_id','=',$id)
							->count();
				if($total_file >= 10) {
					// Do not upload greate 10 files
					$data =  array(
						'upload' => "<p class='text-red tx_L pl10 pt10 append-error'><i class='fa'></i>動画・静止画は全チャンネルあわせて10件まで登録できます</p>"
					);
					return Response::json(array('code' => 1, 'message' => 'LIMITED','data'=>$data));
					
					// return Redirect::back()
					// 	   	                ->withInput()
					// 	   	                ->withErrors('動画・静止画は全チャンネルあわせて10件まで登録できます');
				}

				$data = array(
					'type'    => 9,
					'led_display_setting_name' => $inputs['led_display_setting_name'],
					'display_flag' => $inputs['display_flag'],
					'fade_pattern'	=> $inputs['fade_pattern'],
					'display_second' => $inputs['display_second'],
					'channel_id'  => $id,
				);
			    
				$led_setting = $this->led_display_setting->create($data);
				$temp_name = $_FILES['upload']['tmp_name'];  
				$file_name = $_FILES['upload']['name'];  
				$file_name_relative =  uploaded_signature_video('led_display_setting', $led_setting->id);  
			
				$upload_dir = storage_path()."/upload/led_display_setting/{$led_setting->id}_".$file_name_relative.'_'.$file_name; 
		 	  
				$file_path = $upload_dir;  
				if(move_uploaded_file($temp_name, $file_path))   {  
					$led_setting->file_name = $file_name;
					$led_setting->save();
				}
				Session::flash('flash.heart','led_display_setting');
				Session::flash('flash.form_type','create');
				Session::flash('flash.save_id',$led_setting->id);
				Session::flash('flash.channel_id',$id);
				Session::flash('flash.led_display_setting',$led_setting->id);
				//return Redirect::to('complete');  
			   
				return Response::json(array('code' => 0, 'message' => 'success','data'=>array(
					'url_redirect' => url('/complete')
				)));	
			} // End validation success

		} else { 		// $ model not null
			// Edit
			$validation = Validator::make($inputs,array(
				'display_flag' =>['numeric','between:0,1'],
				'fade_pattern' =>['required','numeric','between:0,3'],
				'display_second' =>['required','numeric','between:1,180'],
			));

			if ($validation->fails()) {
				$data = array();
				$messages = $validation->messages();
				foreach(['display_flag','fade_pattern','display_second','upload'] as $field) {
					$str = $messages->first($field);
					if (!empty($str)) {
						$data[$field] = "{$str}";
					}
				
				}
				if (isset($_FILES['upload']) && $_FILES['upload']['error']){
					$data['upload'] = "選択したファイルが50MBを超えています";
				}
				return Response::json(array('code' => 1, 'message' => 'ERROR VALIDATE','data'=>$data));
			} else {
				$inputs = $this->convertFlag($inputs);
				if(Input::hasFile('upload')) {		
					$validation_file = Validator::make($inputs,array(
						'upload'       =>['mimes:bmp,gif,jpg,jpeg,png','max:51200'],
					));
					if ($validation_file->fails()){
						$data2 = array();
						$messages2 = $validation_file->messages();
						foreach(['upload'] as $field) {
							$str = $messages2->first($field);
							if (!empty($str)) {
								$data2[$field] = "{$str}";
							}
				
						}
						return Response::json(array('code' => 1, 'message' => 'ERROR VALIDATE','data'=>$data2));
					} else {
						$temp_name = $_FILES['upload']['tmp_name'];  
						$file_name = $_FILES['upload']['name'];  
						$file_name_relative =  uploaded_signature_video('led_display_setting', $model->id);  
			
						$upload_dir = storage_path()."/upload/led_display_setting/{$model->id}_".$file_name_relative.'_'.$file_name;
		 	  
						$file_path = $upload_dir;  
					
						if (file_exists(storage_path()."/upload/led_display_setting/{$model->id}_".$file_name_relative.'_'.$model->file_name)) {
							File::delete(storage_path()."/upload/led_display_setting/{$model->id}_".$file_name_relative.'_'.$model->file_name);
						}
						if(move_uploaded_file($temp_name, $file_path))   {  
							$model->file_name = $file_name;
							$model->display_flag = $inputs['display_flag'];
							$model->fade_pattern = $inputs['fade_pattern'];
							$model->display_second = $inputs['display_second'];
							$model->save();
						}  
					
					}
				} else {
					$model->display_flag = $inputs['display_flag'];
					$model->fade_pattern = $inputs['fade_pattern'];
					$model->display_second = $inputs['display_second'];
					$model->led_display_setting_name = $inputs['led_display_setting_name'];
					$model->save();
				
				}	
				Session::flash('flash.heart','led_display_setting');
				Session::flash('flash.form_type','edit');
				Session::flash('flash.save_id',$model->id);
				Session::flash('flash.channel_id',$model->channel_id);
				Session::flash('flash.led_display_setting',$model->id);
		   
				return Response::json(array('code' => 0, 'message' => 'no_file','data'=>array(
					'url_redirect' => url('/complete')
				)));	
		   
				//return Redirect::to('complete');  
			}
		} // End edit
	}
	
	private function processType8($inputs,$id,$ledType=8,$model=null) {
		if(is_null($model)) {
			// Create
			$validation = LedDisplaySetting::validate($inputs, 8);

			//validate data
			if ($validation->fails()) {
				$messages = $validation->messages();
				$data = array();
				foreach(['display_flag','fade_pattern','display_second','upload'] as $field) {
					$str = $messages->first($field);
					if (!empty($str)) {
						$data[$field] = "{$str}";
					}
				
				}
				if (isset($_FILES['upload']) && $_FILES['upload']['error']) {
					$data['upload'] = "選択したファイルが50MBを超えています";
				}
				return Response::json(array('code' => 1, 'message' => 'ERROR VALIDATE','data'=>$data));
			} else {
				$inputs = $this->convertFlag($inputs);
				$total_file = LedDisplaySetting::where('del_flag',C_NOT_DELETE)
					->whereIn('type',array(8,9))
						->where('channel_id','=',$id)
							->count();
				if($total_file >= 10) {
					// Do not upload greate 10 files
					$data =  array(
						'upload' => "<p class='text-red tx_L pl10 pt10 append-error'><i class='fa'></i>動画・静止画は全チャンネルあわせて10件まで登録できます</p>"
					);
					return Response::json(array('code' => 1, 'message' => 'LIMITED','data'=>$data));
				}
				$data = array(
					'type'    => 8,
					'led_display_setting_name' => $inputs['led_display_setting_name'],
					'display_flag' => $inputs['display_flag'],
					'fade_pattern'	=> $inputs['fade_pattern'],
					'channel_id'  => $id,
				);
				
				if (isset($inputs['display_second'])) {
					$data = array_add($data,'display_second',$inputs['display_second']);
				}
			
				$led_setting = $this->led_display_setting->create($data);
				$temp_name = $_FILES['upload']['tmp_name'];  
				$file_name = $_FILES['upload']['name'];  
				$file_name_relative =  uploaded_signature_video('led_display_setting', $led_setting->id);  
			
				$upload_dir = storage_path()."/upload/led_display_setting/{$led_setting->id}_".$file_name_relative.'_'.$file_name; 
		 	  
				$file_path = $upload_dir;  
				if(move_uploaded_file($temp_name, $file_path)) {  
					$led_setting->file_name = $file_name;
					$led_setting->save();
				}  
				
				Session::flash('flash.heart','led_display_setting');
				Session::flash('flash.form_type','create');
				Session::flash('flash.save_id',$led_setting->id);
				Session::flash('flash.channel_id',$id);
				Session::flash('flash.led_display_setting',$led_setting->id);

				return Response::json(array('code' => 0, 'message' => 'success','data'=>array(
					'url_redirect' => url('/complete')
				)));	
			} // End success validate
		} else {
			// Edit
			$rule = array(
				'led_display_setting_name' => ['required', 'min:1', 'max:60'],
				'display_flag' =>['numeric','between:0,1'],
				'fade_pattern' =>['required','numeric','between:0,3']
			);
			if(isset($inputs['display_second'])) {
				$rule += ['display_second'=>['required','numeric','between:1,180']];
			}
			$validation = Validator::make($inputs,$rule);

			if ($validation->fails()) {
				$data = array();
				$messages = $validation->messages();
				foreach(['display_flag','fade_pattern','display_second','upload'] as $field) {
					$str = $messages->first($field);
					if (!empty($str)) {
						//$data[$field] = "<p class='text-red tx_L pl10 pt10 append-error'><i class='fa'></i>{$str}</p>";
						$data[$field] = "{$str}";
					}
				}
				if (isset($_FILES['upload']) && $_FILES['upload']['error']) {
					$data['upload'] = "選択したファイルが50MBを超えています";
				}
				return Response::json(array('code' => 1, 'message' => 'ERROR VALIDATE','data'=>$data));
			
				// $messages = $validation->messages();
				// return Response::json(array('code' => 1, 'message' => 'ERROR VALIDATE','data'=>$messages));

			} else {    // success validate
				$inputs = $this->convertFlag($inputs);

				// Handling upload
				if(isset($_FILES['upload'])) {
					$validation_file = Validator::make($inputs,array(
						'upload'       =>['required','mimes:mp4','max:51200'],
					));
					if ($validation_file->fails()) {
						$messages = $validation_file->messages();
						$data2 = array();
						$messages = $validation_file->messages();
						foreach(['upload'] as $field) {
							$str = $messages->first($field);
							if (!empty($str)) 
								$data2[$field] = "{$str}";
						}
					}
					return Response::json(array('code' => 1, 'message' => 'ERROR VALIDATE','data'=>$data2));
					
				} else { // upload new file
					$temp_name = $_FILES['upload']['tmp_name'];  
					$file_name = $_FILES['upload']['name'];  
					$file_name_relative =  uploaded_signature_video('led_display_setting', $model->id);  
		
					$upload_dir = storage_path()."/upload/led_display_setting/{$model->id}_".$file_name_relative.'_'.$file_name;
	 	  
					$file_path = $upload_dir;  
				
					if (file_exists(storage_path()."/upload/led_display_setting/{$model->id}_".$file_name_relative.'_'.$model->file_name)) {
						File::delete(storage_path()."/upload/led_display_setting/{$model->id}_".$file_name_relative.'_'.$model->file_name);
					}
					if(move_uploaded_file($temp_name, $file_path)) {  
						$model->file_name = $file_name;
						$model->display_flag = $inputs['display_flag'];
						$model->fade_pattern = $inputs['fade_pattern'];
						if(isset($inputs['display_second'])) {
							$model->display_second = $inputs['display_second'];
						}
   						
						$model->save();
					}  
				}
			} else {
				$model->display_flag = $inputs['display_flag'];
				$model->fade_pattern = $inputs['fade_pattern'];
				if(isset($inputs['display_second'])) {
					$model->display_second = $inputs['display_second'];
				} else {
					if(!is_null($model->display_second)) {
						$model->display_second = null;
					}
				}
				$model->save();
			}

			Session::flash('flash.heart','led_display_setting');
			Session::flash('flash.form_type','edit');
			Session::flash('flash.save_id',$model->id);
			Session::flash('flash.channel_id',$model->channel_id);
			Session::flash('flash.led_display_setting',$model->id);
			return Response::json(array('code' => 0, 'message' => 'no_file','data'=>array(
				'url_redirect' => url('/complete')
			)));	

			// }
		} // End edit
	}
	
	public function show($channel_id,$display_setting=null) {
		//View::share(compact('channel_id','display_setting'));
		$model = $this->_model($display_setting);
	
		if ($model->type == 8) {
			//video
			$data = [
				function($model){
					return ['type', alter_val_by_text('hard_title.led_type',$model->type)];
				},
				'led_display_setting_name',
				function($model){
					return ['display_flag', alter_val_by_text('hard_title.led_display_flag',$model->display_flag)];
				},
				function($model){
					return ['fade_pattern', alter_val_by_text('hard_title.led_fade_pattern',$model->fade_pattern)];
				},
				function($model){
					return ['file_name',$model->file_name];
				},
			];
			if($model->display_second > 0){
				$data1 = [
					function($model){
						return ['is_choose_time','表示時間を指定'];
					},
					function($model){
						return ['display_second', $model->display_second." 秒"];
					}
				];
			} else {
				$data1 = [
					function($model){
						return ['is_choose_time','動画の再生時間を使用'];
					},
				];
			}
			$data2 = [
				function($model){
					$total_file = LedDisplaySetting::where('del_flag',C_NOT_DELETE)
						->whereIn('type',array(8,9))
							->where('channel_id','=',$model->channel_id)
								->count();
					return ['count_file',$total_file."/10"];
				},
			];
		
			$data = array_merge($data, $data1, $data2);		
		} elseif ($model->type == 9) {
			//image
			$data = [
				function($model){
					return ['type', alter_val_by_text('hard_title.led_type',$model->type)];
				},
				'led_display_setting_name',
				function($model){
					return ['display_flag', alter_val_by_text('hard_title.led_display_flag',$model->display_flag)];
				},
				function($model){
					return ['fade_pattern', alter_val_by_text('hard_title.led_fade_pattern',$model->fade_pattern)];
				},
				function($model){
					return ['display_second', $model->display_second." 秒"];
				},
				function($model){
					return ['file_name',$model->file_name];
				},
				function($model){
					$total_file = LedDisplaySetting::where('del_flag',C_NOT_DELETE)
						->whereIn('type',array(8,9))
							->where('channel_id','=',$model->channel_id)
								->count();
					return ['count_file',$total_file."/10"];
				}
			];
			// End led type 9 (media type)
		} else {
			$data = [
				function($model){
					return ['type', alter_val_by_text('hard_title.led_type',$model->type)];
				},
				'led_display_setting_name',
				function($model){
					return ['display_flag', alter_val_by_text('hard_title.led_display_flag',$model->display_flag)];
				},
				function($model){
					return ['centering_flag', Lang::get('hard_title.centering_flag')[$model->centering_flag]];
				},
				function($model){
					return ['display_second', $model->display_second . '秒'];
				},
				function($model){
					return ['bg_color', "<div class='colorpicker-element'><span class='input-group-addon' style='width:24px; height:24px; padding:0px 0px;'><i style='background-color: ".$model->bg_color.";width:24px; height:24px;'></i></span></div>"];
				},
				function($model){
					return ['text_color', "<div class='colorpicker-element'><span class='input-group-addon' style='width:24px; height:24px; padding:0px 0px;'><i style='background-color: ".$model->text_color.";width:24px; height:24px'></i></span></div>"];
				},
				'flashing_flag',
				function($model){
					switch($model->bold_flag + $model->italic_flag) {
						case 2:
						return ['font', "<b><i>".alter_val_by_text('hard_title.led_font',$model->font)."</i></b>"];
						break;
						case 1:
						if($model->bold_flag) {
							return ['font', "<b>".alter_val_by_text('hard_title.led_font',$model->font)."</b>"];
						} else {
							return ['font', "<i>".alter_val_by_text('hard_title.led_font',$model->font)."</i>"];
						}
						break;
						case 0:
						return ['font', alter_val_by_text('hard_title.led_font',$model->font)];
						break;
						default: 
						return ['font', alter_val_by_text('hard_title.led_font',$model->font)];
						break;
					}
				},
				function($model) {
					return ['text_size', $model->text_size.'pt'];
				},
				function($model) {
					return ['fade_pattern', alter_val_by_text('hard_title.led_fade_pattern',$model->fade_pattern)];
				}
			];
	
			if($model->type == 7) {
				array_splice($data, 2, 2);
				array_splice($data, 4, 1);
				$data1 = [
					function($model){
						return ['news_contents_length', alter_val_by_text('hard_title.led_news_contents_length',$model->news_contents_length)];
					}
				];
				array_splice($data, 6, 0, $data1);
		
				$data2 = [
					function($model){
						return ['news_display_number', alter_val_by_text('hard_title.led_news_display_number',$model->news_display_number)];
					},
					function($model){
						return ['scroll_speed', alter_val_by_text('hard_title.led_scroll_speed',$model->scroll_speed)];
					},
					function($model){
						return ['scroll_pattern', '右→左'];
					}
				];
				$data = array_merge($data, $data2);
			}

			if($model->type == 5) {
				$disp_clock_flag = function($model){
					return ['disp_clock_flag', Lang::get('flag.disp_clock_flag')[$model->disp_clock_flag]];
				};
				array_splice($data, 2, 0, $disp_clock_flag);
			}
	
			if($model->type == 4) {
				$target_date = function($model){
					return ['target_date', $model->target_date];
				};
				array_splice($data, 4, 0, $target_date);
			}
	
			if($model->type == 6) {
				array_splice($data, 2, 2);
				$data1 = [
					'message',
					function($model){
						return ['scroll_flag', Lang::get('flag.scroll_flag')[$model->scroll_flag]];
					},
				];
				$data = array_merge($data, $data1);
				if($model->scroll_flag == 1) {
					$data2 = [
						function($model){
							return ['scroll_number', alter_val_by_text('hard_title.led_scroll_number',$model->scroll_number)];
						},
						function($model){
							return ['scroll_speed', alter_val_by_text('hard_title.led_scroll_speed',$model->scroll_speed)];
						},
						function($model){
							return ['scroll_pattern', alter_val_by_text('hard_title.led_scroll_pattern',$model->scroll_pattern)];
						},	
					];
					$data = array_merge($data, $data2);
				} else {
					$data2 = [
						function($model){
							return ['centering_flag', Lang::get('hard_title.centering_flag')[$model->centering_flag]];
						},
						function($model){
							return ['display_second', $model->display_second . '秒'];
						},	
					];
					$data = array_merge($data, $data2);
				}	
			}
		}

		$this->_detail($data);
		Session::flash('type','detail');
		$view = View::make('meta.detail', [
			'model' => $model,
			'id' => $display_setting,
			'channel_id'=>$channel_id,
			]);
		return $view;
	}
	
	private function processType3($inputs,$channel_id,$type=3,$led_display_setting=null,$model=null) {
		$validation = LedDisplaySetting::validate($inputs,$type);

		if ($validation->fails()) {
			return Redirect::back()
				->withInput()
					->withErrors($validation);
		} else {
			$inputs = $this->convertFlag($inputs);
			if(is_null($led_display_setting)) {
				if($type == 7){
					$data = array(
						'type' => $type,
						'led_display_setting_name' => $inputs['led_display_setting_name'],
						'display_flag'	=> $inputs['display_flag'],
						'bg_color'		=> $inputs['bg_color'],
						'text_color'	=> $inputs['text_color'],
						'font'	=> $inputs['font'],
						'bold_flag'	=> $inputs['bold_flag'],
						'italic_flag'	=> $inputs['italic_flag'],
						'text_size'	=> $inputs['text_size'],
						'fade_pattern'	=> $inputs['fade_pattern'],
						'channel_id'    =>$channel_id,
						'modified_user'   => Auth::user()->id,
						'news_contents_length'	=> $inputs['news_contents_length'],
						'news_display_number'	=> $inputs['news_display_number'],
						'scroll_speed'	=> $inputs['scroll_speed'],
						'scroll_pattern'	=> 1,
					);
				} else {
					$data = array(
						'type' => $type,
						'led_display_setting_name' => $inputs['led_display_setting_name'],
						'display_flag'	=> $inputs['display_flag'],
						'centering_flag'	=> isset($inputs['centering_flag'])? 1 :0,
						'display_second'	=> $inputs['display_second'],
						'bg_color'		=> $inputs['bg_color'],
						'text_color'	=> $inputs['text_color'],
						'flashing_flag'	=> isset($inputs['flashing_flag'])? 1 :0,
						'font'	=> $inputs['font'],
						'bold_flag'	=> $inputs['bold_flag'],
						'italic_flag'	=> $inputs['italic_flag'],
						'text_size'	=> $inputs['text_size'],
						'fade_pattern'	=> $inputs['fade_pattern'],
						'channel_id'    =>$channel_id,
						'modified_user'   => Auth::user()->id
					);	
				}
				if($type == 4){
					$data += ['target_date'	=> $inputs['target_date']];
				}
				if($type == 5){
					$data += ['disp_clock_flag'	=> $inputs['disp_clock_flag']];
				}
				if($type == 6){
					array_splice($data, 2, 2);
					$data += ['message'	=> $inputs['message']];
					$data += ['scroll_flag'	=> isset($inputs['scroll_flag'])? 1 :0 ];
					if($data['scroll_flag'] == 1){		
						$data += ['scroll_number'	=> $inputs['scroll_number']];
						$data += ['scroll_speed'	=> $inputs['scroll_speed']];
						$data += ['scroll_pattern'	=> $inputs['scroll_pattern']];
					}else{
						$data += ['centering_flag'	=> isset($inputs['centering_flag'])? 1 :0 ];
						$data += ['display_second'	=> $inputs['display_second']];
					}
				}
				$this->led_display_setting->create($data);
				Session::flash('flash.form_type', 'create');
			} else {
				$model->display_flag = $inputs['display_flag'];
		
				if($type == 4) {
					$model->target_date = $inputs['target_date'];
				}
				if($type == 5) {
					$model->disp_clock_flag = $inputs['disp_clock_flag'];
				}
				if($type == 6) {
					$model->message	= $inputs['message'];
					$model->scroll_flag	= isset($inputs['scroll_flag'])? 1 :0;
					if($model->scroll_flag == 1){
						$model->centering_flag	= 0;
						$model->display_second	= null;
						$model->scroll_speed	= $inputs['scroll_speed'];
						$model->scroll_pattern	= $inputs['scroll_pattern'];
						$model->scroll_number = $inputs['scroll_number'];
					}else{
						$model->centering_flag	= isset($inputs['centering_flag'])? 1 :0;
						$model->display_second	= $inputs['display_second'];
						$model->scroll_speed	= null;
						$model->scroll_pattern	= null;
						$model->scroll_number = null;
					}
				}
				if($type != 7 && $type != 6) {
					$model->display_second = $inputs['display_second'];	
				}
				if($type != 7) {
					$model->centering_flag = isset($inputs['centering_flag'])? 1 :0;
					$model->flashing_flag = isset($inputs['flashing_flag'])? 1 :0;
				}
				if($type == 7) {
					$model->news_contents_length = $inputs['news_contents_length'];
					$model->news_display_number = $inputs['news_display_number'];
					$model->scroll_speed = $inputs['scroll_speed'];
					$model->news_contents_length = $inputs['news_contents_length'];
				}
		
				$model->bg_color = $inputs['bg_color'];
				$model->text_color = $inputs['text_color'];
				$model->font = $inputs['font'];
				$model->bold_flag = $inputs['bold_flag'];
				$model->italic_flag = $inputs['italic_flag'];
				$model->text_size = $inputs['text_size'];
				$model->fade_pattern = $inputs['fade_pattern'];
				$model->led_display_setting_name = $inputs['led_display_setting_name'];     // Update new attr led_disp_s_name;
				$model->modified_user = Auth::user()->id;
				$model->save();
				Session::flash('flash.form_type', 'edit');
			}
    
			View::share('channel_id', $channel_id);
			Session::flash('flash.channel_id', $channel_id);
			Session::flash('flash.led_display_setting', $led_display_setting);
	
			Session::flash('flash.heart', 'led_display_setting');
			//Session::flash('flash.save_id', $account->id);
			return Redirect::to('complete');
			//return Redirect::route('admin.channels.show',$inputs['id']);
		}
	}
	
	public function edit($channel_id,$led_display_setting_id=null) {
		$model = $this->formModel($led_display_setting_id,'edit');

		if($model->type != Input::get('led_type'))
		{
			return Redirect::to(route('admin.led_display_settings.edit',array($channel_id,$led_display_setting_id ))."?led_type=".$model->type);
		}

		if(Input::get('led_type') == '8' || Input::get('led_type') == '9')
		{
			$total_file = LedDisplaySetting::where('del_flag',C_NOT_DELETE)
				->whereIn('type',array(8,9))
					->where('channel_id','=',$channel_id)
						->count();
			View::share(compact('total_file'));
		}
		View::share('model', $model);
		return View::make('meta.custom_layout_form');
	}
	
	public function update($channel_id,$led_display_setting=null) {
		$model = LedDisplaySetting::find($led_display_setting);
		$ledType = $model->type;
		switch($ledType) {
			case "3":
			case "4":
			case "5":
			case "6":
			case "7":
				return $this->processType3(Input::all(),$channel_id,$ledType,$led_display_setting,$model);
				break;
			case "8":
				return $this->processType8(Input::all(),$channel_id,8,$model);
				break;
			case "9":
				return $this->processType9(Input::all(),$channel_id,9,$model);
				break;
		}
	}

	public function getSort($id)
	{
		// display a lists leds of a channel
		$led_settings = LedDisplaySetting::where('channel_id',$id)
			->where('del_flag','=',C_NOT_DELETE )
				->whereIn('type',array(3,4,5,6,7,8,9))
					->orderBy('sort_order')
						->get();
		return View::make('led_setting.sort', compact('led_settings','id'));
	}
	
	public function doSort($id)
	{
		$led_settings = Input::get('led_settings');
		if (!empty($led_settings)){
			$i = 1;
			foreach($led_settings as $id_child)
			{
				$led = LedDisplaySetting::find($id_child);
				if (!is_null($led)){
					$led->sort_order = $i;
					$led->save();
				}
				$i++;
			}
	
		}
		Session::flash('flash.doSort','1');
		Session::flash('flash.heart','led_display_setting');
		Session::flash('flash.form_type','edit');
		Session::flash('flash.save_id',$id);
		Session::flash('flash.channel_id',$id);
		Session::flash('flash.led_display_setting',$id);

		return Response::json(array('code' => 0, 'message'=>'success', 'data' => null));
	}

	public function destroy($channel_id, $id = null) {
		Session::flash('type','delete');
		$ob = $this->_model($id);
		$file_name_relative =  uploaded_signature_video('led_display_setting', $ob->id);  
		if (file_exists(storage_path()."/upload/led_display_setting/{$ob->id}_".$file_name_relative.'_'.$ob->file_name)) {
			File::delete(storage_path()."/upload/led_display_setting/{$ob->id}_".$file_name_relative.'_'.$ob->file_name);
		}

		if(!is_null($ob)) {
			$ob->del_flag = 1;
			$ob->save();
		}
		return '{}';
	}

	// Disable all led_display_settings records with this channel
	public function clearChn($channel_id) {
		$led_dsps = LedDisplaySetting::where('channel_id', '=', $channel_id)->where('del_flag', '=', C_NOT_DELETE)->get();
		foreach($led_dsps as $led_dsp) {
			if( ($led_dsp->type = 8) or ($led_dsp->type = 9)) { // remove image, video file
				$ob = $this->_model($led_dsp->id);
				$file_name_relative =  uploaded_signature_video('led_display_setting', $ob->id);  
				if (file_exists(storage_path()."/upload/led_display_setting/{$ob->id}_".$file_name_relative.'_'.$ob->file_name)) {
					File::delete(storage_path()."/upload/led_display_setting/{$ob->id}_".$file_name_relative.'_'.$ob->file_name);
				}
			}
			$led_dsp->del_flag = 1;
			$led_dsp->save();
		}
	}
}