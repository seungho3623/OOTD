let currentPercent = 0;
function updateProgress(percent) {
    const progressBar = document.getElementById('progress-bar');
    progressBar.style.width = percent + '%';
}

window.onload = function() {
    let currentPercent = 0;
    const interval = setInterval(() => {
        currentPercent += 1;
        updateProgress(currentPercent);
        if(currentPercent > 140)
        {
            clearInterval(interval);
            window.location.href = "/Project/outfit.do"
        }
    }, 20);
};