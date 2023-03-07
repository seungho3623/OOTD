# OOTD

- 주제
  - 지역 날씨, 상황별 OOTD 추천

- 기능
  - 날씨 확인

    - 기상청에서 JSON, XML 형식으로 된 데이터 추출
    
    - Java 에서 Get, Post 형식 써서 원하는 지역 데이터 전송 및 추출
    
    - 온도에 따른 계절 판별
    
     
       ※ 3 ~ 5 (봄), 6 ~ 9 (여름), 10 ~ 12 (가을), 1 ~ 2 (겨울)
      
       3월인데 영하 5도라 가정하면 계절은 봄이지만 겨울 날씨이므로 겨울 옷 추천

  - 코디 추천
  
    - 계절에 따른 코디 추천
    
    - 무신사, 룩핀 같은 코디 사이트에서 계절별 데이터 추출
    
    - 3 ~ 5 개 코디 랜덤으로 선정 후 표시

  - 추후 업데이트 예정(확정 X)
  
    - DB에 가지고 있는 옷 정보 저장
    
    - 상황, 장소별 코디 추천
    
    - 데이트 코스 추천

- 일정
  - 3월 
  
    - 1 주차 : 주제 선정
    
    - 2 주차 : 깃 연동
    
    - 3 주차 : 기상청 날씨 API 사용 날씨 추출
    
    - 4 주차 : 최고, 최저, 평균 온도 추출 및 온도에 따른 계절 설정
    

  - 4월 
  
    - 1, 2 주차 : 코디 사이트 데이터 추출
    
    - 3 ,4 주차 : 추출 데이터 계절 별 군집화
    

  - 5월 
  
    - 1 ~ 4 주차 : 군집화 데이터 가지고 코디 추천 및 검증
    
      ※ 기능 검증이 빨리 끝나면 기능 추가 예정

  - 6월 
  
    - 1 ~ 4 주차 : 결과 보고서 작성 및 지속적인 기능 추가 및 검증 예정 
