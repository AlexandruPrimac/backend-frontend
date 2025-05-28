import '../scss/navbar.scss'

window.addEventListener('scroll', function () {
    let navbar = document.querySelector('.navbar');
    navbar.classList.toggle('scrolled', window.scrollY > 50);
});