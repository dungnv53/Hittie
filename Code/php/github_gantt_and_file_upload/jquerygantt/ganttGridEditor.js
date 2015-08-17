/*
 Copyright (c) 2012-2014 Open Lab
 Written by Roberto Bicchierai and Silvia Chelazzi http://roberto.open-lab.com
 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
function GridEditor(master) {
  this.master = master; // is the a GantEditor instance
  this.gridified = $.gridify($.JST.createFromTemplate({}, "TASKSEDITHEAD"));
  this.element = this.gridified.find(".gdfTable").eq(1);
}


GridEditor.prototype.fillEmptyLines = function () {
  var factory = new TaskFactory();

  //console.debug("GridEditor.fillEmptyLines");
  var rowsToAdd = 30 - this.element.find(".taskEditRow").size();

  //fill with empty lines
  for (var i = 0; i < rowsToAdd; i++) {
    var emptyRow = $.JST.createFromTemplate({}, "TASKEMPTYROW");
    //click on empty row create a task and fill above
    var master = this.master;
    emptyRow.click(function (ev) {
      var emptyRow = $(this);
      //add on the first empty row only
      if (!master.canWrite || emptyRow.prevAll(".emptyRow").size() > 0)
        return

      master.beginTransaction();
      var lastTask;
      var start = new Date().getTime();
      var project = 0;
      var start_time = '00:00';
      var end_time = '00:00';
      var level = 0;
      var order = 0;
      if (master.tasks[0]) {
        start = master.tasks[0].start;
        level = master.tasks[0].level + 1;
        project = master.tasks[0].project;
        start_time = master.tasks[0].start_time;
        end_time = master.tasks[0].end_time;
      }

      //fill all empty previouses
      emptyRow.prevAll(".emptyRow").andSelf().each(function () {
        var ch = factory.build("tmp_fk" + new Date().getTime(), "", "", project, start_time, end_time, level, order, start, 1);
        var task = master.addTask(ch);
        lastTask = ch;
      });
      master.endTransaction();
      lastTask.rowElement.click();
      lastTask.rowElement.find("[name=name]").focus()//focus to "name" input
        .blur(function () { //if name not inserted -> undo -> remove just added lines
          var imp = $(this);
          if (imp.val() == "") {
            lastTask.name="タスク "+(lastTask.getRow()+1);
            imp.val(lastTask.name);
          }
        });
    });
    this.element.append(emptyRow);
  }
};


GridEditor.prototype.addTask = function (task, row, hideIfParentCollapsed) {
  //console.debug("GridEditor.addTask",task,row);
  //var prof = new Profiler("editorAddTaskHtml");

  //remove extisting row
  this.element.find("[taskId=" + task.id + "]").remove();

  var taskRow = $.JST.createFromTemplate(task, "TASKROW");
  //save row element on task
  task.rowElement = taskRow;

  this.bindRowEvents(task, taskRow);

  if (typeof(row) != "number") {
    var emptyRow = this.element.find(".emptyRow:first"); //tries to fill an empty row
    if (emptyRow.size() > 0)
      emptyRow.replaceWith(taskRow);
    else
      this.element.append(taskRow);
  } else {
    var tr = this.element.find("tr.taskEditRow").eq(row);
    if (tr.size() > 0) {
      tr.before(taskRow);
    } else {
      this.element.append(taskRow);
    }

  }
  // this.element.find(".taskRowIndex").each(function (i, el) {
  //   $(el).html(i + 1);
  // });
  //prof.stop();


  //[expand]
  if(hideIfParentCollapsed)
  {
    if(task.collapsed) taskRow.find(".exp-controller").removeClass('exp');
    var collapsedDescendant = this.master.getCollapsedDescendant();
    if(collapsedDescendant.indexOf(task) >= 0) taskRow.hide();
  }
          

  return taskRow;
};

GridEditor.prototype.refreshExpandStatus = function(task){
  if(!task) return;
  //[expand]
  var child = task.getChildren(); // Get child
  if(child.length > 0 && task.rowElement.has(".expcoll").length == 0)
  {
    task.rowElement.find(".exp-controller").addClass('expcoll exp');
  }
  else if(child.length == 0 && task.rowElement.has(".expcoll").length > 0)
  {
    task.rowElement.find(".exp-controller").removeClass('expcoll exp');
  }

  if (child.length > 0)
  {
    task.rowElement.find(".altME").addClass('altPar');
  }
  else
  {
	  task.rowElement.find(".altME").removeClass('altPar');
  }

  var par = task.getParent(); // Get parent
  if(par && par.rowElement.has(".expcoll").length == 0)
  {
    par.rowElement.find(".exp-controller").addClass('expcoll exp');
  }

  if (par)
  {
	// if this task has parent meaning if is a child, we will add class Child to task.
    task.rowElement.find(".exp-controller").addClass('child');
    task.rowElement.find(".altME").addClass('altChild');
  }
  else
  {
	  task.rowElement.find(".exp-controller").removeClass('child');
	  task.rowElement.find(".altME").removeClass('altChild');
  }

}

GridEditor.prototype.refreshTaskRow = function (task) {
  //console.debug("refreshTaskRow")
  //var profiler = new Profiler("editorRefreshTaskRow");
  var row = task.rowElement;
  // row.find(".indentCell").find('.vertical_line').remove();
  // for(var i = 0; i < task.level; i++) {
		// row.find(".indentCell").prepend('<span class="vertical_line" align="center"></span>');
  // }

  row.find(".taskRowIndex").html(task.getRow() + 1);
  row.find(".indentCell").css("padding-left", task.level * 18 );
  row.find("[name=name]").val(task.name);
  row.find("[name=code]").val(task.code);
  row.find("[status]").attr("status", task.status);

  row.find("[name=duration]").val(task.duration);
  row.find("[name=start]").val(new Date(task.start).format()).updateOldValue(); // called on dates only because for other field is called on focus event
  row.find("[name=end]").val(new Date(task.end).format()).updateOldValue();
  row.find("[name=depends]").val(task.depends);
  row.find(".taskAssigs").html(task.getAssigsString());

  //profiler.stop();
};

GridEditor.prototype.redraw = function () {
  for (var i = 0; i < this.master.tasks.length; i++) {
    this.refreshTaskRow(this.master.tasks[i]);
  }
};

GridEditor.prototype.reset = function () {
  this.element.find("[taskid]").remove();
};


GridEditor.prototype.bindRowEvents = function (task, taskRow) {
  var self = this;
  //console.debug("bindRowEvents",this,this.master,this.master.canWrite, task.canWrite);
  if (this.master.canWrite && task.canWrite ) {
    self.bindRowInputEvents(task, taskRow);

  } else { //cannot write: disable input
    taskRow.find("input").attr("readonly", true);
  }

  // Execute for dblclick and blur, readOnly
  //taskRow.find("input").editable(); // Added by Chuyen Nguyen - JVB

  self.bindRowExpandEvents(task, taskRow);

  taskRow.find(".edit").click(function () {
    self.openFullEditor(task, taskRow);

    task_detail_id = task['id'];
    // fileupload handler 
    $('#fileupload').fileupload({
        url: '/task-upload/ajax-upload',         // TODO we need or not add baseUrl here.
        formData: { 'task_id': task_detail_id },
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
        url: $('#fileupload').fileupload('option', 'url')+ '?task_id='+task_detail_id,
        dataType: 'json',
        context: $('#fileupload')[0]
    }).always(function () {
        $(this).removeClass('fileupload-processing');
    }).done(function (result) {
        $(this).fileupload('option', 'done')
            .call(this, $.Event('done'), {result: result});
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
  /*taskRow.find(".dbledit").dblclick(function () {self.openFullEditor(task, taskRow)});*/ // Added by JVB - Chuyen Nguyen
};


