내용
- config-server에서 변경 하면 client에서는 서버를 재시작 해야 함
- 그러지 않으려면 방법이 3가지가 있음
- client actuator api 호출
- spring-cloud-bus
- watcher spring cloud server 변경 여부 확인