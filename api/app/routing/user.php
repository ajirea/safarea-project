<?php
use Slim\Psr7\Request;
use Slim\Psr7\Response;
use Slim\Routing\RouteCollectorProxy;

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

// ini teh jang edit
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
// end ini teh jang edit

// edit user
$route->post('/user/{username}', function(Request $request, Response $response, $args) {

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