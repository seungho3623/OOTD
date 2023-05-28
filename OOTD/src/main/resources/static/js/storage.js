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
      // selectedItems.push(likeSelectedItem);
      // sessionStorage.setItem('outfitStorage', JSON.stringify(selectedItems));
      saveDatabase(likeSelectedItem);
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
    // selectedItems.push(detailSelectedItem);
    // sessionStorage.setItem('outfitStorage', JSON.stringify(selectedItems));
    saveDatabase(detailSelectedItem);
    alert('저장 완료');
  } else {
    alert('이미 선택된 아이템입니다.');
  }
});

function loadDatabase() {
  $.ajax({
    type: "POST",
    url: "/Project/getStorageData",
    dataType: "json",
    success: function(data) {
      sessionStorage.setItem("outfitStorage", JSON.stringify(data));
      showDatabase();
    },
    error: function() {
      alert("error");
    }
  });
}

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
      // alert("An error occurred while saving data.");
    }
  });
}

function showDatabase() {
  /* DB 연동 저장소 데이터 */
  //Mode list 값 받아오기
  //const list = /*[[${list}]]*/ [];
  /*
  // outfitContainer 요소 선택
  const outfitContainer = document.getElementById('outfitContainer');

  // list 데이터를 반복하여 HTML에 추가
  list.forEach((outfit, index) => {
      const outfitElement = document.createElement('div');

      const thumbnailImg = document.createElement('img');
      thumbnailImg.src = outfit.thumbnail;
      thumbnailImg.alt = 'Thumbnail';

      const buttonElement = document.createElement('div');
      const urlButton = document.createElement('button');
      urlButton.textContent = '상세';
      // URL 버튼 클릭 이벤트 처리
      urlButton.addEventListener('click', () => {
          window.open(outfit.url, '_blank');
      });

      const deleteButton = document.createElement('button');
      deleteButton.textContent = '삭제';

      // 삭제 버튼 클릭 이벤트 처리
      deleteButton.addEventListener('click', () => {
          // 해당 outfit을 outfitStorage에서 제거
          outfitStorage.splice(index, 1);

          // outfitContainer의 자식 요소 삭제
          outfitContainer.removeChild(outfitElement);

          // 변경된 outfitStorage를 sessionStorage에 저장
          sessionStorage.setItem('outfitStorage', JSON.stringify(outfitStorage));

          location.reload();
      });
      outfitElement.appendChild(thumbnailImg);
      buttonElement.appendChild(urlButton);
      buttonElement.appendChild(deleteButton);
      outfitElement.appendChild(buttonElement);

      outfitContainer.appendChild(outfitElement);
  });
  */

  /* 기존 sessionStorage 방식 적용 */
  // sessionStorage에서 outfitStorage 데이터 가져오기
  const outfitStorageStr = sessionStorage.getItem('outfitStorage');

  // JSON 문자열을 JavaScript 객체로 변환
  const outfitStorage = JSON.parse(outfitStorageStr);

  // outfitContainer 요소 선택
  const outfitContainer = document.getElementById('outfitContainer');

  // outfitStorage 데이터를 반복하여 HTML에 추가
  outfitStorage.forEach((outfit, index) => {
    const outfitElement = document.createElement('div');
    const thumbnailImg = document.createElement('img');

    thumbnailImg.src = outfit.thumbnail;
    thumbnailImg.alt = 'Thumbnail';

    const buttonElement = document.createElement('div');
    const urlButton = document.createElement('button');

    urlButton.textContent = '상세';
    // URL 버튼 클릭 이벤트 처리
    urlButton.addEventListener('click', () => {
      window.open(outfit.url, '_blank');
    });

    const deleteButton = document.createElement('button');
    deleteButton.textContent = '삭제';

    // 삭제 버튼 클릭 이벤트 처리
    deleteButton.addEventListener('click', () => {
      // 해당 outfit을 outfitStorage에서 제거
      outfitStorage.splice(index, 1);

      // outfitContainer의 자식 요소 삭제
      outfitContainer.removeChild(outfitElement);

      // 변경된 outfitStorage를 sessionStorage에 저장
      sessionStorage.setItem('outfitStorage', JSON.stringify(outfitStorage));

      location.reload();
    });

    outfitElement.appendChild(thumbnailImg);
    buttonElement.appendChild(urlButton);
    buttonElement.appendChild(deleteButton);
    outfitElement.appendChild(buttonElement);

    outfitContainer.appendChild(outfitElement);
  });
}