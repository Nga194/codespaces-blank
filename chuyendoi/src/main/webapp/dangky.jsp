<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<style>

body
{

    margin:0px;
}

input
{

     width:300px;
     height: 30px;
     /* border:rgb(106, 201, 207) 1px solid; */
}

label
{
    display:block;
}
.form_group
{
    display: block;
}

.form_group.success input
{
    border:rgb(21, 192, 15) 1px solid;
     
}

.form_group.error input
{
     border:rgb(235, 20, 31) 1px solid;
     background-color: coral;

}


</style>







<body>
    <div id="container" style="width: 100%;  display: flex; justify-content: center;   ">

     

        <div style="width:1000px; height: 500px; background-color: beige;">

            <form id="register_form">

            

                    <div class="form_group">
                        <label for="username">Username</label>
                        <input id="username"/>
                    </div>


                     <div class="form_group">
                        <label for="email">Email</label>
                        <input id="email"/>
                    </div>


                     <div class="form_group">
                        <label for="password">Password</label>
                        <input id="password"/>
                    </div>


                     <div class="form_group">
                        <label for="confirmpassword">Confirm Password</label>
                        <input id="confirmpassword"/>
                    </div>


                    <div>
                        <button type="submit">Register</button>
                    </div>
                </form>

        </div>

    </div>

</body>



<script>

const form=document.getElementById("register_form");
const username=document.getElementById("username");
const email=document.getElementById("email");
const password=document.getElementById("password");
const confirmpassword=document.getElementById("confirmpassword");


form.addEventListener("submit", function(e){

    e.preventDefault();

    var ok=true;
    
    if (!checkRequired([username,email,password,confirmpassword]))
    {
        alert("Cần nhập đầy đủ");
        ok=false;
       
    }

    if (!checkEmail(email))
    {
        alert("Email sai định dạng");
         ok=false;
    }

    if (!checkPasswordMatch(password,confirmpassword))
    {
         alert("Password và confirmpassword không khớp");
          ok=false;
    }

    if (ok)
    {
        alert("Đăng ký thành công!");
    }
});


function checkRequired(arr)
{
    var is_valid=true;
    arr.forEach(phantu => {
       if (phantu.value.trim()==="")
       {
        showError(phantu,"Ô này cần phải nhập");
        is_valid=false;
       }

       else
       {
        showSuccess(phantu);
       }

       


        
    });

    return is_valid;

    return true;
    
}

function checkEmail(input) {


    const value = input.value.trim();
  

    const emailRegex =
        /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,}$/;

    if (emailRegex.test(value)) {

        showSuccess(input);
        return true;

    } else {

        showError(input, "Email is not valid");
        return false;

    }
}

function checkPasswordMatch(input1, input2)
{
    if (input1.value!=input2.value)
    {
        showError(input2,"Mật khẩu phải khớp");
        return false;
    }
    
    return true;
   

}


function showSuccess(input)
{

}

function showError(input)
{
    thediv=input.parentElement;
    thediv.className="form_group error";
    
}


</script>

</html>