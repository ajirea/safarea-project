<?php

use Slim\Psr7\Request;
use Slim\Psr7\Response;

//Tampil order berdasarkan id dropshipper
$route->get('/order/{dropshipper_id}', function (Request $request, Response $response, $args) {

    $query = $this->get('db')->prepare("SELECT A.*, B.thumbnail, B.name FROM orders AS A INNER JOIN products AS B ON B.id=A.product_id WHERE A.user_id=?");
    $query->bindParam(1, $args['dropshipper_id']);
    $query->execute();

    $buyers = $query->fetchAll(PDO::FETCH_OBJ);

    $results = [
        'status' => true,
        'data' => $order
    ];

    $response->getBody()->write(json_encode($results));

    return $response
        ->withHeader('Content-Type', 'application/json');

});	