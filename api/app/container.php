<?php

use Slim\Views\Twig;

// set view renderer to the container
$container->set('view', function($container) {
    $view = new Twig(__DIR__ . '/../templates', [
        // 'cache' => 'path/to/cache'
    ]);

    return $view;
});