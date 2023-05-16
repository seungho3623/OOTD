
function getCoordi(style, gender) {
    $.ajax({
        type: "POST",
        url: "/Project/getCoordi",
        data: {
            style: style,
            gender: gender
        },
        dataType: "json",
        success: function(data) {
            sessionStorage.setItem("coordiData", JSON.stringify(data));
            setTimeout(function() {
                window.location.href = "/Project/outfit.do?page=0";
            }, 1000);
        },
        error: function() {
            alert("error");
        }
    });
}

function showCoordi(page = 0) {
    const coordiIndex = page * 3;
    const coordiData = JSON.parse(sessionStorage.getItem('coordiData'));

    const coordiNames = document.querySelectorAll(".imgDescription");
    const coordiThumbnails = document.getElementsByTagName("input");
    const detailButtons = document.getElementsByClassName("detailButton");

    for (let i = 0; i < coordiNames.length; i++) {
        coordiNames.item(i).innerHTML = coordiData[coordiIndex + i].name;
        coordiThumbnails.item(i).src = coordiData[coordiIndex + i].thumbnail;
        detailButtons.item(i).href = `/Project/detail.do?page=${page}&detail=${i}`;
    }
}

function showCoordiDetail(page = 0, detail = 0) {
    const coordiIndex = (page * 3) + detail;
    const coordiData = JSON.parse(sessionStorage.getItem('coordiData'));

    const coordiName = document.querySelector(".outfitBox .imgDescription");
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
