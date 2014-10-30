 /**
 * CodeNegar Content Tooltip
 *
 * Frontend JavaScript File
 *
 * @package    	WordPress Content Tooltip
 * @author      Farhad Ahmadi
 * @license     http://codecanyon.net/licenses
 * @link		http://codenegar.com/wordpress-content-tooltip-plugin/
 * @version    	2.2.0
 */

function codenegar_make_tooltip($this){
	var post_id = $this.attr("data-ctt-id");
	var min_width = parseInt($this.attr("data-min-width"));
	var max_width = parseInt($this.attr("data-max-width"));
	var remove_delay = parseInt($this.attr("data-remove-delay"));
	var theme = $this.attr("data-theme");
	var funcEase = $this.attr("data-funcease");
	var preferredPosition = $this.attr("data-preferredposition");
	var enable_css_animations = $this.attr("data-enable-css-animations");
	var show_css_animation = $this.attr("data-show-css-animation");
	var hide_css_animation = $this.attr("data-hide-css-animation");
	var popupAnimationSpeed = parseInt($this.attr("data-animation-speed"));
	var popupYOffset = parseInt($this.attr("data-popup-y-offset"));
	var popupOffset = parseInt($this.attr("data-popup-offset"));
	var hideOnPopupClick = ($this.attr("data-hide-on-popup-click")=='on')? true : false;
	var hideOnTriggerClick = ($this.attr("data-hide-on-trigger-click")=='on')? true : false;
	var hideTrigger = ($this.attr("data-hide-trigger")=='on')? true : false;
	var triggerOnClick = ($this.attr("data-trigger-on-click")=='on')? true : false;
	var invertAnimation = ($this.attr("data-invert-animation")=='on')? true : false;
	var popupDistance = parseInt($this.attr("data-popup-distance"));
	$this.smallipop({
		theme: theme,
		funcEase: funcEase,
		preferredPosition: preferredPosition,
		popupAnimationSpeed: popupAnimationSpeed,
		popupYOffset: popupYOffset,
		popupOffset: popupOffset,
		hideOnPopupClick: hideOnPopupClick,
		hideOnTriggerClick: hideOnTriggerClick,
		hideTrigger: hideTrigger,
		triggerOnClick: triggerOnClick,
		invertAnimation: invertAnimation,
		popupDistance: popupDistance,
		onBeforeShow: function(trigger) {
		   jQuery('div.smallipop-instance').css({minWidth: min_width , maxWidth: max_width});
        },
		onAfterHide: function() {
			var selector = 'div.smallipop-instance .sipContent span #data-cn-cct-'+post_id;
			setTimeout(function(){ jQuery(selector).remove(); },remove_delay);
           
        },
		cssAnimations: {
		  enabled: (enable_css_animations=='on')? true : false,
		  show: 'animated ' + show_css_animation,
		  hide: 'animated ' + hide_css_animation
		}
	});
}

jQuery(function(){
	jQuery(".codenegar-ctt-element").each(function(){
		codenegar_make_tooltip(jQuery(this));
	});
});