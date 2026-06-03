document.addEventListener("DOMContentLoaded", function() {

const form=document.getElementById("registration-form");
const username=document.getElementById("username");
const email=document.getElementById("email");
const password=document.getElementById("password");
const confirmPassword=document.getElementById("confirmPassword");


form.addEventListener("submit",function(e){
    e.preventDefault();
    const isRequiredValid=
    checkRequired([username,email,password,confirmPassword]);

    let isFormatValid=false;
    if(isRequiredValid){
        const isUsernameValid=checkLength(username,3,15);
        const isEmailValid=checkEmail(email);
        const isPasswordValid=checkPassword(password);
        const isPasswordMatch=checkPasswordMatch(password,confirmPassword);

        isFormatValid=isUsernameValid && isEmailValid && isPasswordValid && isPasswordMatch;
    }
    if (isFormatValid){
        alert("Dang Ki Thanh cong ");
        form.reset();
        document.querySelectorAll(".form-group").forEach((group)=>{
            group.className="form-group";
        });
    }
});
});

function checkRequired(inputArray){
    let isValid=true;

    inputArray.forEach((input)=>{
        if(input.value.trim()===""){
            showError(input,`${formatFieldName(input)} is required`);
            isValid=false;
        }
        else{
            showSuccess(input);
        }
    });
    return isValid;
}
// chuyển từ đầu tành viết hoa
function formatFieldName(input){
    return input.id.charAt(0).toUpperCase()+input.id.slice(1);
}
function showError(input,message){
    const formGroup=input.closest(".form-group");
    formGroup.className="form-group error";
    const small=formGroup.querySelector("small");
    small.innerText=message;
}
function showSuccess(input){
    const formGroup=input.closest(".form-group");
    formGroup.className="form-group success";
}
function checkLength(input,min,max){
    if(input.value.length<min){
        showError(input,`${formatFieldName(input)} phải có ít nhất ${min} ký tự`);
        return false;
    }
    else if(input.value.length>max){
        showError(input,`${formatFieldName(input)} phải ít hơn ${max} ký tự`);
        return false;
    }
    else{
        showSuccess(input);
        return true;
    }
}
// kiểm email
function checkEmail(input){
    const emailRegex=/^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if(emailRegex.test(input.value.trim())){
        showSuccess(input);
        return true;
    }
    else{
        showError(input,"sai dinh dang email nhe!");
        return false;
    }
}
// xem 2 pw có giống nhau không
function checkPasswordMatch(input1,input2){
    if(input1.value!==input2.value){
        showError(input2,"Password không khớp");
        return false;
    }
    else{
    showSuccess(input2);
    return true;
}
}

function checkPassword(input){
    const value = input.value;
    
    if(value.length < 8){
        showError(input, "Password phải có ít nhất 8 ký tự");
        return false;
    }
    if(!/[A-Z]/.test(value)){
        showError(input, "Password phải có ít nhất 1 chữ hoa");
        return false;
    }
    if(!/[a-z]/.test(value)){
        showError(input, "Password phải có ít nhất 1 chữ thường");
        return false;
    }
    if(!/[!@#$%^&*(),.?":{}|<>]/.test(value)){
        showError(input, "Password phải có ít nhất 1 ký tự đặc biệt (!@#$%...)");
        return false;
    }
    
    showSuccess(input);
    return true;
}

function togglePw(inputId, iconId) {
  const input = document.getElementById(inputId);
  const icon = document.getElementById(iconId);

  input.type = 'text';
  icon.innerHTML = `
    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94"/>
    <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"/>
    <line x1="1" y1="1" x2="23" y2="23"/>
  `;

  setTimeout(function() {
    input.type = 'password';
    icon.innerHTML = `
      <path d="M2 12s3.5-7 10-7 10 7 10 7-3.5 7-10 7S2 12 2 12z"/>
      <circle cx="12" cy="12" r="3"/>
    `;
  }, 1500); // hiện 1.5 giây rồi tự ẩn lại
}