<?php

use Slim\Psr7\Request;
use Slim\Psr7\Response;

//Tampil semua buyers
$route->get('/buyers', function (Request $request, Response $response) {

    $query = $this->get('db')->prepare("SELECT * FROM buyers WHERE deleted_at is null");
    $query->execute();

    $buyers = $query->fetchAll(PDO::FETCH_OBJ);

    $results = [
        'status' => true,
        'data' => $buyers
    ];

    $response->getBody()->write(json_encode($results));

    return $response
        ->withHeader('Content-Type', 'application/json');

});

//Tampil buyers berdasarkan ID
$route->get('/buyers/{id}', function(Request $request, Response $response, $args) {

    $query = $this->get('db')->prepare("SELECT * FROM buyers WHERE id=? AND deleted_at is null");
    $query->bindParam(1, $args['id']);
    $query->execute();

    $buyer = $query->fetch(PDO::FETCH_OBJ);

    $result = [
        'status' => !$buyer ? false : true,
        'data' => []
    ];

    if($buyer)
        $result['data'] = $buyer;
    else
        $result['data']['message'] = 'Pembeli tidak ditemukan';

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
        
});

//Add buyers , harus ada ID dropshippernya
$route->post('/buyers', function(Request $request, Response $response) {
    $input = $request->getParsedBody();

    $query = $this->get('db')->prepare("INSERT INTO buyers (user_id, name, phone) VALUES (?,?,?)");
    $query->bindParam(1, $input['user_id']);
    $query->bindParam(2, $input['name']);
    $query->bindParam(3, $input['phone']);
    $query->execute();

    $buyerId = $this->get('db')->lastInsertId();

    $query = $this->get('db')->prepare("INSERT INTO addresses (buyer_id, address, village, district, city, province, postal_code) VALUES (?,?,?,?,?,?,?)");
    $query->bindParam(1, $buyerId);
    $query->bindParam(2, $input['address']);
    $query->bindParam(3, $input['village']);
    $query->bindParam(4, $input['district']);
    $query->bindParam(5, $input['city']);
    $query->bindParam(6, $input['province']);
    $query->bindParam(7, $input['postal_code']);

    $buyer = $query->execute();

    $result = [
        'status' => $buyer,
        'data' => []
    ];

    if(!$buyer)
        $result['data']['message'] = 'Gagal menambahkan pembeli karena ID dropshipper tidak ada';
    else 
        $result['data']['message'] = 'Berhasil menambahkan pembeli';

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
        
});

//Edit buyers berdasarkan ID buyer
$route->post('/buyers/{id}', function(Request $request, Response $response, $args) {

   $input = $request->getParsedBody();

   $query = $this->get('db')->prepare("SELECT id FROM buyers WHERE id=? AND deleted_at is null");
   $query->bindParam(1, $args['id']);
   $query->execute();

   if($query->rowCount() < 1) {
        $result['status'] = false;
        $result['data']['message'] = 'Pembeli tidak ditemukan';

        $response->getBody()->write(json_encode($result));

        return $response->withHeader('Content-Type', 'application/json');
    }

   $query = "UPDATE buyers SET name=:name, phone=:phone";
   $query = $this->get('db')->prepare("$query WHERE id = :id");
    $query->bindValue(':name', $input['name']);
    $query->bindValue(':phone', $input['phone']);
    $query->bindValue(':id', $args['id']);

    $buyer = $query->execute();

    $result = [
        'status' => $buyer,
        'data' => []
    ];

    if($buyer)
        $result['data']['message'] = 'Pembeli dengan ID :  ' . $args['id'] . ', berhasil diperbarui';
    else
        $result['data']['message'] = 'ID pembeli tidak ada';

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
});

//'Delete' buyer
$route->post('/buyers/delete/{id}', function(Request $request, Response $response, $args) {

    $date = date('Y-m-d H:i:s');
    $query = "UPDATE buyers SET deleted_at=? WHERE id=?";
    $query = $this->get('db')->prepare($query);
    $query->bindValue(1, $date);
    $query->bindValue(2, $args['id']);
    $buyer = $query->execute();

    $result = [
        'status' => $buyer,
        'data' => [
            'message' => 'Pembeli dengan ID :  ' . $args['id'] . ', berhasil dihapus'
        ]
    ];

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
});