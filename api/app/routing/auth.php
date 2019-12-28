<?php
use Slim\Psr7\Request;
use Slim\Psr7\Response;

//Login User
$route->post('/auth/login', function(Request $request, Response $response) {
    
    $input = $request->getParsedBody();

    $result = [
        'status' => true,
        'data' => []
    ];

    if(empty($input['username']) || empty($input['password'])) {
        $result['status'] = false;
        $result['data']['message'] = 'Username atau password harus diisi';

        $response->getBody()->write(json_encode($result));

        return $response->withHeader('Content-Type', 'application/json');
    }

    $query = $this->get('db')->prepare("SELECT A.*, B.token, C.address, C.village, C.district, C.city, C.province, C.postal_code FROM users AS A LEFT JOIN api_tokens AS B ON B.user_id=A.id LEFT JOIN addresses AS C ON C.user_id=A.id WHERE A.username=?");
    $query->bindParam(1, $input['username']);
    $query->execute();

    if($query->rowCount() < 1) {
        $result['status'] = false;
        $result['data']['message'] = 'Pengguna tidak ditemukan';

        $response->getBody()->write(json_encode($result));

        return $response->withHeader('Content-Type', 'application/json');
    }

    $user = $query->fetch(PDO::FETCH_OBJ);

    if(password_verify($input['password'], $user->password)) {
        $result['status'] = true;
        $result['data'] = $user;
    } else {
        $result['status'] = false;
        $result['data']['message'] = 'Password yang anda masukkan salah';
    }

    $response->getBody()->write(json_encode($result));

    return $response->withHeader('Content-Type', 'application/json');
});