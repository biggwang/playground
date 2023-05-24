

# spring-config 사용해 보기

## config-server 구성
- [config-repo](https://github.com/biggwang/spring-config-repo/tree/main/ironman) config로 사용할 repo를 만든다.
- yml에서 config 정보가 저장된 git repo url 등 관련 설정 정보를 넣는다.
- config-server에서 http://localhost:8080/ironman/switch-develop 요청하여 값이 불러오면 성공


## config-client 구성
- yml에서 config-server url등 관련 정보를 넣는다.
- spring-actuator를 넣는다 (그래야 config 서버에서 config 변경 됬을 때 client에게 refresh를 할 수 있다 )
- @RefreshScope는 spring-actuator에서 /actuator/refresh 요청을 받으면 선언된 어노테이션에서 spring bean refresh 되어 값을 갱신해 주는 핵심 역활 

## client 설정 정보 갱신 방법
- /actuator/refresh 요청 
   - 이방법은 간단 하나 연결 된 클라이언트가 많으면 일일이 요청을 날려줘야 하는 불편함이 있음 (서비스 디스커버리를 붙여하는데 그렇게 까지??)
- watcher
   - client가 지속적으로 server에게 pulling 하는데 비효율적
- event bus
   - 이게 제일 나은 방식이라고 생각한다 카프카를 붙이던

## event bus를 이용하여 설정값 자동 갱신하기
- config-client 서버에서 /actuator/busrefresh 를 하면 카프카로 전달되어 다른 서버에도 전달이 되겠지만 서버가 여러대 있으면 특정 서버를 지정에서 actuator를 요청하기는 어렵다
- 그래서 먼저 github webhook을 이용하여 config-server에 이벤트를 전달해 줘야 하는데, github에 바로 쏘면 config-server가 잘 못 받는다고 한다 ([참고](https://happycloud-lee.tistory.com/211)) 
  그래서 별도 서버를 구축하는데 이것도 귀찮고 불필요하니 config-monitor를 꽂으면 그 역활을 해준다고 한다.
- config-monitor 라이브러리를 추가하여 /monitor라는 엔드포인트를 활성화 시켜서 git push 가 발생하면 github 에서 webhook으로 config-server에 /monitor 를 호출하게 한다
- 그러면 그것이 카프카로 이벤트가 전달 되고 client가 일괄 refresh 하게 되어 컨피그 값이 바뀌게 된다.
- 참고로 

## 참고 사이트
- https://cheese10yun.github.io/spring-config-client/
- https://jaehun2841.github.io/2022/03/10/2022-03-11-spring-cloud-config-monitor/#%EB%93%A4%EC%96%B4%EA%B0%80%EB%A9%B0
- https://happycloud-lee.tistory.com/212