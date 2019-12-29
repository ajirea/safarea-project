<?php

use Slim\Psr7\Request;
use Slim\Psr7\Response;

//Tampil semua buyers berdasarkan dropshipper
$route->get('/buyers/{dropshipper_id}', function (Request $request, Response $response, $args) {

    $query = $this->get('db')->prepare("SELECT * FROM buyers WHERE user_id=? AND deleted_at is null");
    $query->bindParam(1, $args['dropshipper_id']);
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

//Tampil buyers berdasarkan ID dan id dropshipper
$route->get('/buyers/{id}/{dropshipper_id}', function(Request $request, Response $response, $args) {
    $query = $this->get('db')->prepare("SELECT B.*, A.address, A.village, A.district, A.city, A.province, A.postal_code FROM buyers AS B LEFT JOIN addresses AS A ON A.buyer_id=B.id WHERE B.id=? AND B.user_id=? AND B.deleted_at is null");
    $query->bindParam(1, $args['id']);
    $query->bindParam(2, $args['dropshipper_id']);
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

//Edit buyers berdasarkan ID buyer dan ID dropshippernya
$route->post('/buyers/{id}/{dropshipper_id}', function(Request $request, Response $response, $args) {

   $input = $request->getParsedBody();

    // tahap cek keberadaan buyer
    $query = $this->get('db')->prepare("SELECT id FROM buyers WHERE id=? AND user_id=? AND deleted_at is null");
    $query->bindParam(1, $args['id']);
    $query->bindParam(2, $args['dropshipper_id']);
    $query->execute();

   if($query->rowCount() < 1) {
        $result['status'] = false;
        $result['data']['message'] = 'Pembeli tidak ditemukan';

        $response->getBody()->write(json_encode($result));

        return $response->withHeader('Content-Type', 'application/json');
    }
    // tahap end cek keberadaan buyer

    // tahap update data buyers
    $query = $this->get('db')->prepare("UPDATE buyers SET name=:name, phone=:phone WHERE id=:id");
    $query->bindValue(':name', $input['name']);
    $query->bindValue(':phone', $input['phone']);
    $query->bindValue(':id', $args['id']);

    $buyer = $query->execute();
    // tahap end update data buyers

    // tahap update alamat buyers
    $query = $this->get('db')->prepare("UPDATE addresses SET address=?, village=?, district=?, city=?, province=?, postal_code=? WHERE buyer_id=?");
    $query->bindParam(1, $input['address']);
    $query->bindParam(2, $input['village']);
    $query->bindParam(3, $input['district']);
    $query->bindParam(4, $input['city']);
    $query->bindParam(5, $input['province']);
    $query->bindParam(6, $input['postal_code']);
    $query->bindParam(7, $args['id']);
    $address = $query->execute();
    // tahap end update alamat buyers

    $result = [
        'status' => ($buyer && $address),
        'data' => []
    ];

    if($buyer && $address)
        $result['data']['message'] = 'Pembeli dengan ID :  ' . $args['id'] . ', berhasil diperbarui';
    else
        $result['data']['message'] = 'Gagal memperbaharui data pembeli';

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
});

//'Delete' buyer berdasarkan ID dan ID dropshippernya
$route->delete('/buyers/{id}/{dropshipper_id}', function(Request $request, Response $response, $args) {

    $date = date('Y-m-d H:i:s');
    $query = "UPDATE buyers SET deleted_at=? WHERE id=? AND user_id=?";
    $query = $this->get('db')->prepare($query);
    $query->bindValue(1, $date);
    $query->bindValue(2, $args['id']);
    $query->bindValue(3, $args['dropshipper_id']);
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