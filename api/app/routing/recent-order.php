<?php

use Slim\Psr7\Request;
use Slim\Psr7\Response;

//Tampil order terkini berdasarkan dropshipper ID
$route->get('/recent-order/{dropshipper_id}', function (Request $request, Response $response, $args) {

    $query = $this->get('db')->prepare("SELECT A.*, B.name, B.thumbnail FROM orders AS A INNER JOIN products AS B ON B.id=A.product_id ORDER BY created_at DESC LIMIT 7");
    $query->bindParam(1, $args['dropshipper_id']);
    $query->execute();

    $order = $query->fetchAll(PDO::FETCH_OBJ);

    $results = [
        'status' => true,
        'data' => $order
    ];

    $response->getBody()->write(json_encode($results));

    return $response
        ->withHeader('Content-Type', 'application/json');

});