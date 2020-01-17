<?php

use Slim\Psr7\Request;
use Slim\Psr7\Response;

//Tambah products
$route->post('/product', function (Request $request, Response $response) {

    $input = $request->getParsedBody();
    $thumbnail = '/noimage.png';

    // unggah thumbnail
    if(isset($input['images'])) {
        $thumbnail = uploadProductImage($input['images'][0], 'thumbnail');
    }

    // tambah ke tabel products
    $query = $this->get('db')->prepare("INSERT INTO products (name, slug, thumbnail, price, stock, description) VALUES (?,?,?,?,?,?)");
    $query->bindParam(1, $input['name']);
    $query->bindParam(2, $input['slug']);
    $query->bindParam(3, $thumbnail);
    $query->bindParam(4, $input['price']);
    $query->bindParam(5, $input['stock']);
    $query->bindParam(6, $input['description']);
    $query->execute();

    $productId = $this->get('db')->lastInsertId();

    if(isset($input['images'])) {
        foreach($input['images'] as $image) {
            $path = uploadProductImage($image, 'image');
            $name = basename($path);
            $query = $this->get('db')->prepare("INSERT INTO product_images (product_id, name, path) VALUES (?,?,?)");
            $query->bindParam(1, $productId);
            $query->bindParam(2, $name);
            $query->bindParam(3, $path);
            $query->execute();
        } 
    }

    $results = [
        'status' => true,
        'data' => [
            'message' => 'Berhasil menambahkan produk'
        ]
    ];

    $response->getBody()->write(json_encode($results));

    return $response
        ->withHeader('Content-Type', 'application/json');
});

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
    $query = $this->get('db')->prepare("SELECT * FROM products WHERE slug=? OR id=?");
    $query->bindParam(1, $args['slug']);
    $query->bindParam(2, $args['slug']);
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

//Tampil semua products berdsarkan dropshipper id
$route->get('/product/dropshipper/{dropshipper_id}', function (Request $request, Response $response, $args) {
    // masih kosong
    $dropshipper = getUser($args['dropshipper_id']);

    if(!$dropshipper)
        return dropshipperNotFound($response);

    $query = $this->get('db')->prepare("SELECT A.*, B.name, B.slug, B.thumbnail, B.price, B.description FROM user_products AS A INNER JOIN products AS B ON B.id=A.product_id WHERE A.user_id=? AND A.status='active' ORDER BY A.id DESC");

    $query->bindParam(1, $dropshipper->id);
    $query->execute();

    $products = $query->fetchAll(PDO::FETCH_OBJ);

    foreach ($products as $index => $product) {
        $query = $this->get('db')->query("SELECT * FROM product_images WHERE product_id=$product->product_id");
        $images = $query->fetchAll(PDO::FETCH_OBJ);
        $products[$index]->images = $images;
    }
    
    $results = [
        'status' => true,
        'data' => $products
    ];

    $response->getBody()->write(json_encode($results));

    return $response->withHeader('Content-Type', 'application/json');
});

// Tampil semua stock berdasarkan dropshipper id
$route->get('/product/dropshipper/{dropshipper_id}/stock', function (Request $request, Response $response, $args) {
    // masih kosong
    $dropshipper = getUser($args['dropshipper_id']);

    if (!$dropshipper)
        return dropshipperNotFound($response);

    $query = $this->get('db')->prepare("SELECT A.*, B.name, B.price, B.thumbnail, B.description FROM user_products AS A INNER JOIN products AS B ON B.id = A.product_id WHERE A.user_id = ? ORDER BY A.status DESC, A.id DESC");

    $query->bindParam(1, $dropshipper->id);
    $query->execute();

    $results = [
        'status' => true,
        'data' => []
    ];

    $products = $query->fetchAll(PDO::FETCH_OBJ);

    foreach ($products as $idx => $product) {
        $product->status_description = getStockStatus($product->status);
        $products[$idx] = $product;
    }

    $results['data'] = $products;

    $response->getBody()->write(json_encode($results));

    return $response->withHeader('Content-Type', 'application/json');
});

