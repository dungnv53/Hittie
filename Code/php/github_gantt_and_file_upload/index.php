<?php
use kartik\select2\Select2;
use yii\helpers\Html;
use app\assets\jQueryGanttAsset;
use app\assets\WbsUploadAsset;
use yii\widgets\ActiveForm;

WbsUploadAsset::register($this);
jQueryGanttAsset::register($this);

/* @var $this yii\web\View */
?>
<!-- include jquery html template used in blueimp upload table append -->
<?= $this->render('upload_template') ?>

<div class="ganttButtonBar noprint">
	<h1 style="float:left"><a href="<?= Yii::$app->homeUrl ?>"><img src="/image/logo.png"></a></h1>
	<div class="buttons">
		<!--<button onclick="$('#workSpace').trigger('undo.gantt');" class="button textual" title="undo"><span class="teamworkIcon">&#39;</span></button>
		<button onclick="$('#workSpace').trigger('redo.gantt');" class="button textual" title="redo"><span class="teamworkIcon">&middot;</span></button>-->
		<span class="ganttButtonSeparator"></span>
		<button onclick="$('#workSpace').trigger('addAboveCurrentTask.gantt');" class="button textual" title="上に挿入 (U)" alt="insert above (U)"><span class="teamworkIcon">l</span></button>
		<button onclick="$('#workSpace').trigger('addBelowCurrentTask.gantt');" class="button textual" title="下に挿入 (L)" alt="insert below (L)"><span class="teamworkIcon">X</span></button>
		<span class="ganttButtonSeparator"></span>
		<button onclick="$('#workSpace').trigger('indentCurrentTask.gantt');" class="button textual" title="右インデント (→)" alt="indent task (RIGHT ARROW)"><span class="teamworkIcon">.</span></button>
		<button onclick="$('#workSpace').trigger('outdentCurrentTask.gantt');" class="button textual" title="左インデント (←)" alt="unindent task (LEFT ARROW)"><span class="teamworkIcon">:</span></button>
		<span class="ganttButtonSeparator"></span>
		<button onclick="$('#workSpace').trigger('moveUpCurrentTask.gantt');" class="button textual" title="上に移動 (CTRL + <)" alt="move up (CTRL + <)"><span class="teamworkIcon">k</span></button>
		<button onclick="$('#workSpace').trigger('moveDownCurrentTask.gantt');" class="button textual" title="下に移動 (CTRL + >)" alt="move down (CTRL + >)"><span class="teamworkIcon">j</span></button>
		<!--<span class="ganttButtonSeparator"></span>
		<button onclick="$('#workSpace').trigger('zoomMinus.gantt');" class="button textual" title="zoom out"><span class="teamworkIcon">)</span></button>
		<button onclick="$('#workSpace').trigger('zoomPlus.gantt');" class="button textual" title="zoom in"><span class="teamworkIcon">(</span></button>-->
		<span class="ganttButtonSeparator"></span>
		<button onclick="$('#workSpace').trigger('deleteCurrentTask.gantt');" class="button textual" title="タスク削除 (キーDELETE)" alt="delete task (DELETE key)"><span class="teamworkIcon">&cent;</span></button>
		<!--<span class="ganttButtonSeparator"></span>
		<button onclick="print();" class="button textual" title="print"><span class="teamworkIcon">p</span></button>
		<span class="ganttButtonSeparator"></span>
		<button onclick="ge.gantt.showCriticalPath=!ge.gantt.showCriticalPath; ge.redraw();" class="button textual" title="Critical Path"><span class="teamworkIcon">&pound;</span></button>-->
		  &nbsp; &nbsp; &nbsp; &nbsp;
	</div>
</div>

<div id="workSpace"></div>


<script type="text/javascript">
var employees = <?php echo $employees; ?>;
var employee_id = <?php echo $employee_id; ?>;
var project_management_id = <?php echo $project_management_id; ?>;
var master_project_id = <?php echo $master_project_id; ?>;
var task_id = <?php echo $task_id; ?>;
var items = <?php echo $items; ?>;
var ge;  //this is the hugly but very friendly global var for the gantt editor
var _tasks = [];
var _roles = [];
var _resources = [];

	
$(function() {
	for (var j = 0; j < items.length; j++) {
		items[j].hasChild = false;
		if ((j > 0) && (parseInt(items[j].level) > parseInt(items[j-1].level)))
		{
			items[j - 1].hasChild = true;
		}
	}

	for (var i = 0; i < items.length; i++) {
		var event = items[i];
		if (event && event.id && event.ask_date && event.due_date) {
			var start = getMilliSeconds(event.ask_date);
			var end = getMilliSeconds(event.due_date);
			var duration = getDiffDays(start, end);
			var project_name = '';
			if (project_management_id)
				project_name = event.project_name;
			else if (master_project_id)
				project_name = event.master_project_name;

			_tasks.push({
				"id": parseInt(event.id),
				"name": event.task_name,
				"code": event.id,
				"project": event.project_management_id,
				"project_name": project_name,
				"description": event.purpose_of_task,
				"master_project": event.master_project_id,
				"employee_id": event.employee_id,
				"assigned_id": event.assigned_id,
				"level": parseInt(event.level),
				"status": "STATUS_ACTIVE",
				"canWrite": true,
				"start": start,
				"end": end,
				"start_time": event.start_time,
				"end_time": event.end_time,
				"execute_time": event.time_to_execute,
				"duration": duration,
				"order": event.display_order,
				"startIsMilestone": true,
				"endIsMilestone": false,
				"collapsed": false,
				"assigs": [],
				"depends": "",
				"progress": event.progress_int,
				"hasChild": event.hasChild,
			});
		}
	}

	for (var i = 0; i < employees.length; i++) {
		var employee = employees[i];
		_resources.push({
			"id" : employee.employee_id,
			"name" : employee.employee_name
		});
	}

	_roles = [];

	var _events = {"tasks":_tasks,
		"selectedRow": 0,
		"canWrite": true,
		"roles" : _roles,
		"resources" : _resources,
		"canWriteOnParent": true
	}

	// here starts gantt initialization
	ge = new GanttMaster({
		deleteTasks: function (task) {
			// Do something
			//console.log(task);
			var _task = {
				id: task.id,
			};
			_params = JSON.stringify(_task);
            $.ajax({
                url: '/wbs/delete',
                type: 'POST',
                data: _params,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function(response) {
					//console.log(response);
				},
				error: function(e) {
					console.log(e);
				}
            });
		},
		saveTask: function (task, theRow) {
			if (typeof theRow !== 'undefined')
				theRow.next().find("input").focus();
			// console.log(task);
			task = filterData(task);
			var _task = {
				id: task.id,
				project_management_id : task.project,
				master_project_id : task.master_project,
				employee_id : task.employee_id,
				assigned_id : task.assigned_id,
				task_id: task_id,
				ask_date: task.start,
				due_date: task.end,
				ask_time: task.start_time,
				due_time: task.end_time,
				task_name: task.name,
				time_to_execute: task.execute_time,
				purpose_of_task : task.description,
				level: task.level,
				progress_int: task.progress,
				display_order: task.order,
				hasChild: task.hasChild,
			};

			_params = JSON.stringify({data: _task});
			//console.log(_params);

            $.ajax({
                url: '/wbs/task',
                type: 'POST',
                data: _params,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function(response) {
					new_task = task;
					new_task.id = response.saved_data.id;
					task.rowElement.attr('taskId', new_task.id);
					//console.log(new_task.id);
				},
				error: function(e) {
					console.log(e);
				}
            });
		}
	});

	function filterData(task) {
		if (typeof task.start_time === 'undefined' || task.start_time === null)
			task.start_time = '00:00';

		if (typeof task.end_time === 'undefined' || task.end_time === null)
			task.end_time = '00:00';

		if (typeof task.employee_id === 'undefined' || !task.employee_id)
			task.employee_id = employee_id;

		if (typeof task.assigned_id === 'undefined' || !task.assigned_id)
			task.assigned_id = employee_id;

		if (typeof task.project_management_id === 'undefined' || !task.project_management_id)
			task.project_management_id = project_management_id;

		if (typeof task.master_project_id === 'undefined' || !task.master_project_id)
			task.master_project_id = master_project_id;

		return task;
	}

	var workSpace = $("#workSpace");
	workSpace.css({width:$(window).width() - 20,height:$(window).height() - 100});
	ge.init(workSpace);
	ge.loadProject(_events);
	ge.checkpoint();

	//overwrite with localized ones
	loadI18n();

	$(window).resize(function(){
		workSpace.css({width:$(window).width() - 1,height:$(window).height() - workSpace.position().top});
		workSpace.trigger("resize.gantt");
	}).oneTime(150,"resize",function(){$(this).trigger("resize")});

  // Set default input tag to ReadOnly and editable when double click
  $('.taskEditRow input').editable();

  // Handle for keydown
  // Added by Chuyen Nguyen - JVB
	$(document).keydown(function(e) {
		_target = $(e.target.outerHTML);
		if (_target.is('input[readonly]'))
		{
			//e.preventDefault();
			e.stopPropagation();
			if(e.keyCode == 37) { // Left arrow
				workSpace.trigger('outdentCurrentTask.gantt');
				e.preventDefault();
			}
			else if(e.keyCode == 39) { // Right arrow
				workSpace.trigger('indentCurrentTask.gantt');
				e.preventDefault();
			}
			else if(e.keyCode == 85) { // U key
				workSpace.trigger('addAboveCurrentTask.gantt');
				e.preventDefault();
			}
			else if(e.keyCode == 76) { // L key
				workSpace.trigger('addBelowCurrentTask.gantt');
				e.preventDefault();
			}
		}

		if(e.keyCode == 46) { // DELETE key
			workSpace.trigger('deleteCurrentTask.gantt');
			e.preventDefault();
		}
		else  if(e.ctrlKey && e.keyCode == 188) { // CTRL + <
			workSpace.trigger('moveUpCurrentTask.gantt');
			e.preventDefault();
		}
		else if(e.ctrlKey && e.keyCode == 190) { // CTRL + >
			workSpace.trigger('moveDownCurrentTask.gantt');
			e.preventDefault();
		}
	});
});
</script>
<div id="gantEditorTemplates" style="display:none;">
  <div class="__template__" type="TASKSEDITHEAD">
  <table class="gdfTable" cellspacing="0" cellpadding="0">
	<thead>
	<tr style="height:40px">
	  <th class="gdfColHeader" style="width:35px;"></th>
	  <th class="gdfColHeader" style="width:25px;"></th>
      <th class="gdfColHeader gdfResizable" style="width:30px; display:none;">code/short name</th>
	  <th class="gdfColHeader gdfResizable" style="width:380px;">タスク名</th>
	  <th class="gdfColHeader gdfResizable" style="width:80px; display:none;">Start</th>
	  <th class="gdfColHeader gdfResizable" style="width:80px; display:none;">End</th>
      <th class="gdfColHeader gdfResizable" style="width:50px; display:none;">dur.</th>
      <th class="gdfColHeader gdfResizable" style="width:50px; display:none;">dep.</th>
	  <th class="gdfColHeader gdfResizable" style="width:100px; display:none;">Assignees</th>
	</tr>
	</thead>
  </table>
  </div>

  <div class="__template__" type="TASKROW"><!--
  <tr taskId="(#=obj.id#)" class="taskEditRow" level="(#=level#)">
	<th class="gdfCell edit" align="right" style="cursor:pointer;"><span class="taskRowIndex">(#=obj.getRow()+1#)</span> <span class="teamworkIcon" style="font-size:12px;" >e</span></th>
	<td class="gdfCell noClip" align="center"><div class="taskStatus cvcColorSquare" status="(#=obj.status#)"></div></td>
    <td class="gdfCell" style="display:none;"><input type="text" name="code" value="(#=obj.code?obj.code:''#)"></td>
	<td class="gdfCell indentCell" style="padding-left:(#=obj.level*10#)px;">
		<span class="teamworkIcon deleteTask" title="このタスクを消します。">d</span>
		<span class="(#=obj.isParent()?'exp-controller expcoll exp':'exp-controller'#) (#=obj.getParent()?'child':''#)" align="center"> </span>
		<span class="altME (#=obj.getParent()?'altChild':''#)" align="center"> </span>
		<span class="nameContaner"><input type="text" name="name" value="" placeholder="タスク名を入力してください..."></span>
	</td>

	<td class="gdfCell" style="display:none;"><input type="text" name="start"  value="" class="date"></td>
	<td class="gdfCell" style="display:none;"><input type="text" name="end" value="" class="date"></td>
    <td class="gdfCell" style="display:none;"><input type="text" name="duration" value="(#=obj.duration#)"></td>
    <td class="gdfCell" style="display:none;"><input type="text" name="depends" value="(#=obj.depends#)" (#=obj.hasExternalDep?"readonly":""#)></td>
	<td class="gdfCell taskAssigs" style="display:none;">(#=obj.getAssigsString()#)</td>
  </tr>
  --></div>

  <div class="__template__" type="TASKEMPTYROW"><!--
  <tr class="taskEditRow emptyRow" >
	<th class="gdfCell" align="right"></th>
	<td class="gdfCell noClip" align="center"></td>
    <td class="gdfCell" style="display:none;"></td>
	<td class="gdfCell"></td>
	<td class="gdfCell" style="display:none;"></td>
	<td class="gdfCell" style="display:none;"></td>
	<td class="gdfCell" style="display:none;"></td>
	<td class="gdfCell" style="display:none;"></td>
	<td class="gdfCell" style="display:none;"></td>
  </tr>
  --></div>

  <div class="__template__" type="TASKBAR">
  <div class="taskBox taskBoxDiv" taskId="(#=obj.id#)" >
	<div class="layout (#=obj.hasExternalDep?'extDep':''#)">
	  <div class="taskStatus" status="(#=obj.status#)"></div>
	  <div class="taskProgress" style="width:(#=obj.progress>100?100:obj.progress#)%; background-color:(#=obj.progress>100?'red':'rgb(153,255,51);'#);"></div>
	  <div class="milestone (#=obj.startIsMilestone?'active':''#)" ></div>

	  <div class="taskLabel"></div>
	  <div class="milestone end (#=obj.endIsMilestone?'active':''#)" ></div>
	</div>
  </div>
  </div>

  <div class="__template__" type="CHANGE_STATUS">
	<div class="taskStatusBox">
	  <div class="taskStatus cvcColorSquare" status="STATUS_ACTIVE" title="active"></div>
	  <div class="taskStatus cvcColorSquare" status="STATUS_DONE" title="completed"></div>
	  <div class="taskStatus cvcColorSquare" status="STATUS_FAILED" title="failed"></div>
	  <div class="taskStatus cvcColorSquare" status="STATUS_SUSPENDED" title="suspended"></div>
	  <div class="taskStatus cvcColorSquare" status="STATUS_UNDEFINED" title="undefined"></div>
	</div>
  </div>



  <div class="__template__" type="TASK_EDITOR"><!--
  <div class="ganttTaskEditor tableEditor">
	<div class="modal-header modal-lg">
		<button type="button" class="close closeButton" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		<h4 id="modal_title" class="modal-title">タスク編集</h4>
	</div>

	<div class="modal-body modal-lg">
		<div class="input-group label-margin" style="display:none;">
			<span class="input-group-addon" >Code/short name</span>
			<input type="text" name="code" id="code" class="form-control">
		</div>

		<div class="input-group label-margin">
			<span class="input-group-addon" >プロジェクト名</span>
			<input type="text" name="project_name" id="project_name" class="form-control" disabled="true" >
		</div>

		<div class="input-group label-margin">
			<span class="input-group-addon" >タスク名</span>
			<input type="text" name="name" id="name" class="form-control">
		</div>

		<div class="input-group label-margin">
			<span class="input-group-addon" >内容</span>
			<input type="text" name="description" id="description" class="form-control">
		</div>

		<div class="input-group label-margin" style="display:none;">
			<span class="input-group-addon" >Status</span>
			<div id="status" class="taskStatus" status=""></div>
		</div>

		<div class="input-group label-margin">
			<span class="input-group-addon" >進捗</span>
			<select name="progress" id="progress" class="form-control" >
							<option value="0">0%</option>
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
			<span class="input-group-addon">依頼日</span>
			<input type="text" name="start" id="start" class="date">&nbsp;
			<input type="text" name="start_time" id="start_time" size="5" class="width-time">&nbsp;
			<input type="checkbox" id="startIsMilestone" style="display:none;">
		</div>

		<div class="input-group month-dropdown">
			<span class="input-group-addon">締切日</span>
			<input type="text" name="end" id="end" class="date">&nbsp;
			<input type="text" name="end_time" id="end_time" size="5" class="width-time">&nbsp;
			<input type="checkbox" id="endIsMilestone" style="display:none;">
		</div>

		<div class="input-group label-margin graph" style="display:none;">
			<span class="input-group-addon" >所要時間</span>
			<input type="text" name="duration" id="duration" size="5" class=""> (days)
		</div>

		<div class="input-group label-margin graph">
			<span class="input-group-addon" >所要時間</span>
			<input type="text" name="execute_time" id="execute_time" size="5" readOnly="true" disabled="true"> 分
		</div>

		<div class="input-group label-margin" style="display:none;">
			<span class="input-group-addon" >担当者分類</span>
			<select name="employee_id" id="employee_id" rel="employees" class="form-control" disabled="true" ></select>
		</div>

		<div class="input-group label-margin">
			<span class="input-group-addon" >担当者名</span>
			<select name="assigned_id" id="assigned_id" rel="employees" class="form-control" ></select>
		</div>

	</div>
	<div style="clear:both"></div>
	<form id="fileupload">
    <div class="cal-modal-container">
 
        <div class="row fileupload-buttonbar">
            <div class="col-lg-7">
                <span class="btn btn-success fileinput-button">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>ファイル選択...</span>
                    <input type="file" name="files[]" multiple>
                </span>
                <button type="submit" class="btn btn-primary start">
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>アップロード</span>
                </button>
                <button type="reset" class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>キャンセル</span>
                </button>
                <button type="button" class="btn btn-danger delete">
                    <i class="glyphicon glyphicon-trash"></i>
                    <span>削除</span>
                </button>
                <input type="checkbox" class="toggle">
                <span class="fileupload-process"></span>
            </div>
            <div class="col-lg-5 fileupload-progress fade">
                <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                    <div class="progress-bar progress-bar-success" style="width:0%;"></div>
                </div>
                <div class="progress-extended">&nbsp;</div>
            </div>
        </div>
        <div class="modal-upload-tbl">
            <table role="presentation" class="table table-striped" id="fileupload_tbl" style="max-height: 280px; overflow-y: auto;"><tbody class="files" id="fileupload_body_tbl"></tbody></table>
        </div>

    </div>
    </div>
    
	<div class="modal-footer modal-lg">
		<button type="button" class="btn btn-default closeButton" data-dismiss="modal">キャンセル</button>
		<button type="button" id="saveButton" class="btn btn-primary">OK</button>
	</div>
  </div>
  --></div>

  <!-- TODO find another way to render view upload template instead of HTML -->

</div>

