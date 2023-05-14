function getWeather() {
    let nullCheck = true;

    $('.selectCity').each(function () {
        if ('' == $(this).val()) {
            alert($(this).attr('title') + "을(를) 확인바람");
            nullCheck = false;
            return false;	// 빈 값에서 멈춤
        }
    });

    if(!nullCheck) return false;

    if (!$("input[name='gender']:checked").val()) {
        nullCheck = false;
        alert("성별을(를) 확인바람");
    }
    
    if(!nullCheck) return false;

    if (!$("input[name='style']:checked").val()) {
        nullCheck = false;
        alert("스타일을(를) 확인바람");
    }

    if(!nullCheck) return false;

    if (nullCheck) {
        const today = new Date();

        let year = today.getFullYear();
        let month = ('0' + (today.getMonth() + 1)).slice(-2);
        let day = ('0' + today.getDate()).slice(-2);
        //let hours = ('0' + today.getHours()).slice(-2);
        //let minutes = ('0' + today.getMinutes()).slice(-2);
        let hours = today.getHours();
        let minutes = today.getMinutes();
        let baseTimes = [2, 5, 8, 11, 14, 17, 20, 23];

        dateData = year + month + day;

        /*
        - 단기예보 현업운영 발표시간 별 예보시각 !
        - Base_time : 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 (1일 8회)
        - API 제공 시간(~이후) : 02:10, 05:10, 08:10, 11:10, 14:10, 17:10, 20:10, 23:10
        - 현재 시간에서 가장 가까운 작은 값 구하기
        */

        if(hours == "00" || hours == "01"){
            day -= 1;
            day = day < 10 ? '0' + day : day;
            if (day === '00') {
                month = ('0' + (today.getMonth())).slice(-2);
                day = new Date(today.getFullYear(), today.getMonth() + 1, 0).getDate();
            }
            dateData = year + month + day;
            hours = "23";
            minutes = "30";
        }
        else{
            dateData = year + month + day;
        }

        let baseTime = Math.max(...baseTimes.filter((time) => time <= hours));
        let apiTime = `${baseTime.toString().padStart(2, "0")}:15`;
        let apiHours = apiTime.slice(0, 2); // 시간 추출
        let apiMinutes = apiTime.slice(3, 5); // 분 추출
        let time = apiHours + apiMinutes;

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
        $.ajax({
            url: "/Project/getWeather.do",
            data: data,
            dataType: "json",
            method: "post",
            success: function (res) {
                if (res[0].resultCode != null) {
                    alert(res[0].resultMsg);
                } else {
                    let weatherData = {};
                    let locationData = {
                        city: cityData,
                        county: countyData,
                        town: townData
                    };
                    let genderData;
                    switch ($("input[name='gender']:checked").val()){
                        case "Man" :
                            genderData = "남성";
                            break;
                        case "Women" :
                            genderData = "여성";
                            break;
                        default :
                            break;
                    }
                    let styleData;
                    switch ($("input[name='style']:checked").val()){
                        case "Casual" :
                            styleData = "캐주얼";
                            break;
                        case "Formal" :
                            styleData = "포멀";
                            break;
                        case "American" :
                            styleData = "아메카지";
                            break;
                        case "Street" :
                            styleData = "스트릿";
                            break;
                        case "GorpCore" :
                            styleData = "고프고어";
                            break;
                        case "Sports" :
                            styleData = "스포츠";
                            break;
                        default :
                            break;
                    }
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
                    sessionStorage.setItem("genderData", JSON.stringify(genderData));
                    sessionStorage.setItem("styleData", JSON.stringify(styleData));

                    const urlParams = new URLSearchParams(window.location.search);
                    urlParams.set("style", styleData);
                    urlParams.set("gender", genderData);

                    //const url = "/html/Loding.html" + "?" + urlParams.toString();
                    const url = "/Project/loding.do" + "?" + urlParams.toString();
                    window.history.pushState("", "", url);

                    window.location.href = url;
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
    //const humidity = weatherData.humidity;
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
    let weatherInfo;

    switch (true) {
        case (weatherData.temperature < 0):
            weatherInfo = "너무 추워! 나가지 말고 집에 있자!";
            break;
        case (0 < weatherData.temperature && weatherData.temperature <= 5):
            weatherInfo = "두꺼운 옷을 입어! 내복을 입는 것도 추천!";
            break;
        case (5 < weatherData.temperature && weatherData.temperature <= 9):
            weatherInfo = "날씨가 쌀쌀해! 코트나 무스탕 추천!";
            break;
        case (9 < weatherData.temperature && weatherData.temperature <= 11):
            weatherInfo = "외투착용 필수! 옷장 속 외투 챙기기!";
            break;
        case (11 < weatherData.temperature && weatherData.temperature <= 16):
            weatherInfo = "본격적인 갈절기 싲가! 일교차 조심해!";
            break;
        case (16 < weatherData.temperature && weatherData.temperature <= 19):
            weatherInfo = "약간은 쌀쌀할거야! 감기 조심해!";
            break;
        case (19 < weatherData.temperature && weatherData.temperature <= 22):
            weatherInfo = "포근한 날씨로 옷 입기 가장 좋은 날씨!";
            break;
        case (22 < weatherData.temperature && weatherData.temperature <= 26):
            weatherInfo = "에어컨 때문에 얇은 외투를 챙겨도 좋아!";
            break;
        case (26 < weatherData.temperature && weatherData.temperature <= 32):
            weatherInfo = "완전 여름이야! 시원하게 입자!";
            break;
        case (32 < weatherData.temperature):
            weatherInfo = "폭염주의! 나가지말자!";
            break;
        default:
            break;
    }
    // 가져온 데이터를 weather-widget에 적용하기
    weatherLocation.textContent = locationString;
    weatherTemp.innerHTML = `${weatherData.temperature} &#8451; (${minTemp} &#8451;,  ${maxTemp} &#8451;)`;
    weatherDesc.textContent = weatherInfo
    weatherWidget.querySelector('.weather-icon').style.backgroundImage = `url('${imagePath}')`;
}