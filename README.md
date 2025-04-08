아래는 지금까지 진행하신 **Java 기반 설치 자동화 프로그램** 개발 및 문제 해결 과정을 `.md` 마크다운 형식으로 정리한 내용입니다:

---

# 📦 Java 설치 자동화 프로그램 개발 기록

## 📁 프로젝트 구조

```
Project/
├── build/          # 컴파일된 class 파일들
├── installer/      # 프로그램 인스톨러 다운로드 경로
├── JSON/           # 계정 정보 및 프로그램 목록 저장 (install_list.json, accounts.json 등)
├── lib/            # 외부 라이브러리 (jackson-core, jackson-databind 등)
├── logs/           # 로그 파일 저장 경로 (log.txt, summary.txt 등)
└── src/
    └── autoinstaller/  # Java 소스 파일 (Main.java, Utils.java, Installer.java 등)
```

---

## ⚙️ 기능 요약

- ✅ 설치할 프로그램 목록 JSON 파일에서 로드
- ✅ 사용자가 프로그램 선택 (예: `1,2,4` 또는 `all`)
- ✅ 설치 상태 캐시로 중복 설치 방지
- ✅ 인스톨러 다운로드 및 설치 명령 실행
- ✅ 콘솔과 로그로 진행 상태 출력
- ✅ 설치 완료 후 요약 리포트 출력 및 별도 파일 저장
- ✅ 관리자와 일반 사용자 로그인 기능
- ✅ 설치 환경 체크 (OS, 권한, 디스크 공간, 인터넷 등)

---

## 📄 주요 클래스 설명

| 클래스명       | 역할 |
|----------------|------|
| `Main`         | 프로그램 진입점, 로그인 후 Installer 실행 |
| `Installer`    | 프로그램 선택, 설치 진행, 요약 리포트 호출 |
| `Utils`        | 공통 유틸리티, 설치 환경 체크, 로깅, 다운로드 등 |
| `Program`      | 설치 프로그램 정보 모델 |
| `Account`      | 사용자 계정 모델 |

---

## 🧱 빌드 방식

### ✅ 수동 빌드 (Fat JAR 없이)

1. 외부 라이브러리 JAR은 `lib/`에 배치
2. 컴파일:

```bat
javac -encoding UTF-8 -cp "lib/*" -d build src/autoinstaller/*.java
```

3. 실행:

```bat
java -cp "build;lib/*" autoinstaller.Main
```

> ⚠️ Windows Terminal에서는 `lib/*` 인식이 안 될 수 있어 VS Code 터미널 또는 PowerShell 권장

---

## 🐞 문제 해결 이력

### 1. `NoClassDefFoundError: TypeReference`
- 원인: 외부 라이브러리 클래스패스 누락
- 해결: `java -cp "build;lib/*"`로 클래스 경로에 포함

### 2. `MissingFormatWidthException: %-s`
- 원인: `%s` 포맷에 너비 미지정 (예: `%-s`)
- 해결: 너비 포함 포맷 지정 (예: `%-20s`)

```java
writer.printf("%-25s %-8s %-10s %-30s%n", "프로그램", "상태", "경과(초)", "비고");
```

---

## 📝 설치 요약 리포트

- 설치된 프로그램, 설치 여부, 소요 시간, 사유 등 출력
- 콘솔 출력 + `logs/summary.txt` 저장

```
[설치 요약 리포트]
────────────────────────────────────────────
프로그램                 상태      경과(초)   비고
────────────────────────────────────────────
Chrome                  성공      12         -
Notepad++               건너뜀    0          이미 설치됨
7-Zip                   실패      5          명령어 오류
────────────────────────────────────────────
성공: 1, 실패: 1, 건너뜀: 1
```

---

## ✅ 향후 개선 계획

- [ ] 설치 진행률 표시 (Progress bar 또는 %)
- [ ] 관리자 UI로 install_list.json 수정 기능 강화
- [ ] 설치 명령어 실행 결과 상세 로그 저장
- [ ] JAR 빌드 자동화 (Gradle 또는 Maven으로 전환 검토)
- [ ] 비밀번호 입력 마스킹

---

## 📌 참고 사항

- 현재 JDK 20 이상에서 URL 생성자 deprecated → `URI.create(url).toURL()` 방식으로 변경
- `Runtime.exec()` → `ProcessBuilder`로 점진적 대체 중

---