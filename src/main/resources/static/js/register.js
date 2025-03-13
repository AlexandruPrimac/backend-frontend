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

document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('form');
    const messageContainer = document.createElement('div');
    messageContainer.id = 'message-container';
    form.parentNode.insertBefore(messageContainer, form.nextSibling);

    // Password toggle functionality
    document.querySelectorAll('.password-toggle').forEach(toggle => {
        toggle.addEventListener('click', () => {
            const passwordInput = toggle.previousElementSibling;
            const isPassword = passwordInput.type === 'password';
            passwordInput.type = isPassword ? 'text' : 'password';
            toggle.classList.toggle('fa-eye-slash');
            toggle.classList.toggle('fa-eye');
        });
    });

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Clear previous messages
        messageContainer.innerHTML = '';

        // Client-side validation
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        // Password match check
        const password = form.password.value;
        const confirmPassword = form.confirmPassword.value;
        if (password !== confirmPassword) {
            showMessage('Passwords do not match', 'danger');
            return;
        }

        // Prepare request data
        const userData = {
            firstName: form.firstName.value.trim(),
            lastName: form.lastName.value.trim(),
            email: form.email.value.trim(),
            password: password
        };

        try {
            const response = await fetch('/api/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });

            const data = await response.json();

            if (response.ok) {
                showMessage('Registration successful! Redirecting to login...', 'success');
                setTimeout(() => {
                    window.location.href = '/login';
                }, 2000);
            } else {
                showMessage(data.message || 'Registration failed', 'danger');
            }
        } catch (error) {
            showMessage('Network error. Please try again.', 'danger');
            console.error('Registration error:', error);
        }
    });

    function showMessage(text, type) {
        messageContainer.innerHTML = `
            <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                ${text}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        `;
    }
});