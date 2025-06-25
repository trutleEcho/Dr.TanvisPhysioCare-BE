# tanvisphysiocare

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [Ktor GitHub page](https://github.com/ktorio/ktor)
- The [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). You'll need to [request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up) to join.

## Features

Here's a list of features included in this project:

| Name                                                                      | Description                                                                        |
| ---------------------------------------------------------------------------|------------------------------------------------------------------------------------ |
| [Rate Limiting](https://start.ktor.io/p/ktor-server-rate-limiting)        | Manage request rate limiting as you see fit                                        |
| [Routing](https://start.ktor.io/p/routing)                                | Provides a structured routing DSL                                                  |
| [WebSockets](https://start.ktor.io/p/ktor-websockets)                     | Adds WebSocket protocol support for bidirectional client connections               |
| [Raw Sockets](https://start.ktor.io/p/ktor-network)                       | Adds raw socket support for TCP and UDP                                            |
| [Raw Secure SSL/TLS Sockets](https://start.ktor.io/p/ktor-network-tls)    | Adds secure socket support for TCP and UDP                                         |
| [Dependency Injection](https://start.ktor.io/p/ktor-di)                   | Enables dependency injection for your server                                       |
| [Koin](https://start.ktor.io/p/koin)                                      | Provides dependency injection                                                      |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization)    | Handles JSON serialization using kotlinx.serialization library                     |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)        | Provides automatic content conversion according to Content-Type and Accept headers |
| [MongoDB](https://start.ktor.io/p/mongodb)                                | Adds MongoDB database to your application                                          |
| [Call Logging](https://start.ktor.io/p/call-logging)                      | Logs client requests                                                               |
| [Call ID](https://start.ktor.io/p/callid)                                 | Allows to identify a request/call.                                                 |
| [Webjars](https://start.ktor.io/p/webjars)                                | Bundles static assets into your built JAR file                                     |
| [Status Pages](https://start.ktor.io/p/status-pages)                      | Provides exception handling for routes                                             |
| [Request Validation](https://start.ktor.io/p/request-validation)          | Adds validation for incoming requests                                              |
| [Sessions](https://start.ktor.io/p/ktor-sessions)                         | Adds support for persistent sessions through cookies or headers                    |
| [Authentication](https://start.ktor.io/p/auth)                            | Provides extension point for handling the Authorization header                     |
| [Firebase authentication](https://start.ktor.io/p/firebase-auth-provider) | Handles Firebase bearer authentication                                             |
| [Authentication OAuth](https://start.ktor.io/p/auth-oauth)                | Handles OAuth Bearer authentication scheme                                         |
| [Authentication JWT](https://start.ktor.io/p/auth-jwt)                    | Handles JSON Web Token (JWT) bearer authentication scheme                          |
| [HSTS](https://start.ktor.io/p/hsts)                                      | Enables HTTP Strict Transport Security (HSTS)                                      |
| [Default Headers](https://start.ktor.io/p/default-headers)                | Adds a default set of headers to HTTP responses                                    |
| [CORS](https://start.ktor.io/p/cors)                                      | Enables Cross-Origin Resource Sharing (CORS)                                       |
| [Compression](https://start.ktor.io/p/compression)                        | Compresses responses using encoding algorithms like GZIP                           |

## Building & Running

To build or run the project, use one of the following tasks:

| Task                          | Description                                                          |
| -------------------------------|---------------------------------------------------------------------- |
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew build`             | Build everything                                                     |
| `buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `buildImage`                  | Build the docker image to use with the fat JAR                       |
| `publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `run`                         | Run the server                                                       |
| `runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

