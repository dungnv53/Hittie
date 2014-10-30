( function( $ ) {
 
    // plugin definition
    $.fn.overlabel = function( options ) {
 
        // build main options before element iteration
        var opts = $.extend( {}, $.fn.overlabel.defaults, options );
 
        var selection = this.filter( 'label[for]' ).map( function() {
 
            var label = $( this );
            var id = label.attr( 'for' );
            var field = document.getElementById( id );
 
            if ( !field ) return;
 
            // build element specific options
            var o = $.meta ? $.extend( {}, opts, label.data() ) : opts;
 
            label.addClass( o.label_class );
 
            var hide_label = function() { label.css( o.hide_css ) };
            var show_label = function() { this.value || label.css( o.show_css ) };
 
            $( field )
                 .parent().addClass( o.wrapper_class ).end()
                 .focus( hide_label ).blur( show_label ).each( hide_label ).each( show_label );
 
            return this;
 
        } );
 
        return opts.filter ? selection : selection.end();
    };
 
    // publicly accessible defaults
    $.fn.overlabel.defaults = {
 
        label_class:   'overlabel-apply',
        wrapper_class: 'overlabel-wrapper',
        hide_css:      { 'text-indent': '-10000px', "overflow": "hidden" },
        show_css:      { 'text-indent': '0px', 'cursor': 'text' },
        filter:        false
 
    };
 
} )( jQuery );


$(document).ready(function() {
 
 
	$("label.overlabel").overlabel();

	$(".login-form").hide();

	$(".login-link").click(function () {
    	$(".login-form").show();
		$(document).bind('click', dialogBlur);
		return false
    });
	$(".login-form .close a").click(function () {
    	$(".login-form").hide();
		$(document).unbind('click', dialogBlur);
		return false
    });

	var dialogBlur = function(event){

		var target = $(event.target);
		if (target.is('.login-form form') || target.parents('.login-form form').length) {
			return;
		}
		$(".login-form").hide();
		$(document).unbind('click', dialogBlur);

	}
});

jQuery(document).ready(
function($)
{
	//When page loads...
	$(".tab_content").hide(); //Hide all content
	$("ul.tabs li:first").addClass("active").show(); //Activate first tab
	$(".tab_content:first").show(); //Show first tab content

	//On Click Event
	$("ul.tabs li").click(function() {

		$("ul.tabs li").removeClass("active"); //Remove any "active" class
		$(this).addClass("active"); //Add "active" class to selected tab
		$(".tab_content").hide(); //Hide all tab content

		var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
		$(activeTab).fadeIn(); //Fade in the active ID content
		return false;
	});
 
	//When page loads...
	$(".tabsid_content").hide(); //Hide all content
	$("ul.tabsid li:first").addClass("active").show(); //Activate first tab
	$(".tabsid_content:first").show(); //Show first tab content

	//On Click Event
	$("ul.tabsid li").click(function() {

		$("ul.tabsid li").removeClass("active"); //Remove any "active" class
		$(this).addClass("active"); //Add "active" class to selected tab
		$(".tabsid_content").hide(); //Hide all tab content

		var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
		$(activeTab).fadeIn(); //Fade in the active ID content
		return false;
	});

}

);
