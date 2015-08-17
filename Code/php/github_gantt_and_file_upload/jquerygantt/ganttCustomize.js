/*
* By Chuyen Nguyen - JVB - MIT TEAM
*/

(function($) {
	console.log(employee_id);
    $.fn.ineditable = function() {
        var _this = $(this);
        _this.prop('readonly', true);
        // Disable ReadOnly.
        _this.dblclick(function() {
			_this.prop('readonly', false);
        });
        // Set Input fields to ReadOnly when blur.
        _this.blur(function() {
            _this.prop('readonly', true);
        });
    };

    $.fn.editable = function() {
        var textBlock = $(this);
        textBlock.prop('readonly', true);
		// textBlock.attr('placeholder', 'Type task name');

        // Disable ReadOnly.
        textBlock.dblclick(function() {
			textBlock.prop('readonly', false);
        });
        // Set Input fields to ReadOnly when blur.
        textBlock.blur(function() {
            textBlock.prop('readonly', true);
        });

		textBlock.parents('td').find('.deleteTask').on('click', function() {
			console.log($(this));
			$('#workSpace').trigger('deleteCurrentTask.gantt');
		});
    };

})(jQuery);

function getMilliSeconds(timestamp)
{
	s = timestamp.split(/[-: ]/);
	var date_test = new Date(s[0], s[1]-1, s[2], s[3], s[4], s[5]);
	var millisecs = date_test.getTime();
	return millisecs;
}

function getDiffDays(start, end)
{
	start = getDateFromMilliS(start);
	end = getDateFromMilliS(end);

	var date1 = new Date(start);
	var date2 = new Date(end);
	var timeDiff = Math.abs(date2.getTime() - date1.getTime());
	var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
	return diffDays;
}

function getDateFromMilliS(time){
	var d = new Date(time);
	
	var month = d.getMonth() + 1;
	var day = d.getDate();
	
	var output = d.getFullYear() + '-' +
		((''+month).length<2 ? '0' : '') + month + '-' +
		((''+day).length<2 ? '0' : '') + day;
	
	return output;
}

//-------------------------------------------  Create some demo data ------------------------------------------------------
function loadI18n() {
  GanttMaster.messages = {
	"CANNOT_WRITE": "CANNOT_WRITE",
	"CHANGE_OUT_OF_SCOPE":"NO_RIGHTS_FOR_UPDATE_PARENTS_OUT_OF_EDITOR_SCOPE",
	"START_IS_MILESTONE":"START_IS_MILESTONE",
	"END_IS_MILESTONE":"END_IS_MILESTONE",
	"TASK_HAS_CONSTRAINTS":"TASK_HAS_CONSTRAINTS",
	"GANTT_ERROR_DEPENDS_ON_OPEN_TASK":"GANTT_ERROR_DEPENDS_ON_OPEN_TASK",
	"GANTT_ERROR_DESCENDANT_OF_CLOSED_TASK":"GANTT_ERROR_DESCENDANT_OF_CLOSED_TASK",
	"TASK_HAS_EXTERNAL_DEPS":"TASK_HAS_EXTERNAL_DEPS",
	"GANTT_ERROR_LOADING_DATA_TASK_REMOVED":"GANTT_ERROR_LOADING_DATA_TASK_REMOVED",
	"ERROR_SETTING_DATES":"ERROR_SETTING_DATES",
	"CIRCULAR_REFERENCE":"CIRCULAR_REFERENCE",
	"CANNOT_DEPENDS_ON_ANCESTORS":"CANNOT_DEPENDS_ON_ANCESTORS",
	"CANNOT_DEPENDS_ON_DESCENDANTS":"CANNOT_DEPENDS_ON_DESCENDANTS",
	"INVALID_DATE_FORMAT":"INVALID_DATE_FORMAT",
	"TASK_MOVE_INCONSISTENT_LEVEL":"TASK_MOVE_INCONSISTENT_LEVEL",

	"GANTT_QUARTER_SHORT":"trim.",
	"GANTT_SEMESTER_SHORT":"sem."
  };
}