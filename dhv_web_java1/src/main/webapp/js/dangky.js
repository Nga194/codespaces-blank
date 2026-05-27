const form=document.getElementById("registration-form");
const username=document.getElementById("username");
const email=document.getElementById("email");
const password=document.getElementById("password");
const confirmPassword=document.getElementById("confirmPassword");

form.addEventListener("submit",function(e){
    e.preventDefault();
    const isRequiredValid=
    checkRequired([username,email,password,confirmPassword]);
});

function checkRequired(inputArray){
    let isValid=true;

    inputArray.forEach((input)=>{
        if(input.value.trim()==="")
        {
            showError(input,'${formatFieldName(input)} is required');
            isValid=false;
        }
        else{
            showSuccess(input);
        }
    });
    return isValid;
}
function formatFieldName(input){
    return input.id.charAt(0).toUpperCase()+input.id.slice(1);
}
function showError(input,message){
    const formGroup=input.parentElement;
    formGroup.className="form-group errpr";
    const small=formGroup.querrySelector("small");
    small.innerText=message;
}
function showSuccess(input){
    const formGroup=input.parentElement;
    formGroup.className="form-group success";
}