//Tampil product berdsarkan slug/id dan dropshipper id
$route->get('/product/dropshipper/{dropshipper_id}/{slug}', function (Request $request, Response $response, $args) {
    // masih kosong
    $dropshipper = getUser($args['dropshipper_id']);

    if (!$dropshipper)
        return dropshipperNotFound($response);

    $query = $this->get('db')->prepare("SELECT A.*, B.name, B.slug, B.thumbnail, B.price, B.description FROM user_products AS A INNER JOIN products AS B ON B.id=A.product_id WHERE A.user_id=? AND (B.id=? OR B.slug=?) AND A.status='active'");

    $query->bindParam(1, $dropshipper->id);
    $query->bindParam(2, $args['slug']);
    $query->bindParam(3, $args['slug']);
    $query->execute();

    $results = [
        'status' => true,
        'data' => []
    ];

    if($query->rowCount() < 1) {
        $results['status'] = false;
        $results['data']['message'] = 'Produk tidak ditemukan';
    } else {
        $product = $query->fetch(PDO::FETCH_OBJ);
        $query = $this->get('db')->query("SELECT * FROM product_images WHERE product_id=$product->id");
        $product->images = $query->fetchAll(PDO::FETCH_OBJ);
        $results['data'] = $product;
    }

    $response->getBody()->write(json_encode($results));

    return $response->withHeader('Content-Type', 'application/json');
});

//Tambah stock berdasarkan produk id dan dropshipper id
$route->post('/product/dropshipper/{dropshipper_id}/{product_id}/stock', function (Request $request, Response $response, $args) {
    $input = $request->getParsedBody();
    $dropshipper = getUser($args['dropshipper_id']);

    if (!$dropshipper)
        return dropshipperNotFound($response);


    // cek apakah barang sudah pernah di stock
    $query = $this->get('db')->prepare("SELECT id FROM user_products WHERE product_id=? AND user_id=?");
    $query->bindParam(1, $args['product_id']);
    $query->bindParam(2, $dropshipper->id);
    $query->execute();

    if($query->rowCount() > 0) {
        $response->getBody()->write(json_encode([
            'status' => false,
            'data' => [
                'message' => 'Stok sudah pernah ditambahkan. Silahkan hapus stok lamanya terlebih dahulu di halaman daftar stok'
            ]
        ]));

        return $response->withHeader('Content-Type', 'application/json');
    }

    $query = $this->get('db')->prepare("INSERT INTO user_products (product_id, user_id, profit_price, qty, status) VALUES (?,?,?,?,?)");

    $query->bindParam(1, $args['product_id']);
    $query->bindParam(2, $dropshipper->id);
    $query->bindParam(3, $input['profit_price']);
    $query->bindParam(4, $input['qty']);
    $query->bindParam(5, $input['status']);
    $stock = $query->execute();

    $results = [
        'status' => $stock,
        'data' => []
    ];

    if ($stock) {
        $results['data']['message'] = 'Stock berhasil ditambahkan';
    } else {
        $results['data']['message'] = 'Produk tidak ditemukan';
    }

    $response->getBody()->write(json_encode($results));

    return $response->withHeader('Content-Type', 'application/json');
});

//Konfirmasi terima barang berdasarkan user_products id dan dropshipper id
$route->post('/product/dropshipper/{dropshipper_id}/{user_products_id}/confirm', function (Request $request, Response $response, $args) {
    $input = $request->getParsedBody();
    $dropshipper = getUser($args['dropshipper_id']);

    if (!$dropshipper)
        return dropshipperNotFound($response);

    $query = $this->get('db')->prepare("UPDATE user_products SET status='active' WHERE status='sending' AND id=? and user_id=?");

    $query->bindParam(1, $args['user_products_id']);
    $query->bindParam(2, $dropshipper->id);
    $stock = $query->execute();

    $results = [
        'status' => $stock,
        'data' => []
    ];

    if ($stock) {
        $results['data']['message'] = 'Barang berhasil diterima. Sekarang status stok barang kamu sudah aktif';
    } else {
        $results['data']['message'] = 'Gagal menerima barang';
    }

    $response->getBody()->write(json_encode($results));

    return $response->withHeader('Content-Type', 'application/json');
});

//Batalkan stock
$route->post('/product/dropshipper/{dropshipper_id}/{user_products_id}/cancel', function (Request $request, Response $response, $args) {
    $input = $request->getParsedBody();
    $dropshipper = getUser($args['dropshipper_id']);

    if (!$dropshipper)
        return dropshipperNotFound($response);

    $query = $this->get('db')->prepare("DELETE FROM user_products WHERE id=? and user_id=?");

    $query->bindParam(1, $args['user_products_id']);
    $query->bindParam(2, $dropshipper->id);
    $stock = $query->execute();

    $results = [
        'status' => $stock,
        'data' => []
    ];

    if ($stock) {
        $results['data']['message'] = 'Stok berhasil dihapus. Silahkan mengembalikan barang';
    } else {
        $results['data']['message'] = 'Gagal menghapus stok barang';
    }

    $response->getBody()->write(json_encode($results));

    return $response->withHeader('Content-Type', 'application/json');
});

function dropshipperNotFound(Response $response) {
    $response->getBody()->write(json_encode([
        'status' => 'false',
        'data' => [
            'message' => 'Dropshipper tidak ditemukan'
        ]
    ]));

    return $response->withHeader('Content-Type', 'application/json');
}   