GridEditor.prototype.bindRowExpandEvents = function (task, taskRow) {
  var self = this;
  //expand collapse
   taskRow.find(".exp-controller").click(function(){
   //expand?
     var el=$(this);
     var taskId=el.closest("[taskid]").attr("taskid");
     var task=self.master.getTask(taskId);
     var descs=task.getDescendant();
     el.toggleClass('exp');
     task.collapsed = !el.is(".exp");
    var collapsedDescendant = self.master.getCollapsedDescendant();

     if (el.is(".exp")){
        for (var i=0;i<descs.length;i++)
        {
          var childTask = descs[i];
          if(collapsedDescendant.indexOf(childTask) >= 0) continue;
          childTask.rowElement.show();
        }

     } else {
        for (var i=0;i<descs.length;i++)
        descs[i].rowElement.hide();
     }
     self.master.gantt.refreshGantt();

   });
}

GridEditor.prototype.bindRowInputEvents = function (task, taskRow) {
  var self = this;

  //bind dateField on dates
  taskRow.find(".date").each(function () {
    var el = $(this);

    //start is readonly in case of deps
    if (task.depends && el.attr("name") == "start") {
      el.attr("readonly", "true");
    } else {
      el.removeAttr("readonly");
    }

    el.click(function () {
      var inp = $(this);
	  inp.pikaday();
      /*inp.dateField({
        inputField:el
      });*/
    });

    el.blur(function (date) {
      var inp = $(this);
      if (inp.isValueChanged()) {
        if (!Date.isValid(inp.val())) {
          // alert(GanttMaster.messages["INVALID_DATE_FORMAT"]);
          console.log(GanttMaster.messages["INVALID_DATE_FORMAT"]); // by JVB
          inp.val(inp.getOldValue());

        } else {
          var date = Date.parseString(inp.val());
          var row = inp.closest("tr");
          var taskId = row.attr("taskId");
          var task = self.master.getTask(taskId);
          var lstart = task.start;
          var lend = task.end;

          if (inp.attr("name") == "start") {
            lstart = date.getTime();
            if (lstart >= lend) {
              var end_as_date = new Date(lstart);
              lend = end_as_date.add('d', task.duration).getTime();
            }

            //update task from editor
            self.master.beginTransaction();
            self.master.moveTask(task, lstart);
            self.master.endTransaction();

          } else {
            lend = date.getTime();
            if (lstart >= lend) {
              lend=lstart;
            }
            lend=lend+3600000*20; // this 20 hours are mandatory to reach the correct day end (snap to grid)

            //update task from editor
            self.master.beginTransaction();
            self.master.changeTaskDates(task, lstart, lend);
            self.master.endTransaction();
          }


          inp.updateOldValue(); //in order to avoid multiple call if nothing changed
        }
      }
    });
  });


  //binding on blur for task update (date exluded as click on calendar blur and then focus, so will always return false, its called refreshing the task row)
  taskRow.find("input:not(.date)").focus(function () {
    $(this).updateOldValue();

  }).blur(function () {
      var el = $(this);
      if (el.isValueChanged()) {
        var row = el.closest("tr");
        var taskId = row.attr("taskId");

        var task = self.master.getTask(taskId);

        //update task from editor
        var field = el.attr("name");

        self.master.beginTransaction();

        if (field == "depends") {
          var oldDeps = task.depends;
          task.depends = el.val();

          //start is readonly in case of deps
          if (task.depends) {
            row.find("[name=start]").attr("readonly", "true");
          } else {
            row.find("[name=start]").removeAttr("readonly");
          }


          // update links
          var linkOK = self.master.updateLinks(task);
          if (linkOK) {
            //synchronize status from superiors states
            var sups = task.getSuperiors();
            for (var i = 0; i < sups.length; i++) {
              if (!sups[i].from.synchronizeStatus())
                break;
            }
            self.master.changeTaskDates(task, task.start, task.end); // fake change to force date recomputation from dependencies
          }

        } else if (field == "duration") {
          var dur = task.duration;
          dur = parseInt(el.val()) || 1;
          el.val(dur);
          var newEnd = computeEndByDuration(task.start, dur);
          self.master.changeTaskDates(task, task.start, newEnd);

        } else if (field == "name" && el.val() == "") { // remove unfilled task
            task.deleteTask();

        } else {
          task[field] = el.val();
        }
        self.master.endTransaction();
      }
    });

  //cursor key movement
  taskRow.find("input").keydown(function (event) {
    var theCell = $(this);
    var theTd = theCell.parents("td");
    var theRow = theTd.parents();
    var col = theTd.prevAll("td").size();

    var ret = true;
    switch (event.keyCode) {

      case 37: //left arrow
        if(this.selectionEnd==0)
          theTd.prev().find("input").focus();
        break;
      case 39: //right arrow
        if(this.selectionEnd==this.value.length)
          theTd.next().find("input").focus();
        break;

      case 38: //up arrow
        var prevRow = theRow.prev();
        var td = prevRow.find("td").eq(col);
        var inp = td.find("input");

        if (inp.size()>0)
          inp.focus();
        break;
      case 40: //down arrow
        var nextRow = theRow.next();
        var td = nextRow.find("td").eq(col);
        var inp = td.find("input");
        if (inp.size()>0)
          inp.focus();
        else
          nextRow.click(); //create a new row
        break;
      case 36: //home
        break;
      case 35: //end
        break;

      case 9: //tab
       case 13: //enter - Added by Chuyen Nguyen - JVB 2015-07-06
		// Execute for dblclick and blur, readOnly - Added by Chuyen Nguyen - JVB
		theRow.find("input").blur();
		theRow.find("input").editable();
		//theTd.next().find("input").focus();
		self.master.saveTask(self.master.currentTask, theRow);
		// console.log(self.master.currentTask);
		// theRow.next().find("input").focus();
       break;
    }
    return ret;

  }).focus(function () {
    $(this).closest("tr").click();
  });


  //change status
  taskRow.find(".taskStatus").click(function () {
    var el = $(this);
    var tr = el.closest("[taskid]");
    var taskId = tr.attr("taskid");
    var task = self.master.getTask(taskId);

    var changer = $.JST.createFromTemplate({}, "CHANGE_STATUS");
    changer.find("[status=" + task.status + "]").addClass("selected");
    changer.find(".taskStatus").click(function (e) {
      e.stopPropagation();
      var newStatus = $(this).attr("status");
      changer.remove();
      self.master.beginTransaction();
      task.changeStatus(newStatus);
      self.master.endTransaction();
      el.attr("status", task.status);
    });
    el.oneTime(3000, "hideChanger", function () {
      changer.remove();
    });
    el.after(changer);
  });



  //bind row selection
  taskRow.click(function () {
    var row = $(this);
    //var isSel = row.hasClass("rowSelected");
    row.closest("table").find(".rowSelected").removeClass("rowSelected");
    row.addClass("rowSelected");

    //set current task
    self.master.currentTask = self.master.getTask(row.attr("taskId"));

    //move highlighter
    self.master.gantt.synchHighlight();

    //if offscreen scroll to element
    var top = row.position().top;
    if (top > self.element.parent().height()) {
      row.offsetParent().scrollTop(top - self.element.parent().height() + 100);
    } else if (top<40){
      row.offsetParent().scrollTop(row.offsetParent().scrollTop()-40+top);
    }
  });

};


