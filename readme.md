# cask example

Initially created from:
```
./run.sh
```

## Useful commands
```
scala-cli setup-ide .
scala-cli export --sbt .
scala-cli export --mill .
scala-cli package App.scala
scala-cli package --docker App.scala
```

##
Intented E2E Test:

1) spin up elastic-search + kibana locally (docker-compose)
2) start this proxy app for tracker
3) use the "app" ... somehow? (How do we invoke tracker?)
4) Interrogate ElasticSearch (dashboard) to manually confirm results
5) Generate regression tests (send _these_ tracker requests and expect _that_ data in elastic search --- or expect _that_ data via the dashboard API)