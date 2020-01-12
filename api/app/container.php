<?php

use Slim\Views\Twig;

// set view renderer to the container
$container->set('view', function($container) {
    $view = new Twig(__DIR__ . '/../templates', [
        // 'cache' => 'path/to/cache'
    ]);

    $view->addExtension(new \App\ViewTwigExtension($container));

    return $view;
});

$container->set('upload_path', function() {
    return __DIR__ . '/../public/uploads';
});

$container->set('random_string', function() {
	$length = 16;
	$string = '';
    while (($len = strlen($string)) < $length) {
        $size = $length - $len;
        $bytes = random_bytes($size);
        $string .= substr(str_replace(['/', '+', '='], '', base64_encode($bytes)), 0, $size);
    }
    return $string;
});

$container->set('routeParser', $app->getRouteCollector()->getRouteParser());