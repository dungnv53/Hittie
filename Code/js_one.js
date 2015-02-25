function checkFbImageSize(e) {
    var max_fb_img_size = "{{ MAX_FB_IMG_SIZE }}";
    _this = $(e).find("input[type='checkbox']");
    img_width  = $(e).find('div.col-xs-3 span.img_upload_width').text();
    img_height = $(e).find('div.col-xs-3 span.img_upload_height').val();
    alert(img_width + img_height);
    alert($(e).find("div.col-xs-3 p.dz-filename").val());

    var img_size = img_width * img_height;
    if( img_size > max_fb_img_size ) {  
        var checkCond = _this.prop('checked');
        // Switch check/unchecked state
        if (checkCond) { 
            // hide err msg
        } else {
            $('div.panel-body#fb_img_edit_upload :button').prop('disabled', true);
            $('div.panel-body#fb_img_create_upload :button').prop('disabled', true);
        }
    }
}

