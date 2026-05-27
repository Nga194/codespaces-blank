<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Form Validator</title>
    <link rel="stylesheet" href="css/dangky.css">
    
</head>

<script>
    function kiemtra()
    {
        alert('22222222222222');

    }

</script>

<body>
    <div class="container">
    <form id="register_form" onsubmit="kiemtra();">
            <h1>Register</h1>
            <div class="form-group">
                <label for="Username" >Username</label>
                <input type="text" placeholder="Enter Username" />
                <small></small>
            </div>
            <div class="form-group">
                <label for="email" >Email</label>
                <input type="email" id="email" placeholder="Enter Email" />
                <small></small>
            </div>

            <div class="form-group">
                <label for="password" >Password</label>
                <input type="password" id="password" placeholder="Enter Password" />
                <small></small>
            </div>

            <div class="form-group">
                <label for="password" >Confirm Password</label>
                <input type="ConfirmPassword" id="ConfirmPassword" placeholder="Confirm Password" />
                <small></small>
            </div>

            <button type="submit">Register</button>
        </form>
        </div>
        <script src="index.js"></script>
</body>
</html>
