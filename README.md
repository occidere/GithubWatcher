# GithubWatcher

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Foccidere%2FGithubWatcher.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Foccidere%2FGithubWatcher?ref=badge_shield)
[![Coverage Status](https://coveralls.io/repos/github/occidere/GithubWatcher/badge.svg?branch=master)](https://coveralls.io/github/occidere/GithubWatcher?branch=master)
<img src="https://img.shields.io/github/languages/top/occidere/GithubWatcher"/>
<img src="https://img.shields.io/github/issues/occidere/GithubWatcher"/>
[![Java Version](https://img.shields.io/badge/java-1.8+-orange.svg)](https://www.java.com/ko/)
[![Scala Version](https://img.shields.io/badge/Scala-2.13-red.svg)](https://www.scala-lang.org/download/)
[![GitHub license](https://img.shields.io/github/license/occidere/GithubWatcher.svg)](https://github.com/occidere/GithubWatcher/blob/master/LICENSE)

Integrated GitHub Activity Notification System.

<br>

## Features
### 1. Follower change notification
Notify follower increase / decrease.

#### Examples
![image](https://user-images.githubusercontent.com/20942871/91848968-42e86f00-ec96-11ea-8316-7781634768ae.png)


<br>

### 2. Repository change notification
Notify Stargazer / Watcher / Fork change both increase and decrease.

#### Examples
![image](https://user-images.githubusercontent.com/20942871/91850418-7e843880-ec98-11ea-9fd5-9b3f1226f42d.png)


<br>

## Work Flow
1. Get latest information of user and repository from Github API
2. Get previous information of user and repository from database (Elasticsearch)
3. Comparing both of them to find changes
    - Increase/Decrease of Followers
    - Increate/Decrease of Stargazers
    - Increate/Decrease of Watchers
    - Increate/Decrease of Forks
4. Send notification message on [Line](https://line.me/en/)
5. Save latest information on database

<br>

## Build and Run

### Build with sbt
```bash
git clone https://github.com/occidere/GithubWatcher.git
cd GithubWatcher
sbt clean assembly
```

### Run jar
```bash
export gw_tasks=followerWatchTask,repositoryWatchTask # tasks
export gw_user_id=${YOUR_GITHUB_USER_ID}
export gw_es_endpoint=${YOUR_ES_ENDPOINT} # default: localhost:9200
export gw_line_bot_id=${YOUR_LINE_BOT_ID}
export gw_line_channel_token=${YOUR_LINE_CHANNEL_TOKEN}
export gw_github_api_token=${YOUR_GITHUB_API_TOKEN}

java -jar target/scala-2.13/GithubWatcher.jar
```


## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Foccidere%2FGithubWatcher.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Foccidere%2FGithubWatcher?ref=badge_large)
