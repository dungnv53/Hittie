<?php
use kartik\select2\Select2;
use yii\helpers\Html;
use app\assets\FullCalendarAsset;
use app\assets\jQueryFileUploadAsset;
use yii\widgets\ActiveForm;

jQueryFileUploadAsset::register($this);
FullCalendarAsset::register($this);

/* @var $this yii\web\View */
?>

<div class="row">
    <div class="col-sm-4" style="padding-left: 0px">
        <?php
        //$addon = ['append' => ['content' => Html::button('選択', ['class' => 'btn btn-primary', 'title' => 'マスタ選択', 'id' => 'select_employee',]), 'asButton' => true]];
        //echo Select2::widget(['id' => "table_employees", 'name' => 'tables', 'data' => $employees, 'addon' => $addon, 'options' => ['placeholder' => 'employee'], 'pluginOptions' => ['maximumInputLength' => 10],]);
        ?>
        <br /><br /><br />
    </div>
    <button id="reload_schedule_btn" class="btn btn-primary button-reload" onclick="reload_calendar()">リロード</button>
    <div style="clear:both"></div>
</div>

<!-- The table listing the files available for upload/download -->
<table role="presentation" class="table table-striped"><tbody class="files"></tbody></table>

<div id='calendar'></div>
<div id="create_event" class="modal fade">
    <!-- fix me  -->
    <div class="modal-dialog width-dialog" style="margin-left: 240px;">
        <div class="modal-content" style="width:1200px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 id="modal_title" class="modal-title">イベント</h4>
            </div>
            <div class="modal-body">
                <div class="input-group label-margin">
                    <span class="input-group-addon width-label1" id="basic-addon1">プロジェクト名</span>
                    <input id="event_id" type="hidden" class="form-control" aria-describedby="basic-addon1">
                    <input id="event_employee_id" type="hidden" class="form-control" aria-describedby="basic-addon1">
                    <input id="event_project_name" type="text" class="form-control" aria-describedby="basic-addon1">
                </div>
                <div class="input-group label-margin">
                    <span class="input-group-addon width-label2" id="basic-addon1">担当者名</span>
                    <input id="event_id" type="hidden" class="form-control" aria-describedby="basic-addon1">
                    <input id="event_employee_name" type="text" class="form-control" aria-describedby="basic-addon1">
                </div>
                <div class="input-group label-margin">
                    <span class="input-group-addon width-label5" id="basic-addon1">イベントタイトル</span>
                    <input id="event_id" type="hidden" class="form-control" aria-describedby="basic-addon1">
                    <input id="event_actual" type="hidden" class="form-control" aria-describedby="basic-addon1">
                    <input id="event_title" type="text" class="form-control" aria-describedby="basic-addon1">
                </div>
                <div class="input-group label-margin">
                    <span class="input-group-addon label-width" id="basic-addon2">イベントの説明</span>
                    <input id="event_description" type="text" class="form-control" aria-describedby="basic-addon2">
                </div>
                <div class="input-group label-margin">
                    <span class="input-group-addon clearfix width-label3" id="basic-addon3">進捗</span>
                    <select id="event_progress" class="form-control" aria-describedby="basic-addon3">
                        <option value="">0%</option>
                        <option value="10">10%</option>
                        <option value="20">20%</option>
                        <option value="30">30%</option>
                        <option value="40">40%</option>
                        <option value="50">50%</option>
                        <option value="60">60%</option>
                        <option value="70">70%</option>
                        <option value="80">80%</option>
                        <option value="90">90%</option>
                        <option value="100">100%</option>
                    </select>
                </div>
                <div class="input-group month-dropdown label-margin">
                    <span class="input-group-addon">予定開始日時</span>
                    <input id="fromDate" type="text" class="form-control input-date radio_ask_actual">
                </div>
                <div class="input-group month-dropdown">
                    <span class="input-group-addon">予定終了日時</span>
                    <input id="endDate" type="text" class="form-control input-date radio_ask_actual">
                </div>
                <div class="input-group month-dropdown width-time">
                    <div class="input-group month-dropdown radio_actual">
                        <input type="radio" name="executetime_input_type" value="1" id="radio_date" onclick="checkRadio()">
                    </div>
                    <span class="input-group-addon fix-border">作業開始時間</span>
                    <input id="fromDateActual" type="text" class="form-control input-date radio_ask_actual">      
                </div>
                <div class="input-group float-time">
                    <input id="fromTimeActual" type="text" class="form-control width-time">
                </div>
                <div class="input-group month-dropdown">
                    <span class="input-group-addon">作業終了時間</span>
                    <input id="endDateActual" type="text" class="form-control input-date radio_ask_actual">
                </div>
                <div class="input-group float-time">
                    <input id="endTimeActual" type="text" class="form-control width-time">
                </div>
                <div class="input-group month-dropdown">
                    <div class="input-group month-dropdown radio_actual">
                        <input type="radio" name="executetime_input_type" value="0" id="radio_time" onclick="checkRadio()">
                    </div>
                    <span class="input-group-addon fix-border width-label4">作業時間</span>
                    <input id="actual_execute_time" type="text" class="form-control width-input1" onchange="Calc_time()">
                    <span class="input-group-addon">分</span>
                </div>
            </div>
            <div style="clear:both"></div>
            <div class="notice"></div>
            <!-- upload file  -->

            <div class="container">
            <?php $form = ActiveForm::begin([
                'id'     => 'fileupload',
                'action' => 'ajax-upload',
                'method' => 'POST',
                // 'enctype' => 'multipart/form-data'   
            ]); ?>
            <!-- <form id="fileupload" action="ajax-upload" method="POST" enctype="multipart/form-data"> -->

                <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
                <div class="row fileupload-buttonbar">
                    <div class="col-lg-7">
                        <!-- The fileinput-button span is used to style the file input field as button -->
                        <span class="btn btn-success fileinput-button">
                            <i class="glyphicon glyphicon-plus"></i>
                            <span>Add files...</span>
                            <input type="file" name="files[]" multiple>
                        </span>
                        <button type="submit" class="btn btn-primary start">
                            <i class="glyphicon glyphicon-upload"></i>
                            <span>Start upload</span>
                        </button>
                        <button type="reset" class="btn btn-warning cancel">
                            <i class="glyphicon glyphicon-ban-circle"></i>
                            <span>Cancel upload</span>
                        </button>
                        <button type="button" class="btn btn-danger delete">
                            <i class="glyphicon glyphicon-trash"></i>
                            <span>Delete</span>
                        </button>
                        <input type="checkbox" class="toggle">
                        <!-- The global file processing state -->
                        <span class="fileupload-process"></span>
                    </div>
                    <!-- The global progress state -->
                    <div class="col-lg-5 fileupload-progress fade">
                        <!-- The global progress bar -->
                        <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                            <div class="progress-bar progress-bar-success" style="width:0%;"></div>
                        </div>
                        <!-- The extended global progress state -->
                        <div class="progress-extended">&nbsp;</div>
                    </div>
                </div>
                <!-- The table listing the files available for upload/download -->
                <table role="presentation" class="table table-striped" id="fileupload_tbl"><tbody class="files" id="fileupload_body_tbl"></tbody></table>

                <?php ActiveForm::end(); ?>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">キャンセル</button>
                <button id="create_event_btn" type="button" class="btn btn-primary">OK</button>
            </div>

        </div> <!-- end modal content -->
    </div> <!-- end modal dialog -->