GridEditor.prototype.openFullEditor = function (task, taskRow) {

  var self = this;

  //task editor in popup
  var taskId = taskRow.attr("taskId");
  //console.debug(task);

  //make task editor
  var taskEditor = $.JST.createFromTemplate({}, "TASK_EDITOR");

  //make employee option
  var getEl = taskEditor.find("[rel=employees]");
  // loop on already resources
  for (var i = 0; i < task.master.resources.length; i++) {
    var _emp = task.master.resources[i];
	var optn = $("<option>");
	optn.val(_emp.id).html(_emp.name);
	getEl.append(optn);
  }

  taskEditor.find("#name").val(task.name);
  taskEditor.find("#project_name").val(task.project_name);
  taskEditor.find("#employee_id").val(task.employee_id);
  taskEditor.find("#assigned_id").val(task.assigned_id);
  taskEditor.find("#description").val(task.description);
  taskEditor.find("#code").val(task.code);
  taskEditor.find("#progress").val(task.progress ? parseFloat(task.progress) : 0);
  taskEditor.find("#status").attr("status", task.status);

  if (task.startIsMilestone)
    taskEditor.find("#startIsMilestone").attr("checked", true);
  if (task.endIsMilestone)
    taskEditor.find("#endIsMilestone").attr("checked", true);

  taskEditor.find("#duration").val(task.duration);
  taskEditor.find("#execute_time").val(task.execute_time);

  var startDate = taskEditor.find("#start");
  startDate.val(new Date(task.start).format('yyyy/MM/dd'));
  //start is readonly in case of deps
  if (task.depends) {
    startDate.attr("readonly", "true");
  } else {
    startDate.removeAttr("readonly");
  }

  taskEditor.find("#end").val(new Date(task.end).format('yyyy/MM/dd'));
  taskEditor.find("#start_time").val(task.start_time);
  taskEditor.find("#end_time").val(task.end_time);

  //taskEditor.find("[name=depends]").val(task.depends);

  //make assignments table
  var assigsTable = taskEditor.find("#assigsTable");
  assigsTable.find("[assigId]").remove();
  // loop on already assigned resources
  for (var i = 0; i < task.assigs.length; i++) {
    var assig = task.assigs[i];
    var assigRow = $.JST.createFromTemplate({task:task, assig:assig}, "ASSIGNMENT_ROW");
    assigsTable.append(assigRow);
  }

	//get Time Diff in Mins
	function timeDiffMins(start_date, start_time, end_date, end_time) {
		if (!start_date || !start_time || !end_date || !end_time)
			return 0;
		var minutes = 0;
		var _start = start_date+' '+start_time;
		var _end = end_date+' '+end_time;

		var dd = moment(_end, 'YYYY/MM/DD HH:mm').diff(moment(_start, 'YYYY/MM/DD HH:mm'));
		var duration = moment.duration(dd);
	
		if(duration > 0){
			minutes = duration.asMinutes();
			minutes = Math.round(minutes);
		}
		return minutes;
	}

  //define start end callbacks
  function startChangeCallback(date) {
    var dur = parseInt(taskEditor.find("#duration").val());
    date.clearTime();
    taskEditor.find("#end").val(new Date(computeEndByDuration(date.getTime(), dur)).format('yyyy/MM/dd'));
	timeChangeCallback();
  }

  function endChangeCallback(end) {
    var start = taskEditor.find("#start").val();
	start = Date.parseString(new Date(start).format('dd/MM/yyyy'));
    end.setHours(23, 59, 59, 999);

    if (end.getTime() < start.getTime()) {
      var dur = parseInt(taskEditor.find("#duration").val());
      start = incrementDateByWorkingDays(end.getTime(), -dur);
      taskEditor.find("#start").val(new Date(computeStart(start)).format('yyyy/MM/dd'));
    } else {
      taskEditor.find("#duration").val(recomputeDuration(start.getTime(), end.getTime()));
		timeChangeCallback();
    }
  }

  function timeChangeCallback() {
		execute_time = timeDiffMins(
			taskEditor.find("#start").val(),
			taskEditor.find("#start_time").val(),
			taskEditor.find("#end").val(),
			taskEditor.find("#end_time").val()
		);
      taskEditor.find("#execute_time").val(execute_time);
  }


  if (!self.master.canWrite || !task.canWrite) {
    taskEditor.find("input,textarea").attr("readOnly", true);
    taskEditor.find("input:checkbox,select").attr("disabled", true);
    taskEditor.find("#saveButton").remove();

  } else {

	taskEditor.find("#start").pikaday({
		format: 'YYYY/MM/DD',
		showTime: false,
		onOpen: function () {
			if (!this.getDate()) {
				this.setDate(moment().format());
			}
			else
				return;
		},
		onSelect: function () {
			this.config().field.value = moment(this.getDate()).format(this.config().format);
		},
		onClose: function () {
			if (this.getDate()) {
				this.config().field.value = moment(this.getDate()).format(this.config().format);
				var new_time = moment(this.getDate()).format('DD/MM/YYYY');
				startChangeCallback(Date.parseString(new_time));
			}
		}
    });

	taskEditor.find("#end").pikaday({
		format: 'YYYY/MM/DD',
		showTime: false,
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
				var new_two = moment(this.getDate()).format('DD/MM/YYYY');
				endChangeCallback(Date.parseString(new_two));
			}
		}
      });

	// bind time on start_time and end_time
	taskEditor.find("#start_time, #end_time").pikaday({
		format: 'HH:mm',
		showDate: false,
		use24hour: true,
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
			timeChangeCallback();
		}
    });

    //bind blur on duration
    taskEditor.find("#duration").change(function () {
      var start = taskEditor.find("#start").val();
		start = Date.parseString(new Date(start).format('dd/MM/yyyy'));
      var el = $(this);
      var dur = parseInt(el.val());
      dur = dur <= 0 ? 1 : dur;
      el.val(dur);
      taskEditor.find("#end").val(new Date(computeEndByDuration(start.getTime(), dur)).format('yyyy/MM/dd'));
    });


    //bind add assignment
    taskEditor.find("#addAssig").click(function () {
      var assigsTable = taskEditor.find("#assigsTable");
      var assigRow = $.JST.createFromTemplate({task:task, assig:{id:"tmp_" + new Date().getTime()}}, "ASSIGNMENT_ROW");
      assigsTable.append(assigRow);
      $("#bwinPopupd").scrollTop(10000);
    });

    taskEditor.find("#status").click(function () {
      var tskStatusChooser = $(this);
      var changer = $.JST.createFromTemplate({}, "CHANGE_STATUS");
      changer.find("[status=" + task.status + "]").addClass("selected");
      changer.find(".taskStatus").click(function (e) {
        e.stopPropagation();
        tskStatusChooser.attr("status", $(this).attr("status"));
        changer.remove();
      });
      tskStatusChooser.oneTime(3000, "hideChanger", function () {
        changer.remove();
      });
      tskStatusChooser.after(changer);
    });

    //close task - Added by Chuyen Nguyen - JVB
    taskEditor.find(".closeButton").click(function () {
		$("#__blackpopup__").trigger("close");
	});

    //save task
    taskEditor.find("#saveButton").click(function () {
      var task = self.master.getTask(taskId); // get task again because in case of rollback old task is lost

      self.master.beginTransaction();
      task.name = taskEditor.find("#name").val();
      task.employee_id = taskEditor.find("#employee_id").val();
      task.assigned_id = taskEditor.find("#assigned_id").val();
      task.description = taskEditor.find("#description").val();
      task.code = taskEditor.find("#code").val();
      task.progress = parseFloat(taskEditor.find("#progress").val());
      task.duration = parseInt(taskEditor.find("#duration").val());
      task.execute_time = parseInt(taskEditor.find("#execute_time").val());
      task.start_time = parseInt(taskEditor.find("#start_time").val());
      task.end_time = parseInt(taskEditor.find("#end_time").val());
      task.startIsMilestone = taskEditor.find("#startIsMilestone").is(":checked");
      task.endIsMilestone = taskEditor.find("#endIsMilestone").is(":checked");

      //set assignments
      taskEditor.find("tr[assigId]").each(function () {
        var trAss = $(this);
        var assId = trAss.attr("assigId");
        var resId = trAss.find("[name=resourceId]").val();
        var roleId = trAss.find("[name=roleId]").val();
        var effort = millisFromString(trAss.find("[name=effort]").val());


        //check if an existing assig has been deleted and re-created with the same values
        var found = false;
        for (var i = 0; i < task.assigs.length; i++) {
          var ass = task.assigs[i];

          if (assId == ass.id) {
            ass.effort = effort;
            ass.roleId = roleId;
            ass.resourceId = resId;
            ass.touched = true;
            found = true;
            break;

          } else if (roleId == ass.roleId && resId == ass.resourceId) {
            ass.effort = effort;
            ass.touched = true;
            found = true;
            break;

          }
        }

        if (!found) { //insert
          var ass = task.createAssignment("tmp_" + new Date().getTime(), resId, roleId, effort);
          ass.touched = true;
        }

      });

      //remove untouched assigs
      task.assigs = task.assigs.filter(function (ass) {
        var ret = ass.touched;
        delete ass.touched;
        return ret;
      });

      //change dates
      get_start = taskEditor.find("#start").val() + ' ' + taskEditor.find("#start_time").val();
		saved_start = Date.parseString(new Date(get_start).format('dd/MM/yyyy'));

      get_end = taskEditor.find("#end").val() + ' ' + taskEditor.find("#end_time").val();
		saved_end = Date.parseString(new Date(get_end).format('dd/MM/yyyy'));

      task.setPeriod(saved_start.getTime(), saved_end.getTime() + (3600000 * 24));

      //change status
      task.changeStatus(taskEditor.find("#status").attr("status"));

		// Save task to server
		// self.master.saveTask(task);

      if (self.master.endTransaction()) {
        $("#__blackpopup__").trigger("close");
      }

    });
  }

  // var ndo = createBlackPage(600, 520).append(taskEditor);
  var ndo = createBlackPage(900, 'auto').append(taskEditor); // 900 to match with modal-lg bootstrap

};

// dungnv, used in file upload
function clearDupplicateRow() {
    // remove dupplicate table files rows. 
    // Because we had many task_detail, each has different task_attach files
    // blueimp lib load existing file then append them to table (role=presentation), so we need clear this table before load other task files
    uploaded_table = $('#fileupload_body_tbl');
    uploaded_table.empty();
}
