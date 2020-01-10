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
	    $response->getBody()->write(json_encode([
            'version' => '1.0.0',
            'company' => 'Safarea',
            'team' => 'The Perjalanan Team',
        ]));
	    return $response->WithHeader('Content-Type', 'application/json');
	});

    require __DIR__ . '/routing/user.php';
    require __DIR__ . '/routing/auth.php';
    require __DIR__ . '/routing/product.php';
    require __DIR__ . '/routing/buyers.php';
    require __DIR__ . '/routing/order.php';
    require __DIR__ . '/routing/recent-order.php';
    
});