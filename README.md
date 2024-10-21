<h1>Spring boot를 활용한 코딩 테스트 웹개발 미니 프로젝트</h1> <br>

<h2>주요기능</h2> <br>
1. Spring Security jwt토큰 인증을 통한 로그인, 페이지 이동 <br>
2. docker 컨테이너를 활용한 자바 코딩테스트 <br>
3. 웹소켓을 활용해 시험자의 코드를 실시간으로 볼 수 있는 실전 코딩 테스트 감독 모드 지원 <br>
4. 포트원 아임포트 결제 모듈을 이용한 결제 시스템 <br>


<h3>1. Spring Security jwt토큰 인증을 통한 로그인, 페이지 이동</h3>

![jwt](https://github.com/user-attachments/assets/22094dcf-e2af-485c-b7a8-21d2c457c277) <br>
-사용자가 로그인하면, 서버는 사용자 ID와 권한 정보를 기반으로 JWT를 생성합니다.
 이 토큰은 Authorization이라는 이름의 HTTP-Only 쿠키에 저장되어, JavaScript에서 접근할 수 없도록 설정하여 XSS 공격을 예방합니다.

-JWT를 기반으로 한 인증 시스템을 통해 사용자가 인증된 경우에만 특정 페이지에 접근할 수 있도록 설정하였습니다. 
 이를 통해 민감한 데이터 보호 및 사용자 경험을 개선했습니다.


<h3>2. docker 컨테이너를 활용한 자바 코딩테스트</h3>

![test](https://github.com/user-attachments/assets/782c14cc-6693-4c25-a1d6-20afc3263445) <br>
Docker를 활용하여 안전하고 효율적인 코딩 테스트 환경을 구축했습니다.
사용자가 제출한 Java 코드를 Docker 컨테이너에서 실행하고, 그 결과를 실시간으로 반환하는 시스템입니다.

특징
1) Docker 컨테이너 활용: 사용자 코드를 격리된 Docker 환경에서 실행하여 보안을 강화했습니다. 이를 통해 다른 사용자 코드와의 충돌을 방지하고, 실행 중 오류가 발생하더라도 시스템의 안정성을 유지할 수 있습니다.

2) 동적 파일 생성 및 실행: 사용자가 제출한 코드는 Docker 컨테이너 내에서 동적으로 Java 파일로 생성되고, start.sh 스크립트를 통해 실행됩니다. 이 과정에서 코드 실행 결과는 result.txt 파일에 기록됩니다.

3) 결과 검증 및 피드백: 실행 결과를 주기적으로 확인하고, 결과 파일에서 코드의 성공 여부를 판단합니다. failed가 포함된 경우에는 오류 메시지를 반환하여 사용자가 코드에 대해 다시 검토할 수 있도록 합니다.

4) AJAX를 통한 실시간 피드백: 사용자 인터페이스에서는 AJAX를 활용하여 코드 제출과 결과 확인을 비동기적으로 처리합니다. 사용자가 제출한 코드의 결과가 서버에서 확인되면 즉시 화면에 반영되도록 하여, 사용자 경험을 향상시켰습니다.

이 시스템을 통해 사용자들은 안전하고 효율적으로 코딩 테스트를 수행할 수 있으며, 코드 실행의 결과를 실시간으로 확인함으로써 학습 효과를 극대화할 수 있습니다. 
또한, Docker를 활용한 실행 환경 구축을 통해 클라우드 기반의 코딩 테스트 시스템을 설계하는 데 필요한 기술적 경험을 쌓을 수 있었습니다.


<h3>3. 웹소켓을 활용한 실시간 코딩 테스트 감독 시스템</h3>

![supervise](https://github.com/user-attachments/assets/05c7c1a6-6578-49aa-bf09-2a424264329b) <br>
AJAX와 WebSocket 기술을 활용하여 여러 명의 수험생이 동시에 코딩 테스트를 진행하는 상황에서, 감독자가 각 수험생의 코드를 실시간으로 확인할 수 있는 웹 기반 시스템을 구축했습니다.

특징
1) 실시간 코드 업데이트: WebSocket을 통해 수험생이 작성하는 코드가 즉시 감독자 페이지에 반영됩니다. 수험생이 코드 에디터에 입력하는 내용은 실시간으로 전달되어, 감독자는 현재 작성 중인 코드를 즉시 확인할 수 있습니다.

2) 다중 사용자 지원: 여러 수험생이 동시에 시험을 치르는 동안, 각 수험생에 대한 독립적인 코드 영역이 생성됩니다. 이를 통해 감독자는 각 수험생의 코드를 별도로 모니터링할 수 있으며, 효율적인 관리가 가능합니다.

이 시스템은 감독자가 수험생의 코드를 실시간으로 모니터링하고, 필요한 경우 즉시 개입할 수 있는 환경을 제공합니다.
특히, 채용 시험으로 원격 코딩 테스트를 실시하는 추세에 이는 챗gpt등 ai사용시 복붙을 적발할 수 있어 시험의 공정성을 높이고, 수험생에게는 보다 안전한 시험 경험을 제공합니다. 
또한, WebSocket을 활용한 실시간 데이터 전송 방식은 향후 다른 프로젝트에서도 응용할 수 있는 유용한 기술적 경험을 쌓게 해주었습니다.


<h3>4. 포트원 아임포트 결제 모듈을 이용한 결제 시스템</h3>

![portone](https://github.com/user-attachments/assets/13af3280-cac3-4827-bcd7-f62834d3ebc4) <br>

테스트로 만들어 본 결제모듈 입니다. 현재 구매내역을 DB에 저장하는 로직으로 만들었고 차후에 구매시 실전 코딩테스트 페이지를 일정기간동안 이용할 수 있게 구현을 할 예정입니다.
