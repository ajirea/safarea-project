<?php

use Slim\Psr7\Request;
use Slim\Psr7\Response;

//Tampil semua products
$route->get('/product', function (Request $request, Response $response) {

    // ambil data products dari database
    $query = $this->get('db')->prepare("SELECT * FROM products ORDER BY created_at DESC");
    $query->execute();
    $products = $query->fetchAll(PDO::FETCH_OBJ);

    foreach($products as $index => $product) {
        $query = $this->get('db')->query("SELECT * FROM product_images WHERE product_id=$product->id");
        $images = $query->fetchAll(PDO::FETCH_OBJ);
        $products[$index]->images = $images;
    }

    $results = [
        'status' => true,
        'data' => $products
    ];

    $response->getBody()->write(json_encode($results));

    return $response
        ->withHeader('Content-Type', 'application/json');
});

//Tampil products berdasarkan slug
$route->get('/product/{slug}', function (Request $request, Response $response, $args) {

    // ambil data products dari database
    $query = $this->get('db')->prepare("SELECT * FROM products WHERE slug=?");
    $query->bindParam(1, $args['slug']);
    $query->execute();
    
    // periksa apakah product berdasarkan slug ditemukan
    if($query->rowCount() < 1) {
        $results = [
            'status' => false,
            'data' => [
                'message' => 'Produk tidak ditemukan'
            ]
        ];
        $response->getBody()->write(json_encode($results));
        return $response->withHeader('Content-Type', 'application/json');
    }

    // jika ditemukan
    // masukkan data ke variable $product
    $product = $query->fetch(PDO::FETCH_OBJ);

    // ambil data images
    $query = $this->get('db')->query("SELECT * FROM product_images WHERE product_id=$product->id");
    $images = $query->fetchAll(PDO::FETCH_OBJ);
    
    // masukkan images ke atribut images pada obyek $product
    $product->images = $images;

    $results = [
        'status' => true,
        'data' => $product
    ];

    $response->getBody()->write(json_encode($results));

    return $response
        ->withHeader('Content-Type', 'application/json');
});