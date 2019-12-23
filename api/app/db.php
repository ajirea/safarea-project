<?php

try {
	
    // init connection
    $connection = new PDO("mysql:host=localhost;dbname=safarea", "root", "");
    
    // add to container
    $container->set('db', $connection);
} catch (\PDOException $error) {
    die('PDO ERROR: ' . $error->getMessage());
}