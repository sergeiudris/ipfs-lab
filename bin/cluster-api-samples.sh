
curl http://localhost:9094/version
# {"version":"0.13.1+gitc26d0deb49453f9baa7256afdaa088290231540b"}

curl http://localhost:9095/version
curl -X POST http://localhost:9095/version
# Commit: 5b1687d
# Client Version: go-ipfs/0.4.23/5b1687d
# Protocol Version: ipfs/0.1.0

curl -X POST -F file=@dc-dev.yml "http://localhost:9094/add?local=true"
# {"name":"dc-dev.yml","cid":{"/":"QmWedZNceTpCUsdFXT9oFgCHeoHTQq2Y7hzGmxBX9L9W2i"},"size":950}

curl -X POST -F file=@cluster-samples.sh "http://localhost:9095/api/v0/add?local=true"
# {"Name":"cluster-samples.sh","Hash":"QmY4QHEm2bZ3VhMFsopfZevqUS2B4L1QzCFYyQxfoidr8g","Size":"286"}
