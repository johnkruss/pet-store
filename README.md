# osn-demo

[![forthebadge](https://forthebadge.com/images/badges/powered-by-electricity.svg)](https://forthebadge.com)

[![forthebadge](https://forthebadge.com/images/badges/made-with-crayons.svg)](https://forthebadge.com)

![Built with ChatGPT](https://img.shields.io/badge/Built%20with-ChatGPT-blue?style=for-the-badge)

A repo to demonstrate code structures and testing practices that can effectively document and demonstrate code

Project created with Kotlin, 2.0.0, Java 21, Micronaut 4.4.3, Kotest 5.9.0, and of course docker

To build/test - 

First run `docker compose up -d` to pull and start up a container running localstack

Localstack is a convenient, mostly open source, container based AWS emulator that provides a bunch of 
convenient services to test against

From there simple run `./gradlew build` to see tests executed 

Presentation created using [Excalidraw](https://excalidraw.com/)
