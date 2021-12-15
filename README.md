# 호스트들의 Alive 상태 모니터링 REST API

## 요구 사항 미만족
- maria db 사용
- DDL SQL은 파일로 Github에 추가한다.
- 등록된 호스트 Alive 상태 확인
- api 문서화

# Host
호스트 리소스 API

## 호스트 조회
**GET** 메소드를 사용하여 호스트를 조회할 수 있다.

### 요청 필드
| 필드 명 | 데이터 타입 | 설명       |필수 여부|
|------|--------|----------|:---:|
| id   | Number | 호스트의 아이디 |O|

### 요청 예시
```http request
GET /api/hosts/1 HTTP/1.1
Host: localhost
Accept: "application/hal+json"

{
    "name":"atto-research.com",
    "address":"210.114.6.150"
}
```

### 응답 필드
| 필드 명                 | 데이터 타입 | 설명                   |
|----------------------|--------|----------------------|
| id                   | Number | 호스트의 아이디             |
| name                 | String | 호스트의 이름              |
| address              | String | 호스트의 IP 주소           |
| createdDateTime      | String | 호스트의 생성된 시간          |
| lastModifiedDateTime | String | 호스트의 마지막 수정 시간       |
| lastAliveDateTime    | String | 호스트의 마지막 Alive 확인 시간 |
| _links.self.href     | String | 생성된 호스트의 정보 조회 링크    |

### 응답 예시
```http request
HTTP/1.1 201 Created
Location: "http://localhost/api/hosts/1"
Content-Type:"application/hal+json;charset=UTF-8"

{
    "id":1,
    "name":"atto-research.com",
    "address":"210.114.6.150",
    "createdDateTime":"2021-12-14T23:21:23.831437",
    "lastModifiedDateTime":"2021-12-14T23:21:23.831437",
    "lastAliveDateTime":"2021-12-14T23:21:23.231456",
    "_links":{
        "self":{
            "href":"http://localhost/api/hosts/1"
            }
    }
}
```

## 호스트 등록
**POST** 메소드를 사용하여 호스트를 등록할 수 있다.

### 요청 필드
| 필드 명    |데이터 타입| 설명         |필수 여부|
|---------|---|------------|:---:|
| name    |String| 호스트의 이름    |O|
| address |String| 호스트의 IP 주소 |O|

### 요청 예시
```http request
POST /api/hosts HTTP/1.1
Host: localhost
Content-Type:"application/hal+json;charset=UTF-8"
Accept: "application/hal+json"
Content-Length: 54

{
    "name":"atto-research.com",
    "address":"210.114.6.150"
}
```

### 응답 필드
| 필드 명                 | 데이터 타입 | 설명                   |
|----------------------|--------|----------------------|
| id                   | Number | 호스트의 아이디             |
| name                 | String | 호스트의 이름              |
| address              | String | 호스트의 IP 주소           |
| createdDateTime      | String | 호스트의 생성된 시간          |
| lastModifiedDateTime | String | 호스트의 마지막 수정 시간       |
| lastAliveDateTime    | String | 호스트의 마지막 Alive 확인 시간 |
| _links.self.href     | String | 생성된 호스트의 정보 조회 링크    |

### 응답 예시
```http request
HTTP/1.1 201 Created
Location: "http://localhost/api/hosts/1"
Content-Type:"application/hal+json;charset=UTF-8"

{
    "id":1,
    "name":"atto-research.com",
    "address":"210.114.6.150",
    "createdDateTime":"2021-12-14T23:21:23.831437",
    "lastModifiedDateTime":"2021-12-14T23:21:23.831437",
    "lastAliveDateTime":"2021-12-14T23:21:30.476521",
    "_links":{
        "self":{
            "href":"http://localhost/api/hosts/1"
            }
    }
}
```

## 호스트 수정
**PUT** 메소드를 사용하여 호스트를 수정할 수 있다.

### 요청 필드
| 필드 명    |데이터 타입| 설명         |필수 여부|
|---------|---|------------|:---:|
| name    |String| 호스트의 이름    |O|
| address |String| 호스트의 IP 주소 |O|

### 요청 예시
```http request
PUT /api/hosts/1 HTTP/1.1
Host: localhost
Content-Type:"application/hal+json;charset=UTF-8"
Accept: "application/hal+json"
Content-Length: 48

{
    "name":"google.com",
    "address":"142.250.207.14"
}
```

### 응답 필드
| 필드 명                 | 데이터 타입 | 설명                   |
|----------------------|--------|----------------------|
| id                   | Number | 호스트의 아이디             |
| name                 | String | 호스트의 이름              |
| address              | String | 호스트의 IP 주소           |
| createdDateTime      | String | 호스트의 생성된 시간          |
| lastModifiedDateTime | String | 호스트의 마지막 수정 시간       |
| lastAliveDateTime    | String | 호스트의 마지막 Alive 확인 시간 |
| _links.self.href     | String | 호스트의 정보 조회 링크    |

### 응답 예시
```http request
HTTP/1.1 200 Ok
Location: "http://localhost/api/hosts/1"
Content-Type:"application/hal+json;charset=UTF-8"

{
    "id":1,
    "name":"google.com",
    "address":"142.250.207.14",
    "createdDateTime":"2021-12-14T23:21:23.831437",
    "lastModifiedDateTime":"2021-12-14T23:21:23.831437",
    "lastAliveDateTime":"2021-12-14T23:21:40.213437",
        "_links":{
            "self":{
                "href":"http://localhost/api/hosts/1"
            }
    }
}
```

## 호스트 삭제
**DELETE** 메소드를 사용하여 호스트를 삭제할 수 있다.

### 요청 필드
| 필드 명    |데이터 타입| 설명         |필수 여부|
|---------|---|------------|:---:|
| name    |String| 호스트의 이름    |O|

### 요청 예시
```http request
DELETE /api/hosts/1 HTTP/1.1
Host: localhost
Accept: "application/hal+json"
```

### 응답 예시
```http request
HTTP/1.1 204 No Content
```