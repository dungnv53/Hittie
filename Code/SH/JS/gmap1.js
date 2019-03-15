<?php 
/*
*Template Name: Find Studio
*/   
get_header();

$response = wp_remote_get( 'https://studio.api.staging.f45training.com/v1/studios?per_page=300' ); // 1000 or is_all
// TODO handle big number of studios (real DB may be 3000?), see more or pagination

$body = wp_remote_retrieve_body( $response );

function groupStudioByCountry($data) {
    $result = array();
    foreach ($data as $key => $element) {
        $result[$element['country']][$element['state']][] = $element; // TODO may be add region
    }

    return $result;
}

?>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCAoolWuVR4rZa68IhBVDuRE2NSWA0SccA&callback=initMap&libraries=places"></script>
<style type="text/css">
    .left-result {
        width: 27%;
        float: left;
    }
    .pull-left {
        float: left !important;
    }
    .pull-right {
        float: right !important;
    }
    #map {
        width: 73%;
    }
    .studio-item {
        padding: 10px;
        border-bottom: solid 1px gray;
    }
    .studioList ul {
        columns: 3;
        -webkit-columns: 3;
        -moz-columns: 3;
    }
    .section-country {
        width: 100%;
        padding-right: 10px;
        padding-left: 10px;
    }
    /*.section-country ul {*/
        /*-moz-column-count: 3;*/
        /*-moz-column-gap: 20px;*/
        /*-webkit-column-count: 3;*/
        /*-webkit-column-gap: 20px;*/
        /*column-count: 3;*/
        /*column-gap: 20px;*/
    /*}*/

</style>
<main class="contentSection">
  <!-- BG IMG SECTION WITH SIDE HEADING BOX -->
  <div class="left-result pull-left">
        <div class="search-section">
            <div>
                <div>
                    <input type="text" name="studio_name" id="filterStudioResult" placeholder="Search map">
                    <button type="button" name="search" id="search" onclick="search()">Search</button>
<!--                    onclick="search()"-->
                </div>
            </div>
        </div>
        <div class="result" id="studioSearchResult">
<!--            TODO group by state
-->
                <div class="store-group" id="searchResultInner">
                    <div id="firstStudioInner" style=""></div>
                    <h5 id="noStudioResult">No result</h5>
                </div>

        </div>
    </div>

  <div class="bgSecWithSideHeading hero innerpage studio" id="map" style="height: 850px" class="pull-right">

  </div>

  <!-- BG IMG SECTION WITH SIDE HEADING BOX ENDS -->
  <!-- PUNCHLINE SECTION -->
  <section class="punchline float">
    <div class="container">

      <h1 class="heading">FIND YOUR STUDIO BY STATE</h1>
      <!-- Studio List -->
      <div class="s-content studioListColumn space-between flex">


    </div><!-- end s-content studioListColumn -->

<?php {
    global $body;
    $body = json_decode($body, true);
    $body = @$body['data']['studios'];
    $studios = groupStudioByCountry($body);
//    echo "<pre/>";
//    var_dump(@$body);
//    var_dump(@$studios);
//    die;
    foreach(@$studios as $country => $states) {
//        echo "<pre/>";
//        print_r($studio);
//        die;
    ?>
    <!-- Studio List -->
<!--        TODO group by country / state-->
      <div class="s-content studioListColumn space-between flex">
        <div class="section-country">
            <h3><?php echo $country; ?></h3>
        <div class="studioList col">
          <?php
          foreach($states as $state => $studios) {
          ?>
              <ul>
              <?php
              $count = 0;
              foreach($studios as $studio) {

              ?>
                  <?php if(!$count) { ?>
                      <li><h3><?php echo $state; ?></h3></li>
                  <?php }?>
                      <li>
                          <a href="#" class="coming-soon">
                              <div>
                                  <span class="text-wrapper"><?php echo @$studio['name']; ?></span>
                                  <span class="greater-than"></span>
                              </div>
                          </a>
                      </li>

               <?php
                  $count++;
              }?>
              </ul>
          <?php } ?>

        </div>
        </div>
        <!-- end div country -->

    </div><!-- end s-content studioListColumn -->

<?php
        } // End foreach
} // end condition
?>

    </div>

