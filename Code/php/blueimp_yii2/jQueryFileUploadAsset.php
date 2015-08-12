<?php
/**
 *  FullCalendar Asset for Yii2 Framework
 */

namespace app\assets;

use yii\web\AssetBundle;

/**
 * @author 
 */

class jQueryFileUploadAsset extends AssetBundle
{
    public $basePath = '@webroot';
    public $baseUrl  = '@web/jqueryfileupload';
    public $css      = [
        'jquery.fileupload.css',
        'jquery.fileupload-ui.css',
    ];

    public $js       = [
        'vendor/jquery.ui.widget.js',
        // 'jquery.iframe-transport.js',
        'jquery.fileupload.js',
        'jquery.fileupload-process.js',
        'jquery.fileupload-audio.js',
        'jquery.fileupload-image.js',
        'jquery.fileupload-video.js',
        'jquery.fileupload-validate.js',
        'jquery.fileupload-ui.js',
        // 'task_upload.js',

    ];

    public $depends  = [
        'yii\web\JqueryAsset',
    ];
}