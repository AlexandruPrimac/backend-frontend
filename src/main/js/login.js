import '../scss/login.scss'

// Password toggle
document.querySelector('.password-toggle').addEventListener('click', function(e) {
    e.preventDefault()
    const passwordInput = document.querySelector('#password')
    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password'
    passwordInput.setAttribute('type', type)
    this.classList.toggle('fa-eye')
    this.classList.toggle('fa-eye-slash')
}) // Form validation
;(() => {
    'use strict'
    const forms = document.querySelectorAll('.needs-validation')
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
            }
            form.classList.add('was-validated')
        }, false)
    })
})()
