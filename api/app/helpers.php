<?php

function uploadAvatar($avatar, $username) {
    global $container;

    // mengambil temporary name file yang di upload dari sistem I/O php
    $tmpName = $avatar->getStream()->getMetaData('uri');

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
        $uploadLocation = $container->get('upload_path') . '/avatar/' . $username . '.jpg';

        // cek apakah sudah ada file yang sama
        // jika ya, maka hapus terlebih dahulu
        if (file_exists($uploadLocation))
            unlink($uploadLocation);

        // atur ukuran gambar menggunakan depedensi \Gregwar\Image\Image
        // atur ukuran gambar menjadi 180 x 180 px
        // save dengan format jpg dan kualitas gambar 85%
        // dokumentasi Gregwar\Image\Image: https://github.com/Gregwar/Image
        Gregwar\Image\Image::open($tmpName)
            ->zoomCrop(180, 180, 0xffffff)
            ->save($uploadLocation, 'jpg', 85);

    }

    return '/uploads/avatar/' . $username . '.jpg';
}

/**
 * Fungsi untuk mendapatkan data user berdasarkan id atau username
 *
 * @param string $id (Bisa berupa username atau api_token)
 * @return mixed
 */
function getUser($token) {
    global $container;
    $db = $container->get('db');
    $query = $db->prepare("SELECT A.*, B.token FROM user AS A INNER JOIN api_tokens AS B ON B.user_id=A.id WHERE A.username=? OR B.token=?");
    $query->bindParam(1, $token);
    $query->bindParam(2, $token);
    $query->execute();

    if($query->rowCount() < 1) return false;

    return $query->fetch(PDO::FETCH_OBJ);
}