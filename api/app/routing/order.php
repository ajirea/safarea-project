<?php

use Slim\Psr7\Request;
use Slim\Psr7\Response;

//Tampil order berdasarkan id dropshipper
$route->get('/order/{dropshipper_id}', function (Request $request, Response $response, $args) {

    $query = $this->get('db')->prepare("SELECT A.*, B.thumbnail, B.name, C.name AS buyer_name, C.phone FROM orders AS A INNER JOIN products AS B ON B.id=A.product_id INNER JOIN buyers AS C ON C.id=A.buyer_id WHERE A.user_id=?");
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

//Tampil order berdasarkan order id
$route->get('/order/{dropshipper_id}/{order_id}', function (Request $request, Response $response, $args) {

    $query = $this->get('db')->prepare("SELECT A.*, B.thumbnail, B.name, C.name AS buyer_name, C.phone FROM orders AS A INNER JOIN products AS B ON B.id=A.product_id INNER JOIN buyers AS C ON C.id=A.buyer_id WHERE A.user_id=? AND A.id=? ");
    $query->bindParam(1, $args['dropshipper_id']);
    $query->bindParam(2, $args['order_id']);
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

//Add order , harus ada ID
$route->post('/order', function(Request $request, Response $response) {
    $input = $request->getParsedBody();

    $query = $this->get('db')->prepare("INSERT INTO orders (user_id, buyer_id, phone, created_at, product_id, qty, description, total) VALUES (?,?,?,?,?,?,?,?)");
    $query->bindParam(1, $input['user_id']);
    $query->bindParam(2, $input['buyer_id']);
    $query->bindParam(3, $input['phone']);
    $query->bindParam(4, $input['created_at']);
    $query->bindParam(5, $input['product_id']);
    $query->bindParam(6, $input['qty']);
    $query->bindParam(7, $input['description']);
    $query->bindParam(8, $input['total']);
    $query->execute();


    $order = $query->execute();

    $result = [
        'status' => $order,
        'data' => []
    ];

    if(!$order)
        $result['data']['message'] = 'Gagal menambahkan order';
    else 
        $result['data']['message'] = 'Berhasil menambahkan order';

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
        
});

