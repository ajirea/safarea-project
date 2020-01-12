<?php
use Slim\Psr7\Request;
use Slim\Psr7\Response;
use Slim\Routing\RouteCollectorProxy;

$app->get('/', function (Request $request, Response $response, $args) {
    $response->getBody()->write("Hello World!");
    return $response;
});

$app->get('/store/{username}', function(Request $request, Response $response, $args) {
    $dropshipper = getUser($args['username']);

    if(!$dropshipper)
        return dropshipperNotFound($response);

    $query = $this->get('db')->prepare("SELECT A.*, B.name, B.slug, B.thumbnail, B.price, C.username, (A.profit_price+B.price) AS profited_price FROM user_products AS A INNER JOIN products AS B ON B.id=A.product_id INNER JOIN users AS C ON C.id = A.user_id WHERE A.user_id=? AND A.status='active'");

    $query->bindParam(1, $dropshipper->id);
    $query->execute();

    $products = $query->fetchAll(PDO::FETCH_OBJ);

    return $this->get('view')->render($response, 'catalog.twig', [
        'products' => $products,
        'dropshipper' => $dropshipper
    ]);
})->setName('catalog');

$app->get('/store/{username}/{slug}', function(Request $request, Response $response, $args) {
    $dropshipper = getUser($args['username']);

    if(!$dropshipper)
        return dropshipperNotFound($response);

    $query = $this->get('db')->prepare("SELECT A.*, B.name, B.slug, B.thumbnail, B.price, B.description, C.username, B.description, (A.profit_price+B.price) AS profited_price FROM user_products AS A INNER JOIN products AS B ON B.id=A.product_id INNER JOIN users AS C ON C.id = A.user_id WHERE A.user_id=? AND A.status='active' AND B.slug=?");

    $query->bindParam(1, $dropshipper->id);
    $query->bindParam(2, $args['slug']);
    $query->execute();

    if($query->rowCount() < 1)
        return dropshipperNotFound($response);

    $product = $query->fetch(PDO::FETCH_OBJ);

    $query = $this->get('db')->query("SELECT * FROM product_images WHERE product_id=$product->product_id");
    $images = $query->fetchAll(PDO::FETCH_OBJ);
    $product->images = $images;

    return $this->get('view')->render($response, 'catalog-detail.twig', [
        'product' => $product,
        'dropshipper' => $dropshipper
    ]);
})->setName('catalog-detail');

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
    
});