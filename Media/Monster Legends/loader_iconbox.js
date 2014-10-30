var DIALOG_ELEMENT_BASE = 'linksalpha_dialog';
(function(callback) {
	var script = document.createElement('script');
    script.type = "text/javascript";
	script.src = '//www.linksalpha.com/scripts/require.js';
    document.getElementsByTagName('head')[0].appendChild(script);
    callback();
})(function() {
	var check_interval = setInterval(check_require_js, 100);
	function check_require_js() {
		if(typeof require != "undefined") {
			clearInterval(check_interval);
			chalhun();
		}
	}
	function chalhun() {
		require({
		    paths: {'jquery183': '//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min'}
		});
		require(['jquery183'], function () {
			LinksAlphaSocialIconJS = new function() {
				var BASE_URL = 'http://www.linksalpha.com';
				var LA_SELECTED_ELEM_ID = 'linksalpha_tag_410642840';
				function linksalpha_load() {
		        	linksalpha_load_css();
		        	$('a.linksalpha_button:not(a[id^=linksalpha_tag_]):first').attr('id', 'linksalpha_tag_410642840');
		        	$(document.body).append('<div id="linksalpha_dialog_linksalpha_tag_410642840" name="linksalpha_dialog_linksalpha_tag_410642840" class="linksalpha_dialog linksalpha_div linksalpha_display_none"><div class="linksalpha_dialog_title linksalpha_div"><div class="linksalpha_dialog_title_lhs linksalpha_div">Share</div><div class="linksalpha_dialog_title_rhs linksalpha_div"><a class="linksalpha_dialog_close linksalpha_link" href="#"><img src="//lh4.ggpht.com/qbqsmreEsMDrobsBjGqecXZ3uV-ygokYp4hRBF1FJwVzx2cRSkDfnmbLJJzzUM3gaT09COQLoCITuuoboelCIg=s10" class="linksalpha_image" /></a></div><br style="clear: both !important;" /></div><div class="linksalpha_dialog_buttons linksalpha_div"><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=aolmail" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_aolmail linksalpha_span linksalpha_button_icon_box_bg">AOL Mail</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=delicious" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_delicious linksalpha_span linksalpha_button_icon_box_bg">Delicious</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=digg" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_digg linksalpha_span linksalpha_button_icon_box_bg">Digg</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=diigo" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_diigo linksalpha_span linksalpha_button_icon_box_bg">Diigo</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" ><a rel="nofollow"  href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=email" class="linksalpha_button_email linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_email linksalpha_span linksalpha_button_icon_box_bg">Email</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=evernote" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_evernote linksalpha_span linksalpha_button_icon_box_bg">Evernote</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" ><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=facebook" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_facebook linksalpha_span linksalpha_button_icon_box_bg">Facebook</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=foursquare" class="linksalpha_button_url_vid linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_foursquare linksalpha_span linksalpha_button_icon_box_bg">foursquare</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=gmail" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_gmail linksalpha_span linksalpha_button_icon_box_bg">Gmail</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" ><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=googleplus" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_googleplus linksalpha_span linksalpha_button_icon_box_bg">Google Plus</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=hotmail" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_hotmail linksalpha_span linksalpha_button_icon_box_bg">Hotmail</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=hyves" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_hyves linksalpha_span linksalpha_button_icon_box_bg">Hyves</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=instapaper" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_instapaper linksalpha_span linksalpha_button_icon_box_bg">Instapaper</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" ><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=linkedin" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_linkedin linksalpha_span linksalpha_button_icon_box_bg">LinkedIn</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=livejournal" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_livejournal linksalpha_span linksalpha_button_icon_box_bg">LiveJournal</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=mailru" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_mailru linksalpha_span linksalpha_button_icon_box_bg">Mail.ru</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=netlog" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_netlog linksalpha_span linksalpha_button_icon_box_bg">Netlog</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=pinterest" class="linksalpha_button_url_body_photo linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_pinterest linksalpha_span linksalpha_button_icon_box_bg">Pinterest</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=pocket" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_pocket linksalpha_span linksalpha_button_icon_box_bg">Pocket</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="print" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=print" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_print linksalpha_span linksalpha_button_icon_box_bg">Print</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=printfriendly" class="linksalpha_button_url linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_printfriendly linksalpha_span linksalpha_button_icon_box_bg">Printfriendly</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=reddit" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_reddit linksalpha_span linksalpha_button_icon_box_bg">Reddit</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=sonico" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_sonico linksalpha_span linksalpha_button_icon_box_bg">Sonico</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=stumbleupon" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_stumbleupon linksalpha_span linksalpha_button_icon_box_bg">Stumble</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=tumblr" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_tumblr linksalpha_span linksalpha_button_icon_box_bg">Tumblr</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" ><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=twitter" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_twitter linksalpha_span linksalpha_button_icon_box_bg">Twitter</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=vkontakte" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_vkontakte linksalpha_span linksalpha_button_icon_box_bg">Vkontakte</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=weibo" class="linksalpha_button_url_title_photo linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_weibo linksalpha_span linksalpha_button_icon_box_bg">Weibo</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=xing" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_xing linksalpha_span linksalpha_button_icon_box_bg">Xing</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="_blank" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=yahoomail" class="linksalpha_button_url_title_body linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_yahoomail linksalpha_span linksalpha_button_icon_box_bg">Yahoo Mail</span></a></div><div class="linksalpha_button_icon_box linksalpha_div" style="display: none !important"><a rel="nofollow" target="popup" href="http://www.linksalpha.com/social/redirect?url=http%3A%2F%2Fwww.monsterlegends.info%2Fmonster-legends-breeding-guide-with-pictures%2F&amp;s=yammer" class="linksalpha_button_url_title linksalpha_link"><span class="linksalpha_button_icon_box linksalpha_button_icon_box_yammer linksalpha_span linksalpha_button_icon_box_bg">Yammer</span></a></div><div class="linksalpha_div linksalpha_button_icon_box_none">No match found.</div></div><div class="linksalpha_dialog_buttons_options linksalpha_div"><a class="linksalpha_dialog_buttons_options_more linksalpha_link" href="#">More</a><input type="text" class="linksalpha_dialog_buttons_options_search linksalpha_input" placeholder="Search" /></div><div class="linksalpha_dialog_footer linksalpha_div"><div class="linksalpha_dialog_footer_lhs linksalpha_div">&nbsp;</div><div class="linksalpha_dialog_footer_center linksalpha_div"><a target="_blank" href="http://www.linksalpha.com/about/privacy_sharing" class="linksalpha_link">Ad Choices</a></div><div class="linksalpha_dialog_footer_rhs linksalpha_div"><img src="http://lh6.ggpht.com/lZtB1sKxWkxrZ1-kDJmQrngFUspnRVaqhPK3P_r4kbzF1yhQ0OWTyAkNkJeo1NihU_SUvRhCmy0i1ELulQ8=s10" class="linksalpha_image" style="padding-left:2px !important;" /></div><br style="clear: both !important;" /></div></div>');
		            linksalpha_dialog_events();
		            linksalpha_content();
		            linksalpha_info();
		        }
		        function linksalpha_load_css() {
					if (typeof window.LinksAlphaSocialIconJS=="undefined") {
						$( document.createElement('link') ).attr({
			                href: BASE_URL + '/stylesheets/social_iconbox.css?t=118.375015701578861070',
			                type: 'text/css',
			                rel: 'stylesheet'
			            }).appendTo('head');
			            linksalpha_load_sprite();
			        }
		        }
		        $.fn.preload = function() {
					this.each(function() {
						$('<img/>')[0].src = this;
					});
				}
				function linksalpha_load_sprite() {
					var image_src = BASE_URL + '/images/iconbox_18.png';
					$([image_src]).preload();
				}
		        function linksalpha_content() {
		        	var icon_link;
		        	var icon_link_href;
		        	var LA_SELECTED_ELEM = $("#"+LA_SELECTED_ELEM_ID);
		        	var LA_SELECTED_ELEM_DATA_URL = $("#"+LA_SELECTED_ELEM_ID).attr('data-url');
		        	if (typeof LA_SELECTED_ELEM_DATA_URL!="undefined" && LA_SELECTED_ELEM_DATA_URL!="") {
		        		if (LA_SELECTED_ELEM_DATA_URL.length > 500) {
		        			LA_SELECTED_ELEM_DATA_URL = false;
		        		}
		        	}
		        	var LA_SELECTED_ELEM_DATA_TEXT = LA_SELECTED_ELEM.attr('data-text');
		        	if (typeof LA_SELECTED_ELEM_DATA_TEXT!="undefined" && LA_SELECTED_ELEM_DATA_TEXT!="") {
		        		LA_SELECTED_ELEM_DATA_TEXT = LA_SELECTED_ELEM_DATA_TEXT.substring(0, 300);
		        	} else {
		        		LA_SELECTED_ELEM_DATA_TEXT = false;
		        	}
		        	var LA_SELECTED_ELEM_DATA_DESC = LA_SELECTED_ELEM.attr('data-desc')
		        	if (typeof LA_SELECTED_ELEM_DATA_DESC!="undefined" && LA_SELECTED_ELEM_DATA_DESC!="") {
		        		LA_SELECTED_ELEM_DATA_DESC = LA_SELECTED_ELEM_DATA_DESC.substring(0, 700);
		        	} else {
		        		LA_SELECTED_ELEM_DATA_DESC = false;
		        	}
		        	var LA_SELECTED_ELEM_DATA_IMG = LA_SELECTED_ELEM.attr('data-image');
		        	if (typeof LA_SELECTED_ELEM_DATA_IMG!="undefined" && LA_SELECTED_ELEM_DATA_IMG!="") {
		        		if (LA_SELECTED_ELEM_DATA_IMG.length > 500) {
		        			LA_SELECTED_ELEM_DATA_IMG = false;
		        		}
		        	}
		        	$("#"+DIALOG_ELEMENT_BASE+"_"+LA_SELECTED_ELEM_ID).find("div.linksalpha_button_icon_box").each(function(){
		        		icon_link = $(this).find("a:first");
		        		icon_link_href = icon_link.attr('href');
		        		if(icon_link.hasClass('linksalpha_button_url_title')) {
	        				if (LA_SELECTED_ELEM_DATA_TEXT) {
	        					icon_link_href = icon_link_href+"&title="+encodeURIComponent(LA_SELECTED_ELEM_DATA_TEXT);
	        				};
	        			} else if(icon_link.hasClass('linksalpha_button_url_title_body')) {
	        				if (LA_SELECTED_ELEM_DATA_TEXT) {
	        					icon_link_href = icon_link_href+"&title="+encodeURIComponent(LA_SELECTED_ELEM_DATA_TEXT);
	        				}
	        				if (LA_SELECTED_ELEM_DATA_DESC) {
	        					icon_link_href = icon_link_href+"&body="+encodeURIComponent(LA_SELECTED_ELEM_DATA_DESC);
	        				}
	        			} else if(icon_link.hasClass('linksalpha_button_url_body_photo')) {
	        				if (LA_SELECTED_ELEM_DATA_DESC) {
        						icon_link_href = icon_link_href+"&body="+encodeURIComponent(LA_SELECTED_ELEM_DATA_DESC);
        					}
        					if(LA_SELECTED_ELEM_DATA_IMG) {
	        					icon_link_href = icon_link_href+"&image="+encodeURIComponent(LA_SELECTED_ELEM_DATA_IMG);
	        				}
	        			} else if(icon_link.hasClass('linksalpha_button_email')) {
	        				icon_link_href = "mailto:?";
	        				if (LA_SELECTED_ELEM_DATA_TEXT) {
        						icon_link_href = icon_link_href+"subject="+encodeURIComponent(LA_SELECTED_ELEM_DATA_TEXT);
        					}
        					if(LA_SELECTED_ELEM_DATA_DESC) {
	        					icon_link_href = icon_link_href+"&body="+encodeURIComponent(LA_SELECTED_ELEM_DATA_DESC+"... http://p.ost.im/8gRKst");
	        				} else {
	        					icon_link_href = icon_link_href+"&body="+encodeURIComponent("http://p.ost.im/8gRKst");
	        				}
	        			} else if(icon_link.hasClass('linksalpha_button_url_vid')) {
	        				if (typeof LA_SELECTED_ELEM.attr('data-foursquare-vid') != "undefined") {
	        					icon_link_href = icon_link_href+"&vid="+encodeURIComponent(LA_SELECTED_ELEM.attr('data-foursquare-vid'));
	        				};
	        			}
	        			if(icon_link.attr('target')=="popup") {
	        				icon_link.on("click", function(){
	        					linksalpha_connect_popup($(this).attr("href"));
	        					return false;
	        				});
	        			}
	        			if(icon_link.attr('target')=="print") {
	        				icon_link.on("click", function() {
	        					print(document);
	        					return false;
	        				});
	        			}
	        			icon_link.attr('href', icon_link_href);
					});
		        	LA_SELECTED_ELEM.attr("target", "_blank");
					var share_href = LA_SELECTED_ELEM.attr('href');
					var share_query;
					if (LA_SELECTED_ELEM_DATA_URL) {
						share_query = share_query+"&link="+encodeURIComponent(LA_SELECTED_ELEM_DATA_URL);
					}
					if (LA_SELECTED_ELEM_DATA_TEXT) {
						share_query = share_query+"&title="+encodeURIComponent(LA_SELECTED_ELEM_DATA_TEXT);
					}
					if (LA_SELECTED_ELEM_DATA_DESC) {
						share_query = share_query+"&body="+encodeURIComponent(LA_SELECTED_ELEM_DATA_DESC);
					}
					if (LA_SELECTED_ELEM_DATA_IMG) {
						share_query = share_query+"&image="+encodeURIComponent(LA_SELECTED_ELEM_DATA_IMG);
					}
					if (typeof share_query != "undefined") {
						share_query = share_query.replace("undefined&", "");
						share_href = share_href+"?"+share_query;
					};
					$("#"+LA_SELECTED_ELEM_ID).attr('href', share_href);
		        }
		        function linksalpha_info(){
		        	if($("#linksalpha_info_loader").length==0) {
		        		var social_query = [];
		        		social_query.push(encodeURIComponent('link') + "=" + encodeURIComponent("http://www.monsterlegends.info/monster-legends-breeding-guide-with-pictures/"));
		        		if (typeof document.referrer != undefined) {
		        			if (document.referrer.length > 0 && document.referrer.length < 700) {
		        				social_query.push(encodeURIComponent('referer') + "=" + encodeURIComponent(document.referrer));
		        			};
		        		}
		        		var LA_SELECTED_ELEM = $("#"+LA_SELECTED_ELEM_ID);
			        	var LA_SELECTED_ELEM_DATA_TEXT = LA_SELECTED_ELEM.attr('data-text');
			        	if (typeof LA_SELECTED_ELEM_DATA_TEXT!="undefined" && LA_SELECTED_ELEM_DATA_TEXT!="") {
			        		social_query.push(encodeURIComponent('title') + "=" + encodeURIComponent(LA_SELECTED_ELEM_DATA_TEXT.substring(0, 300)));
			        	}
			        	social_query = social_query.join("&");
			        	var this_url = 'http://www.linksalpha.com/social/info_2?'+social_query;
			        	var social_info = '<iframe height="0" width="0" frameborder="0" id="linksalpha_info_loader" src="'+this_url+'"></iframe>';
		        		$('body').append(social_info);
		        	}
		        }
		        function linksalpha_connect_popup(link) {
				    var w = 800, h = 550;
				    var left = (screen.width/2)-(w/2);
				    var top = (screen.height/2)-(h/2);
				    var newwindow = window.open(link, 'linksalpha', 'toolbar=no, status=0, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=yes, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
				    if (window.focus) {
				        newwindow.focus();
				    }
				    return false;
				};
				function linksalpha_dialog_events() {
		        	var browser_agent = "browser";
		        	if(browser_agent=="browser") {
		        		var this_elem = $("#"+DIALOG_ELEMENT_BASE+"_"+LA_SELECTED_ELEM_ID);
				    	this_elem.unbind("mouseleave");
			        	$("#"+LA_SELECTED_ELEM_ID).hover(function(){
			        		linksalpha_dialog_events_hc(this_elem);
			        	});
				    	$("#"+LA_SELECTED_ELEM_ID).click(function() {
				    		linksalpha_dialog_events_hc(this_elem);
				    		return false;
				    	});
			        }
			        linksalpha_dialog_close();
			    }
			    function linksalpha_dialog_events_hc(this_elem){
        			linksalpha_dialog_show();
	            	setTimeout(function() {
	            		if (!this_elem.is(":hover") && !$("#"+LA_SELECTED_ELEM_ID).is(":hover")) {
		            		linksalpha_dialog_hide();
		            	} else {
		            		this_elem.mouseleave(function() {
			        			setTimeout(function() {
					            	if (!this_elem.is(":hover") && !$("#"+LA_SELECTED_ELEM_ID).is(":hover")) {
					            		linksalpha_dialog_hide();
					            	}
								}, 500);
					    	});
						}
	            	}, 500);
			    }
			    function linksalpha_dialog_show() {
			    	var this_elem = $("#"+DIALOG_ELEMENT_BASE+"_"+LA_SELECTED_ELEM_ID);
	        		if (this_elem.css('width')!='160px') {
			    		return false;
			    	};
	        		this_elem.find(".linksalpha_dialog_links").remove();
	        		this_elem.removeClass('linksalpha_display_none');
	        		this_elem.addClass('linksalpha_display_block');
	        		linksalpha_dialog_position(this_elem);
	                this_elem.parent().find('a').blur();
                    linksalpha_dialog_options();
					return false;
			    }
			    function linksalpha_dialog_hide() {
			    	$(".linksalpha_dialog_buttons_options_more").attr('style', 'display: block !important');
		    		$(".linksalpha_dialog_buttons_options_search").attr('style', 'display: none !important');
		    		var this_elem = $("#"+DIALOG_ELEMENT_BASE+"_"+LA_SELECTED_ELEM_ID);
	        		this_elem.addClass('linksalpha_display_none');
	        		this_elem.removeClass('linksalpha_display_block');
		    		this_elem.find(".linksalpha_dialog_links").remove();
		    		this_elem.find(".linksalpha_dialog_buttons_options_search").val("");
		    		this_elem.find(".linksalpha_dialog_buttons_options_search").trigger("keyup");
		    		return false;
			    }
			    function linksalpha_dialog_close(){
			    	$(".linksalpha_dialog_close").on("click", function() {
			    		linksalpha_dialog_hide();
			    		return false;
			    	});
			    }
		        function linksalpha_dialog_options() {
			    	$(".linksalpha_dialog_buttons_options_more").on("click", function() {
			    		var this_elem = $(this);
			    		this_elem.parents(".linksalpha_dialog:first").find("div.linksalpha_button_icon_box").attr('style', 'display: block !important');
			    		this_elem.attr('style', 'display: none !important');
			    		this_elem.parent().find(".linksalpha_dialog_buttons_options_search").attr('style', 'display: inline-block !important').focus();
			    		linksalpha_dialog_search();
			    		return false;
			    	});
			    }
			    function linksalpha_dialog_search() {
			    	$(".linksalpha_dialog_buttons_options_search").on("keyup", function() {
			    		var this_elem = $(this);
			    		var this_text = this_elem.val();
			    		var all_buttons = this_elem.parents(".linksalpha_dialog:first").find(".linksalpha_dialog_buttons span.linksalpha_button_icon_box");
			    		var visible_count = 0;
			    		var this_network_div;
			    		all_buttons.each(function() {
			    			var this_network = $(this);
			    			this_network_div = this_network.parents("div.linksalpha_button_icon_box:first");
			    			var this_network_text = this_network.text();
			    			if (this_network_text.toLowerCase().indexOf(this_text.toLowerCase())>-1){
			    				this_network_div.attr('style', 'display: block !important');
			    				visible_count += 1;
			    			} else {
			    				this_network_div.attr('style', 'display: none !important');
			    			}
			    		});
			    		if (visible_count) {
			    			this_network_div.parents(".linksalpha_dialog_buttons:first").find('div.linksalpha_button_icon_box_none').attr('style', 'display: none !important');
			    		} else {
			    			this_network_div.parents(".linksalpha_dialog_buttons:first").find('div.linksalpha_button_icon_box_none').attr('style', 'display: block !important');
			    		}
			    	});
			    }
			    function linksalpha_dialog_position(this_dialog) {
			    	var this_button = $("#"+LA_SELECTED_ELEM_ID);
			    	var this_position_top = this_button.offset().top - $(window).scrollTop();
	        		var this_position_left = this_button.offset().left - $(window).scrollLeft();
	        		var window_height = $(window).height();
	        		var widnow_width = $(window).width();
	        		var popup_height = this_dialog.height();
	        		var popup_width = this_dialog.width();
	        		if ((this_position_top+popup_height)>window_height) {
	        			top_offset = this_button.offset().top - popup_height - 1;
	        		} else {
	        			top_offset = this_button.offset().top + this_button.height();
	        		}
	        		if ((this_position_left+popup_width)>widnow_width) {
	        			left_offset = this_button.offset().left - popup_width + this_button.width();
	        		} else {
	        			left_offset = this_button.offset().left;
	        		}
	        		this_dialog.attr('style', 'top: '+top_offset+'px !important; left: '+left_offset+'px !important');
	        		return;
			    }
			    if (window.addEventListener) {
					window.addEventListener('load', linksalpha_load(), false);
				}  else if (window.attachEvent) {
					window.attachEvent('onload', linksalpha_load());
				}
		    }
    	});
	}
});