/*
 * jQuery File Upload Plugin JS Example 8.9.1
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2010, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */

/* global $, window */

/* loaded from calendar, ... page
   TODO determine should use this instead of inline js in views
   */

$(function () {
    $('#create_event').on('shown.bs.modal', function() {
        clearDupplicateRow();

        // the event_id is task_detail_id 
        var event_id = $('#event_id').val();
        task_id = parseInt(event_id);     // task_detail_id, and has same value as event_id (only var name is different)

        // fileupload handler 
        $('#fileupload').fileupload({
            url: '/task-upload/ajax-upload',         // TODO we need or not add baseUrl here.
            formData: { 'task_id': task_id },
            maxFileSize: 1*1024*1024*1024,
            messages: {
                maxFileSize: '最大ファイルサイズは1GBに制限します。',
            }
        });

        // Load existing files; (load from DB not scandir in default blueimp)
        $('#fileupload').addClass('fileupload-processing');
        $.ajax({
            // Uncomment the following to send cross-domain cookies:
            //xhrFields: {withCredentials: true},
            // we need send task_id to load attachments by task
            url: $('#fileupload').fileupload('option', 'url') +'?task_id='+event_id,
            dataType: 'json',
            context: $('#fileupload')[0]
        }).always(function () {
            $(this).removeClass('fileupload-processing');
        }).done(function (result) {
            $(this).fileupload('option', 'done')
                .call(this, $.Event('done'), {result: result});
        });
    });

    // handler custom blueimp delete action
    $('#fileupload_body_tbl').on('click', 'button', function (e) {
        e.preventDefault();
        var $link = $(this);

        var req = $.ajax({
          dataType: 'json',
          url: $link.data('url'),
          type: 'GET' //DELETE
        });

        req.success(function () {
          $link.closest('tr').remove();
        });
    });
});

function clearDupplicateRow() {
    // remove dupplicate table files rows. 
    // Because we had many task_detail, each has different task_attach files
    // blueimp lib load existing file then append them to table (role=presentation), so we need clear this table before load other task files
    uploaded_table = $('#fileupload_body_tbl');
    uploaded_table.empty();
}
