(function() {
    const sidebar = document.getElementById('sidebar');
    const toggle = document.getElementById('sidebarToggle');
    const app = document.querySelector('.app');
    if (!sidebar || !toggle) return;

    const STORAGE_KEY = 'devpr-sidebar-collapsed';
    const applyState = (collapsed) => {
        sidebar.classList.toggle('collapsed', collapsed);
        if (app) app.classList.toggle('sidebar-collapsed', collapsed);
    };

    const saved = localStorage.getItem(STORAGE_KEY);
    applyState(saved === 'true');

    toggle.addEventListener('click', () => {
        const collapsed = sidebar.classList.toggle('collapsed');
        localStorage.setItem(STORAGE_KEY, String(collapsed));
    });
})();
