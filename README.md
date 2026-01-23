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
| <img width="540" height="860" alt="예누" src="https://github.com/user-attachments/assets/6cb4fa3f-be17-4452-a24e-8758b3c424da" />| <img width="540" height="860" alt="지누" src="https://github.com/user-attachments/assets/9895ca59-93e0-461d-b7b7-0de8852511e7" />|<img width="540" height="860" alt="도누" src="https://github.com/user-attachments/assets/05da9d15-d85d-4501-beaa-d9569bd3c9dd" /> | <img width="540" height="860" alt="민누" src="https://github.com/user-attachments/assets/092dc12c-f1a8-497b-bddd-c72d7efbc52c" /> |

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

| 카테고리 | 기술/라이브러리 |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose (Material3) |
| Architecture | Clean Architecture |
| Module | Single Module |
| Pattern | MVI |
| DI | Hilt |
| Async | Coroutines & Flow |
| Network | Retrofit2 + OkHttp |
| Serialization | Kotlinx Serialization |
| Image | Coil |
| Local DB | DataStore (Preferences) |
| Navigation | Compose Navigation |

**클린 아키텍쳐 선정 이유**

클린 아키텍쳐는 도메인 레이어를 두어 데이터와 UI를 분리합니다.
도메인 레이어는 코틀린 언어로 작성되었기 때문에 데이터 또는 UI 레이어에 사용되는 외부 도구에 문제가 생기더라도 순수하게 보존됩니다.
구글 권장 아키텍쳐에 비해 코드 작성량이 많다는 단점이 있으나, 엄격한 의존성 분리와 정형화된 프로젝트 구조를 유도합니다.

<br>

**MVI 패턴 선정 이유**

MVVM과 달리 UI 액션을 Intent라는 객체로 관리합니다.
보일러 플레이트 코드가 많고 매번 객체가 생성된다는 단점이 있으나, 데이터가 단방향으로 처리되어 추적이 용이합니다.
뷰모델이 처리하는 요청이 하나의 데이터 흐름으로서 관리되기 때문에 순서 보장이나 디바운스 처리와 같은 동시성 문제가 비교적 쉽게 해소됩니다.

<br>

