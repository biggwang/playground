## 목표
각 레이스 컨디션에서 베타 제어를 하는 방법에 대해서 알고 실무에서 제대로 사용 할 수 있도록 하자

## 전략

### java syncronized
- 단일 서버에서만 가능

### mysql pessimistic lock
- 다른 트랙잭션은 읽기, 쓰기 불가 (최악 성능...)
- 서로 자원이 필요한 경우, 데드락 발생 위험
- 데이터 자체 락을 잡기 때문에 읽기가 많은 DB라면 성능 이슈가 있음
  ```
  docker run --name mysqldb -e MYSQL_ROOT_PASSWORD=Password -e MYSQL_DATABASE=mydb -d -p 3306:3306 mysql:latest
  ```
### redis 
- 일기 성능이 빠르고 reddison을 이용하여 spin lock 이 아닌 pub sub 형태로 효율적으로 lock을 핸들링 할 수 있고 타임아웃 설정 할 수 있어서 효율적이다.


## redis lock 적용

### 설정 방법
```
# redis 설치
docker run --name myredis -d -p 6379:6379 redis

# yml 추가
spring.redis.host=localhost
spring.redis.port=6379
```
### 내용
- lettuce: 스핀락을 사용하여 실습하지 않았음
- redisson: pub-sub 기반으로 적합하여 사용
- 비지니스 트랜잭션 보다 넓게 락을 잡아야 더티 리드가 생기지 않는다.
- 여러 연산이 있을 때 트랜잭션 단위로 락을 묶지 않을때 정합성 깨지게 된다. 그래서 락을 어디서 잡을지 고민은 해야 한다.

### 알아 볼 것
- 레디슨에서 락잡고 왜 별도 Service에 위임해야 동작 할까????
- mysql lock보다 레디스락이 더 좋은걸까?
- 데드락을 발생 시키는 케이스 재현
- mysql 비관적 락 사용시 다른 트랜잭션 진입 막히는거 테스트

## 실행 방법
- docker로 mysql, redis를 실행 시키고 테스트 코드를 돌린다.
    