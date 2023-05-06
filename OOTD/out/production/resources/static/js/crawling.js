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
            window.location.href = "/html/DDZA_1.html";
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
    // coordiThumbnail.src = "https://image.msscdn.net/images/codimap/list/l_3_2023050414030200000073371.jpg?202305060305";
    // coordiName.innerHTML = "인기 만점";
    // coordiDescription.innerHTML = "레터링이 돋보이는 반소매 티셔츠와 벌룬 핏 팬츠를 매치하고 백팩으로 마무리한 캐주얼 룩";
    // coordiURL.setAttribute("onclick", "location.href='https://www.musinsa.com/app/codimap/views/22961'");

    for (let i = 0; i < coordiData[index].itemThumbnails.length; i++) {
        itemThumbnails
            .item(i + 1)
            .src = coordiData[index].itemThumbnails[i];
        // itemThumbnails
        //     .item(i + 1)
        //     .src = "https://image.msscdn.net/images/goods_img/20180403/747941/747941_16778032535978_220.jpg";
    }

}
