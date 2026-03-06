(function() {
    const THEME_KEY = 'devpr-theme';
    const root = document.documentElement;
    const buttons = [
        document.getElementById('themeToggle'),
        document.getElementById('settingsThemeToggle')
    ].filter(Boolean);

    const detectPreferred = () => window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';

    const apply = (theme) => {
        root.setAttribute('data-theme', theme);
        localStorage.setItem(THEME_KEY, theme);
    };

    const saved = localStorage.getItem(THEME_KEY) || detectPreferred();
    apply(saved);

    buttons.forEach(btn => btn.addEventListener('click', () => {
        const next = root.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
        apply(next);
    }));
})();
