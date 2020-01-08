<?php

use Gregwar\Image\Image;

function uploadAvatar($avatar, $username) {
    global $container;

    // mengambil temporary name file yang di upload dari sistem I/O php
    $tmpName = $avatar->getStream()->getMetaData('uri');
    $fileName = $container->get('random_string'); 

    // mendapatkan informasi ukuran dimensi gambar
    $imagesize = getimagesize($tmpName);

    // cek apakah file gambar yg diupload adalah valid
    // karena $imagesize akan menghasilkan nilai false
    // jika gambar yg diupload tidak valid
    if ($imagesize) {

        // filter username dari entitas html
        $username = htmlentities($username);

        // atur lokasi penyimpanan gambar
        // $this->get('upload_path') sudah di atur di dalam container di file app/container.php
        $uploadLocation = $container->get('upload_path') . '/avatar/' . $fileName . '.jpg';

        // cek apakah sudah ada file yang sama
        // jika ya, maka hapus terlebih dahulu
        if (file_exists($uploadLocation))
            unlink($uploadLocation);

        // atur ukuran gambar menggunakan depedensi \Gregwar\Image\Image
        // atur ukuran gambar menjadi 180 x 180 px
        // save dengan format jpg dan kualitas gambar 85%
        // dokumentasi Gregwar\Image\Image: https://github.com/Gregwar/Image
        Image::open($tmpName)
            ->zoomCrop(180, 180, 0xffffff)
            ->save($uploadLocation, 'jpg', 85);

    }

    return '/uploads/avatar/' . $fileName . '.jpg';
}

/**
 * Fungsi untuk upload gambar produk
 *
 * @param string  $file example: http://site.com/image.jpg
 * @param string  $type ["thumbnail", "image"]
 *
 * @return string
 */
function uploadProductImage($file, $type = 'thumbnail')
{
    global $container;

    $width = 200;
    $height = 210;

    if($type == 'image') {
        $width = 700;
        $height = 583;
    }

    $filename = pathinfo($file, PATHINFO_FILENAME);
    $uploadDir = '/uploads/products/'. $type .'-' . $filename . '.jpg';
    $tempDir = 'uploads/temp/' . basename($file);

    file_put_contents($tempDir, file_get_contents($file));

    // atur lokasi penyimpanan gambar
    // $this->get('upload_path') sudah di atur di dalam container di file app/container.php
    $uploadLocation = $container->get('upload_path') . '/products/'.$type.'-' . $filename . '.jpg';
    // cek apakah sudah ada file yang sama
    // jika ya, maka hapus terlebih dahulu
    if (file_exists($uploadLocation))
        unlink($uploadLocation);

    // atur ukuran gambar menggunakan depedensi \Gregwar\Image\Image
    // atur ukuran gambar menjadi 180 x 180 px
    // save dengan format jpg dan kualitas gambar 85%
    // dokumentasi Gregwar\Image\Image: https://github.com/Gregwar/Image
    Image::open($tempDir)
        ->zoomCrop($width, $height, 0xffffff)
        ->save($uploadLocation, 'jpg', 85);

    unlink($tempDir);

    return $uploadDir;
}

/**
 * Fungsi untuk mendapatkan data user berdasarkan id atau username
 *
 * @param string $token (Bisa berupa id atau username atau api_token)
 * @return mixed
 */
function getUser($token) {
    global $container;
    $db = $container->get('db');
    $query = $db->prepare("SELECT A.*, B.token FROM users AS A INNER JOIN api_tokens AS B ON B.user_id=A.id WHERE A.username=? OR B.token=? OR A.id=?");
    $query->bindParam(1, $token);
    $query->bindParam(2, $token);
    $query->bindParam(3, $token);
    $query->execute();

    if($query->rowCount() < 1) return false;

    return $query->fetch(PDO::FETCH_OBJ);
}

/**
 * Fungsi untuk mendapatkan status pada barang yang di stok
 *
 * @param string $status [take, sent, active, non-active, etc]
 *
 * @return string
 */
function getStockStatus($status)
{
    $result = '';
    switch ($status) {
        case 'take':
            $result = 'Menunggu diambil';
            break;

        case 'sending':
            $result = 'Sedang dikirim';
            break;

        case 'active':
            $result = 'Stok atif';
            break;

        case 'non-active':
            $result = 'Stok tidak aktif';
            break;

        case 'returning':
            $result = 'Menunggu dikembalikan';
            
        default:
            $result = '';
            break;
    }

    return $result;
}