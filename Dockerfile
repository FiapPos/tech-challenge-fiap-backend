FROM ubuntu:latest
LABEL authors="settman"

ENTRYPOINT ["top", "-b"]