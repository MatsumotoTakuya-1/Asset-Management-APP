# ----------------------------------------
# Step 1: Node.jsでReactアプリをビルド
# ----------------------------------------
#node:20を使う。　frontendはラベル
FROM node:20 AS frontend

# 作業ディレクトリ
WORKDIR /app/client

#package.json,package-lock.jsonをコピーし、npm i
#package*.jsonが変更されない限り、キャッシュされる
COPY client/package*.json ./
RUN npm install

# client全体をコピーし、distフォルダを作成。
COPY client/ ./
RUN npm run build

# ----------------------------------------
# Step 2: Kotlin + Gradle でSpring Bootをビルド
# ----------------------------------------
#jdk(javaのsdkはver21),gladleのverはコピペ
FROM gradle:8.4.0-jdk21 AS backend

WORKDIR /app

# 依存キャッシュ用（高速化）
COPY server/build.gradle.kts server/settings.gradle.kts /app/server/
COPY server/gradle /app/server/gradle

RUN mkdir -p /app/server/src/main/resources/static

RUN gradle -p server build --no-daemon || true

# すべてコピー（Reactのビルド成果物含む）
COPY server /app/server
COPY --from=frontend /app/client/dist/ /app/server/src/main/resources/static/

# 本ビルド
RUN gradle -p server bootJar --no-daemon

# ----------------------------------------
# Step 3: 実行用の軽量イメージ（JRE）
# ----------------------------------------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=backend /app/server/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]