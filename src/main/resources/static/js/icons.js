(function () {
  if (!window.feather) return;
  // Replace all [data-feather] with SVG icons, preserving stroke width tweaks if needed
  feather.replace({
    width: 22,
    height: 22,
    'stroke-width': 2.1,
  });
})();
