const likeButtons = document.querySelectorAll('.likeButton');
const detailLikeButton = document.querySelector('.detailLikeButton');
let selectedItems = JSON.parse(sessionStorage.getItem('outfitStorage')) || [];

//page에 따라서 buttonIndex 가 +3 이 되어야 정상적으로 저장이 가능함.
likeButtons.forEach((button, index) => {
  button.addEventListener('click', (event) => {
    const coordiDataStr = sessionStorage.getItem('coordiData');
    const coordiData = JSON.parse(coordiDataStr);

    const adjustedIndex = (page * 3) + index;
    const likeSelectedItem = {
      thumbnail: coordiData[adjustedIndex].thumbnail,
      url: coordiData[adjustedIndex].url
    };

    const isDuplicate = selectedItems.find(item => {
      return item.thumbnail === likeSelectedItem.thumbnail && item.url === likeSelectedItem.url;
    });

    if (!isDuplicate) {
      selectedItems.push(likeSelectedItem);
      sessionStorage.setItem('outfitStorage', JSON.stringify(selectedItems));
      //saveDatabase(likeSelectedItem);
      alert('저장 완료');
    } else {
      alert('이미 선택된 아이템입니다.');
    }
  });
});

detailLikeButton.addEventListener('click', (event) => {
  const coordiDataStr = sessionStorage.getItem('coordiData');
  const coordiData = JSON.parse(coordiDataStr);

  const adjustedIndex = (page * 3) + detail;
  const detailSelectedItem = {
    thumbnail: coordiData[adjustedIndex].thumbnail,
    url: coordiData[adjustedIndex].url
  };

  const isDuplicate = selectedItems.find(item => {
    return item.thumbnail === detailSelectedItem.thumbnail && item.url === detailSelectedItem.url;
  });

  if (!isDuplicate) {
    selectedItems.push(detailSelectedItem);
    sessionStorage.setItem('outfitStorage', JSON.stringify(selectedItems));
    //saveDatabase(detailSelectedItem);
    alert('저장 완료');
  } else {
    alert('이미 선택된 아이템입니다.');
  }
});

function saveDatabase(item) {
  $.ajax({
    type: "POST",
    url: "/Project/storage.do/storage",
    data: {
      thumbnail: item.thumbnail,
      url: item.url
    },
    success: function(response) {
      if (response.success) {
        alert("Data saved successfully!");
      } else {
        alert("Failed to save data.");
      }
    },
    error: function() {
      alert("An error occurred while saving data.");
    }
  });
}