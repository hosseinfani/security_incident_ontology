/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//function initialize() {
//    var mapProp = {
//        center:new google.maps.LatLng(51.508742,-0.120850),
//        zoom:5,
//        mapTypeId:google.maps.MapTypeId.ROADMAP
//    };
//    var map=new google.maps.Map(document.getElementById("googleMap"), mapProp);
//}
//google.maps.event.addDomListener(window, 'load', initialize);

var $ajaxXmlHttpRequest;
function refresh()
{
    var query = document.getElementById('query').value;
    var result = document.getElementById('result');
    var url = '?query='+encodeURIComponent(query);
    var popup = document.getElementById('backgroundPopup');
    popup.style.display="block";
    
    $ajaxXmlHttpRequest = generateAjaxXmlHttpRequest();
    $ajaxXmlHttpRequest.onreadystatechange = function()
    {
        if ($ajaxXmlHttpRequest.readyState == 4 && $ajaxXmlHttpRequest.status == 200)
        {
            result.innerHTML = $ajaxXmlHttpRequest.responseText;
            if(result.innerHTML.search("Error") < 0){
                
                onDateChange(null);
            }
            popup.style.display="none";
        }
    }
    $ajaxXmlHttpRequest.addEventListener("error", function(evt){alert("an error occured");popup.style.display="none";}, false);
    $ajaxXmlHttpRequest.open("GET", url,true);
    $ajaxXmlHttpRequest.send();
}
function generateAjaxXmlHttpRequest()
{
    //if ($ajaxXmlHttp)
    //return $ajaxXmlHttp;
    var $ajaxXmlHttp = null;
    if (window.XMLHttpRequest)
    {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        $ajaxXmlHttp=new XMLHttpRequest();
    }
    else
    {
        // code for IE6, IE5
        $ajaxXmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    return $ajaxXmlHttp;
}

$checkCount = 6;
function boxclick(checkbox, securityincidenttype){
    
    var query = document.getElementById('query').value;
    if(checkbox.checked)
        ++$checkCount;
    else{
        if($checkCount <= 1){
        alert("At least one type should be selected!");
        checkbox.checked = true;
        return;
        }
        --$checkCount;
    }
    if(checkbox.checked)
        query = query.replace("#{?securityIncidentId rdf:type sio:" + securityincidenttype + "}", "{?securityIncidentId rdf:type sio:" + securityincidenttype + "}");
    else
        query = query.replace("{?securityIncidentId rdf:type sio:" + securityincidenttype + "}", "#{?securityIncidentId rdf:type sio:" + securityincidenttype + "}");
    document.getElementById('query').value = query;
    refresh();
    
}
$map = null;
$markers = [];
function initialize()
{
    var mapOpt = {
      center:new google.maps.LatLng(43.658900, -79.38000),
      zoom:15,
      mapTypeId:google.maps.MapTypeId.ROADMAP
      };
    $map = new google.maps.Map(document.getElementById("googleMap"),mapOpt);
    onDateChange(null);
}
google.maps.event.addDomListener(window, 'load', initialize);

function locateIncidents(from, to){
    var popup = document.getElementById('backgroundPopup');
    popup.style.display="block";
    for (var i = 0; i < $markers.length; i++) {
        $markers[i].setMap(null);
    }
    $markers = [];
    var result = $.parseJSON(document.getElementById('result').children[0].value);
    for(var i = 0; i < result.length; ++i){   
        var date = extractNumber(result[i].day) + "-" + extractNumber(result[i].month) + "-" + extractNumber(result[i].year);
        var rdate = reverse(date);
        if(rdate.localeCompare(from) < 0 || rdate.localeCompare(to) > 0)
            continue;
        var type = result[i].type.substr(result[i].type.lastIndexOf("/") + 1);
        var pinColor = document.getElementById(type).nextElementSibling.style.color;
        var pinLetter = type[0].toUpperCase();
        var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=" + pinLetter + "|" + rgb2hex(pinColor) + "|000000");
        var marker = new google.maps.Marker({
            position:new google.maps.LatLng(extractNumber(result[i].lat),extractNumber(result[i].lng)), 
            icon:pinImage, 
            map:$map});
        marker.setMap($map);
        marker.infoWindow = new google.maps.InfoWindow({content:'<div id="content">' + date.replace(/-/g,",") + '</div>', maxWidth: 200});
        google.maps.event.addListener(marker, 'click', function(){this.infoWindow.open($map,this);});
        $markers.push(marker);
    }
     popup.style.display="none";
}

function extractNumber(string){
    return string.substring(string.indexOf("\"") + 1, string.indexOf("^^") - 1);
}
function rgb2hex(rgb){
 rgb = rgb.match(/^rgba?[\s+]?\([\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?/i);
 return (rgb && rgb.length === 4) ?
  ("0" + parseInt(rgb[1],10).toString(16)).slice(-2) +
  ("0" + parseInt(rgb[2],10).toString(16)).slice(-2) +
  ("0" + parseInt(rgb[3],10).toString(16)).slice(-2) : '';
}

$fromDate = null;
$toDate = null;
function onDateChange(v){
    var from = document.getElementById('from').value.split("-");
    var to = document.getElementById('to').value.split("-");
    
    for(var i=0; i < 3; ++i){
        if(parseInt(from[i]) < 1 || parseInt(to[i]) < 1){
            alert("Choose a proper date!");
            rollbackDate();
        }
    }
    
    if(document.getElementById('from').value.localeCompare(document.getElementById('to').value)> 0){
        alert("From date should be after To date!");
        rollbackDate();
        return;
    }
    $fromDate = document.getElementById('from').value;
    $toDate = document.getElementById('to').value;
    locateIncidents(document.getElementById('from').value, document.getElementById('to').value);
    return false;
}
function rollbackDate(){
    var from = $fromDate.split("-");
    var to = $toDate.split("-");
    tc_setDay('from', from[2], false);
    tc_setMonth('from', from[1], false);
    tc_setYear('from', from[0], false);
    tc_setDay('to', to[2], false);
    tc_setMonth('to', to[1], false);
    tc_setYear('to', to[0], false);
}
function reverse(s){
    return s.split("-").reverse().join("-");
}