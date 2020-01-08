<?php
use DI\Container;
use Slim\Factory\AppFactory;
use Slim\Psr7\Request;
use Slim\Psr7\Response;
use Psr\Http\Server\RequestHandlerInterface as RequestHandler;

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

$app->addBodyParsingMiddleware();

// Add Error Middleware
$errorMiddleware = $app->addErrorMiddleware(true, true, true);

// Set container injection
require __DIR__ . '/../app/container.php';

// load helpers
require __DIR__ . '/../app/helpers.php';

// App Middleware for API
$app->add(function(Request $request, RequestHandler $handler) {
    $params = $request->getQueryParams();
    $token = null;

    // ambil api_token dari beberapa kondisi
    // kondisi satu api_token diambil dari query parameter seperti http://localhost?api_token=blabla
    if(isset($params['api_token']))
        $token = $params['token'];
    // kondisi dua api_token diambil dari header
    elseif($request->hasHeader('api_token')) {
        $token = $request->getHeaderLine('api_token');
    }

    $user = getUser($token);

    // masukkan $user ke atribut $request
    $request = $request->withAttribute('user', $user);

    // kembalikan $request
    return $handler->handle($request);
});

// load database connection and routes
require __DIR__ . '/../app/db.php';
require __DIR__ . '/../app/routes.php';

// run the app
$app->run();
