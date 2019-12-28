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

    $query = $this->get('db')->prepare("SELECT * FROM users WHERE username=?");
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

    $result = [
        'status' => $user,
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

    $query = $this->get('db')->prepare("INSERT INTO users (username, email, name, password, store_name, phone, avatar) VALUES (?,?,?,?,?,?,?)");
    $query->bindParam(1, $input['username']);
    $query->bindParam(2, $input['email']);
    $query->bindParam(3, $input['name']);
    $query->bindParam(4, $password);
    $query->bindParam(5, $input['store_name']);
    $query->bindParam(6, $input['phone']);
    $query->bindParam(7, $avatar);

    $user = $query->execute();

    $result = [
        'status' => $user,
        'data' => []
    ];

    if(!$user)
        $result['data']['message'] = 'Gagal menambahkan user karena username atau email sudah digunakan';
    else {

        $userId = $this->get('db')->lastInsertId();
        $token = $this->get('random_string');

        $query = $this->get('db')->prepare("INSERT INTO api_tokens (user_id, token) VALUES (?,?)");
        $query->bindParam(1, $userId);
        $query->bindParam(2, $token);
        $query->execute();

        $result['data']['message'] = 'Berhasil menambahkan user';

    }

    $response->getBody()->write(json_encode($result));

    return $response
        ->withHeader('Content-Type', 'application/json');
        
});

// //Hapus user
// $route->delete('/user/{username}', function(Request $request, Response $response, $args) {

//     $query = $this->get('db')->prepare("DELETE FROM users WHERE username=?");
//     $query->bindParam(1, $args['username']);

//     $user = $query->execute();

//     $result = [
//         'status' => $user,
//         'data' => [
//             'message' => 'User ' . $args['username'] . ' berhasil dihapus'
//         ]
//     ];

//     $response->getBody()->write(json_encode($result));

//     return $response
//         ->withHeader('Content-Type', 'application/json');
        
// });