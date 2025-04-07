아래는 `AutoInstaller` 프로젝트를 위한 `README.md` 템플릿입니다. GitHub에 업로드하거나 문서화할 때 바로 사용하실 수 있도록 구성하였습니다.

---

```markdown
# AutoInstaller 🔧

**AutoInstaller**는 지정된 프로그램 목록에 따라 설치 파일을 다운로드하고 자동으로 설치를 진행하는 콘솔 기반 Java 자동화 도구입니다.  
일반 사용자와 관리자 기능을 구분하여 설치 실행 및 설치 목록 관리를 편리하게 수행할 수 있습니다.

---

## 📦 주요 기능

- ✅ 프로그램 설치 자동화 (선택 설치 또는 전체 설치)
- ✅ 설치 전 환경 점검 (권한, 디스크, 인터넷)
- ✅ 프로그램 목록 관리 기능 (관리자 전용)
- ✅ 설치 결과 로그 기록 및 필터링
- ✅ 콘솔 기반 UI 및 명확한 메시지 출력
- ✅ `.jar` 단독 실행 지원

---

## 🔐 로그인 방식

일반 사용자 계정과 관리자 계정으로 나뉩니다.

관리자 로그인은 5회 이상 로그인 실패 시 프로그램이 종료됩니다.

---

## 🗂️ 프로젝트 구조

```
AutoInstaller/
├─ src/                    ← Java 소스 파일
│  ├─ Main.java            ← 실행 진입점
│  ├─ Auth.java            ← 로그인 기능
│  ├─ Installer.java       ← 사용자 설치 로직
│  ├─ Admin.java           ← 관리자 기능
│  ├─ Utils.java           ← 유틸리티 메서드
│  ├─ Program.java         ← 프로그램 데이터 클래스
├─ data/
│  ├─ install_list.json    ← 설치할 프로그램 목록
│  ├─ install_log.txt      ← 설치 결과 로그
│  ├─ install_cache.txt    ← 설치 캐시 (미사용)
├─ installer/              ← 인스톨러 다운로드 저장 폴더
├─ manifest.txt            ← JAR 메타정보 파일
├─ build.bat               ← JAR 빌드 스크립트
├─ run.bat                 ← 프로그램 실행 스크립트
```

---

## 📄 install_list.json 예시

```json
[
  {
    "name": "Google Chrome",
    "url": "https://dl.google.com/chrome/install/latest/chrome_installer.exe",
    "saveAs": "chrome_installer.exe",
    "checkPath": "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
    "installCommand": "chrome_installer.exe /silent /install"
  }
]
```

---

## 🛠️ 실행 방법

### 1. 빌드

**빌드 스크립트 사용 (build.bat)**

```bash
> build.bat
```

또는 수동으로:

```bash
javac -d build src\*.java
jar cfm AutoInstaller.jar manifest.txt -C build .
```

### 2. 실행

**JAR 실행**

```bash
> run.bat
```

또는 수동 실행:

```bash
java -jar AutoInstaller.jar
```

---

## 📋 관리자 기능

- 프로그램 목록 보기
- 프로그램 추가 (모든 항목 필수 입력)
- 프로그램 수정
- 프로그램 삭제
- 설치 로그 보기 (성공, 실패, 전체 보기)

---

## 📌 향후 개선 예정

- 비밀번호 입력 가리기
- 설치 진행률 시각화
- GUI(Swing/JavaFX) 추가
- Jackson 기반 JSON 파싱 리팩토링

---

## 📃 라이선스

이 프로젝트는 MIT 라이선스를 따릅니다.
```

---