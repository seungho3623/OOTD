let cityData;
let countyData;
let townData;
let dateData;
let weatherData = {};

window.onload = function (){
    loadArea('city');
};

$('#step1').change(function (){
    cityData = $('#step1').find('option:selected').text();
    countyData = "";
    townData = "";
    loadArea('county', $(this));
});

$('#step2').change(function (){
    countyData = $('#step2').find('option:selected').text();
    loadArea('town', $(this));
});

$('#step3').change(function (){
    townData = $('#step3').find('option:selected').text();
});

function loadArea(type, element) {
    let value = $(element).find('option:selected').text();
    let params = {type: type, keyword: value};

    console.log(params);
    $.ajax({
        url: "/Project/weatherStep.do",
        data: params,
        dataType: "json",
        method : "post",
        success : function(res){
            if (type == 'city'){
                res.forEach(function (city) {
                    $('#step1').append('<option value="'+city.areacode+'">'+city.step1+'</option>')
                });
            }
            else if(type == 'county'){
                $('#county').siblings().remove();
                $('#town').siblings().remove();
                res.forEach(function (county) {
                    $('#step2').append('<option value="'+county.areacode+'">'+county.step2+'</option>')
                });
            }
            else{
                $('#town').siblings().remove();
                res.forEach(function (town) {
                    $('#step3').append('<option value="'+town.areacode+'">'+town.step3+'</option>')
                });
            }
        },
        error : function(xhr){
            alert(xhr.responseText);
        }
    });
}