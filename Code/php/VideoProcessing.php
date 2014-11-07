<?php

namespace LaravelPrototype\Common;
/*
* TODO: explain namespace \imagick
* system/env config
* n other skill combined here.
*/
class VideoProcessing {

    public function Processing($faceImg){
        set_time_limit (300);
        putenv("OMP_THREAD_LIMIT=1");
        putenv("MAGICK_THREAD_LIMIT=1");
        apache_setenv("OMP_THREAD_LIMIT", 1);
        apache_setenv("MAGICK_THREAD_LIMIT", 1);
        //\IMagick::setResourceLimit(6, 1);
        
        $material_path = \Config::get('contants.public_path') . '/asset/material/';
        $imgPath = $material_path . "tmp/";

        $source = $material_path . 'Transform_data.xml';
        $xmlstr = file_get_contents($source);
        $xmlcont = @simplexml_load_string($xmlstr);
        $face_name = $faceImg;
        $face_dir = $imgPath . basename($face_name, ".jpg");
        $this->_new_working_dir($face_dir);

        $face = new \Imagick();
        $face->setResourceLimit(6, 1);
        $face->readImage($face_name);
        $face->resizeImage(191,232,\imagick::FILTER_LANCZOS,1); // Anh Thu dollar face make standard :)
        $face_w = $face->getimagewidth();
        $face_h = $face->getimageHeight();


        $start = time();  // debug code still here :(


        foreach($xmlcont as $frame)
        {
            if((intval($frame->time) >= 44) && (intval($frame->time) <= 79)) { // part1
                $imgFace = clone $face;
                $imgFace = $this->_transform(-2, 2.42*$face_w*0.625, 2.42*$face_h*0.625, $imgFace);
                $frm = new \Imagick($material_path . "frames_800/frame_0".$frame->time. ".png");
                $frm->setResourceLimit(6, 1);
                $frm->compositeImage($imgFace, \imagick::COMPOSITE_DSTOVER, intval(($frame->x +92.4 -241)*0.625), intval(($frame->y -20.8 -277)*0.625));
                $frm->setImageCompressionQuality(25);
                $frm->stripImage();
                $img_outp = $face_dir."/frame_0". $frame->time. ".png";
                $frm->writeImage(''.$img_outp);
            }

            else if((intval($frame->time) >= 80) && (intval($frame->time) <= 125)) { // part2
                $imgFace = clone $face;
                $imgFace = $this->_transform(-2, 2.42*$face_w*0.625, 2.42*$face_h*0.625, $imgFace);
                $frm = new \Imagick($material_path . "frames_800/frame_0".$frame->time. ".png");
                $frm->setResourceLimit(6, 1);
                $frm->compositeImage($imgFace, \imagick::COMPOSITE_DSTOVER, intval(($frame->x +62 -241)*0.625), intval(($frame->y -32.3 -277)*0.625));
                $frm->setImageCompressionQuality(25);
                $frm->stripImage();
                $img_outp = $face_dir."/frame_0". $frame->time. ".png";
                $frm->writeImage(''.$img_outp);
            } else if((intval($frame->time) >= 132) && (intval($frame->time) <= 196)) { // part3  132 (196)
                $anchor_x = 43.7;      // anchor caused by weak AE skill.
                $anchor_y = 56.6;

                $x_zoom = $frame->scl_x/100;
                $y_zoom = $frame->scl_y/100;
                $scl_x = floatval($x_zoom)*36.2/100 * $face_w;
                $scl_y = floatval($y_zoom)*39.5/100 * $face_h;
                $scl_d = sqrt($scl_x*$scl_x/4 + $scl_y*$scl_y/4);

                $scl_D = sqrt($anchor_x*$anchor_x*$x_zoom*$x_zoom + $anchor_y*$anchor_y*$y_zoom*$y_zoom);
                $diameter = abs($scl_D - $scl_d);   //

                $base_angle = atan($scl_y/$scl_x);
                $base_img_rotate = 0/180 *pi(); // Caused by when generate data from AE
                $rt = floatval($frame->rt)/180 *pi();
                $delta_rot = $base_angle + $rt + $base_img_rotate;

                $delta_x = $diameter * cos($delta_rot);
                $delta_y = $diameter * sin($delta_rot);

                $new_anchor_x = $frame->x + $delta_x;  // anchor point is in center of face image
                $new_anchor_y = $frame->y + $delta_y;


                $imgFace = clone $face;
                $imgFace = $this->_transform(floatval($frame->rt), $scl_x*0.625, $scl_y*0.625, $imgFace);
                $shift_x = sin($frame->rt/180 * pi()) *$scl_y;  // X pos shift caused by rotate
                $shift_y = sin($frame->rt/180 * pi()) *$scl_x;

                $imgBG = new \Imagick($material_path . "frames_800/frame_0".$frame->time. ".png");
                $imgBG->setResourceLimit(6, 1);
                if($frame->rt > 0) {
                    $imgBG->compositeImage($imgFace, \imagick::COMPOSITE_DSTOVER, intval(($new_anchor_x-$shift_x)*0.625), intval($new_anchor_y*0.625));
                } else {
                    $imgBG->compositeImage($imgFace, \imagick::COMPOSITE_DSTOVER, intval($new_anchor_x*0.625), intval(($new_anchor_y +$shift_y)*0.625));
                }
                $imgBG->setImageCompressionQuality(25);
                $imgBG->stripImage();
                $img_outp = $face_dir."/frame_0". $frame->time. ".png";
                $imgBG->writeImage(''.$img_outp);
            } else if((intval($frame->time) >= 377) && (intval($frame->time) <= 455)) { // part4
                $x_zoom = $frame->scl_x/100;        // TODO move to new method
                $y_zoom = $frame->scl_y/100;
                $scl_x = floatval($x_zoom)*36.2/100 * $face_w;
                $scl_y = floatval($y_zoom)*39.5/100 * $face_h;
                $scl_d = sqrt($scl_x*$scl_x/4 + $scl_y*$scl_y/4);
                $diameter = $scl_d;

                $base_angle = atan($scl_y/$scl_x);
                $rt = floatval($frame->rt)/180 *pi();
                $delta_rot = $base_angle + $rt;

                $delta_x = $diameter * cos($delta_rot);
                $delta_y = $diameter * sin($delta_rot);

                $new_anchor_x = $frame->x - $delta_x;  // anchor point is in center of face image
                $new_anchor_y = $frame->y - $delta_y;

                $imgFace = clone $face;
                $imgFace = $this->_transform(floatval($frame->rt), $scl_x*0.625, $scl_y*0.625, $imgFace); //floatval($frame->rt)
                $shift_x = sin($frame->rt/180 * pi()) *$scl_y;  // X pos shift caused by rotate
                $shift_y = sin($frame->rt/180 * pi()) *$scl_x;

                $imgBG = new \Imagick($material_path . "frames_800/frame_0".$frame->time. ".png");
                $imgBG->setResourceLimit(6, 1);

                if($frame->rt > 0) {
                    $imgBG->compositeImage($imgFace, \imagick::COMPOSITE_DSTOVER, intval(($new_anchor_x-$shift_x+5)*0.625), intval(($new_anchor_y-5)*0.625));
                } else {
                    $imgBG->compositeImage($imgFace, \imagick::COMPOSITE_DSTOVER, intval(($new_anchor_x+5)*0.625), intval(($new_anchor_y +$shift_y-5)*0.625));
                }
                $imgBG->setImageCompressionQuality(25);
                $imgBG->stripImage();
                $img_outp = $face_dir."/frame_0". $frame->time. ".png";
                $imgBG->writeImage(''.$img_outp);
            }
        }

        $this->_compose($face_dir, $material_path);
    }

