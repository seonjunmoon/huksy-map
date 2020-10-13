/**
  * Map project javascript file written for CS61B/CS61BL.
  * This is not an example of good javascript or programming practice.
  * Feel free to improve this front-end for your own personal pleasure.
  * Authors: Alan Yao (Spring 2016), Colby Guan (Spring 2017), Alexander Hwang (Spring 2018), Eli Lipsitz (Spring 2019)
  * If using, please credit authors.
  **/

$(function() {
    'use strict';

    /* ══════════════════════════════════ ೋღ PROPERTIES ღೋ ════════════════════════════════ */
    const $body = $('#mapbody');
    const $routeStatus = $('#status-route');
    const $loadingStatus = $('#status-loading');
    const $errorStatus = $('#status-error');
    const $warningsContainer = $('#status-warnings');
    const $directionsText = $('#directions-text');
    const themeableElements = ['body', '.actions', '.card', '.search', '.ui-autocomplete',
                                '.status', '.settings', '.clear', '.action-icon'];
    const SAFE_WIDTH = 1120;
    const SAFE_HEIGHT = 800;
    // psuedo-lock
    var getInProgress = false;
    var updatePending = false;
    var route_params = {};
    var map;
    var dest;
    var markers = [];
    var host;
    var ullon_bound, ullat_bound, lrlon_bound, lrlat_bound;
    var img_w, img_h;
    var constrain, theme;

    const base_move_delta = 64;
    const MAX_DEPTH = 7;
    const MIN_DEPTH = 1; // Depth limits based on pulled data
    const START_LAT = 47.6553;
    const START_LON = -122.3035;
    const START_DEPTH = 3;
    const ROOT_ULLAT = 47.754097979680026;
    const ROOT_ULLON = -122.6953125;
    const ROOT_LRLAT = 47.51720069783939;
    const ROOT_LRLON = -121.9921875;

    var width = $body.width();
    var height = $body.height();
    var depth = START_DEPTH;
    var lat = START_LAT;
    var lon = START_LON;

    /* Set server URIs */
    host = document.location.origin;
    const raster_server = host + '/raster';
    const route_server = host + '/route';
    const clear_route = host + '/clear_route';
    const search = host + '/search';

    /* ════════════════════════════ ೋღ HELPERS ღೋ ══════════════════════════ */
    /* get_londpp is offset by 1 since lon is twice as wide. */
    function get_londpp() { return (ROOT_LRLON - ROOT_ULLON) / 256 / Math.pow(2, depth + 1); }
    /* get_latdpp is negative. */
    function get_latdpp() { return (ROOT_LRLAT - ROOT_ULLAT) / 256 / Math.pow(2, depth); }

    function get_view_bounds() {
        return {
            ullat: lat - (0.5 * height * get_latdpp()),
            ullon: lon - (0.5 * width * get_londpp()),
            lrlat: lat + (0.5 * height * get_latdpp()),
            lrlon: lon + (0.5 * width * get_londpp()),
            depth: depth
        }
    }

    function removeMarkers() {
        for (var i = 0; i < markers.length; i++) {
            markers[i].element.remove();
        }
        markers = [];
    }

    function updateImg() {
        if (getInProgress) {
            updatePending = true;
            return;
        }

        $loadingStatus.show();
        getInProgress = true;
        var params = get_view_bounds();
        console.log(params);
        $warningsContainer.empty();
        $.get({
            async: true,
            url: raster_server,
            data: params,
            success: function(data) {
                console.log(data);
                if (data.success) {
                    $loadingStatus.hide();
                    map.src = 'data:image/png;base64,' + data.image;
                    ullon_bound = data.ullon;
                    ullat_bound = data.ullat;
                    lrlon_bound = data.lrlon;
                    lrlat_bound = data.lrlat;
                    getInProgress = false;

                    updateT();
                    if (updatePending) {
                        updatePending = false;
                        updateImg();
                    }
                } else {
                    $loadingStatus.hide();
                }
            },
            error: function() {
                getInProgress = false;
                $errorStatus.show();
                setTimeout(function() {
                    $errorStatus.fadeOut();
                }, 4000);
            },
            dataType: 'json'
        });
    }

    function updateT() {
        var londpp = get_londpp();
        var latdpp = get_latdpp();
        var computed = get_view_bounds();
        var tx = (ullon_bound - computed.ullon) / londpp;
        var ty = (ullat_bound - computed.ullat) / latdpp;

        var newHash = "lat=" + lat + "&lon=" + lon + "&depth=" + depth;
        history.replaceState(null, null, document.location.pathname + '#' + newHash);

        map.style.transform = 'translateX(' + tx + 'px) translateY(' + ty + 'px)';
        for (var i = 0; i < markers.length; i++) {
            const marker = markers[i];
            const marker_tx = (marker.lon - computed.ullon) / londpp;
            const marker_ty = (marker.lat - computed.ullat) / latdpp;
            marker.element.css('transform', 'translateX(' + marker_tx + 'px) translateY(' + marker_ty + 'px)');
        }
        // validate transform - true if img needs updating
        return computed.ullon < ullon_bound || computed.ullat > ullat_bound ||
            computed.lrlon > lrlon_bound || computed.lrlat < lrlat_bound;
    }

    function updateRoute() {
        $.get({
            async: true,
            url: route_server,
            data: route_params,
            success: function(data) {
                data = JSON.parse(data);
                updateImg();
                if (data.success) {
                    $directionsText.html(data.directions);
                } else {
                    $directionsText.html('No routing directions to display.');
                }
            },
        });
    }

    function conditionalUpdate() {
        if (updateT()) {
            console.log('Update required.');
            updateImg();
        }
    }

    function zoom(delta) {
        depth += delta;
        updateImg();
    }

    function zoomIn() {
        if (depth === MAX_DEPTH) {
            return;
        }
        zoom(1);
    }

    function zoomOut() {
        if (depth === MIN_DEPTH) {
            return;
        }
        zoom(-1);
    }

    function handleDimensionChange() {
        width = $body.width();
        height = $body.height();
        updateT();
    }

    function updateConstrain() {
        if (constrain) {
            $('#mapbody').css({
                'max-height': SAFE_HEIGHT,
                'max-width': SAFE_WIDTH
            });
        } else {
            $('#mapbody').css({
                'max-height': '',
                'max-width': ''
            });
        }
        handleDimensionChange();
    }

    function handleHashParameters() {
        // https://stackoverflow.com/a/2880929/437550
        var hash = window.location.hash.substring(1).split('&');
        for (var i = 0; i < hash.length; i += 1) {
            var temp = hash[i].split('=');

            if (temp[0] === 'lat') {
                lat = parseFloat(temp[1]);
            } else if (temp[0] === 'lon') {
                lon = parseFloat(temp[1]);
            } else if (temp[0] === 'depth') {
                depth = parseInt(temp[1]);
                console.log("new depth " + depth);
            }
        }
    }

    /* only ran once */
    function loadCookies() {
        const allcookies = document.cookie.replace(/ /g, '').split(';');
        var foundConstrain = false;
        var foundTheme = false;
        for (var i = 0; i < allcookies.length; i++) {
            const kv = allcookies[i].split('=');
            if (kv[0] === 'constrain') {
                constrain = (kv[1] === 'true');
                foundConstrain = true;
                if (constrain === true) {
                    updateConstrain();
                }
            } else if (kv[0] === 'theme') {
                theme = kv[1];
                foundTheme = true;
            }
        }
        if (!foundConstrain) {
            document.cookie = 'constrain=false';
            constrain = false;
        }
        if (!foundTheme) {
            document.cookie = 'theme=default';
            theme = 'default';
        }
        const date = new Date();
        // Expire 7 days from now
        date.setTime(date.getTime() + 604800000);
        document.cookie = 'expires='+date.toGMTString();
    }

    function setTheme() {
        themeableElements.forEach(function(elem) {
            $(elem).removeClass('solarized');
            $(elem).removeClass('eighties');
            $(elem).addClass(theme);
        });
    }

    function setCookie(key, value) {
        document.cookie = key + '=' + value.toString();
    }

    /* ══════════════════════════════════ ೋღ SETUP ღೋ ════════════════════════════════ */

    map = document.getElementById('map');
    map.addEventListener('load', function() {
            console.log('Updating map with image: ' + map.width + 'x' + map.height);
            var warnings = [];
            if (map.width > width + 512) {
                warnings.push("got much wider image than expected. window width: " + width + ". got: " + map.width);
            }
            if (map.height > height + 512) {
                warnings.push("got much taller image than expected. window height: " + height + ". got: " + map.height);
            }
            if (warnings.length > 0) {
                var ele = $('<div/>', {
                    class: 'card-content'
                });
                ele.html("Warnings:<br>" + warnings.join("<br>"));
                ele.appendTo($warningsContainer);
            }
    })
    dest = document.getElementById('dest');
    dest.style.visibility = 'hidden';
    loadCookies();
    handleHashParameters();
    setTheme();
    updateImg();

    /* Make search bar do autocomplete things */
    $('#tags').autocomplete({
          source: search,
          minLength: 2,
          select: function (event, ui) {
              $.get({
                  async: true,
                  url: search,
                  dataType: 'json',
                  data: { term: ui.item.value, full: true},
                  success: function(data) {
                      removeMarkers();
                      for (var i = 0; i < data.length; i++) {
                          console.log(data[i]);
                          const ele = $('<img/>', {
                              id: "marker_" + data[i].id,
                              src: 'round_marker.gif',
                              class: 'rmarker'
                          });
                          ele.appendTo($('#markers'));
                          markers.push({lat: data[i].lat, lon: data[i].lon, element: ele});
                      }
                      updateT();
                  },
              });
          }
    });
    setTheme();

    /* ══════════════════════════════════ ೋღ EVENTS ღೋ ════════════════════════════════ */

    $('.ui-autocomplete').mouseenter(function() {
        $('.actions').addClass('active');
    }).mouseleave(function() {
        $('.actions').removeClass('active');
    });

    /* Enables drag functionality */
    $body.on('mousedown', function(event) {
      if (event.which !== 1) {
          return; // ignore non-left clicks
      }
      var startX = event.pageX;
      var startY = event.pageY;
      var startLon = lon;
      var startLat = lat;

      $body.on('mousemove', function(event) {
        const dx = event.pageX - startX;
        const dy = event.pageY - startY;
        lon = startLon - (dx * get_londpp());
        lat = startLat - (dy * get_latdpp());
        updateT();
      });

      $body.on('mouseup', function(event) {
        $body.off('mousemove');
        $body.off('mouseup');
        conditionalUpdate();
      });
    });

    $('.zoomin').click(function() {
       zoomIn();
    });

    $('.zoomout').click(function() {
       zoomOut();
    });

    $('.clear').click(function() {
        $.get({
            async: true,
            url: clear_route,
            success: function() {
                dest.style.visibility = 'hidden';
                $directionsText.html('No routing directions to display.');
                updateImg();
            },
        });
    });

    $('.info').click(function() {
        $(this).toggleClass('active');
        $('.settings-container').removeClass('active');
    });
    $('.fa-cog').click(function() {
        $('.settings-container').addClass('active');
        if (constrain) {
            $('#constrain-input').prop('checked', true);
        }
        $('input[name=theme][value=' + theme + ']').prop('checked', true);
        $('.info').removeClass('active');
    });
    $('.close-settings').click(function() {
        $('.settings-container').removeClass('active');
    });

    $('.fa-map-signs').click(function() {
        $('.directions-container').addClass('active');
    });
    $('.close-directions').click(function() {
        $('.directions-container').removeClass('active');
    });

    $('body').dblclick(function handler(event) {
        if (route_params.start_lon && route_params.end_lon) { //finished routing, reset routing
            route_params = {};
        }
        const offset = $body.offset();
        const viewbounds = get_view_bounds();
        var click_lon = (event.pageX - offset.left) * get_londpp() + viewbounds.ullon;
        var click_lat = (event.pageY - offset.top) * get_latdpp() + viewbounds.ullat;

        if (route_params.start_lon) { // began routing already but not finished
            route_params.end_lon = click_lon;
            route_params.end_lat = click_lat;
            $routeStatus.hide();
            updateRoute();
            dest.style.visibility = 'visible';
            updateImg();
        } else {
            route_params.start_lon = click_lon;
            route_params.start_lat = click_lat;
            $routeStatus.show();
        }
    });

    /* Enables scroll wheel zoom */
    $(window).bind('mousewheel DOMMouseScroll', function(event){
        if (event.originalEvent.wheelDelta > 0 || event.originalEvent.detail < 0) {
            zoomIn();
        } else {
            zoomOut();
        }
    });

    /* Prevent image dragging */
    $('img').on('dragstart', function(event) { event.preventDefault(); });

    // Allow for window resizing
    window.onresize = function() {
        handleDimensionChange();
        updateImg();
    };

    window.onhashchange = function() {
        handleHashParameters();
        updateImg();
    };

    $('#constrain-input').change(function() {
        constrain = $(this).is(':checked');
        updateConstrain();
        setCookie('constrain', constrain);
        updateImg();
    });

    $('input[type=radio][name=theme]').change(function() {
        theme = this.value;
        setCookie('theme', this.value);
        setTheme();
    });

    /* Keyboard navigation callbacks */
    document.onkeydown = function(e) {
        var delta = base_move_delta;
        switch (e.keyCode) {
            case 37: //left
                lon -= delta * get_londpp();
                conditionalUpdate();
                break;
            case 38: //up
                lat -= delta * get_latdpp();
                conditionalUpdate();
                break;
            case 39: //right
                lon += delta * get_londpp();
                conditionalUpdate();
                break;
            case 40: //down
                lat += delta * get_latdpp();
                conditionalUpdate();
                break;
            case 189: //minus
                zoomOut();
                break;
            case 187: //equals/plus
                zoomIn();
                break;
        }
    };
});
