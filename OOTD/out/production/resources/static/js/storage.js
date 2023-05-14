const likeButtons = document.querySelectorAll('.likeButton');
let selectedItems = JSON.parse(sessionStorage.getItem('outfitStorage')) || [];

//pageIndex에 따라서 buttonIndex 가 +3 이 되어야 정상적으로 저장이 가능함.
likeButtons.forEach((button, index) => {
  button.addEventListener('click', (event) => {
    const coordiDataStr = sessionStorage.getItem('coordiData');
    const coordiData = JSON.parse(coordiDataStr);
    
    const adjustedIndex = (pageIndex * 3) + index;
    const selectedItem = {
      thumbnail: coordiData[adjustedIndex].thumbnail,
      url: coordiData[adjustedIndex].url
    };

    const isDuplicate = selectedItems.find(item => {
      return item.thumbnail === selectedItem.thumbnail && item.url === selectedItem.url;
    });

    if (!isDuplicate) {
      selectedItems.push(selectedItem);
      sessionStorage.setItem('outfitStorage', JSON.stringify(selectedItems));
      alert('저장 완료');
    } else {
      alert('이미 선택된 아이템입니다.');
    }
  });
});