    private function _transform($rotate, $scale_x, $scale_y, $face) {
        $scale_x = intval($scale_x);
        $scale_y = intval($scale_y);

        $bg = new \ImagickPixel('none');
        // $imgFace = $face;
        $face->scaleImage($scale_x, $scale_y, false);
        $face->rotateImage($bg, $rotate);
        $face->setImageCompressionQuality(25);
        $face->stripImage();
        return $face;
    }
    private function _compose($face_dir, $material_path) {
        $ffmpeg = "/usr/bin/ffmpeg";
        $videoPath = $material_path . "videos/";
        $name = rand(1, 99999);
        exec("cp -r {$material_path}buffer_800/frame_0*.png $face_dir/");
        exec("$ffmpeg -framerate 30 -i $face_dir/frame_0%03d.png -preset ultrafast -s:v 800x450 -c:v libx264 -profile:v high -crf 23 -pix_fmt yuv420p -r 30 -vb 12M  $face_dir.ts");
        // exec("$ffmpeg -i $face_dir/movies_$name.mp4 -c copy -bsf:v h264_mp4toannexb -f mpegts $face_dir.ts");
        exec("$ffmpeg -i \"concat:{$material_path}intermediate1.ts|$face_dir.ts|{$material_path}intermediate3.ts\" -c copy -bsf:a aac_adtstoasc -preset ultrafast {$videoPath}output_$name.mp4");
        exec("cp  $face_dir/frame_0392.png {$videoPath}thumbnail_$name.png");
        exec("rm -rf $face_dir/");
        exec("$ffmpeg -i {$videoPath}output_$name.mp4 -i {$material_path}audio.wav -c:v libx264 -c:a aac  -strict experimental -preset ultrafast {$videoPath}final_$name.mp4");
        unlink("{$videoPath}output_$name.mp4");
        unlink("$face_dir.ts");
        \Session::set("final_video", url("asset/material/videos") . "/final_$name.mp4");
        \Session::set("final_video_src", "{$videoPath}final_$name.mp4");
        \Session::set("final_thumbnail", url("asset/material/videos") . "/thumbnail_$name.png");
    }
    private function _new_working_dir($dir_name) {
        exec("mkdir -p $dir_name 2>&1", $o, $v);
        exec("chmod 777 $dir_name 2>&1", $o, $v);
    }

} 