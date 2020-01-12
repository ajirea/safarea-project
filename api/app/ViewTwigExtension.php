<?php

namespace App;

use Twig\TwigFunction;
use Twig\Extension\GlobalsInterface;
use Twig\Extension\AbstractExtension;

class ViewTwigExtension extends AbstractExtension implements GlobalsInterface
{
	private $container;
    public function __construct($container)
    {
        $this->container = $container;
    }

    public function getGlobals(): array
    {
        return [
        ];
    }

    public function getFunctions()
    {
        return [
            new TwigFunction('route', 'route'),
            new TwigFunction('url', 'url'),
            new TwigFunction('price_format', 'priceFormat')
        ];
    }
}