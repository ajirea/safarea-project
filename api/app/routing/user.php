<?php
use Slim\Psr7\Request;
use Slim\Psr7\Response;

//Tampil semua user
$route->get('/user', function(Request $request, Response $response) {

    $query = $this->get('db')->prepare("SELECT * FROM users");
    $query->execute();

    $users = $query->fetchAll(PDO::FETCH_OBJ);

    $results = [
        'status' => true,
        'data' => $users
    ];

    $response->getBody()->write(json_encode($results));

    return $response
        ->withHeader('Content-Type', 'application/json');

});

//Tampil user berdasarkan username
$route->get('/user/{username}', function(Request $request, Response $response, $args) {

    $query = $this->get('db')->prepare("SELECT U.*, A.address, A.village, A.district, A.city, A.province, A.postal_code FROM users AS U LEFT JOIN addresses AS A ON A.user_id=U.id WHERE U.username=?");
    $query->bindParam(1, $args['username']);
    $query->execute();

    $user = $query->fetch(PDO::FETCH_OBJ);

    $result = [
        'status' => !$user ? false : true,
        'data' => []
    ];

    if($user)
        $result['data'] = $user;
    else
        $result['data']['message'] = 'User tidak ditemukan';

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
        
});

//Edit user
$route->post('/user/{username}', function(Request $request, Response $response, $args) {

    $input = $request->getParsedBody();
    $uploadedFiles = $request->getUploadedFiles();
    $query = "UPDATE users SET name=:name, username=:username, email=:email, phone=:phone";

    if (isset($uploadedFiles['avatar'])) {
        $query .= ", avatar=:avatar";
    }

    $query = $this->get('db')->prepare("$query WHERE username = :where_username");
    $query->bindValue(':name', $input['name']);
    $query->bindValue(':username', $input['username']);
    $query->bindValue(':email', $input['email']);
    $query->bindValue(':phone', $input['phone']);

    // tahap upload gambar avatar
    if (isset($uploadedFiles['avatar'])) {
        $avatar = $uploadedFiles['avatar'];
        if($avatar->getError() === UPLOAD_ERR_OK) {

            // fungsi uploadAvatar ada di dalam file app/helpers.php
            // file helpers.php di load pada file public/index.php
            $query->bindValue(':avatar', uploadAvatar($avatar, $input['username']));
        }
    }
    // selesai tahap upload gambar avatar

    $query->bindValue(':where_username', $args['username']);

    $user = $query->execute();

    // tahap update alamat buyers
    $query = $this->get('db')->prepare("UPDATE addresses SET address=?, village=?, district=?, city=?, province=?, postal_code=? WHERE buyer_id=?");
    $query->bindParam(1, $input['address']);
    $query->bindParam(2, $input['village']);
    $query->bindParam(3, $input['district']);
    $query->bindParam(4, $input['city']);
    $query->bindParam(5, $input['province']);
    $query->bindParam(6, $input['postal_code']);
    $query->bindParam(7, $args['id']);
    $address = $query->execute();
    // tahap end update alamat buyers 


    $result = [
        'status' => ($user && $address),
        'data' => []
    ];

    if($user)
        $result['data']['message'] = 'User ' . $args['username'] . ' berhasil diperbarui';
    else
        $result['data']['message'] = 'Username tidak ada';

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');    
});

//Tambah user
$route->post('/user', function(Request $request, Response $response) {
    $input = $request->getParsedBody();
    $uploadedFiles = $request->getUploadedFiles();
    $password = password_hash($input['password'], PASSWORD_DEFAULT);

    // tahap upload gambar avatar
    $avatar = '';
    if (isset($uploadedFiles['avatar'])) {
        $avatar = $uploadedFiles['avatar'];
        if ($avatar->getError() === UPLOAD_ERR_OK) {

            // fungsi uploadAvatar ada di dalam file app/helpers.php
            // file helpers.php di load pada file public/index.php
            $avatar = uploadAvatar($avatar, $input['username']);
        }
    }
    // selesai tahap upload gambar avatar

    // tahap menambahkan data user
    $query = $this->get('db')->prepare("INSERT INTO users (username, email, name, password, store_name, phone, avatar) VALUES (?,?,?,?,?,?,?)");
    $query->bindParam(1, $input['username']);
    $query->bindParam(2, $input['email']);
    $query->bindParam(3, $input['name']);
    $query->bindParam(4, $password);
    $query->bindParam(5, $input['store_name']);
    $query->bindParam(6, $input['phone']);
    $query->bindParam(7, $avatar);
    $user = $query->execute();
    // selesai tahap menambahkan user

    $result = [
        'status' => $user,
        'data' => []
    ];

    // tahap menambahkan data alamat dan API token jika user berhasil ditambahkan
    if($user) {
        $userId = $this->get('db')->lastInsertId();
        $query = $this->get('db')->prepare("INSERT INTO addresses (user_id, address, village, district, city, province, postal_code) VALUES (?,?,?,?,?,?,?)");
        $query->bindParam(1, $userId);
        $query->bindParam(2, $input['address']);
        $query->bindParam(3, $input['village']);
        $query->bindParam(4, $input['district']);
        $query->bindParam(5, $input['city']);
        $query->bindParam(6, $input['province']);
        $query->bindParam(7, $input['postal_code']);
        $query->execute();

        $token = $this->get('random_string');
        $query = $this->get('db')->prepare("INSERT INTO api_tokens (user_id, token) VALUES (?,?)");
        $query->bindParam(1, $userId);
        $query->bindParam(2, $token);
        $query->execute();

        $result['data']['message'] = 'Berhasil menambahkan user';
    } else {
        $result['data']['message'] = 'Gagal menambahkan user karena username atau email sudah digunakan';
    }

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
        
});

//Hapus user
$route->delete('/user/{username}', function(Request $request, Response $response, $args) {

    $query = $this->get('db')->prepare("DELETE FROM users WHERE username=?");
    $query->bindParam(1, $args['username']);

    $user = $query->execute();

    $result = [
        'status' => $user,
        'data' => [
            'message' => 'User ' . $args['username'] . ' berhasil dihapus'
        ]
    ];

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
        
});