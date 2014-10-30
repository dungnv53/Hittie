function compareVersion( v1, v2 ) {

	if( v1 === v2 ) 
		return 0;
	
	var a1 = v1.toString().split(".");
	var a2 = v2.toString().split(".");
	
	for( var i = 0; i < a1.length && i < a2.length; i++ ) {
		
		var diff = parseInt( a1[i], 10 ) - parseInt( a2[i], 10 );
		if( diff > 0 )
			return 1;
		else if( diff < 0 )
			return -1;
	}
	diff = a1.length - a2.length;
	return ( diff > 0 ) ? 1 : ( diff < 0 ) ? -1 : 0;
}

jQuery(document).ready(function($) {
	
	if( 'undefined' === typeof( tab_settings ) || 'undefined' === typeof( $.ui ) ) 
		return;
	
	if( 'undefined' === typeof( tab_settings.enabled_tabs ) )
		tab_settings.enabled_tabs = {};
		
	if( 'undefined' === typeof( tab_settings.disabled_tabs ) )
		tab_settings.disabled_tabs = {};
	
	tab_settings.tab_ends = {};
	
	var i = 1;
	
	// Get jQuery UI Version
	var uiversion = $.ui.version;
	
	// Check if jQuery 1.8
	var is_old_ui = compareVersion( uiversion, "1.9") < 0;
	
	// Post UI Tabs functions
	var PostUITabs = {

		// Function to create the tab sets
		create_tabsets: function( total ) {

			for( ;i <= total; i++ ) {
				
				var 
					tabs_el = '#tabs-' + i;
					leftmost_tab = null, 
					rightmost_tab = null, 
					settings = {};
				
				if( tab_settings.enabled_tabs.hasOwnProperty(i) ) {
					leftmost_tab  = Math.min.apply( null, tab_settings.enabled_tabs[i] );
					rightmost_tab = Math.max.apply( null, tab_settings.enabled_tabs[i] );
					tab_settings.tab_ends[tabs_el] = { left: leftmost_tab, right: rightmost_tab };
				}
				if( tab_settings.disabled_tabs.hasOwnProperty(i) )
					settings.disabled = tab_settings.disabled_tabs[i];
				
				if( tab_settings.select_active === '1' )
					is_old_ui ? settings.selected = leftmost_tab : settings.active = leftmost_tab;
				
				$(tabs_el).tabs(settings);
			}
		},
		
		// Function to switch tab using navigation buttons
		move_to_tab: function( o ) { 
		
			var put_tabset = $( $(this).attr('href') );
			
			if( 'undefined' === typeof( put_tabset ) )
				return false;
			
			var tab_ends = tab_settings.tab_ends[put_tabset.selector];
			
			if( 'undefined' === typeof( tab_ends ) )
				return false;
			
			var index, wrap_to_index, adding;
			var tabs = $( put_tabset.selector + ' ul' ).children();
			
			// Move index one place
			switch( o.data.direction ) {
				case 'left':
					index = PostUITabs.get_sibling_tab( put_tabset, 'left' );
					wrap_to_index = tab_ends.right;
					adding = false;
				break;
				case 'right':
					index = PostUITabs.get_sibling_tab( put_tabset, 'right' );
					wrap_to_index = tab_ends.left;
					adding = true;
				break;
				default: return false;
			}
			// Check the next index is valid
			if( 'undefined' === typeof( tabs[index] ) || index > tab_ends.right || index < 0 ) {
				PostUITabs.set_active_tab( put_tabset, wrap_to_index );
				return false;
			}
			
			// Loop through additional tabs if required
			while( index < tabs.length ) {
				
				// Negatives are valid, but will not work when the tab is disabled
				if( index < 0 ) {
					PostUITabs.set_active_tab( put_tabset, wrap_to_index );
					break;
				}
				// Move the index up/down if the index is a disabled tab
				if( PostUITabs.is_disabled_tab( put_tabset, index ) ) {
					index = adding ? index + 1 : index - 1;
					continue;
				}
				// Ok valid index, set tab
				PostUITabs.set_active_tab( put_tabset, index ); 
				return false;
			}
			return false; 
		},
				// Function to get the next tab index
		get_sibling_tab: function( selector, dir ) {
			var option = is_old_ui ? "selected" : "active";
			return dir === 'left' ? selector.tabs( "option", option ) - 1 : selector.tabs( "option", option ) + 1;
		},
		
		// Function to select active tab
		set_active_tab: function( selector, index ) {
			is_old_ui ? selector.tabs( 'select', index ) : selector.tabs( 'option', 'active', index );
		},
		
		// Function to determine if tab is diabled
		is_disabled_tab: function( selector, index ) {
			return $.inArray( index, selector.tabs("option", "disabled") ) > -1;
		}
	};
	
	PostUITabs.create_tabsets( tab_settings.total_tabsets );
	
	// Attach function to click for nav buttons
	$('.put-prev').click({ direction:"left"  }, PostUITabs.move_to_tab );
	$('.put-next').click({ direction:"right" }, PostUITabs.move_to_tab );
	
	// Disable the click functionality of disabled tabs(deals with single case where all tabs are disabled in a set)
	$('.ui-state-disabled a').click(function(e){ e.preventDefault(); });
});