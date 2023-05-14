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
            setTimeout(function() {
                window.location.href = "/Project/outfit.do?pageIndex=0";
            }, 1000);
        },
        error: function() {
            alert("error");
        }
    });
}

function showCoordi(pageIndex = 0) {
    const coordiIndex = pageIndex * 3;
    const coordiData = JSON.parse(sessionStorage.getItem('coordiData'));

    const coordiNames = document.querySelectorAll(".imgDescription");
    const coordiThumbnails = document.getElementsByTagName("input");
    const detailButtons = document.getElementsByClassName("detailButton")

    for (let i = 0; i < coordiNames.length; i++) {
        coordiNames.item(i).innerHTML = coordiData[coordiIndex + i].name;
        coordiThumbnails.item(i).src = coordiData[coordiIndex + i].thumbnail;
        detailButtons.item(i).href = `/Project/detail.do?pageIndex=${pageIndex}&detailIndex=${i}`;
    }
}

function showCoordiDetail(pageIndex = 0, detailIndex = 0) {
    const coordiIndex = (pageIndex * 3) + detailIndex;
    const coordiData = JSON.parse(sessionStorage.getItem('coordiData'));

    const coordiName = document.querySelector(".leftBox .imgDescription");
    const coordiDescription = document.querySelector(".itemsDescription");
    const coordiThumbnail = document.getElementsByTagName("input").item(0);
    const itemThumbnails = document.getElementsByTagName("input");
    const coordiURL = document.querySelector(".detailButton");

    coordiThumbnail.src = coordiData[coordiIndex].thumbnail;
    coordiName.innerHTML = coordiData[coordiIndex].name;
    coordiDescription.innerHTML = coordiData[coordiIndex].description;
    //coordiURL.setAttribute("onclick", `location.href='${coordiData[index].url}'`);
    coordiURL.setAttribute("onclick", `window.open('${coordiData[coordiIndex].url}', '_blank')`);

    for (let i = 0; i < coordiData[coordiIndex].itemThumbnails.length; i++) {
        itemThumbnails.item(i + 1).src = coordiData[coordiIndex].itemThumbnails[i];
    }
}
