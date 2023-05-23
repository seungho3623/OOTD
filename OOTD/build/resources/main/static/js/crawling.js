function startCrawling(style, gender) {
    $.ajax({
        type: "POST",
        url: "/Project/getCoordi",
        data: {
            gender: gender,
            style: style
        },
        dataType: "json",
        success: function(data) {
            sessionStorage.setItem("coordiData", JSON.stringify(data));
            window.location.href = "/Project/outfit.do";
        },
        error: function() {
            alert("error");
        }
    });
}

function showCoordi() {
    const coordiData = JSON.parse(sessionStorage.getItem('coordiData'));

    const coordiNames = document.querySelectorAll(".imgDescription");
    const coordiThumbnails = document.getElementsByTagName("input");

    for (let i = 0; i < coordiNames.length; i++) {
        coordiNames.item(i).innerHTML = coordiData[i].name;
        coordiThumbnails.item(i).src = coordiData[i].thumbnail;
    }
}

function showCoordiDetail(index = 0) {
    const coordiData = JSON.parse(sessionStorage.getItem('coordiData'));

    const coordiName = document.querySelector(".leftBox .imgDescription");
    const coordiDescription = document.querySelector(".itemsDescription");
    const coordiThumbnail = document.getElementsByTagName("input").item(0);
    const itemThumbnails = document.getElementsByTagName("input");
    const coordiURL = document.querySelector(".detailButton");

    coordiThumbnail.src = coordiData[index].thumbnail;
    coordiName.innerHTML = coordiData[index].name;
    coordiDescription.innerHTML = coordiData[index].description;
    coordiURL.setAttribute("onclick", `location.href='${coordiData[index].url}'`);

    for (let i = 0; i < coordiData[index].itemThumbnails.length; i++) {
        itemThumbnails.item(i + 1).src = coordiData[index].itemThumbnails[i];
    }

}
