// ShopSphere minimal frontend helpers
document.addEventListener('DOMContentLoaded', function () {
    // Auto-hide alerts after 4s
    document.querySelectorAll('.alert').forEach(function (a) {
        setTimeout(function () { a.classList.remove('show'); }, 4000);
    });
});
