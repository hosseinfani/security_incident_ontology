<?php
include_once './sparql.php';
require_once('./calendar/classes/tc_calendar.php');

$defaultQuery = 'PREFIX sio: <http://ls3.rnet.ryerson.ca/ontologies/sio/>
PREFIX event: <http://purl.org/NET/c4dm/event.owl#>
PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>
PREFIX time: <http://www.w3.org/2006/time#>
SELECT distinct ?type ?lat ?lng ?year ?month ?day 
WHERE
{
    GRAPH <http://ls3.rnet.ryerson.ca/SecurityIncident/test>
    {
        {?securityIncidentId rdf:type sio:Assault}              UNION
        {?securityIncidentId rdf:type sio:AssaultWithWeapon}    UNION
        {?securityIncidentId rdf:type sio:SexualAssault}        UNION
        {?securityIncidentId rdf:type sio:Robbery}              UNION
        {?securityIncidentId rdf:type sio:Voyeurism}            UNION
        {?securityIncidentId rdf:type sio:IndecentExposure}     UNION
        {<p> <p> <o>}

        ?securityIncidentId rdf:type ?type.

        ?securityIncidentId event:place ?placeId.
        ?placeId geo:lat  ?lat.
        ?placeId geo:long ?lng.

        ?securityIncidentId event:time ?timeId.
        ?timeId time:inDateTime ?dateTime.
        ?dateTime time:year ?year.
        ?dateTime time:month ?month.
        ?dateTime time:day ?day.

        FILTER 
        (
            (?type != owl:NamedIndividual)  && 
            (
                1 = 1
            )
        )
        
    }
}';
//(xsd:integer(fn:substring-before(fn:replace(?year,"\"",""),"^^")) > @year) ||
//((xsd:integer(fn:substring-before(fn:replace(?year,"\"",""),"^^")) = @year) && (xsd:integer(fn:substring-before(fn:replace(?month,"\"",""),"^^")) > @month))||
//(((xsd:integer(fn:substring-before(fn:replace(?year,"\"",""),"^^")) = @year) && (xsd:integer(fn:substring-before(fn:replace(?month,"\"",""),"^^")) = @month)) && (xsd:integer(fn:substring-before(fn:replace(?day,"\"",""),"^^")) > @day))

if(!empty($_GET["query"])){
    $query = $_GET["query"];
    $result = json_encode(queryVirtuoso($query));
    if(strpos($result, "Error") === false){
        echo '<textarea style="height: 0px; width: 100%;visibility: hidden">';
    }
    else{ 
        echo '<textarea style=" width: 100%>';
    }
    echo $result;
    echo '</textarea>';
    return;
}
 else {
    $defaultQuery = str_replace("@day", date('d'), str_replace("@month", date('m'), str_replace("@year", date('Y'), $defaultQuery)));
    $result = json_encode(queryVirtuoso($defaultQuery));
}

