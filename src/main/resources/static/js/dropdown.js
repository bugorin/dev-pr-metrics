(function() {
    const menu = document.getElementById('userMenu');
    const button = document.getElementById('userMenuButton');
    const dropdown = document.getElementById('userMenuDropdown');
    if (!menu || !button || !dropdown) return;

    const close = () => menu.classList.remove('open');

    button.addEventListener('click', (e) => {
        e.stopPropagation();
        menu.classList.toggle('open');
    });

    document.addEventListener('click', (e) => {
        if (!menu.contains(e.target)) close();
    });
})();
