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
            startCrawling(style, gender);
        }
    }, 20);
};