</div>

<div class="progress_template" style="display:none;">
    <div class="progress_bar">
        <div class="bar_col1">
			<label for="0"><input type="radio" class="prog_btn" value=0 >0%</label>
			<label for="20"><input type="radio" class="prog_btn" value=20 >20%</label>
			<label for="40"><input type="radio" class="prog_btn" value=40 >40%</label>
			<label for="60"><input type="radio" class="prog_btn" value=60 >60%</label>
			<label for="80"><input type="radio" class="prog_btn" value=80 >80%</label>
			<label for="100"><input type="radio" class="prog_btn" value=100 >100%</label>
        </div>
		<div class="bar_col2"><button type="submit" class="actual_start_btn" value="" >開始</button></div>
		<div class="bar_col3"><button type="submit" class="actual_end_btn" value="" >終了</button></div>
    </div>
</div>

<!-- The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    {% console.log('load scandir : ' + file); %}
    <tr class="template-upload fade">
        <td>
            <span class="preview"></span>
        </td>
        <td>
            <p class="name">{%=file.name%}</p>
            <strong class="error text-danger"></strong>
        </td>
        <td>
            <p class="size">Processing...</p>
            <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
        </td>
        <td>
            {% if (!i && !o.options.autoUpload) { %}
                <button class="btn btn-primary start" disabled>
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>Start</span>
                </button>
            {% } %}
            {% if (!i) { %}
                <button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>Cancel</span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td>
            <span class="preview">
                {% if (file.thumbnailUrl) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img src="{%=file.thumbnailUrl%}"></a>
                {% } %}
            </span>
        </td>
        <td>
            <p class="name">
                {% if (file.url) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
                {% } else { %}
                    <span>{%=file.name%}</span>
                {% } %}
            </p>
            {% if (file.error) { %}
                <div><span class="label label-danger">Error</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <span class="size">{%=o.formatFileSize(file.size)%}</span>
        </td>
        <td>
            {% if (file.deleteUrl) { %}
                <button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                    <i class="glyphicon glyphicon-trash"></i>
                    <span>Delete</span>
                </button>
                <input type="checkbox" name="delete" value="1" class="toggle">
            {% } else { %}
                <button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>Cancel</span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>

<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> -->
<!-- The Templates plugin is included to render the upload/download listings -->
<script src="//blueimp.github.io/JavaScript-Templates/js/tmpl.min.js"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="//blueimp.github.io/JavaScript-Load-Image/js/load-image.all.min.js"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="//blueimp.github.io/JavaScript-Canvas-to-Blob/js/canvas-to-blob.min.js"></script>
<!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
<script src="//netdna.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<!-- blueimp Gallery script -->
<script src="//blueimp.github.io/Gallery/js/jquery.blueimp-gallery.min.js"></script>

<script>
        //var selected_employee = $("#table_employees").val();
        var fromDatePicker, endDatePicker, fromDateActualPicker, endDateActualPicker, fromTimeActualPicker, endTimeActualPicker;
        var fCalendar;
        var events = [];
        var items = <?php echo $items; ?>;
        var actual = <?php echo $items; ?>;

        for (var i = 0; i < items.length; i++) {
            var event = items[i];
            var className = 'fc-event-width-overirde-left';
            if (event && event.id && event.start && event.end) {
                var start_time = event.start || event.start;
                var end_time = event.end || event.end;
                var actual_start = event.actual_start || event.actual_start;
                var actual_end = event.actual_end || event.actual_end;
                if (start_time && end_time) {
                    events.push({
                        'id': event.id,
                        'title': event.summary,
                        'project_name': event.project_name,
                        'employee_name': event.employee_name,
                          //'description': event.htmlLink,
                        'start': start_time,
                        'end': end_time,
                        'employee_id': event.employee_id,
                        'actual_start': actual_start,
                        'actual_end': actual_end,
                        'actual_execute_time': event.actual_execute_time,
                        'executetime_input_type': event.executetime_input_type,
                        'progress': event.progress,
                        'actual' : 0,
                        'className': className
                    });
                }
            }
        }

        for (var i = 0; i < actual.length; i++) {
            var event = actual[i];
            var className = 'finished fc-event-width-overirde-right';
            if (event && event.id && event.actual_start && event.actual_end) {
                var start_time = event.actual_start || event.actual_start;
                var end_time = event.actual_end || event.actual_end;
                var actual_start = event.start || event.start;
                var actual_end = event.end || event.end;
                if (start_time && end_time) {
                    events.push({
                        'id': event.id,
                        'title': event.summary,
                        'project_name': event.project_name,
                        'employee_name': event.employee_name,
                          //'description': event.htmlLink,
                        'start': start_time,
                        'end': end_time,
                        'employee_id': event.employee_id,
                        'actual_start': actual_start,
                        'actual_end': actual_end,
                        'actual': 1,
                        'actual_execute_time': event.actual_execute_time,
                        'executetime_input_type': event.executetime_input_type,
                        'progress': event.progress,
                        'className': className
                    });
                }
            }
        }

    $(function () {
        jQuery.cookie = function(name, value, options) {
            if (typeof value != 'undefined') { // name and value given, set cookie
                options = options || {};
                if (value === null) {
                    value = '';
                    options.expires = -1;
                }
                var expires = '';
                if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
                    var date;
                    if (typeof options.expires == 'number') {
                        date = new Date();
                        date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
                    } else {
                        date = options.expires;
                    }
                    expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
                }
                // CAUTION: Needed to parenthesize options.path and options.domain
                // in the following expressions, otherwise they evaluate to undefined
                // in the packed version for some reason...
                var path = options.path ? '; path=' + (options.path) : '';
                var domain = options.domain ? '; domain=' + (options.domain) : '';
                var secure = options.secure ? '; secure' : '';
                document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
            } else { // only name given, get cookie
                var cookieValue = null;
                if (document.cookie && document.cookie != '') {
                    var cookies = document.cookie.split(';');
                    for (var i = 0; i < cookies.length; i++) {
                        var cookie = jQuery.trim(cookies[i]);
                        // Does this cookie string begin with the name we want?
                        if (cookie.substring(0, name.length + 1) == (name + '=')) {
                            cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                            break;
                        }
                    }
                }
                return cookieValue;
            }
        };

        fromDatePicker = new Pikaday({
            field: document.getElementById('fromDate'),
            format: 'YYYY/MM/DD HH:mm:ss',
            showSeconds: true,
            onOpen: function () {
                if (!this.getDate()) {
                    this.setDate(moment().format());
                }
            },
            onSelect: function () {
                this.config().field.value = moment(this.getDate()).format(this.config().format);
            },
            onClose: function () {
                if (this.getDate()) {
                    this.config().field.value = moment(this.getDate()).format(this.config().format);
                }
            }
        });
        endDatePicker = new Pikaday({
            field: document.getElementById('endDate'),
            format: 'YYYY/MM/DD HH:mm:ss',
            showSeconds: true,
            onOpen: function () {
                if (!this.getDate()) {
                    this.setDate(moment().format());
                }
            },
            onSelect: function () {
                this.config().field.value = moment(this.getDate()).format(this.config().format);
            },
            onClose: function () {
                if (this.getDate()) {
                    this.config().field.value = moment(this.getDate()).format(this.config().format);
                }
            }
        });
        fromDateActualPicker = new Pikaday({
            field: document.getElementById('fromDateActual'),
            format: 'YYYY/MM/DD',
            showTime: false,
            onOpen: function () {
                var chk = $("#fromDateActual").val();
                if(chk != ""){
                    this.setDate(chk);
                }else{
                    this.setDate(moment().format());
                }
                // if (!this.getDate()) {
                //     this.setDate(moment().format());
                // }
            },
            onSelect: function () {
                this.config().field.value = moment(this.getDate()).format(this.config().format);
                Calc_time();
            },
            onClose: function () {
                if (this.getDate()) {
                    this.config().field.value = moment(this.getDate()).format(this.config().format);
                }
                Calc_time();
            }
        });
        fromTimeActualPicker = new Pikaday({
            field: document.getElementById('fromTimeActual'),
            format: 'HH:mm:ss',
            showSeconds: true,
            showDate: false,
            use24hour: true,
            onOpen: function () {
                var time_from = $("#fromTimeActual").val().trim();
                var date_from = $("#fromDateActual").val().trim();
                var chk = date_from + " " + time_from;
                if(chk != " "){
                    this.setDate(chk);
                }else{
                    this.setDate(moment().format());
                }
                // if (!this.getDate()) {
                //     this.setDate(moment().format());
                // }
            },
            onSelect: function () {
                this.config().field.value = moment(this.getDate()).format(this.config().format);
                Calc_time();
            },
            onClose: function () {
                if (this.getDate()) {
                    this.config().field.value = moment(this.getDate()).format(this.config().format);
                }
                Calc_time();
            }
        });
        endDateActualPicker = new Pikaday({
            field: document.getElementById('endDateActual'),
            format: 'YYYY/MM/DD',
            showTime: false,
            onOpen: function () {
                var chk = $("#endDateActual").val();
                if(chk != ""){
                    this.setDate(chk);
                }else{
                    this.setDate(moment().format());
                }                
                // if (!this.getDate()) {
                //     this.setDate(moment().format());
                // }
            },
            onSelect: function () {
                this.config().field.value = moment(this.getDate()).format(this.config().format);
                Calc_time();
            },
            onClose: function () {
                if (this.getDate()) {
                    this.config().field.value = moment(this.getDate()).format(this.config().format);
                }
                Calc_time();
            }
        });
        endTimeActualPicker = new Pikaday({
            field: document.getElementById('endTimeActual'),
            format: 'HH:mm:ss',
            showSeconds: true,
            showDate: false,
            use24hour: true,
            onOpen: function () {
                var time_end = $("#endTimeActual").val().trim();
                var date_end = $("#endDateActual").val().trim();
                var chk = date_end + " " + time_end;
                if(chk != " "){
                    this.setDate(chk);
                }else{
                    this.setDate(moment().format());
                }
                // if (!this.getDate()) {
                //     this.setDate(moment().format());
                // }
            },
            onSelect: function () {
                this.config().field.value = moment(this.getDate()).format(this.config().format);
                Calc_time();
            },
            onClose: function () {
                if (this.getDate()) {
                    this.config().field.value = moment(this.getDate()).format(this.config().format);
                }
                Calc_time();
            }
        });

		var currentDay = getCurrentDate();
		var myDefaultView = (!$.cookie('calendarDefaultView')) ? 'month' : $.cookie('calendarDefaultView');
		var myDefaultDay = (!$.cookie('calendarDefaultDay')) ? currentDay : $.cookie('calendarDefaultDay');

        fCalendar = $('#calendar').fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay'
            },
            lang: 'ja',
			defaultView: myDefaultView,
			defaultDate: moment(myDefaultDay),
			viewRender: function(view, element){
				if (view.name == 'month')
					view_start = view.title.replace(' ', '').replace(/[^0-9.]/g, ' ').trim().replace(' ', '-');
				else
					view_start = view.start.format('YYYY-MM-DD');

				$.cookie('calendarDefaultView', view.name, {expires:7, path: '/'});
				$.cookie('calendarDefaultDay', view_start, {expires:7, path: '/'});
			},
            selectable: true,
            selectHelper: true,
            editable: true,
            select: function (start, end) {
				// Removed 2015/06/13
            },
            eventClick: function (calEvent, jsEvent, view) {
				var $element = $(jsEvent.target);
				var event_id = calEvent.id;
				if( $element.hasClass("progress_bar") ) {
					return false;
				}
				else if ( $element.hasClass("prog_btn") ) {
					prog_name = $(jsEvent.target.outerHTML).attr('name');
					progress = $(jsEvent.target.outerHTML).val();
					var params = {
						id: event_id,
						progress: progress
					};
					updateEvent(params);
				}
				else if ($element.hasClass("actual_start_btn") ) {
					actual_start = getDateTime();
					var params = {
						id: event_id,
						actual_start: actual_start
					};
					updateEvent(params);
				}
				else if ( $element.hasClass("actual_end_btn") ) {
					actual_end = getDateTime();
					actual_start = calEvent.actual_start;
					duration_minutes = '';

					if (actual_start != null && actual_start != '0000/00/00 00:00:00')
					{
						var dd = moment(actual_end,"YYYY/MM/DD HH:mm:ss").diff(moment(actual_start,"YYYY/MM/DD HH:mm:ss"));
						var duration = moment.duration(dd);

						if (duration > 0){
							duration_minutes = duration.asMinutes();
							duration_minutes = Math.round(duration_minutes);
						}
					}

					var params = {
						id: event_id,
						actual_end: actual_end,
						actual_execute_time: duration_minutes
					};
					updateEvent(params);
				}
				else {
					defaultEventClick(calEvent, jsEvent, view);
				}

            },
            eventResize: function (event, delta, revertFunc) {
                updateEvent(event);
            },
            eventDrop: function (event, delta, revertFunc) {
                updateEvent(event);
            },
			// some other options,
			eventRender: function(event, element, view) {
				var duration = event.execute_time;
				if (duration)
				{
					duration = ' (' + duration + ')';
					element.find('.fc-time').append(duration);
				}

				if (view.name == 'agendaDay'){
					var id = event.id;
					var progress = event.progress;
					//var act_start = event.start.format('HH:MM:SS');
					//var act_end = event.end.format('HH:MM:SS');
					element.find('.fc-title').parents('.fc-v-event').not( ".finished" ).prepend($('.progress_template .progress_bar').clone().attr('id', 'event_'+id));
					element.find('.prog_btn').each(function(i) {
						var x = Math.floor((Math.random() * 10E4) + 1);
						$(this).attr('name', 'progress-'+x);
						$(this).attr('rel', 'progress-'+id);
					});
					element.find('input:radio[rel="progress-'+id+'"]').filter('[value="'+progress+'"]').prop('checked', true);
					//element.find('.actual_start_btn').text(act_start);
					//element.find('.actual_end_btn').text(act_end);
				}
			},
            events: events,
			axisFormat: 'HH:mm',
			timeFormat: 'HH:mm'
        });

        /*$('#select_employee').on('click', function () {
            var selected_employee = $("#table_employees").val();
            var events = [];
			var params = {
				employee_id: selected_employee,
			};
			params = JSON.stringify(params);
			App.Util.showLoadingMessage('イベントの挿入...');
			App.Util.request('refresh', params, function (response) {
				var items = response.events;
				var actual = response.actual;
				for (var i = 0; i < actual.length; i++) {
					var event = actual[i];
					var className = 'finished fc-event-width-overirde-right';
					if (event && event.id && event.actual_start && event.actual_end) {
						var start_time = event.actual_start || event.actual_start;
						var end_time = event.actual_end || event.actual_end;
						var actual_start = event.start || event.start;
						var actual_end = event.end || event.end;
						if (start_time && end_time && event.employee_id == selected_employee) {
							events.push({
								'id': event.id,
								'title': event.summary,
								'project_name': event.project_name,
								'employee_name': event.employee_name,
								  //'description': event.htmlLink,
								'start': start_time,
								'end': end_time,
                                'employee_id': event.employee_id,
								'actual_start': actual_start,
								'actual_end': actual_end,
								'progress': event.progress,
								'execute_time': event.execute_time,
                                'actual_execute_time': event.actual_execute_time,
                                'executetime_input_type': event.executetime_input_type,
                                'actual' : 1,
                                'className': className
                            });
                        }
                    }
                }
                for (var i = 0; i < items.length; i++) {
                    var event = items[i];
                    var className = 'fc-event-width-overirde-left';
                    if (event && event.id && event.start && event.end) {
                        var start_time = event.start || event.start;
                        var end_time = event.end || event.end;
                        var actual_start = event.actual_start || event.actual_start;
                        var actual_end = event.actual_end || event.actual_end;
                        if (start_time && end_time && event.employee_id == selected_employee) {
                            events.push({
                                'id': event.id,
                                'title': event.summary,
                                'project_name': event.project_name,
                                'employee_name': event.employee_name,
                                  //'description': event.htmlLink,
                                'start': start_time,
                                'end': end_time,
                                'employee_id': event.employee_id,
                                'actual_start': actual_start,
                                'actual_end': actual_end,
                                'execute_time': event.execute_time,
                                'actual_execute_time': event.actual_execute_time,
                                'executetime_input_type': event.executetime_input_type,
                                'progress': event.progress,
                                'actual' : 0,
                                'className': className
                            });
                        }
                    }
                }

                // render event
                fCalendar.fullCalendar('removeEvents');
                fCalendar.fullCalendar('addEventSource', events);         
                fCalendar.fullCalendar('rerenderEvents');
            }, function (response) {
                App.Util.errorMessageBox("失敗しました。ブラウザをリロードしてください。");
                console.log(response);
            });
        });*/

        $('#create_event').on('shown.bs.modal', function() {
            
            // alert($('#event_id').val());
            $('#fileupload').fileupload({
                url: 'ajax-upload',
                // TODO add more param here (uid, task_id ...)
            });

            var task_id = $('#event_id').val();
            console.log('in modal shown.bs ' + task_id);

            // Load existing files:
            // before upload/modify action, we need get file uploded (it use get() in UploadHandler)
            $('#fileupload').addClass('fileupload-processing');
            $.ajax({
                // Uncomment the following to send cross-domain cookies:
                //xhrFields: {withCredentials: true},
                // TODO add more param here (uid, task_id ...)
                url: $('#fileupload').fileupload('option', 'url') +'?task_id='+task_id,
                dataType: 'json',
                context: $('#fileupload')[0]
            }).always(function () {
                $(this).removeClass('fileupload-processing');
            }).done(function (result) {
                $(this).fileupload('option', 'done')
                    .call(this, $.Event('done'), {result: result});

                console.log(result);
                // $.each(result.files, function (index, file) {
                //     $('<td/>').html('<button class="btn btn-danger delete" data-type="' + file.deleteType + '" data-url="' + file.deleteUrl + '" title="Delete">' + 
                //         '<i class="glyphicon glyphicon-trash"></i>' +
                //     '<span>Delete</span></button>').appendTo('#fileupload_body_tbl');
                // });
            });
        });

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
            console.log($link);
          });
        });

        $('#create_event').on('hidden.bs.modal', function () {
            // remove old table data 
            uploaded_table = $('#fileupload_body_tbl');
            uploaded_table.empty();

            console.log('on close event_id ' + $('#event_id').val());
            // clear table


            fCalendar.fullCalendar('unselect');
            $('#event_project_name').parent().removeClass('has-error');
            $('#event_employee_name').parent().removeClass('has-error');
            $('#event_title').parent().removeClass('has-error');
            $('#fromDate').parent().removeClass('has-error');
            $('#endDate').parent().removeClass('has-error');
            $('#fromDateActual').parent().removeClass('has-error');
            $('#endDateActual').parent().removeClass('has-error');
            $('#actual_execute_time').parent().removeClass('has-error');
            $('input:radio[name=executetime_input_type]:checked').parent().removeClass('has-error');
        });

        $('#fromDateActual').change(Calc_time());
        $('#endDateActual').change(Calc_time());
        $('#actual_execute_time').change(Calc_time());
        $("#radio_time").unbind('click');
        $("#radio_date").click(function(){
            $(".notice").html("");
            $("#endDateActual").css('background-color','#fff');
            $("#endTimeActual").css('background-color','#fff');
            $("#actual_execute_time").css('background-color','#fff');
            var fromDate = $("#fromDate").val().trim();

            var actual_date_end = $("#endDateActual").val().trim();
            var actual_time_end = $("#endTimeActual").val().trim();
            
            if(actual_date_end.length == 0 || actual_time_end.length == 0){
                $("#create_event_btn").attr('disabled','disabled');
                var actual_date_start = "";
                var actual_time_start = "";
                if(fromDate.length > 0){
                    var actual_start_splt = fromDate.split(" ");
                    if (typeof actual_start_splt[0] != "undefined") {
                        if(moment(actual_start_splt[0].trim(), ["YYYY/MM/DD"], true).isValid()){
                            actual_date_start = actual_start_splt[0].trim();
                        }   
                    }

                    if (typeof actual_start_splt[1] != "undefined") {
                        if(moment(actual_start_splt[1].trim(), ["HH:mm:ss"], true).isValid()){
                            actual_time_start = actual_start_splt[1].trim();
                        }
                    }   
                }           
                var actual_date_end = "";
                var actual_time_end = "";

                $('#fromDateActual').val(actual_date_start);
                $('#fromTimeActual').val(actual_time_start);
                
                $('#endDateActual').val(actual_date_end);
                $('#endTimeActual').val(actual_time_end);
                
                $('#actual_execute_time').val(0);
            }
        });
        $("#radio_time").click(function(){
            $(".notice").html("");
            $("#endDateActual").css('background-color','#fff');
            $("#endTimeActual").css('background-color','#fff');
            $("#actual_execute_time").css('background-color','#fff');
            $("#create_event_btn").removeAttr('disabled');
            var fromDate = $("#fromDate").val().trim();
            var endDate = $("#endDate").val().trim();
            var actual_date_start = $("#fromDateActual").val().trim();
            var actual_time_start = $("#fromTimeActual").val().trim();
            var actual_date_end = $("#endDateActual").val().trim();
            var actual_time_end = $("#endTimeActual").val().trim();
            
            if(actual_date_end.length == 0 || actual_time_end.length == 0){
                var actual_date_start = "";
                var actual_time_start = "";
                if(fromDate.length > 0){
                    var actual_start_splt = fromDate.split(" ");
                    if (typeof actual_start_splt[0] != "undefined") {
                        if(moment(actual_start_splt[0].trim(), ["YYYY/MM/DD"], true).isValid()){
                            actual_date_start = actual_start_splt[0].trim();
                        }   
                    }

                    if (typeof actual_start_splt[1] != "undefined") {
                        if(moment(actual_start_splt[1].trim(), ["HH:mm:ss"], true).isValid()){
                            actual_time_start = actual_start_splt[1].trim();
                        }
                    }   
                }               

                var actual_date_end = "";
                var actual_time_end = "";
                if(endDate.length > 0){
                    var actual_end_splt = endDate.split(" ");               
                    if (typeof actual_end_splt[0] != "undefined") {
                        if(moment(actual_end_splt[0].trim(), ["YYYY/MM/DD"], true).isValid()){
                            actual_date_end = actual_end_splt[0].trim();
                        }   
                    }

                    if (typeof actual_end_splt[1] != "undefined") {
                        if(moment(actual_end_splt[1].trim(), ["HH:mm:ss"], true).isValid()){
                            actual_time_end = actual_end_splt[1].trim();
                        }
                    }   
                }   
                
                $('#fromDateActual').val(actual_date_start);
                $('#fromTimeActual').val(actual_time_start);
                
                $('#endDateActual').val(actual_date_end);
                $('#endTimeActual').val(actual_time_end);
               
                // $('#actual_execute_time').val(0);
                // Calculate time
                var duration_minutes = 0;
                var actual_start = $("#fromDateActual").val().trim();
                var actual_start_time = $("#fromTimeActual").val().trim();            

                var actual_end = $("#endDateActual").val().trim();            
                var actual_end_time = $("#endTimeActual").val().trim();

                var start_time = actual_start + " " + actual_start_time;
                var end_time = actual_end + " " + actual_end_time;
                
                var chk_start = moment(start_time, ["YYYY/MM/DD HH:mm:ss"], true).isValid();
                var chk_end = moment(end_time, ["YYYY/MM/DD HH:mm:ss"], true).isValid();

                if(chk_start && chk_end){
                    var start_date = moment(start_time);
                    var end_date = moment(end_date);

                    var dd = moment(end_time,"YYYY/MM/DD HH:mm:ss").diff(moment(start_time,"YYYY/MM/DD HH:mm:ss"));
                    var duration = moment.duration(dd);
                    
                    if(duration>0){
                        duration_minutes = duration.asMinutes();
                        duration_minutes = Math.round(duration_minutes);
                    }    
                }
                
                $("#actual_execute_time").val(duration_minutes);
            }
        });
    });
	
	function defaultEventClick(calEvent, jsEvent, view)
	{
		$('#modal_title').text('更新イベント');
		$('#event_id').val(calEvent.id);

        console.log('calEvent ' + calEvent.id);
		$('#event_progress').val(calEvent.progress);
		$('#event_project_name').val(calEvent.project_name);
		$('#event_employee_id').val(calEvent.employee_id);
		$('#event_employee_name').val(calEvent.employee_name);
		$('#event_title').val(calEvent.title);
		$('#event_actual').val(calEvent.actual);
		$('#event_description').val(calEvent.description);
		$("#fromDate").prop("readonly", true);
		$("#endDate").prop("readonly", true);
		$(".notice").html("");
		$("#endDateActual").css('background-color','#fff');
		$("#endTimeActual").css('background-color','#fff');
		$("#actual_execute_time").css('background-color','#fff');
		if (calEvent.actual == 0)
		{
			var actual_start = calEvent.actual_start;
			var actual_end = calEvent.actual_end;

			var actual_date_start = "";
			var actual_time_start = "";

			if(actual_start != null){
				var actual_start_splt = actual_start.split(" ");                    
				actual_date_start = actual_start_splt[0].trim();                    
				actual_time_start = actual_start_splt[1].trim();    
			}               

			var actual_date_end = "";
			var actual_time_end = "";
			if(actual_end != null){
				var actual_end_splt = actual_end.split(" ");
				actual_date_end = actual_end_splt[0].trim();
				actual_time_end = actual_end_splt[1].trim();
			}   
								
			$('#fromDateActual').val(actual_date_start);
			$('#fromTimeActual').val(actual_time_start);
			
			$('#endDateActual').val(actual_date_end);
			$('#endTimeActual').val(actual_time_end);
			
			// fromDateActualPicker.setDate(calEvent.actual_start.format('YYYY/MM/DD'));
			// endDateActualPicker.setDate(calEvent.actual_end.format('YYYY/MM/DD'));
			$("input[name=executetime_input_type][value=0]").prop("checked",true);
			if (calEvent.start) {
				fromDatePicker.setDate(calEvent.start.format('YYYY/MM/DD HH:mm:ss'));
			}
			if (calEvent.end) {
				endDatePicker.setDate(calEvent.end.format('YYYY/MM/DD HH:mm:ss'));
			}
		}
		else if (calEvent.actual == 1)
		{
			$('#fromDate').val(calEvent.actual_start);
			$('#endDate').val(calEvent.actual_end);
			// $('#fromDateActual').val(calEvent.start);
			// $('#endDateActual').val(calEvent.end);
			if (calEvent.start) {
				fromDateActualPicker.setDate(calEvent.start.format('YYYY/MM/DD HH:mm:ss'));
			}
			if (calEvent.end) {
				endDateActualPicker.setDate(calEvent.end.format('YYYY/MM/DD HH:mm:ss'));
			}
		}

		if (calEvent.executetime_input_type == 1){
			$('input:radio[name=executetime_input_type][id=radio_date]').prop('checked', true);
			$("#actual_execute_time").prop("readonly", true);
			$("#fromDateActual").prop("readonly", false);
			$("#endDateActual").prop("readonly", false);
			$("#fromTimeActual").prop("readonly", false);
			$("#endTimeActual").prop("readonly", false);
		} else if(calEvent.executetime_input_type == 0) {
			$('input:radio[name=executetime_input_type][id=radio_time]').prop('checked', true);
			$("#fromDateActual").prop("readonly", true);
			$("#endDateActual").prop("readonly", true);
			$("#fromTimeActual").prop("readonly", true);
			$("#endTimeActual").prop("readonly", true);
			$("#actual_execute_time").prop("readonly", false);
		} else {
			$('input:radio[name=executetime_input_type]').prop('checked', false);
			$("#fromDateActual").prop("readonly", false);
			$("#endDateActual").prop("readonly", false);
			$("#fromTimeActual").prop("readonly", false);
			$("#endTimeActual").prop("readonly", false);
			$("#actual_execute_time").prop("readonly", false);
		}

		$('#actual_execute_time').val(calEvent.actual_execute_time);
		$('#create_event').modal('show');

		$('#create_event_btn').unbind("click");
		$('#create_event_btn').click(function () {
			var actual = $('#event_actual').val();
			var title = $('#event_title').val();
			var fromDate = $('#fromDate').val();
			var endDate = $('#endDate').val();
			var fromDateActual = $('#fromDateActual').val() + ' ' + $('#fromTimeActual').val();
			var endDateActual = $('#endDateActual').val() + ' ' + $('#endTimeActual').val();
			var progress = $('#event_progress option:selected').val();
			var employee_id = $('#event_employee_id').val();
			var actual_execute_time = $('#actual_execute_time').val();
			var executetime_input_type = $('input:radio[name=executetime_input_type]:checked').val();
			if (title && fromDate && endDate) {
				calEvent.id = $('#event_id').val();
				// calEvent.project_name = project_name;
				// calEvent.employee_name = employee_name;
				calEvent.title = title;
				calEvent.progress = progress;
				calEvent.description = $('#event_description').val();
				if (actual == 0)
				{
					calEvent.start = moment(fromDate).format('YYYY/MM/DD HH:mm:ss');
					calEvent.end = moment(endDate).format('YYYY/MM/DD HH:mm:ss');
				}
				else if (actual == 1)
				{
					calEvent.start = moment(fromDateActual).format('YYYY/MM/DD HH:mm:ss');
					calEvent.end = moment(endDateActual).format('YYYY/MM/DD HH:mm:ss');
				}
				calEvent.actual_execute_time = actual_execute_time;

				var tempEvent = {
					id: $('#event_id').val(),
					// project_name: project_name,
					// employee_name: employee_name,
					title: title,
					progress: progress,
					employee_id: employee_id,
					start: moment(fromDate).format('YYYY/MM/DD HH:mm:ss'),
					end: moment(endDate).format('YYYY/MM/DD HH:mm:ss'),
					actual_start: moment(fromDateActual).format('YYYY/MM/DD HH:mm:ss'),
					actual_end: moment(endDateActual).format('YYYY/MM/DD HH:mm:ss'),
					actual_execute_time: actual_execute_time,
					executetime_input_type: executetime_input_type,
				};

				// update event to Google Calendar
				updateEvent(tempEvent);
				fCalendar.fullCalendar('updateEvent', calEvent);
				$('#create_event').modal('hide');
			}
			else {
				if (!title) {
					$('#event_title').parent().addClass('has-error');
				}
				else {
					$('#event_title').parent().removeClass('has-error');
				}
				if (!fromDate) {
					$('#fromDate').parent().addClass('has-error');
				}
				else {
					$('#fromDate').parent().removeClass('has-error');
				}
				if (!endDate) {
					$('#endDate').parent().addClass('has-error');
				}
				else {
					$('#endDate').parent().removeClass('has-error');
				}
				if (!fromDateActual) {
					$('#fromDateActual').parent().addClass('has-error');
				}
				else {
					$('#fromDateActual').parent().removeClass('has-error');
				}
				if (!endDateActual) {
					$('#endDateActual').parent().addClass('has-error');
				}
				else {
					$('#endDateActual').parent().removeClass('has-error');
				}
				if (!actual_execute_time) {
					$('#actual_execute_time').parent().addClass('has-error');
				}
				else {
					$('#actual_execute_time').parent().removeClass('has-error');
				}
				if (!executetime_input_type) {
					$('input:radio[name=executetime_input_type]:checked').parent().addClass('has-error');
				}
				else {
					$('input:radio[name=executetime_input_type]:checked').parent().removeClass('has-error');
				}
			}
		});		
	}

    function reload_calendar(){
        var actual = $('#event_actual').val();
        var title = $('#event_title').val();
        var fromDate = $('#fromDate').val();
        var endDate = $('#endDate').val();
        var fromDateActual = $('#fromDateActual').val() + ' ' + $('#fromTimeActual').val();
        var endDateActual = $('#endDateActual').val() + ' ' + $('#endTimeActual').val();
        var progress = $('#event_progress option:selected').val();
        var employee_id = $('#event_employee_id').val();
        var actual_execute_time = $('#actual_execute_time').val();
        var executetime_input_type = $('input:radio[name=executetime_input_type]:checked').val();
        var events = [];
        var params = {
            id: $('#event_id').val(),
            title: title,
            progress: progress,
            employee_id: employee_id,
            start: moment(fromDate).format('YYYY/MM/DD HH:mm:ss'),
            end: moment(endDate).format('YYYY/MM/DD HH:mm:ss'),
            actual_start: moment(fromDateActual).format('YYYY/MM/DD HH:mm:ss'),
            actual_end: moment(endDateActual).format('YYYY/MM/DD HH:mm:ss'),
            actual_execute_time: actual_execute_time,
            executetime_input_type: executetime_input_type,
        };
        params = JSON.stringify(params);
        App.Util.showLoadingMessage('イベントの挿入...');
        App.Util.request('update', params, function (response) {
                //location.reload();
                var items = response.events;
                var actual = response.actual;
                for (var i = 0; i < actual.length; i++) {
                    var event = actual[i];
                    var className = 'finished fc-event-width-overirde-right';
                    if (event && event.id && event.actual_start && event.actual_end) {
                        var start_time = event.actual_start || event.actual_start;
                        var end_time = event.actual_end || event.actual_end;
                        var actual_start = event.start || event.start;
                        var actual_end = event.end || event.end;
                        if (start_time && end_time) {
                            events.push({
                                'id': event.id,
                                'title': event.summary,
                                'project_name': event.project_name,
                                'employee_name': event.employee_name,
                                  //'description': event.htmlLink,
                                'start': start_time,
                                'end': end_time,
                                'employee_id': event.employee_id,
                                'actual_start': actual_start,
                                'actual_end': actual_end,
                                'progress': event.progress,
                                'execute_time': event.execute_time,
                                'actual_execute_time': event.actual_execute_time,
                                'executetime_input_type': event.executetime_input_type,
                                'actual' : 1,
                                'className': className
                            });
                        }
                    }
                }
                for (var i = 0; i < items.length; i++) {
                    var event = items[i];
                    var className = 'fc-event-width-overirde-left';
                    if (event && event.id && event.start && event.end) {
                        var start_time = event.start || event.start;
                        var end_time = event.end || event.end;
                        var actual_start = event.actual_start || event.actual_start;
                        var actual_end = event.actual_end || event.actual_end;
                        if (start_time && end_time) {
                            events.push({
                                'id': event.id,
                                'title': event.summary,
                                'project_name': event.project_name,
                                'employee_name': event.employee_name,
                                  //'description': event.htmlLink,
                                'start': start_time,
                                'end': end_time,
                                'employee_id': event.employee_id,
                                'actual_start': actual_start,
                                'actual_end': actual_end,
                                'execute_time': event.execute_time,
                                'actual_execute_time': event.actual_execute_time,
                                'executetime_input_type': event.executetime_input_type,
                                'progress': event.progress,
                                'actual' : 0,
                                'className': className
                            });
                        }
                    }
                }

                // render event
                fCalendar.fullCalendar('removeEvents');
                fCalendar.fullCalendar('addEventSource', events);         
                fCalendar.fullCalendar('rerenderEvents');
                
        }, function (response) {
            App.Util.errorMessageBox("失敗しました。ブラウザをリロードしてください。");
            console.log(response);
        });
    }
