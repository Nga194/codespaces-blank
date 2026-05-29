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
        const isPasswordValid=checkLength(password,6,25);
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
    const formGroup=input.parentElement;
    formGroup.className="form-group error";
    const small=formGroup.querySelector("small");
    small.innerText=message;
}
function showSuccess(input){
    const formGroup=input.parentElement;
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