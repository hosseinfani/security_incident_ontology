<?php
/* in this file the ARC2 library is used to connect to SPRQL endpoint write queries and get back result*/
include_once "lib/arc/ARC2.php";
ini_set('display_errors', 'On');
error_reporting(E_ALL);

function queryVirtuoso($query)
{
    $config = array(
        /* store name If you want to connect to a SPARQL end point like virtuoso*/
    'store_name' => 'http://ls3.rnet.ryerson.ca/SecurityIncident/test',
    'remote_store_endpoint' => 'http://s2-ls3.rnet.ryerson.ca:8890/sparql',
    'endpoint_write_key' => 'REPLACE_THIS_WITH_SOME_KEY', /* optional, but without one, everyone can write! */
      );
    
    $store = ARC2::getRemoteStore($config);
    if(!$store)
        echo 'ERROR: cannot connect to the sparql endpoint!';
    $parser = ARC2::getSPARQLParser();
    $parser->parse($query);
    if (!$parser->getErrors()) 
        $query_infos = $parser->getQueryInfos();
    else 
    {
        echo "Error!";
        var_dump($parser->getErrors());
    }
    $rows = $store->query($query, 'rows');
    //var_dump($rows);
    return $rows;
}
?>
