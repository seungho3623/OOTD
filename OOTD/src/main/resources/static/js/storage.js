const likeButtons = document.querySelectorAll('.likeButton');
let selectedItems = JSON.parse(sessionStorage.getItem('outfitStorage')) || [];

likeButtons.forEach((button, index) => {
  button.addEventListener('click', (event) => {
    const coordiDataStr = sessionStorage.getItem('coordiData');
    const coordiData = JSON.parse(coordiDataStr);

    const selectedItem = {
      thumbnail: coordiData[index].thumbnail,
      url: coordiData[index].url
    };

    const isDuplicate = selectedItems.some(item => {
      return item.thumbnail === selectedItem.thumbnail && item.url === selectedItem.url;
    });

    if (!isDuplicate) {
      selectedItems.push(selectedItem);
      sessionStorage.setItem("outfitStorage", JSON.stringify(selectedItems));
      alert('저장 완료');
    } else {
      alert('이미 선택된 아이템입니다.');
    }
  });
});
