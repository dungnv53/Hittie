<?php

public function uploadPhotos($inputs,$name,$cat_id){

        $result = array();
        $arr_style = Config::get('app.image_thumbnails');
        foreach($inputs as $input){
            $size = getimagesize($input);
            $maximum = array(
                'width' => MAX_WIDTH,
                'height' => MAX_HEIGHT
            );


            $this->newTmpDir();

            if($this->validate(array('photo'=>$input))->fails() || ($size[0] > $maximum['width'] || $size[1] > $maximum['height'])) {
                $file = public_path(). "/uploads/tmp/test.txt";
                $file2 = public_path(). "/uploads/tmp/test2.txt";
                $file3 = public_path(). "/uploads/tmp/test3.txt";
                $file4= public_path(). "/uploads/tmp/test4.txt"; 
                $img_size = public_path(). "/uploads/tmp/img_size.txt";

                $max_size = $this->getBigImageSize($size[0], $size[1], MAX_WIDTH, MAX_HEIGHT);
                // if(!empty($max_size['max_width'])) {
                //     File::put($img_size, $max_size['max_width']);
                File::put($img_size, $max_size['max_width'] . " " .$max_size['max_height']);

                // need some random huh ?
                $destinationPath = public_path(). "/uploads/tmp/";
                $original_name = $input->getClientOriginalName();
                $filename       = public_path(). "/uploads/tmp/". $input->getClientOriginalName();
                $tmp_img        = public_path(). "/uploads/tmp/tmp_". $input->getClientOriginalName();

                File::put($file, " des: " .$destinationPath);
                File::put($file2, " filename: " .$tmp_img);
                // File::put($file2, " input: " .$input);

                // sleep(4);

                $input->move($destinationPath, $filename);

                copy($filename, $tmp_img);

                // resize image
                $resize_image = new \Imagick($filename); // filename or input
                $resize_image->scaleImage($max_size['max_width'], $max_size['max_height']);
                // $resize_image->scaleImage(0, 4500);
                $img_outp_path = public_path(). "/uploads/tmp/". $original_name;
                
                $resize_image->writeImage($img_outp_path);

                File::put($file3, " input: test". str_random(6));
                // Kt muc /tmp trong attachment --> chua co thi tao moi
                // Luu anh tam vao day cho tien quan ly

                $attachment = new Attachment($arr_style);
                $attachment->photo = $img_outp_path;

                $attachment->width = $resize_image->getimageWidth();
                $attachment->height = $resize_image->getimageHeight();
                $attachment->last_member_update = \Auth::user()->id;

                if($name)
                    $attachment->name = $name;
                else
                    $attachment->name = $input->getClientOriginalName();
                if($cat_id)
                    $attachment->category_id = $cat_id;


                $attachment->project_id = \Session::get("project_id");
                $attachment->save();

                File::put($file3, $attachment->photo->url('original'));
                rename($tmp_img, public_path().$attachment->photo->url('original'));

                $attachment->source = $attachment->photo->url('thumbnail');
                $result = array_add($result, $attachment->id, $attachment);

                // xoa file tam

            } else {
                $attachment = new Attachment($arr_style);
                $attachment->photo = $input;

                $attachment->width = $size[0];
                $attachment->height = $size[1];
                $attachment->last_member_update = \Auth::user()->id;

                
                if($name)
                    $attachment->name = $name;
                else
                    $attachment->name = $input->getClientOriginalName();
                if($cat_id)
                    $attachment->category_id = $cat_id;


                $attachment->project_id = \Session::get("project_id");
                $attachment->save();
                $attachment->source = $attachment->photo->url('thumbnail');
                $result = array_add($result, $attachment->id, $attachment);

                }
            // $resize_image = new \Imagick($input);
            // $resize_image->resizeImage(400,600);
            // $attachment->photo = $resize_image;

        }
        return $result;
    }