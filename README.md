# Kurly Android Project

이 프로젝트는 Clean Architecture와 MVVM 패턴을 기반으로 설계되었으며, 안정적이고 확장 가능한 코드 구조를 지향합니다.

## 🛠 기술 스택

- **Language:** Kotlin
- **Architecture:** MVVM + Clean Architecture
- **Dependency Injection:** Hilt
- **Network:** Retrofit, OkHttp, GSON
- **Database:** Room (로컬 데이터 저장 및 좋아요 기능)
- **Image Loading:** Glide
- **UI:** XML, ViewBinding, RecyclerView, SwipeRefreshLayout
- **Async:** Coroutines, Flow
- **Local Mock Server:** `mockserver` 모듈을 통한 API 시뮬레이션

## 📁 프로젝트 구조

프로젝트는 크게 `app` 모듈과 `mockserver` 모듈로 구성되어 있습니다.

### 1. `app` 모듈

`com.gomguk.kurly` 패키지 하위의 주요 구성은 다음과 같습니다.

- **`data`**: 데이터 소스와 관련된 구현체입니다.
    - `api`: Retrofit 인터페이스 및 네트워크 통신 정의
    - `local`: Room Database, Entity, DAO 정의
    - `repository`: 도메인 레이어의 인터페이스를 구현하며, 데이터 소스(Remote/Local)를 결정
    - `model`: API 응답 데이터 모델
- **`domain`**: 비즈니스 로직을 담당하는 순수 Kotlin 레이어입니다.
    - `usecase`: 특정 기능 단위의 비즈니스 로직 (예: 섹션 목록 가져오기, 좋아요 토글 등)
    - `model`: 도메인에서 사용하는 데이터 모델 (필요 시 정의)
    - `repository`: 데이터 레이어와의 통신을 위한 인터페이스 정의
- **`ui`**: 화면 표시 및 사용자 상호작용을 담당합니다.
    - `main`: 메인 화면 관련 Activity, ViewModel, Adapter
- **`di`**: Hilt를 사용한 의존성 주입 설정 모듈들이 위치합니다.
- **`util`**: 확장 함수 및 유틸리티 클래스

### 2. `mockserver` 모듈

- 로컬 환경에서 API 응답을 테스트하기 위한 라이브러리 모듈입니다.
- `assets`에 저장된 JSON 파일을 기반으로 Mock 응답을 생성하여 네트워크 연결 없이도 앱의 기능을 테스트할 수 있게 합니다.

## 🚀 주요 기능

1. **섹션 기반 상품 리스트**: 메인 화면에서 다양한 섹션(추천 상품 등)을 리스트 형태로 확인할 수 있습니다.
2. **무한 스크롤 (Paging)**: 스크롤이 끝에 닿을 때 다음 페이지의 데이터를 자동으로 로드합니다.
3. **좋아요 기능**: 상품에 좋아요를 표시할 수 있으며, 로컬 DB(Room)에 저장되어 앱 재시작 시에도 유지됩니다.
4. **새로고침**: `SwipeRefreshLayout`을 사용하여 최신 데이터를 다시 불러올 수 있습니다.

## ⚙️ 빌드 및 실행 방법

1. Android Studio를 실행하고 프로젝트를 엽니다.
2. Gradle Sync를 완료합니다.
3. 에뮬레이터 또는 실제 기기를 연결한 후 `app` 모듈을 실행합니다.
