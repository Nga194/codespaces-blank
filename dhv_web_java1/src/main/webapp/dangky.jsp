<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Form Validator</title>
    <link rel="stylesheet" href="css/dangky.css">
    <script src="dangky.js"></script>
</head>

<script>
    function kiemtra()
    {
        alert('22222222222222');

    }

</script>

<body>
    <div class="container">
    <form id="registration-form" >
            <h1>Register</h1>
            <div class="form-group">
                <label for="Username" >Username</label>
                <input type="text" id="username" placeholder="Enter Username" />
                <small></small>
            </div>
            <div class="form-group">
                <label for="email" >Email</label>
                <input type="email" id="email" placeholder="Enter Email" />
                <small></small>
            </div>

            <div class="form-group">
  <label for="password">Password</label>
  <div class="input-wrap">
    <input type="password" id="password" placeholder="Enter Password" />
    <span class="ico" onclick="togglePw('password', 'eye-pw')">
      <svg id="eye-pw" width="20" height="20" viewBox="0 0 24 24"
        fill="none" stroke="currentColor"
        stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
        <path d="M2 12s3.5-7 10-7 10 7 10 7-3.5 7-10 7S2 12 2 12z"/>
        <circle cx="12" cy="12" r="3"/>
      </svg>
    </span>
  </div>
  <small></small>
</div>

<div class="form-group">
  <label for="confirmPassword">Confirm Password</label>
  <div class="input-wrap">
    <input type="password" id="confirmPassword" placeholder="Confirm Password" />
    <span class="ico" onclick="togglePw('confirmPassword', 'eye-cpw')">
      <svg id="eye-cpw" width="20" height="20" viewBox="0 0 24 24"
        fill="none" stroke="currentColor"
        stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
        <path d="M2 12s3.5-7 10-7 10 7 10 7-3.5 7-10 7S2 12 2 12z"/>
        <circle cx="12" cy="12" r="3"/>
      </svg>
    </span>
  </div>
  <small></small>
</div>

            <button type="submit">Register</button>
        </form>
        </div>
        
</body>
</html>
