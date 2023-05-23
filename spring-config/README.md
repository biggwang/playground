

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
   - 이게 제일 나은 방식이라고 생각한다 카프카를 붙이던, 