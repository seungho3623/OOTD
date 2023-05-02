function getWeather() {
    let nullCheck = true;
    $('.emptyCheck').each(function () {
        if ('' == $(this).val()) {
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
        } else if (townCode == '' && countyCode != '') {
            areacode = countyCode;
        } else if (townCode != '') {
            areacode = townCode;
        }

        let data = {"areacode": areacode, "baseDate": dateData, "baseTime": time};
        let test = 0;

        $.ajax({
            url: "/Project/getWeather.do",
            data: data,
            dataType: "json",
            method: "post",
            success: function (res) {
                console.log(res);
                if (res[0].resultCode != null) {
                    alert(res[0].resultMsg);
                } else {
                    let weatherData = {};
                    let locationData = {
                        city: cityData,
                        county: countyData,
                        town: townData
                    };
                    $.each(res, function (index, item) {
                        switch (item.category) {
                            case "PTY":
                                switch (item.fcstValue) {
                                    case "1":
                                        weatherData.rainPrecipitaion = "비";
                                        break;
                                    case "2":
                                        weatherData.rainPrecipitaion = "비/눈";
                                        break;
                                    case "3":
                                        weatherData.rainPrecipitaion = "눈";
                                        break;
                                    case "4":
                                        weatherData.rainPrecipitaion = "소나기";
                                        break;
                                    default:
                                        //weatherData.rainPrecipitaion = item.fcstValue;
                                        weatherData.rainPrecipitaion = "강수없음";
                                        break;
                                }
                                break;
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
            error: function (xhr) {
                alert(xhr.responseText);
            }
        });
    }
}

function displayData() {
    // sessionStorage에서 데이터 가져오기
    let imagePath;
    const weatherData = JSON.parse(sessionStorage.getItem('weatherData'));
    const locationData = JSON.parse(sessionStorage.getItem('locationData'));
    const maxTemp = weatherData.highTemperature;
    const minTemp = weatherData.lowTemperature;
    const weatherStatus = weatherData.weather;
    const humidity = weatherData.humidity;
    const rainPrecipitation = weatherData.rainPrecipitaion;
    const city = locationData.city;
    const county = locationData.county;
    const town = locationData.town;
    const locationString = `${city} ${county} ${town}`;
    //날씨에 따른 아이콘
    if (rainPrecipitation == "강수없음") {
        switch (weatherStatus) {
            case "맑음":
                imagePath = "/img/Weather/맑음.jpg";
                break;
            case "구름많음":
                imagePath = "/img/Weather/구름많음.jpg";
                break;
            case "흐림":
                imagePath = "/img/Weather/흐림.jpg";
                break;
            default:
                imagePath = "/img/Weather/맑음.jpg";
                break;
        }
    } else {
        switch (rainPrecipitation) {
            case "비":
                imagePath = "/img/Weather/비.jpg";
                break;
            case "비/눈":
                imagePath = "/img/Weather/눈.jpg";
                break;
            case "눈":
                imagePath = "/img/Weather/눈.jpg";
                break;
            case "소나기":
                imagePath = "/img/Weather/비.jpg";
                break;
            default:
                imagePath = "/img/Weather/비.jpg";
                break;
        }
    }

    // weather-widget 요소 찾기
    const weatherWidget = document.querySelector('.weather-widget');

    // weather-widget 내부의 요소들 찾기
    const weatherLocation = weatherWidget.querySelector('.weather-location');
    const weatherTemp = weatherWidget.querySelector('.weather-temp');
    const weatherDesc = weatherWidget.querySelector('.weather-desc');

    // 가져온 데이터를 weather-widget에 적용하기
    weatherLocation.textContent = locationString;
    weatherTemp.innerHTML = `${weatherData.temperature} &#8451; (${minTemp} &#8451;,  ${maxTemp} &#8451;)`;
    weatherDesc.textContent = weatherData.description;
    weatherWidget.querySelector('.weather-icon').style.backgroundImage = `url('${imagePath}')`;
}