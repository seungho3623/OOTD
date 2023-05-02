function getWeather() {
    let nullCheck = true;
    $('.emptyCheck').each(function (){
        if ('' == $(this).val()){
            alert($(this).attr('title') + "을(를) 확인바람");
            nullCheck = false;
            return false;	// 빈 값에서 멈춤
        }
    });

    if (nullCheck) {
        const today = new Date();

        let year = today.getFullYear();
        let month = ('0' + (today.getMonth() + 1)).slice(-2);
        let day = ('0' + today.getDate()).slice(-2);

        dateData = year + month + day;
        let time = "0800";

        let areacode = "";
        let cityCode = $('#step1 option:selected').val();
        let countyCode = $('#step2 option:selected').val();
        let townCode = $('#step3 option:selected').val();

        if (townCode == '' && countyCode == '') {
            areacode = cityCode;
        }
        else if(townCode == '' && countyCode != '') {
            areacode = countyCode;
        }
        else if(townCode != '') {
            areacode = townCode;
        }

        let data = {"areacode" : areacode, "baseDate" : dateData, "baseTime" : time};

        $.ajax({
            url: "/Project/getWeather.do",
            data: data,
            dataType: "json",
            method : "post",
            success : function(res){
                console.log(res);
                if (res[0].resultCode != null) {
                    alert(res[0].resultMsg);
                }
                else {
                    let weatherData = {};
                    let locationData = {
                        city : cityData,
                        county: countyData,
                        town : townData
                    };
                    $.each(res, function(index, item) {
                        switch (item.category) {
                            case "TMP":
                                weatherData.temperature = item.fcstValue;
                                break;
                            case "DTX":
                                weatherData.highTemperature = item.fcstValue;
                                break;
                            case "DTN":
                                weatherData.lowTemperature = item.fcstValue;
                                break;
                            case "REH":
                                weatherData.humidity = item.fcstValue;
                                break;
                            case "POP":
                                weatherData.rainProbability = item.fcstValue;
                                break;
                            case "SKY":
                                switch (item.fcstValue) {
                                    case "1":
                                        weatherData.weather = "맑음";
                                        break;
                                    case "3":
                                        weatherData.weather = "구름 많음";
                                        break;
                                    case "4":
                                        weatherData.weather = "흐림";
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
                    });
                    sessionStorage.setItem("weatherData", JSON.stringify(weatherData));
                    sessionStorage.setItem("locationData", JSON.stringify(locationData));
                    window.location.href = "/html/DDZA_1.html";
                }
            },
            error : function(xhr){
                alert(xhr.responseText);
            }
        });
    }
}

function displayData() {
    const locationData = JSON.parse(sessionStorage.getItem("locationData"));
    const weatherData = JSON.parse(sessionStorage.getItem("weatherData"));

    // locationData 출력
    let locationHtml = "";
    for (let key in locationData) {
        locationHtml += "<p>" + key + " : " + locationData[key] + "</p>";
    }
    $("#resultLocation").html(locationHtml);

    // weatherData 출력
    let weatherHtml = "";
    for (let key in weatherData) {
        switch (key) {
            case "TMP":
                weatherHtml += "<p>온도 : " + weatherData[key] + "</p>";
                break;
            case "DTX":
                weatherHtml += "<p>최고 온도 : " + weatherData[key] + "</p>";
                break;
            case "DTN":
                weatherHtml += "<p>최저 온도 : " + weatherData[key] + "</p>";
                break;
            case "REH":
                weatherHtml += "<p>습도 : " + weatherData[key] + "</p>";
                break;
            case "POP":
                weatherHtml += "<p>강수 확률 : " + weatherData[key] + "</p>";
                break;
            case "SKY":
                switch (weatherData[key]) {
                    case "1":
                        weatherHtml += "<p>날씨 : 맑음</p>";
                        break;
                    case "3":
                        weatherHtml += "<p>날씨 : 구름 많음</p>";
                        break;
                    case "4":
                        weatherHtml += "<p>날씨 : 흐림</p>";
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
    $("#resultWeather").html(weatherHtml);
}