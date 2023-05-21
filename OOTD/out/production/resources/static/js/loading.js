function setLoadingParam() {
    const urlParams = new URLSearchParams(window.location.search);

    urlParams.set("style", JSON.parse(sessionStorage.getItem('styleData')));
    urlParams.set("gender", JSON.parse(sessionStorage.getItem('genderData')));

    //const url = "/html/Loading.html" + "?" + urlParams.toString();
    const url = "/Project/loading.do" + "?" + urlParams.toString();
    window.history.pushState("", "", url);

    window.location.href = url;
}

function updateProgress(percent) {
    const progressBar = document.getElementById('progress-bar');
    progressBar.style.width = percent + '%';
}

window.onload = function() {
    let currentPercent = 0;
    const interval = setInterval(() => {
        currentPercent += 1;
        updateProgress(currentPercent);
        if(currentPercent > 100)
        {
            clearInterval(interval);
            getCoordi(style, gender);
        }
    }, 20);
};