<?php
use Slim\Psr7\Request;
use Slim\Psr7\Response;
use Slim\Routing\RouteCollectorProxy;

$app->get('/', function (Request $request, Response $response, $args) {
    $response->getBody()->write("Hello world!");
    return $response;
});

$app->group('/api', function(RouteCollectorProxy $route) {
    $route->get('/user', function(Request $request, Response $response) {
        return $this->get('view')->render($response, 'layouts/app.twig');
    });
});