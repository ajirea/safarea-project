<?php
use DI\Container;
use Slim\Factory\AppFactory;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

require __DIR__ . '/../vendor/autoload.php';

// instantiate container from PHP-DI
$container = new Container();

// set app container to use PHP-DI container
AppFactory::setContainer($container);

// create app
$app = AppFactory::create();

// set base path of app
$app->setBasePath("/safarea-api");

// Add Routing Middleware
$app->addRoutingMiddleware();

// Add Error Middleware
$errorMiddleware = $app->addErrorMiddleware(true, true, true);

// Set container injection
require __DIR__ . '/../app/container.php';

// load database connection and routes
require __DIR__ . '/../app/db.php';
require __DIR__ . '/../app/routes.php';

// run the app
$app->run();