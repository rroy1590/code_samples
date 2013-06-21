<?php
require_once("simple_html_dom.php");

if($argv === null || empty($argv) || count($argv) < 2)
{
	echo "Invalid entry!\n\nEx:\n$ ~ php crawler.php http://www.google.com\n";
	exit;
}

$base_url = $argv[1];
$base_url_params = parse_url($base_url);

$links_in = array();
$domains_checked = array();
$links_in[] = $base_url."/";

$index = 0;
$numChecked = 0;
while(count($links_in) > 0)
{
  $test_url = $links_in[$index];
  $domains_checked[] = $test_url;
  
  echo $test_url." checked: ".$numChecked++.PHP_EOL;
  
  $html = file_get_html($test_url);

  $dom = new DOMDocument();
  @$dom->loadHTML($html);

  // grab all the hrefs on the page
  $xpath = new DOMXPath($dom);
  $hrefs = $xpath->evaluate("/html/body//a");
  
  for ($i = 0; $i < $hrefs->length; $i++) {
     $href = $hrefs->item($i);
     $url = $href->getAttribute('href');
     $links[] = $url;
     $url_params = parse_url($url);
     $new_url=$base_url.$url_params['path'];
   
     if(!isset($url_params['host']) || $url_params['host'] === $base_url_params['host'])
     {
       if(!in_array($new_url, $domains_checked) && !in_array($new_url."/",$domains_checked) && !in_array($new_url."/",$links_in) && !in_array($new_url, $links_in) && strpos($url,'@') === false && strpos($url, '..') === false)
       {
         // echo "INSIDE: ".$base_url.$url_params['path'];
         $links_in[] = $base_url.$url_params['path'];
       }
     }
  }
  unset($links_in[$index]);
  $index++;
}

