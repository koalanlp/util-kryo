KoalaNLP-arirang
==============
[![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-arirang_2.12.svg?style=flat-square&label=release)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-arirang_2.12%22)
[![ScalaDoc](https://img.shields.io/badge/doc-Scala-red.svg?style=flat-square)](http://koalanlp.github.io/util-kryo/api/scala/kr/bydelta/koala/index.html)
[![JavaDoc](https://img.shields.io/badge/doc-Java-blue.svg?style=flat-square)](http://koalanlp.github.io/util-kryo/api/java/index.html)
[![분석기별 품사비교표](https://img.shields.io/badge/%ED%92%88%EC%82%AC-%EB%B9%84%EA%B5%90%ED%91%9C-blue.svg?style=flat-square)](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)

[![Build Status](https://img.shields.io/travis/koalanlp/util-kryo.svg?style=flat-square&branch=master)](https://travis-ci.org/koalanlp/util-kryo)
[![codecov](https://img.shields.io/codecov/c/github/koalanlp/util-kryo.svg?style=flat-square)](https://codecov.io/gh/koalanlp/util-kryo)
[![Known Vulnerabilities](https://snyk.io/test/github/koalanlp/util-kryo/badge.svg?style=flat-square)](https://snyk.io/test/github/koalanlp/util-kryo)

[![MIT License](https://img.shields.io/badge/license-MIT-green.svg?style=flat-square)](https://tldrlegal.com/license/mit-license)
[![nodejs-koalanlp](https://img.shields.io/badge/Nodejs-KoalaNLP-blue.svg?style=flat-square)](https://koalanlp.github.io/nodejs-koalanlp)
[![py-koalanlp](https://img.shields.io/badge/Python-KoalaNLP-blue.svg?style=flat-square)](https://koalanlp.github.io/py-koalanlp)

# 소개
KoalaNLP는 한국어 형태소 및 구문 분석기의 모음이며, __서로 다른 형태의 형태소 분석기를__ 모아,
__동일한 인터페이스__ 아래에서 사용할 수 있도록 하는 것이 목적입니다.

이 저장소는 Kryo Serialization을 이용할 수 있도록 돕는 유틸리티 묶음입니다.

# SBT/Maven

| 패키지명 | 버전 | Java | Scala| 설명 |
| -------- | ---- | ---- | ---- |---- |
| `koalanlp-kryo` | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-kryo_2.12.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-kryo_2.12%22) | 8+ | 2.11+ | Kryo serialization 패키지입니다. |

## Dependency 추가하기
KoalaNLP는 Scala 2.11과, 2.12에서 컴파일 되었으며, Scala 2.11+과 Java 8+을 지원합니다.

SBT를 사용하시는 경우, 다음과 같이 추가하시면 됩니다.
(버전은 Latest Release 기준입니다. SNAPSHOT을 사용하시려면, `latest.integration`을 사용하세요.)
```sbt
libraryDependencies += "kr.bydelta" %% "koalanlp-kryo" % "latest.release"
```

Maven을 사용하시는 경우, 다음과 같습니다. `${TAGGER_VER}`위치에는 품사분석기의 버전을 써주세요.
```xml
<dependency>
  <groupId>kr.bydelta</groupId>
  <artifactId>koalanlp-kryo_2.12</artifactId>
  <version>${TAGGER_VER}</version>
</dependency>
```

> [질문] 저는 Java개발자입니다. 왜 패키지명칭에 `_2.12`가 붙나요?
>
> KoalaNLP가 Scala에서 개발되었기 때문에, 개발 당시 Scala의 버전인 2.12가 뒤에 붙은 것입니다.

# 사용방법
아래에는 대표적인 특징만 기술되어 있습니다.

상세한 사항은 [KoalaNLP-core Wiki](https://github.com/koalanlp/KoalaNLP-core/wiki/Home) 또는 이 패키지의 [![ScalaDoc](http://javadoc-badge.appspot.com/kr.bydelta/koalanlp-kryo_2.12.svg?label=scaladoc&style=flat-square)](http://koalanlp.github.io/util-kryo/api/scala/kr/bydelta/koala/index.html)을 참고하십시오.

## Kryo Serializer 생성

다음과 같이 Scala와 KoalaNLP의 data class들을 지원하는 새 kryo를 생성할 수 있습니다.

```scala
import kr.bydelta.koala.kryo.KryoWrap

val kryo = KryoWrap.kryo
```
이후 작업은 kryo 사용법과 동일하게 진행하시면 됩니다.

# License 조항
KoalaNLP-core와 인터페이스 통합을 위한 코드는 v1.8.0부터 [*MIT License*](https://tldrlegal.com/license/mit-license)을 따르며,
각 분석기의 License와 저작권은 각 프로젝트에서 지정한 바를 따릅니다. (`kr.bydelta.koala.helper` 하위에 새로 수정되어 등록된 Class/Object는 각 프로젝트의 결과물을 조금 수정한 판본이며, 저작권은 각 프로젝트에 귀속됩니다.)
* Arirang: [Apache License 2.0](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))

# 결과 비교
[Wiki:결과비교](https://github.com/nearbydelta/KoalaNLP/wiki/4.-결과-비교)를 참조해주세요.
