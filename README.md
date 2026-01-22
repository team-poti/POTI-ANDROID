<div align="center">

### POTI - ANDROID

</div>

<img width="12144" height="5400" alt="4" src="https://github.com/user-attachments/assets/637217a6-2025-4f9e-a91e-91d0d852ffa6" />

<!--
![직사각형 썸네일](https://github.com/user-attachments/assets/28f0b728-2e48-4fd1-bcff-b2bee6b0ef25)
-->

### About Service
> ‘분철’이란?

팬덤 문화로 **굿즈를 공동구매해
나누는 행위**를 의미합니다.

<br>

> 분철을 효율적으로 할 수 없을까?

현재 분철은 원하는 멤버의 굿즈를 얻기 위해
여러 플랫폼을 돌며 검색과 채팅을 반복해야 하는 **비효율적인 구조**입니다.

분철 정보가 흩어져 있어 찾기 어렵고, 진행 상황을 수동으로 체크해야 하며 
입금, 배송 상태가 개인 기억에 의존하며 **신뢰 판단 기준이 없어** 모집자와 참여자 모두 피로도가 높습니다.

<br>

> 포티는 이렇게 해결해요.

- 검색의 비효율을, **옵션 기반 검색 아이돌 굿즈 추천 시스템**으로 해결합니다.
- 진행 관리의 불편함을, **상태바**와 **참여자 관리 페이지**로 해결합니다.
- 신뢰와 안전에 대한 불안을, **EMA 별점 시스템** 도입과 **거래 이력 기반 신뢰도 시스템**으로 해결합니다.

<br>

### About Team

> Contributors

| [손예림](https://github.com/sonyerim) | [이지현(👑)](https://github.com/jyvnee) | [전도연](https://github.com/doyeon0307) | [천민재](https://github.com/cmj7271) |
| --- | --- | --- | --- |

<!-- 사진 넣어야 함! -->

<br>

> Conventions

<div align="left">
  <a href="https://billowy-parakeet-433.notion.site/Code-Convention-2f084ff90888804087dae4e726c0bddf?pvs=74">
    <img
      src="https://img.shields.io/badge/Code%20Convention-Notion-5C7CFA?style=for-the-badge"
      height="52"
      alt="Code Convention"
    />
  </a>

  <a href="https://billowy-parakeet-433.notion.site/Git-Convention-2f084ff90888800295f2eaa9fe253c15?source=copy_link">
    <img
      src="https://img.shields.io/badge/Git%20Convention-Notion-5C7CFA?style=for-the-badge"
      height="52"
      alt="Git Convention"
    />
  </a>

  <a href="https://billowy-parakeet-433.notion.site/Package-Convention-2f084ff9088880ce9721f9aa0b05cec5?source=copy_link">
    <img
      src="https://img.shields.io/badge/Package%20Convention-Notion-5C7CFA?style=for-the-badge"
      height="52"
      alt="Package Convention"
    />
  </a>
</div>

<br>
<br>

> Tech Stacks

| **카테고리** | **기술/라이브러리** | **선정 이유** |
| --- | --- | --- |
| **Language** | Kotlin | - |
| **UI** | **Jetpack Compose** (Material3) | 복잡한 UI 상태 관리 |
| **Architecture** | **Clean Architecture** | 비즈니스 로직과 UI 분리 |
| **Module** | **Single Module** | 단기간 완성을 위한 프로젝트 복잡도 간소화 |
| **Pattern** | **MVI** | 단방향 데이터 흐름으로 이벤트 처리 용이  |
| **DI** | **Hilt** | 안드로이드 표준. 설정이 쉽고 레퍼런스가 많음 |
| **Async** | Coroutines & Flow | 비동기 처리 표준 |
| **Network** | Retrofit2 + OkHttp | 네트워크 표준 |
| **Serialization** | **Kotlinx Serialization** | Gson/Moshi보다 빠르고, Kotlin 친화적이며 Type-safe함 |
| **Image** | **Coil** | Compose에 최적화된 가벼운 이미지 로더 |
| **Local DB** | DataStore (Preferences) | 로그인 토큰 등 간단한 저장소 (Room은 채팅 구현 시 고려) |
| **Navigation** | **Compose Navigation** | 단일 액티비티 구조에 필수 |

<br>

