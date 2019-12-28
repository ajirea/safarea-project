<?php
use Slim\Psr7\Request;
use Slim\Psr7\Response;
use Slim\Routing\RouteCollectorProxy;

$app->get('/', function (Request $request, Response $response, $args) {
    $response->getBody()->write("Hello world!");
    return $response;
});

$app->group('/api', function(RouteCollectorProxy $route) {

	$route->get('', function (Request $request, Response $response, $args) {
	    $response->getBody()->write("API");
	    return $response;
	});

    require __DIR__ . '/routing/user.php';
    require __DIR__ . '/routing/auth.php';
    require __DIR__ . '/routing/product.php';
    require __DIR__ . '/routing/buyers.php';
    
});