// Simple confirmation dialogs
function confirmAction(message) {
    return confirm(message);
}

// Format date
function formatDate(dateString) {
    if (!dateString) return '-';
    let date = new Date(dateString);
    return date.toLocaleString();
}

// Show success message
function showSuccess(message) {
    alert('✅ ' + message);
}

// Show error message
function showError(message) {
    alert('❌ ' + message);
}

// Auto hide flash messages after 5 seconds
document.addEventListener('DOMContentLoaded', function() {
    let flashMessages = document.querySelectorAll('.flash-message');
    flashMessages.forEach(function(msg) {
        setTimeout(function() {
            msg.style.display = 'none';
        }, 5000);
    });
});