?>
<!--<!DOCTYPE html>-->
<html>
    <head>
        <title>Ryerson Security Incident Data Mart</title>
        <script src="js/jquery/jquery.js" type="text/javascript"></script>
        <script src="http://maps.googleapis.com/maps/api/js"></script>
        <script type="text/javascript" charset="UTF-8" src="js/index.js"></script>
        
        <link href="calendar/calendar.css" rel="stylesheet" type="text/css" />
        <script language="javascript" src="calendar/calendar.js"></script>

        <style type="text/css">
        body { font-size: 11px; font-family: "verdana"; }

        pre { font-family: "verdana"; font-size: 10px; background-color: #FFFFCC; padding: 5px 5px 5px 5px; }
        pre .comment { color: #008000; }
        pre .builtin { color:#FF0000;  }
        </style>
    </head>
            
    <body>
        <table style="width: 100%;height: 100%">
            <tbody>
<!--                <tr>
                    <td style="width: 50%">
                        <h3>SPARQL Query</h3>
                        <li>
                            <label for="graph-uri">Graph Name (IRI)</label>
                            <input type="text" name="graphuri" id="graphuri" style="margin: 0px; width: 80%;" value="http://ls3.rnet.ryerson.ca/SecurityIncident/test" disabled="">
                            <input type="button" name="go" id="go" value="Go" onclick="javascript: callSparql ('go', 'graphuri', 'query', 'result', 'error'); return false" style="margin: 0px; width: 20%; visibility: hidden" >
                        </li>
                        <textarea name="query" id="query" style="margin: 0px; height: 100%; width: 100%;" disabled=""><?php //echo $defaultQuery;?></textarea>
                        <textarea id="result" style="margin: 0px; width: 100%;visibility: hidden"><?php //echo $result;?></textarea>
                        <textarea id="error" style="margin: 0px; width: 100%;visibility: hidden"></textarea>
                    </td>
                    <td style="width: 100%">
                        <table>
                            <tbody>-->
                                <tr style="height: 2%; text-align: center;">
                                    <td colspan="4">
                                        <input type="checkbox" id="Assault" onclick="boxclick(this,'Assault')" checked="">
                                        <label style="color: #468847">Assault</label> 
                                        <input type="checkbox" id="AssaultWithWeapon" onclick="boxclick(this,'AssaultWithWeapon')" checked="">
                                        <label style="color: violet">Assault with Weapon</label>
                                        <input type="checkbox" id="SexualAssault" onclick="boxclick(this,'SexualAssault')" checked="">
                                        <label style="color: tomato">Sexual Assault</label>
                                        <input type="checkbox" id="Robbery" onclick="boxclick(this,'Robbery')" checked="">
                                        <label style="color: peru">Robbery</label>
                                        <input type="checkbox" id="Voyeurism" onclick="boxclick(this,'Voyeurism')" checked="">
                                        <label style="color: darkgrey">Voyeurism</label>
                                        <input type="checkbox" id="IndecentExposure" onclick="boxclick(this,'IndecentExposure')" checked="">
                                        <label style="color: #0077b3">Indecent Exposure</label> 
                                <textarea name="query" id="query" style="height: 0px;visibility: hidden" ><?php echo $defaultQuery;?></textarea>
                                        <div id="result" >
                                            <?php
                                            if(strpos($result, "Error") === false){
                                                echo '<textarea id="result" style="height: 0px; width: 100%;visibility: hidden">';
                                            }
                                            else{ 
                                                echo '<textarea id="result" style="width: 100%>';
                                            }
                                            echo $result;
                                            echo '</textarea>';
                                            ?>
                                        </div>
                                    </td>
                                </tr>
                                <tr style="height: 2%;" >
                                    <td style="text-align: right;width: 30%">From</td>
                                    <td style="width: 15%">
                                        <?php
                                          $myCalendar = new tc_calendar("from", true);
                                          $myCalendar->setIcon("calendar/images/iconCalendar.gif");
                                          $myCalendar->setDate(date('d'), date('m'), date('Y'));
                                          $myCalendar->setPath("calendar/");
                                          $myCalendar->setYearInterval(1960, 2015);
                                          $myCalendar->setOnChange("onDateChange('from')");
                                          $myCalendar->writeScript();
                                          $myCalendar->autoSubmit(FALSE, "");
                                        ?>
                                    </td>
                                    <td style="text-align: right;width: 1%">To:</td>
                                    <td style="width: 50%">
                                            <?php
                                          $myCalendar = new tc_calendar("to", true);
                                          $myCalendar->setIcon("calendar/images/iconCalendar.gif");
                                          $myCalendar->setDate(date('d'), date('m'), date('Y'));
                                          $myCalendar->setPath("calendar/");
                                          $myCalendar->setYearInterval(1960, 2015);
                                          $myCalendar->setOnChange("onDateChange('to')");
                                          $myCalendar->autoSubmit(FALSE, "");
                                          $myCalendar->writeScript();
                                        ?>
                                    </td>
                                    
                                </tr>
                                        
                                <tr> 
                                    <td colspan="4"><div id="googleMap" style="width: 100%;height: 100%" ></div>
                                    </td>
                                </tr>
<!--                            </tbody>
                        </table>
                        
                    </td>
                </tr>-->
            </tbody>
        </table>
        <div id="backgroundPopup" style="text-align: center;display: none; position: fixed; top: 0; left: 0; bottom: 0; right: 0; background: #000000; opacity: .3; -moz-opacity: .3; filter: alpha(opacity=30); border: 1px solid #cecece; z-index: 1; display: none;">
            <div style="position: absolute;top: 50%;left: 0px;width: 100%;"><img src="img/loading.gif" ></div>
        </div>        
    </body>
</html>