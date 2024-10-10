let ticking = false;
const progressBarElement = document.getElementById('progress');
const navContentElement = document.getElementById("nav-content");

window.addEventListener('scroll', function () {
    if (!ticking) {
        window.requestAnimationFrame(function () {
            const scrollHeight = document.documentElement.scrollHeight - document.documentElement.clientHeight;
            const scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
            const scrollPercentage = (scrollTop / scrollHeight) * 100;
            progressBarElement.style.width = scrollPercentage + '%';
            //background:linear-gradient(to right, #4dc0b5 var(--scroll), transparent 0);

            console.log(scrollHeight, scrollTop, scrollPercentage)
            ticking = false;
        });
        ticking = true;
    }

    if (scrollY > 10) {
        header.classList.add("bg-white");
        header.classList.add("shadow");
        navContentElement.classList.remove("bg-gray-100");
        navContentElement.classList.add("bg-white");
    } else {
        header.classList.remove("bg-white");
        header.classList.remove("shadow");
        navContentElement.classList.remove("bg-white");
        navContentElement.classList.add("bg-gray-100");
    }
});

//Javascript to toggle the menu
document.getElementById('nav-toggle').onclick = function () {
    document.getElementById("nav-content").classList.toggle("hidden");
}