// Password confirmation validation
const password = document.getElementById('password');
const confirmPassword = document.getElementById('confirmPassword');

function validatePassword() {
    if(password.value !== confirmPassword.value) {
        confirmPassword.setCustomValidity("Passwords don't match");
    } else {
        confirmPassword.setCustomValidity('');
    }
}

password.onchange = validatePassword;
confirmPassword.onkeyup = validatePassword;

// Password strength indicator
password.addEventListener('input', function() {
    const strength = Math.min(Math.floor(this.value.length / 4), 2);
    document.getElementById('password-strength').className = `strength-${strength}`;
});