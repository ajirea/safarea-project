<?php
use Slim\Psr7\Request;
use Slim\Psr7\Response;
use Slim\Routing\RouteCollectorProxy;

//Tampil semua user
$route->get('/user', function(Request $request, Response $response) {

    $query = $this->get('db')->prepare("SELECT * FROM users");
    $query->execute();

    $users = $query->fetchAll(PDO::FETCH_OBJ);

    $results = [
        'status' => true,
        'data' => $users
    ];

    $response->getBody()->write(json_encode($results));

    return $response
        ->withHeader('Content-Type', 'application/json');

});

//Tampil user berdasarkan username
$route->get('/user/{username}', function(Request $request, Response $response, $args) {

    $query = $this->get('db')->prepare("SELECT * FROM users WHERE username=?");
    $query->bindParam(1, $args['username']);
    $query->execute();

    $user = $query->fetch(PDO::FETCH_OBJ);

    $result = [
        'status' => !$user ? false : true,
        'data' => []
    ];

    if($user)
        $result['data'] = $user;
    else
        $result['data']['message'] = 'User tidak ditemukan';

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
        
});

//Edit user

$route->put('/user/{username}', function(Request $request, Response $response, $args) {

    $query = "UPDATE users SET username=:username, email=:email, password=:password, phone=:phone, avatar=:avatar";
    $input = $request->getParsedBody();

    if (!empty(trim($input['password']))) {
        $query .= ", password=:password";
    }

    if (!empty(trim($input['avatar']))) {
        $query .= ", avatar=:avatar";
    }

    $query = $this->get('db')->prepare($query . "WHERE username = :where_username");
    $query->bindParam('username', $input['username']);
    $query-> bindParam('email', $input['email']);
    
    if (!empty(trim($input['password']))) {
        $query = bindParam('password', password_hash($input['password'], PASSWORD_DEFAULT));
    }

    if (!empty(trim($input['avatar']))) {
        $query = bindParam('avatar',($input['avatar']));
    }

    $query->bindParam('where_username', $input['username']);
    $query->bindParam(':avatar',$input['password']);
    $query->bindParam(':password',$input['password']);
    $query->bindParam('email',$input['email']);

    // $query = $this->get('db')->prepare("UPDATE users SET username=?, email=?, name=?, password=?, store_name=?,
    // phone=?, avatar=? WHERE username = ?");

    // $query->bindParam(1, $input['username']);
    // $query->bindParam(2, $input['email']);
    // $query->bindParam(3, $input['name']);
    // $query->bindParam(4, $password);
    // $query->bindParam(5, $input['store_name']);
    // $query->bindParam(6, $input['phone']);
    // $query->bindParam(7, $input['avatar']);
    // $query->bindParam(8, $args['username']);

    $user = $query->execute();

    $result = [
        'status' => $user,
        'data' => []
    ];

    if($user)
        $result['data']['message'] = 'User ' . $args['username'] . ' berhasil diperbarui';
    else
        $result['data']['message'] = 'Username tidak ada';

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
        
});

//Tambah user
$route->post('/user', function(Request $request, Response $response) {
    $input = $request->getParsedBody();
    $password = password_hash($input['password'], PASSWORD_DEFAULT);

    $query = $this->get('db')->prepare("INSERT INTO users (username, email, name, password, store_name, phone, avatar) VALUES (?,?,?,?,?,?,?)");
    $query->bindParam(1, $input['username']);
    $query->bindParam(2, $input['email']);
    $query->bindParam(3, $input['name']);
    $query->bindParam(4, $password);
    $query->bindParam(5, $input['store_name']);
    $query->bindParam(6, $input['phone']);
    $query->bindParam(7, $input['avatar']);

    $user = $query->execute();

    $result = [
        'status' => $user,
        'data' => []
    ];

    if($user)
        $result['data']['message'] = 'User ' . $input['username'] . ' berhasil ditambahkan';
    else
        $result['data']['message'] = 'Gagal menambahkan user karena username atau email sudah digunakan';

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
        
});

//Hapus user
$route->delete('/user/{username}', function(Request $request, Response $response, $args) {

    $query = $this->get('db')->prepare("DELETE FROM users WHERE username=?");
    $query->bindParam(1, $args['username']);

    $user = $query->execute();

    $result = [
        'status' => $user,
        'data' => [
            'message' => 'User ' . $args['username'] . ' berhasil dihapus'
        ]
    ];

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
        
});