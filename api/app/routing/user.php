<?php
use Slim\Psr7\Request;
use Slim\Psr7\Response;
use Slim\Routing\RouteCollectorProxy;

$route->get('/user', function(Request $request, Response $response) {

    $query = $this->get('db')->prepare("SELECT * FROM users");
    $query->execute();

    $users = $query->fetchAll();

    $response->getBody()->write(json_encode($users));

    return $response
        ->withHeader('Content-Type', 'application/json');

});

$route->get('/user/{username}', function(Request $request, Response $response, $args) {

    $query = $this->get('db')->prepare("SELECT * FROM users WHERE username=?");
    $query->bindParam(1, $args['username']);
    $query->execute();

    $user = $query->fetch();

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