</section>
<!-- PUNCHLINE SECTION ENDS-->
</main>
<script>
  var map;
  var service;
  var infowindow;

  var locations = [
      // [-33.8702159, 151.1967052],
      // [-33.86629902495427, 151.2026265613075],
      // [-33.8670821, 151.20412769999996],
      // [-33.86580010000001, 151.20820030000004],
      // [-33.8711017, 151.20790060000002],
      // [-33.8657077, 151.20927929999993],
      // [-33.8670193, 151.2095127],
      // [-33.8626435, 151.20859799999994],
      // [-33.87354569999999, 151.208802],
      // [-33.8779027, 151.20528749999994],
  ];
  var markersArray = [];

  // var image = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';
  var image = 'https://web.f45training.com/wp-content/themes/f45/images/icons/marker-star-30x.png';

  // get user location (need permission)
  // search nearby using lat/long move
  // display result in left
  // center point button
  // search button (should be in case auto search lag or not work)
  // Gmap have an option for updated result when map moves (may be need some delay
  // double check API result (it seem not correctly lat/long) and seem alway return 12 records
  // If confirm ok then move to refactor
  // if not then need manual search in frontend side with list of studio (few thousand) and current lat/long circle
  // testing
  // update config API key, radius, ...
  // list studio footer

  function initMap() {

    var defaultLocation = new google.maps.LatLng(-33.867, 151.195);

    infowindow = new google.maps.InfoWindow();

    // Try HTML5 geolocation.
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            defaultLocation = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };

            // infoWindow.setPosition(pos);pos
            // infoWindow.setContent('Location found.');
            // infoWindow.open(map);
            // map.setCenter(pos);
        }, function() {
            // handleLocationError(true, infoWindow, map.getCenter());
            console.log('Can not get or no location');
        });
    } else {
        // Browser doesn't support Geolocation
        // handleLocationError(false, infoWindow, map.getCenter());
        console.log('Can not get or no location');
    }

    map = new google.maps.Map(
      document.getElementById('map'), {
            center: defaultLocation,
            zoom: 15,
            styles: [
                {
                    "elementType": "geometry",
                    "stylers": [
                        {
                            "color": "#f5f5f5"
                        }
                    ]
                },
                {
                    "elementType": "labels.icon",
                    "stylers": [
                        {
                            "visibility": "off"
                        }
                    ]
                },
                {
                    "elementType": "labels.text.fill",
                    "stylers": [
                        {
                            "color": "#616161"
                        }
                    ]
                },
                {
                    "elementType": "labels.text.stroke",
                    "stylers": [
                        {
                            "color": "#f5f5f5"
                        }
                    ]
                },
                {
                    "featureType": "administrative.land_parcel",
                    "elementType": "labels.text.fill",
                    "stylers": [
                        {
                            "color": "#bdbdbd"
                        }
                    ]
                },
                {
                    "featureType": "poi",
                    "elementType": "geometry",
                    "stylers": [
                        {
                            "color": "#eeeeee"
                        }
                    ]
                },
                {
                    "featureType": "poi",
                    "elementType": "labels.text.fill",
                    "stylers": [
                        {
                            "color": "#757575"
                        }
                    ]
                },
                {
                    "featureType": "poi.park",
                    "elementType": "geometry",
                    "stylers": [
                        {
                            "color": "#e5e5e5"
                        }
                    ]
                },
                {
                    "featureType": "poi.park",
                    "elementType": "labels.text.fill",
                    "stylers": [
                        {
                            "color": "#9e9e9e"
                        }
                    ]
                },
                {
                    "featureType": "road",
                    "elementType": "geometry",
                    "stylers": [
                        {
                            "color": "#ffffff"
                        }
                    ]
                },
                {
                    "featureType": "road.arterial",
                    "elementType": "labels.text.fill",
                    "stylers": [
                        {
                            "color": "#757575"
                        }
                    ]
                },
                {
                    "featureType": "road.highway",
                    "elementType": "geometry",
                    "stylers": [
                        {
                            "color": "#dadada"
                        }
                    ]
                },
                {
                    "featureType": "road.highway",
                    "elementType": "labels.text.fill",
                    "stylers": [
                        {
                            "color": "#616161"
                        }
                    ]
                },
                {
                    "featureType": "road.local",
                    "elementType": "labels.text.fill",
                    "stylers": [
                        {
                            "color": "#9e9e9e"
                        }
                    ]
                },
                {
                    "featureType": "transit.line",
                    "elementType": "geometry",
                    "stylers": [
                        {
                            "color": "#e5e5e5"
                        }
                    ]
                },
                {
                    "featureType": "transit.station",
                    "elementType": "geometry",
                    "stylers": [
                        {
                            "color": "#eeeeee"
                        }
                    ]
                },
                {
                    "featureType": "water",
                    "elementType": "geometry",
                    "stylers": [
                        {
                            "color": "#c9c9c9"
                        }
                    ]
                },
                {
                    "featureType": "water",
                    "elementType": "labels.text.fill",
                    "stylers": [
                        {
                            "color": "#9e9e9e"
                        }
                    ]
                }
            ]
      });

    // var request = {
    //   query: 'Museum of Contemporary Art Australia',
    //   fields: ['name', 'geometry'],
    // };

    // service = new google.maps.places.PlacesService(map);

    // service.findPlaceFromQuery(request, function(results, status) {
    //   if (status === google.maps.places.PlacesServiceStatus.OK) {
    //     for (var i = 0; i < results.length; i++) {
    //       createMarker(results[i]);
    //     }
    //
    //     map.setCenter(results[0].geometry.location);
    //   }
    // });

    map.addListener('center_changed', function() {
        // 3 seconds after the center of the map has changed, pan back to the
        // marker.
        var NewMapCenter = map.getCenter();
        var latitude = NewMapCenter.lat();
        var longitude = NewMapCenter.lng();
        console.log(latitude + " " + longitude);

        // window.setTimeout(function() {
        //     map.panTo(marker.getPosition());
        // }, 3000);
    });

      var infowindow = new google.maps.InfoWindow();

      var marker, i;

      // for (i = 0; i < locations.length; i++) {
      //     console.log(locations[i]);
      //     marker = new google.maps.Marker({
      //         position: new google.maps.LatLng(locations[i][0], locations[i][1]),
      //         map: map,
      //         title: 'test',
      //         icon: image
      //     });

          // google.maps.event.addListener(marker, 'click', (function(marker, i) {
          //     return function() {
          //         infowindow.setContent(locations[i][0]);
          //         infowindow.open(map, marker);
          //     }
          // })(marker, i));
      // }

  }

  function search() {
      // get lat / long or pass parameters
      var NewMapCenter = map.getCenter();
      var latitude = NewMapCenter.lat();
      var longitude = NewMapCenter.lng();
      var current_center = {latitude: latitude, longitude: longitude};
      console.log(latitude + " " + longitude);

      // jQuery('#search').on('click',function () {
      //     // $('.store-finder .left').toggleClass('active');
      // });
      jQuery.ajax({
          type: "GET",
          url: "https://studio.api.staging.f45training.com/v1/studios?per_page=10",
          data: {
              search: 'NEARBY',
              lat: latitude,
              lng: longitude,
              distance: 20
          },
          success: function(data) {
              console.log(data.data);
              var data = data.data;

              var firstStudioResult = jQuery('#firstStudioInner');
              // var cartBottom = cartBar.find('.cart-bar-bottom');

              if (data.studios && data.studios.length > 0) {
                  jQuery('#noStudioResult').hide();
                  // clear old search
                  jQuery('.studio-item').remove();

                  var length = data.studios.length;
                  for (var i = 0; i < length; i++) {
                      var item = data.studios[i];
                      firstStudioResult.append(buildStudioDetail(item, current_center));
                      addMarker(item, map);
                      // testMarker();
                  }

                  // itemQuantities.html(data.item_quantities);
              } else {
                  jQuery('.studio-item').hide();
                  jQuery('#noStudioResult').show();
              }

          },
          dataType: 'json'
      });

  }

  function addMarker(data, map) {
      // TODO clear marker
      // console.log(data.latitude + " "+data.longitude);

      var marker2 = new google.maps.Marker({
          position: new google.maps.LatLng(data.latitude, data.longitude),
          map: map,
          title: data.name,
          icon: image
      });
  }

  function addMarker2(location, map) {
      var marker = new google.maps.Marker({
          position: location,
          title: 'Home Center',
          map:map,
          icon: image
      });
  }


  function testMarker() {
      var sydney = new google.maps.LatLng(-33.867, 151.195);
      var marker = new google.maps.Marker({position: sydney, map: map, icon: image});
  }

  function buildStudioDetail(data, current_center) {
      // console.log(current_center);
      // console.log(data);
      var distance = calc_distance(data.latitude, data.longitude, current_center.latitude, current_center.longitude);
      console.log(distance);

      return '<div data-href="#" class="studio-item" data-index="0" data-lat="'+ data.latitude +'" data-long="'+ data.longitude +'" data-email="" data-image="" >' +
          '    <div class="type"></div>' +
          '    <div class="name"><strong>'+ data.name +'</strong>' + '<span class="pull-right"><strong>'+ distance + '</strong></span></div>' +
          '<div class="address">'+ data.address  + '</div>' +
          '<div class="phone"> ' +'</div>' +
          '<div class="country">' + '</div>' +
          '</div>';
  }

  function buildStudioDetail2(data, current_center) {
      var distance = calc_distance(data.latitude, data.longitude, current_center.latitude, current_center.longitude);
      console.log(distance);

      return
          '<li><h3>'+ data.state + '</h3></li>'+
          '<li>'+
          '  <a href="#">'+
          '    <div>'+
          '        <span class="text-wrapper">'+ data.name +'</span>'+
          '        <span class="greater-than"></span>'+
          '    </div>'+
          '  </a>'+
          '</li>';

  }


  function calc_distance(lat1,lon1,lat2,lon2) {
      var R = 6371; // km (change this constant to get miles)
      var dLat = (lat2-lat1) * Math.PI / 180;
      var dLon = (lon2-lon1) * Math.PI / 180;
      var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
          Math.cos(lat1 * Math.PI / 180 ) * Math.cos(lat2 * Math.PI / 180 ) *
          Math.sin(dLon/2) * Math.sin(dLon/2);
      var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
      var d = R * c;
      if (d>1) return Math.round(d)+"km";
      else if (d<=1) return Math.round(d*1000)+"m";
      return d;
  }

  var groupBy = function(xs, key) {
      return xs.reduce(function(rv, x) {
          (rv[x[key]] = rv[x[key]] || []).push(x);
          return rv;
      }, {});
  };

  function clearOverlays() {
      for (var i = 0; i < markersArray.length; i++ ) {
          markersArray[i].setMap(null);
      }
      markersArray.length = 0;
  }

  jQuery(document).ready(function () {
      // initMap();
      console.log(map);

      // initLoadingElement();
      var resizeId;
      jQuery(window).resize(function () {
          clearTimeout(resizeId);
          resizeId = setTimeout(function () {
              // app.global.equalHeightInit();
              // app.global.slickStaff();
          });
      });

      jQuery(window).scroll(function () {
      });

      // testMarker();

      // jQuery('#search').click(function (e) {
      //     e.preventDefault();
          // var target = $(e.target);
          // var regionId = target.data('value');
          // jQuery.ajax({
          //     type: "POST",
          //     url: ajax.url,
          //     data: {
          //         action: 'set_current_region',
          //         region_id: regionId
          //     },
          //     success: function(data){
          //         if(data.home_url){
          //             window.location.replace(data.home_url);
          //         }else{
          //             window.location.replace('/');
          //         }
          //
          //     },
          //     dataType: 'json'
          // });

          // search();
      // });

      jQuery("#filterStudioResult").on("keyup", function() {
          var value = jQuery(this).val().toLowerCase();
          jQuery("#studioSearchResult div").filter(function() {
              jQuery(this).toggle(jQuery(this).text().toLowerCase().indexOf(value) > -1)
          });
      });

  });

  jQuery(window).on('load', function () {
  });
</script>
<!--<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAMGPyL0Sl6kQc4ri9oVaLELjMgw1c2M5A&callback=initMap"></script>-->
<?php get_footer(); ?>

