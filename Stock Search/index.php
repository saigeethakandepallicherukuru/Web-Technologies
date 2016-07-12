<?php 
header("Access-Control-Allow-Origin: *");
    if(isset($_GET["CompanySymbol"]) && !isset($_GET["getQuote"]))
    {
		$symbol=$_GET["CompanySymbol"];
        $lookupURL="http://dev.markitondemand.com/MODApis/Api/v2/Lookup/JSON?input=".$symbol;
        $contents = file_get_contents($lookupURL); 
        $contents = utf8_encode($contents); 
        echo $contents;
	} 
    if(isset($_GET["CompanySymbol"]) && isset($_GET["getQuote"])) {
        $symbol=$_GET["CompanySymbol"];
        $url="http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol=".$symbol;
        $contents = file_get_contents($url); 
        $contents = utf8_encode($contents);
        echo $contents;
    }
    if(isset($_GET["CompanyNews"])) {
        $symbol=$_GET["CompanyNews"];
        $accountKey = '7sKAk4ZxYiMsM9z5wMe6+P8aH85Io/KVxNSHtBmCToc';
        $ServiceRootURL =  'https://api.datamarket.azure.com/Bing/Search/v1/News?Query=%27'.$symbol.'%27&$format=json';
        
        $context = stream_context_create(array(
        'http' => array(
            'request_fulluri' => true,
            'header'  => "Authorization: Basic " . base64_encode($accountKey . ":" . $accountKey)
        )
        ));
        $response = file_get_contents($ServiceRootURL, 0, $context);
        echo $response; 
    }
    if(isset($_GET["ChartSymbol"])) {
        $interactiveURL="http://dev.markitondemand.com/MODApis/Api/v2/InteractiveChart/json?parameters={%22Normalized%22:false,%22NumberOfDays%22:1095,%22DataPeriod%22:%22Day%22,%22Elements%22:[{%22Symbol%22:%22".$_GET["ChartSymbol"]."%22,%22Type%22:%22price%22,%22Params%22:[%22ohlc%22]}]}";
        $contents = file_get_contents($interactiveURL); 
        echo $contents;
    }
?>