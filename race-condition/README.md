# 목표
각 레이스 컨디션에서 베타 제어를 하는 방법에 대해서 알고 실무에서 제대로 사용 할 수 있도록 하자

# 전략
## [MySQL Row-Level Lock](https://sg-choi.tistory.com/292) (feat: 비관적 락)
- SELECT ~ FOR UPDATE 원자적 쿼리를 이용하여 업데이트 할 떄 다른데서 같은 로우를 CRUD를 못하게 한다.
- SELECT ~ FOR UPDATE 쿼리는 Row 단위 이기 때문에 같은 데이터를 수정하지 않느다면 대기 하지 않는다.
- 서로 자원이 필요한 경우, [데드락](https://dataonair.or.kr/db-tech-reference/d-lounge/expert-column/?mod=document&uid=52944) 발생 위험 (각 트랜잭션에서 각각의 락을 잡고 각각의 자원을 필요로 할 때...)
- 데이터 자체 락을 잡기 때문에 읽기가 많은 DB라면 성능 이슈가 있음

## [MySQL User-Level Lock](https://techblog.woowahan.com/2631/) (feat: GET LOCK)
- [Named Lock](https://velog.io/@this-is-spear/MySQL-Named-Lock) 이라고 함 (MySQL 기능)
- 테이블(메타데이터) 단위로 락이 잡힌다고 함
- 비지니스 로직과 락 로직에 커넥션 풀을 분리하는게 좋다 락 거는 것 때문에 트랜잭션이 고갈되면 다른 서비스에 영향을 줄 수 있기 때문
- Redis, ZooKeeper와 다르게 인프라 구축에 대한 비용이 없어서 장점 (근데 레디스는 거의 구축되어 있으므로 레디쓰 쓰면 됨...)
- [참고](https://sg-choi.tistory.com/292)

## ZooKeeper Lock

## Redis Lock
- 일기 성능이 빠르고 reddison을 이용하여 spin lock 이 아닌 pub sub 형태로 효율적으로 lock을 핸들링 할 수 있고 타임아웃 설정 할 수 있어서 효율적이다.

### 설정 방법
```
# redis 설치
docker run --name myredis -d -p 6379:6379 redis

# yml 추가
spring.redis.host=localhost
spring.redis.port=6379
```

# 실습 내용 정리
- 비지니스 트랜잭션 보다 넓게 락을 잡아야 더티 리드가 생기지 않는다.
- redisson으로 커스텀 어노테이션과 @Transactional을 같이 사용하면 lock이 제대로 동작하지 않는다. 생각해보면 이것은 순서가 있는것이다 락을 먼저 걸고 트랜잭션 시작하고 커밋후 다시 락을 해제 해야 하는 순서로 동작 해야 하는데
스프링 aop가 프록시 될때 어느것을 먼저 실행할지 알 수 없을것 같다. 그래서 락잡는걸 Order(1)로 하니 잘됬다 참고참고..
- 충돌이 자주 발생하면 오히려 롤백 기능을 줄일수 있어 좋지만 읽기 성능이 떨어진다.. 대신 낙관적락은 락을 거는게 아니고 충돌이 발생 하면 그때 대처하자 라는 개념
읽기 성능은 좋아지지만 충돌이 많으면 롤백을 해줘야 하기 때문에 성능 저하가 오히려 발생 할 수 있다.
- 레디스가 빠르다고는 했지만, 일단 디비락과 레디스락 속도 차이는 테스트 코드에서는 없었음 (thread 1000)
- 내생각에는 MySQL은 비지니스 로직을 수행하기 위핸 메인 디비 이기 때문에 베타 제어를 위한 락으로 사용하는것 보다 별도로 락을 위해 빠른 레디스를 이용하는게 좋다고 생각한다.
잘 못 사용하면 데드락이나 성능저하 커넥션풀 부족현상도 있을수 있기 때문이다.

# 알아 볼 것
- [레디스 트랙잭션](https://sabarada.tistory.com/178)

# 실행 방법
- docker로 mysql, redis를 실행 시키고 테스트 코드를 돌린다.
 ```
docker run --name mysqldb -e MYSQL_ROOT_PASSWORD=Password -e MYSQL_DATABASE=mydb -d -p 3306:3306 mysql:latest
  ```