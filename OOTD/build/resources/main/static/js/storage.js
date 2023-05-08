const likeButtons = document.querySelectorAll('.likeButton');

likeButtons.forEach((button, index) => {
  button.addEventListener('click', (event) => {
    // sessionStorage에서 coordiData 가져오기
    const coordiDataStr = sessionStorage.getItem('coordiData');

    // JSON 문자열을 JavaScript 객체로 변환
    const coordiData = JSON.parse(coordiDataStr);

    // selectedItems 배열 선언
    let selectedItems = [];

    // 클릭된 버튼의 인덱스에 해당하는 아이템 가져오기
    const selectedItem = {
      thumbnail: coordiData[index].thumbnail,
      url: coordiData[index].url
    };
    selectedItems.push(selectedItem);

    sessionStorage.setItem("outfitStorage", JSON.stringify(selectedItems));
    alert('저장 완료');
  });
});