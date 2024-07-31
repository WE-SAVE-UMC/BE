# BE

## 1. Stack Info

- JAVA 17
- SpringBoot 3.3.2
- Build Tool Gradle - groovy

### Dependencies

- Spring Web, Spring Data JPA, Lombok, MySql Driver, Spring Security

## 2. 브랜치 전략

![image](https://github.com/Ttottoga/BE/assets/86754153/7de4ebee-ed04-4b53-9460-5cb443927c57)

- `master`는 언제든지 배포가 가능한 상태(릴리즈)
- 새로운 프로젝트는 `develop`을 기반으로 별도 `feature` 브랜치를 생성하여 작업을 진행함
- 브랜치는 로컬에 commit하고, 정기적으로 원격 브랜치에 push함
- 피드백이나 도움이 필요하거나,코드 병합 할 준비가 되었다면 pull request를 만듬
- 다른사람이 변경된 코드를 검토 한 뒤 승인하면 `master`에 병합함
- 병합된 `master`는 즉시 배포할 수 있으며, 배포 해야만 함
