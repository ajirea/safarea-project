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

    $result = [
        'status' => true,
        'data' => []
    ];

    if($query->rowCount() < 1) {
        $result['status'] = false;
        $result['data']['message'] = 'Order tidak ditemukan';
    } else {
        $result['data'] = $query->fetch(PDO::FETCH_OBJ);
    }

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');

});	

//Add order , harus ada ID dropshipper
$route->post('/order/{dropshipper_id}', function(Request $request, Response $response, $args) {
    $input = $request->getParsedBody();

    $dropshipper = getUser($args['dropshipper_id']);

    if(!$dropshipper)
        return dropshipperNotFound($response);

    $query = $this->get('db')->prepare("SELECT B.price, A.profit_price FROM user_products AS A INNER JOIN products AS B ON B.id=A.product_id WHERE A.user_id=? AND A.product_id=?");
    $query->bindParam(1, $dropshipper->id);
    $query->bindParam(2, $input['product_id']);
    $query->execute();

    if($query->rowCount() < 1)
        return dropshipperNotFound($response);

    $product = $query->fetch(PDO::FETCH_OBJ);
    $total = ($product->profit_price + $product->price) * $input['qty'];
    $query = $this->get('db')->prepare("INSERT INTO orders (user_id, buyer_id, product_id, price, profit_price, qty, total, description, created_at) VALUES (?,?,?,?,?,?,?,?,?)");
    $query->bindParam(1, $dropshipper->id);
    $query->bindParam(2, $input['buyer_id']);
    $query->bindParam(3, $input['product_id']);
    $query->bindParam(4, $product->price);
    $query->bindParam(5, $product->profit_price);
    $query->bindParam(6, $input['qty']);
    $query->bindParam(7, $total);
    $query->bindParam(8, $input['description']);
    $query->bindParam(9, $input['created_at']);

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

