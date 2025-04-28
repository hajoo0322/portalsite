# Makefile

ifeq ($(OS), Windows_NT)
  GRADLE_CMD = gradlew.bat
else
  GRADLE_CMD = ./gradlew
endif

.PHONY: build docker-build up start logs stop fast-build fast

APP_NAME=portalsite-app:latest

start: build docker-build up

restart: stop build remove docker-build up


build:
	$(GRADLE_CMD) build

docker-build:
	docker build -t $(APP_NAME) .

up:
	docker-compose up -d

logs:
	docker-compose logs -f

stop:
	docker-compose down

fast-build:
	$(GRADLE_CMD) build -x test

fast: fast-build docker-build up

remove:
	docker rmi $(APP_NAME)