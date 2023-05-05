function changePage() {
    window.location.href = "/Project/outfit.do";
}

document.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        changePage();
    }
});