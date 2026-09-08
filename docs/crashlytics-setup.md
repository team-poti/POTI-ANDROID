# Crashlytics 1단계 적용 및 검증

## 적용 범위

- Crashlytics Gradle plugin 3.0.8, 기존 Firebase BoM을 통한 SDK 버전 관리.
- Manifest를 variant별 생성하여 Firebase가 Application보다 먼저 초기화되어도 prodRelease에서만 자동 수집.
- Firebase Analytics, Mixpanel, Performance는 이번 변경에 추가하지 않음.
- CrashReporter를 Hilt로 주입. 원격 non-fatal 보고는 prodRelease에서만 허용.
- HttpResponseHandler에서 알 수 없는 예외를 NetworkError로 변환하기 전에 보고.
- BaseResponse의 필수 data 누락은 계약 위반으로 구분하고 업무 오류는 보고에서 제외.
- BaseViewModel 예외 경계에서도 예상하지 못한 예외를 보고. NetworkError는 다시 보고하지 않음.
- TokenAuthenticator의 재발급 파싱/예상 밖 오류, 필수 data 누락, 토큰 저장·삭제 실패 보고.
- 취소, 일반 I/O, HTTP 오류, 분류된 NetworkError, 업무 오류는 non-fatal에서 제외.

non-fatal은 고정 operation과 예외 클래스, 원본 stack trace만 전송한다. 원본 message, cause, suppressed exception은 제거한다. 자동 fatal/ANR 보고는 SDK 경로이므로 이 필터를 통과하지 않는다. 앱의 uncaught 예외 메시지에도 개인정보를 넣지 않아야 한다. Timber 로그 전체는 원격으로 전달하지 않는다.

## 환경 정책

| Variant | 기본 자동 수집 | 앱 non-fatal |
| --- | --- | --- |
| prodRelease | ON | ON |
| devRelease | OFF | OFF |
| devDebug / prodDebug | OFF | OFF |
| devMock / prodMock | OFF | OFF |

현재 release의 minify 설정은 false로 유지했다. 따라서 난독화 해제 검증 대상은 아직 없으며, R8 활성화 시 mapping 업로드와 실제 스택 복원을 별도 검증해야 한다. Firebase 설정에 mock 패키지가 없으면 mock APK 빌드는 별도 Firebase 설정이 필요하다.

## 실제 콘솔 확인 절차 — 기기 필요

1. QA 기기에서 devDebug를 설치한다. 운영 앱과 패키지가 분리된 devDebug를 권장한다.
2. `adb shell am start -n com.poti.android.dev/com.poti.android.core.monitoring.CrashlyticsTestActivity`를 실행한다.
3. 테스트 Activity가 수집을 활성화하고 1초 뒤 `POTI Crashlytics QA crash` 예외를 발생시킨다.
4. 크래시 후 앱을 다시 열고 네트워크 연결 상태에서 업로드를 기다린다.
6. 해당 Firebase 프로젝트의 Crashlytics에서 `POTI Crashlytics QA crash`, 앱 버전, 파일/라인을 확인한다.
7. QA 종료 후 `adb shell am start -n com.poti.android.dev/com.poti.android.core.monitoring.CrashlyticsTestActivity --ez disable_collection true`로 devDebug 수집을 다시 끈다. prodDebug는 prodRelease와 패키지가 같아 SDK 수집 override가 이어질 수 있으므로 운영 패키지에서 QA override를 사용하지 않는다.

DebugProbe는 src/debug에만 있어 release APK에는 포함되지 않는다. 자동 실행되는 크래시 버튼이나 외부에서 호출 가능한 테스트 컴포넌트는 추가하지 않았다.

추가로 SDK의 `recordException(IllegalStateException("POTI QA non-fatal"))`을 debugger에서 호출하고 재시작하여 non-fatal 전송을 검증할 수 있다. 이는 전송 경로 검증이며 prodRelease 전용 CrashReporter 정책 검증과 구분한다.

ANR은 테스트 crash와 별개다. 지원 OS의 QA 기기에서 별도 ANR 재현과 재실행 후 콘솔 확인이 필요하다. 실제 콘솔 확인 전에는 운영 수집 검증 완료로 표시하지 않는다.

공식 참고: [Android 설정](https://firebase.google.com/docs/crashlytics/android/get-started), [수집 제어 및 보고](https://firebase.google.com/docs/crashlytics/android/customize-crash-reports).