/*
    function send_data()
    {
        App.Util.showLoadingMessage("データを保存中...");
    }
*/
    // update event in Google Calendar
    function updateEvent(event)
    {
		var events = [];
		var params = event;
		params = JSON.stringify(params);
		App.Util.showLoadingMessage('イベントの挿入...');
		App.Util.request('update', params, function (response) {
				//location.reload();
				var items = response.events;
				var actual = response.actual;
				for (var i = 0; i < actual.length; i++) {
					var event = actual[i];
					var className = 'finished fc-event-width-overirde-right';
					if (event && event.id && event.actual_start && event.actual_end) {
						var start_time = event.actual_start || event.actual_start;
						var end_time = event.actual_end || event.actual_end;
						var actual_start = event.start || event.start;
						var actual_end = event.end || event.end;
						if (start_time && end_time) {
							events.push({
								'id': event.id,
								'title': event.summary,
								'project_name': event.project_name,
								'employee_name': event.employee_name,
								  //'description': event.htmlLink,
								'start': start_time,
								'end': end_time,
                                'employee_id': event.employee_id,
								'actual_start': actual_start,
								'actual_end': actual_end,
								'progress': event.progress,
								'execute_time': event.execute_time,
                                'actual_execute_time': event.actual_execute_time,
                                'executetime_input_type': event.executetime_input_type,
                                'actual' : 1,
                                'className': className
                            });
                        }
                    }
                }
                for (var i = 0; i < items.length; i++) {
                    var event = items[i];
                    var className = 'fc-event-width-overirde-left';
                    if (event && event.id && event.start && event.end) {
                        var start_time = event.start || event.start;
                        var end_time = event.end || event.end;
                        var actual_start = event.actual_start || event.actual_start;
                        var actual_end = event.actual_end || event.actual_end;
                        if (start_time && end_time) {
                            events.push({
                                'id': event.id,
                                'title': event.summary,
                                'project_name': event.project_name,
                                'employee_name': event.employee_name,
                                  //'description': event.htmlLink,
                                'start': start_time,
                                'end': end_time,
                                'employee_id': event.employee_id,
                                'actual_start': actual_start,
                                'actual_end': actual_end,
								'execute_time': event.execute_time,
                                'actual_execute_time': event.actual_execute_time,
                                'executetime_input_type': event.executetime_input_type,
                                'progress': event.progress,
                                'actual' : 0,
                                'className': className
                            });
                        }
                    }
                }

                // render event
                fCalendar.fullCalendar('removeEvents');
                fCalendar.fullCalendar('addEventSource', events);         
                fCalendar.fullCalendar('rerenderEvents');
		}, function (response) {
			App.Util.errorMessageBox("失敗しました。ブラウザをリロードしてください。");
			console.log(response);
		});
    }

    function checkRadio(){
        if($("#radio_date").is(':checked')) {
            $("#actual_execute_time").prop("readonly", true);
            $("#fromDateActual").prop("readonly", false);
            $("#fromTimeActual").prop("readonly", false);
            $("#endDateActual").prop("readonly", false);
            $("#endTimeActual").prop("readonly", false);
            $("#actual_execute_time").unbind('change');

        } else if ($("#radio_time").is(':checked')){
            $("#fromDateActual").prop("readonly", true);
            $("#fromTimeActual").prop("readonly", true);
            $("#endDateActual").prop("readonly", true);
            $("#endTimeActual").prop("readonly", true);
            $("#actual_execute_time").prop("readonly", false);
            $("#fromDateActual").unbind('change');
            $("#endDateActual").unbind('change');
        } else {
            $("#fromDateActual").prop("readonly", false);
            $("#endDateActual").prop("readonly", false);
            $("#actual_execute_time").prop("readonly", false);
        }
    }

    function getCurrentDate(){
        var d = new Date();

        var month = d.getMonth() + 1;
        var day = d.getDate();

        var output = d.getFullYear() + '-' +
        ((''+month).length<2 ? '0' : '') + month + '-' +
        ((''+day).length<2 ? '0' : '') + day;

        return output;
    }

	function getDateTime() {
		var now     = new Date(); 
		var year    = now.getFullYear();
		var month   = now.getMonth()+1; 
		var day     = now.getDate();
		var hour    = now.getHours();
		var minute  = now.getMinutes();
		var second  = now.getSeconds(); 
		if(month.toString().length == 1) {
			var month = '0'+month;
		}
		if(day.toString().length == 1) {
			var day = '0'+day;
		}   
		if(hour.toString().length == 1) {
			var hour = '0'+hour;
		}
		if(minute.toString().length == 1) {
			var minute = '0'+minute;
		}
		if(second.toString().length == 1) {
			var second = '0'+second;
		}   
		var dateTime = year+'/'+month+'/'+day+' '+hour+':'+minute+':'+second;   
		 return dateTime;
	}

    function Calc_time(){
        if($("#radio_date").is(':checked')) {
            var duration_minutes = 0;
            var actual_start = $("#fromDateActual").val().trim();
            var actual_start_time = $("#fromTimeActual").val().trim();            

            var actual_end = $("#endDateActual").val().trim();            
            var actual_end_time = $("#endTimeActual").val().trim();

            var start_time = actual_start + " " + actual_start_time;
            var end_time = actual_end + " " + actual_end_time;
            
            var chk_start = moment(start_time, ["YYYY/MM/DD HH:mm:ss"], true).isValid();
            var chk_end = moment(end_time, ["YYYY/MM/DD HH:mm:ss"], true).isValid();
            if(actual_end.length == 0 && actual_end_time.length > 0){
                $("#endDateActual").css('background-color','#ff4c42');
                $(".notice").html("<p style='text-align:center; color:red; '>作業終了時間入力してください</p>");
            } else if(actual_end.length > 0 && actual_end_time.length == 0){
                $("#endTimeActual").css('background-color','#ff4c42');
                $(".notice").html("<p style='text-align:center; color:red; '>作業終了時間入力してください</p>");
            } else {
                $(".notice").html("");
                $("#endDateActual").css('background-color','#fff');
                $("#endTimeActual").css('background-color','#fff');
                $("#create_event_btn").removeAttr('disabled');
                if(chk_start && chk_end){
                    var start_date = moment(start_time);
                    var end_date = moment(end_date);

                    var dd = moment(end_time,"YYYY/MM/DD HH:mm:ss").diff(moment(start_time,"YYYY/MM/DD HH:mm:ss"));
                    var duration = moment.duration(dd);
                    
                    if(duration>0){
                        duration_minutes = duration.asMinutes();
                        duration_minutes = Math.round(duration_minutes);
                    }
                }
                
                $("#actual_execute_time").val(duration_minutes);
            }
            
        } else if ($("#radio_time").is(':checked')){
            if(!$.isNumeric($("#actual_execute_time").val()) || $("#actual_execute_time").val() < 0){
                $(".notice").html("<p style='text-align:center; color:red; '>作業時間入力してください</p>");
                $("#actual_execute_time").focus();
                $("#create_event_btn").attr('disabled','disabled');
                $("#actual_execute_time").css('background-color','#ff4c42');
            } else {
                var duration_minutes = $("#actual_execute_time").val().trim();
                var endDateActual = "";
                var endTimeActual = "";
                if(duration_minutes >= 0){
                    var actual_start = $("#fromDateActual").val().trim();
                    var actual_start_time = $("#fromTimeActual").val().trim();            

                    var start_time = actual_start + " " + actual_start_time;
                    
                    var chk_start = moment(start_time, ["YYYY/MM/DD HH:mm:ss"], true).isValid();

                    if(chk_start){
                        var start_date = moment(start_time);
                        var event_id = $("#event_id").val().trim();
                         
                        /*var params = {
                        		event_id : event_id,
                        		start_time: start_time,
                        		execute_time: duration_minutes
                            };
                        params = JSON.stringify(params);
                        App.Util.showLoadingMessage('イベントの挿入...');
                        App.Util.request('getenddate', params, function (response) {*/
                        	var end_date = start_date.add({minutes: duration_minutes});
                            
                            var end_date_str = end_date.format('YYYY/MM/DD HH:mm:ss');
                            // var end_date_str = response.end_date;                            
                            var chk_end = moment(end_date_str, ["YYYY/MM/DD HH:mm:ss"], true).isValid();                            
                            if(chk_end){
                                var end_date_splt = end_date_str.split(" ");

                                if (typeof end_date_splt[0] != "undefined") {
                                    endDateActual = end_date_splt[0];   
                                }

                                if (typeof end_date_splt[1] != "undefined") {
                                    endTimeActual = end_date_splt[1];   
                                }

                                $("#endDateActual").val(endDateActual);            
                                $("#endTimeActual").val(endTimeActual);
                            }
                        /*}, function (response) {
                            App.Util.errorMessageBox("失敗しました。ブラウザをリロードしてください。");
                            console.log(response);
                        });*/
                    }	
                }
                $(".notice").html("");
                $("#actual_execute_time").css('background-color','#fff');
                $("#create_event_btn").removeAttr('disabled');
            }
        }
    }

</script>
